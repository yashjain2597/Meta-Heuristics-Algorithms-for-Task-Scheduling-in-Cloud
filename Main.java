package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
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
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class Main {


	public static List<cloudlet3> cloudletList;

	public static List<vm3> vmlist;

	public static List<vm3> createVM(int userId, int vms) {

		LinkedList<vm3> list = new LinkedList<vm3>();


		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 1000;
		long bw = 1000;
		int pesNumber = 12; //number of cpus
		String vmm = "Xen"; //VMM name

		vm3[] vm = new vm3[vms];
		double[][] comcost= {{0.00,0.17,0.21},{0.17,0.00,0.22},{0.21,0.22,0.00},{0.22,0.00,0.23},{0.24,0.21,0.00}};

		for(int i=0;i<vms;i++){
			
			vm[i] = new vm3(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared(),comcost[i]);

			list.add(vm[i]);
		}

		return list;
	}


	public static List<cloudlet3> createCloudlet(int userId, int cloudlets){
		LinkedList<cloudlet3> list = new LinkedList<cloudlet3>();
		Random rand = new Random();

		long length = 1000;
		long fileSize = 1000;
		long outputSize = 1000;
		int pesNumber = 12;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		cloudlet3[] cloudlet = new cloudlet3[cloudlets];
		double[][] executioncost= new double[1000][3];
		int[][] datasize= new int[1000][2];
		for(int i=0;i<1000;i++)
		{
			for(int j=0;j<2;j++)
			{
				datasize[i][j]=rand.nextInt(20);
				System.out.println(datasize[i][j]);
			}
		}
		for(int i=0;i<1000;i++)
		{
			for(int j=0;j<3;j++)
			{
				executioncost[i][j]=rand.nextDouble();
			}
		}

		for(int i=0;i<cloudlets;i++){
			Random random = new Random();
			cloudlet[i] = new cloudlet3(i, length + random.nextInt(2000), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel, executioncost[i], datasize[i]);
			//cloudlet[i] = new Cloudlet(i, length + random.nextInt(2000), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			
			cloudlet[i].setUserId(userId);
			list.add(cloudlet[i]);
		}

		return list;
	}


	////////////////////////// STATIC METHODS ///////////////////////

	public static void main(String[] args) {
		Log.printLine("Starting Main Program...");

		try {
			int num_user = 2;   // number of grid users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0");
			@SuppressWarnings("unused")

			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();
			vmlist = createVM(brokerId,5); //creating 3 vms
			cloudletList = createCloudlet(brokerId,200); // creating 5 cloudlets
			

			broker.submitVmList(vmlist);
			broker.submitCloudletList(cloudletList);
			GeneticAlgorithm ga = new GeneticAlgorithm(1000, 0.01, 0.8, 2, cloudletList, vmlist);

			System.out.println("Population Initialization");
			int chromosomeLength = 5;
			Population population = ga.initPopulation(chromosomeLength);

			ga.evalPopulation(population);
			
			int iteration = 1;
			while (iteration <= 15) 
			{	

				Individual fit = population.getFittest(0);
				
				System.out.print("Fittest: ");
				for(int j=0;j<5;j++) {
					System.out.print(fit.chromosome[j] + " ");
				}
				System.out.println("  fitness => " + fit.getFitness());
				
				for(int j=0;j<5;j++)
				{
					broker.bindCloudletToVm(j, fit.chromosome[j]);
				}

				population = ga.crossoverPopulation(population);

				population = ga.mutatePopulation(population);

				ga.evalPopulation(population);
				iteration++;
				
			}
			System.out.println("Best solution of GA: " + population.getFittest(0).toString());
			int[][] particles = new int[population.size()][5];
			for(int ind=0;ind<population.size();ind++)
			{
				for(int index=0;index<5;index++)
				{
					particles[ind][index] = population.population[ind].chromosome[index];
				}
			}
			Swarm swarm = new Swarm(particles, 150, population.size(), cloudletList, vmlist);
			swarm.run(particles);
			System.out.println("Best solution of PSO: " + swarm.bestPosition.toString());
			
			broker.bindCloudletToVm(0, swarm.bestPosition.getA());
			broker.bindCloudletToVm(1, swarm.bestPosition.getB());
			broker.bindCloudletToVm(2, swarm.bestPosition.getC());
			broker.bindCloudletToVm(3, swarm.bestPosition.getD());
			broker.bindCloudletToVm(4, swarm.bestPosition.getE());
			
			CloudSim.startSimulation();

			List<Cloudlet> newList = broker.getCloudletReceivedList();

			CloudSim.stopSimulation();

			printCloudletList(newList);

			Log.printLine("GA-PSO finished!");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}

	private static Datacenter createDatacenter(String name){

		List<Host> hostList = new ArrayList<Host>();
		/*List<Pe> peList2 = new ArrayList<Pe>();

		peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList2.add(new Pe(1, new PeProvisionerSimple(mips)));*/
		List<Pe> peList = new ArrayList<Pe>();
		
		int mips = 5000;
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); 
		
		mips = 2500;
		peList.add(new Pe(1, new PeProvisionerSimple(mips))); 
		
		mips = 2500;
		peList.add(new Pe(2, new PeProvisionerSimple(mips))); 
		
		mips = 1500;
		peList.add(new Pe(3, new PeProvisionerSimple(mips)));
			
		mips = 1000;
		peList.add(new Pe(4, new PeProvisionerSimple(mips)));

		int hostId=0;
		int ram = 4096; //host memory (MB)
		long storage = 10000000; //host storage
		int bw = 10000;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our first machine

		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 5.30;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage=0.001;
		double costPerBw=0.001;		// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;
		double value1=0;
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
						indent + indent + dft.format(cloudlet.getExecStartTime())+ indent + indent + indent + dft.format(cloudlet.getFinishTime()));
			}
			value1= value1+Double.parseDouble(dft.format(cloudlet.getActualCPUTime()));
		}
		Log.printLine("================ Execution Result Ends here ==================");
		System.out.println("This (GA+PSO) schedule plan takes "+value1/10+" ms  to finish execution.");
	}
}
