
package org.cloudbus.cloudsim.HSGA;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

public class Chromosomes{
	
	protected ArrayList<Gene> geneList;
	
	public Chromosomes(ArrayList<Gene> geneList){
		this.geneList=geneList;		
	}
	
	public ArrayList<Gene> getGeneList(){
		return this.geneList;
	}
	
	public void updateGene(int index,Vm vm){
		Gene gene=this.geneList.get(index);
		gene.setVmForGene(vm);
		this.geneList.set(index, gene);
	}
}
