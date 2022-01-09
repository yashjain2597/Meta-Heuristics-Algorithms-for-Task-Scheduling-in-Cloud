package Fitness_Function;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import FitnessFunction.*;
import FitnessFunction.Constant;
import FitnessFunction.PSO;
import FitnessFunction.SchedulerParticle;
import FitnessFunction.SchedulerParticleUpdate;

public class PsoScheduling {
static Datacenter[] datacenter;
private static List<Vm> vmlist;
private static List<Cloudlet> cloudletList;
private static final PSO PSOSchedularInstance = new PSO();;
public static double mapping[] = PSOSchedularInstance.run();
public double[] getPsoMapping() { return mapping; }

private static void createTasks(int brokerId,String filePath, int taskNum)
{
	try
	{
		@SuppressWarnings("resource")
		BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		String data = null;
		int index = 0;
		
		//cloudlet properties.
		int pesNumber = 1;
		long fileSize = 1000;
		long outputSize = 1000;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		while ((data = br.readLine()) != null)
		{
			System.out.println(data);
			String[] taskLength=data.split("\t");
			for(int j=0;j<20;j++){
				Cloudlet task=new Cloudlet(index+j, (long) Double.parseDouble(taskLength[j]), pesNumber, fileSize,
						outputSize, utilizationModel, utilizationModel,
						utilizationModel);
				task.setUserId(brokerId);
				cloudletList.add(task);
				if(cloudletList.size()==taskNum)
				{	
					br.close();
					return;
				}
			}
			index+=20;
		}
		
	} 
	catch (IOException e)
	{
		e.printStackTrace();
	}
}


public static void main(String[] args) {
double execTimeMatrix[][] = PSOSchedularInstance.getExecTimeMatrix();
double communTimeMatrix[][] = PSOSchedularInstance.getCommunTimeMatrix();
Log.printLine("Starting Task Schedular Simulation...");
try {
	int num_user = 2;
	Calendar calendar = Calendar.getInstance();
	boolean trace_flag = false;
	CloudSim.init(num_user, calendar, trace_flag);
	datacenter = new Datacenter[Constant.NO_OF_DATA_CENTERS];
	for(int i = 0; i < Constant.NO_OF_DATA_CENTERS; i++) {
		datacenter[i] = createDatacenter("Datacenter_" + i);
}
	DatacenterBroker broker = createBroker();
	int brokerId = broker.getId();
	vmlist = new ArrayList<Vm>();        	
	//int mips = 1000;
	long size = 10000;        	
	int ram = 512;
	long bw = 1000;
	int pesNumber = 1;
	String vmm = "Xen";
	Vm vm1 = new Vm(0, brokerId, 5000, pesNumber, ram, bw, size,
			vmm, new CloudletSchedulerSpaceShared());
	Vm vm2 = new Vm(1, brokerId, 2500, pesNumber, ram, bw, size,
			vmm,new CloudletSchedulerTimeShared());
	Vm vm3 = new Vm(2, brokerId, 2500, pesNumber, ram, bw, size,
			vmm,new CloudletSchedulerTimeShared());
	Vm vm4 = new Vm(3, brokerId, 1500, pesNumber, ram, bw, size,
			vmm, new CloudletSchedulerSpaceShared());
	Vm vm5 = new Vm(4, brokerId, 1000, pesNumber, ram, bw, size,
			vmm, new CloudletSchedulerSpaceShared());

	// add the VMs to the vmList
	vmlist.add(vm1);
	vmlist.add(vm2);
	vmlist.add(vm3);
	vmlist.add(vm4);
	vmlist.add(vm5);

broker.submitVmList(vmlist);
cloudletList = new ArrayList<Cloudlet>();

createTasks(brokerId,"data\\cloudlets.txt",200);
HashSet<Integer> dcIds = new HashSet<>();
HashMap<Integer, Integer> hm = new HashMap<>();
for(Datacenter dc: datacenter) {
	if(!dcIds.contains(dc.getId())) 
	dcIds.add(dc.getId());
}
Iterator<Integer> it = dcIds.iterator();
for(int i = 0; i < mapping.length; i++) {
	if(hm.containsKey((int) mapping[i])) continue;
	hm.put((int) mapping[i], (int) it.next());
}
for(int i = 0; i < mapping.length; i++) 
	mapping[i] = hm.containsKey((int) mapping[i]) ? hm.get((int) mapping[i]) : mapping[i];
//broker.submitMapping(mapping);
broker.submitCloudletList(cloudletList);
CloudSim.startSimulation();
List<Cloudlet> newList = broker.getCloudletReceivedList();
CloudSim.stopSimulation();
printCloudletList(newList);
Log.printLine("simulating PSO finished!");
}catch(Exception e) {
System.out.println("An error has been occurred!\n" + e.getMessage());
}
}
private static void printCloudletList(List<Cloudlet> list) {
int size = list.size();
double value1=0;
Cloudlet cloudlet;
String indent = "    ";
Log.printLine();
Log.printLine("========== OUTPUT ==========");
Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
double mxFinishTime = 0;
DecimalFormat dft = new DecimalFormat("###.##");
for (int i = 0; i < size; i++) {
cloudlet = list.get(i);
Log.print(indent + cloudlet.getCloudletId() + indent + indent);
if (cloudlet.getStatus() == Cloudlet.SUCCESS){
Log.print("SUCCESS");
Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
indent + indent + dft.format(cloudlet.getFinishTime()));
mxFinishTime = Math.max(mxFinishTime, cloudlet.getFinishTime());
}
value1= value1+Double.parseDouble(dft.format(cloudlet.getActualCPUTime()));
}
PSOSchedularInstance.printBestFitness();
System.out.println(mxFinishTime);
System.out.println("This (G&PSO) schedule plan takes "+value1/10+" ms  to finish execution.");
}
private static Datacenter createDatacenter(String name)
{
	List<Host> hostList = new ArrayList<Host>();
	List<Pe> peList = new ArrayList<Pe>();
	
	int mips = 5000;
	peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store MIPS Rating
	
	mips = 2500;
	peList.add(new Pe(1, new PeProvisionerSimple(mips))); 
	
	mips = 2500;
	peList.add(new Pe(2, new PeProvisionerSimple(mips))); 
	
	mips = 1500;
	peList.add(new Pe(3, new PeProvisionerSimple(mips)));
		
	mips = 1000;
	peList.add(new Pe(4, new PeProvisionerSimple(mips))); 
												
	int hostId = 0;
	int ram = 4096; // host memory (MB)
	long storage = 10000000; // host storage
	int bw = 10000;

	hostList.add(new Host(hostId, new RamProvisionerSimple(ram),
			new BwProvisionerSimple(bw), storage, peList,
			new VmSchedulerTimeShared(peList)));
	String arch = "x86"; // system architecture
	String os = "Linux"; // operating system
	String vmm = "Xen";
	double time_zone = 5.30; 
	double cost = 3.0; 
	double costPerMem = 0.05; 
	double costPerStorage = 0.001; 
								
	double costPerBw = 0.001; 
	
	
	LinkedList<Storage> storageList = new LinkedList<Storage>();

	DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
			arch, os, vmm, hostList, time_zone, cost, costPerMem,
			costPerStorage, costPerBw);

	
	Datacenter datacenter = null;
	try
	{
		datacenter = new Datacenter(name, characteristics,
				new VmAllocationPolicySimple(hostList), storageList, 0);
	} catch (Exception e)
	{
		e.printStackTrace();
	}

	return datacenter;
}

private static DatacenterBroker createBroker() {
DatacenterBroker broker = null;
try {
broker = new DatacenterBroker("Broker");
} catch (Exception e) {
e.printStackTrace();
}
return broker;
}
}
