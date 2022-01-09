package org.cloudbus.cloudsim.HSGA;

import org.cloudbus.cloudsim.core.SimEntity;

public class Gene{

	private Cloudlet task;
	private Vm vm;
	public Gene(Cloudlet cl, Vm v)
	{
		this.task=cl;
		this.vm=v;
	}
	public Cloudlet getCloudletFromGene()
	{
		return this.task;
	}
	public Vm getVmFromGene()
	{
		return this.vm;
	}
	public void setCloudletForGene(Cloudlet cl)
	{
		this.task=cl;
	}
	public void setVmForGene(Vm vm)
	{
		this.vm=vm;
	}
	
}
