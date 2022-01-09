package org.cloudbus.cloudsim.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

public class GeneticAlgorithm {
	private int populationSize;
	public static List<cloudlet3> cloudletList;
	public static List<vm3> vmlist;

	private double mutationRate;

	private double crossoverRate;

	private int elitismCount;

	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, List<cloudlet3> cloudletList, List<vm3> vmlist) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.cloudletList = cloudletList;
		this.vmlist = vmlist;
	}

	public Population initPopulation(int chromosomeLength) {
		Population population = new Population(this.populationSize, chromosomeLength);
		return population;
	}

	public double calcFitness(Individual individual) {

		double cost = 0.0;
		double computationCost = 0;
		for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
			double[] executionCostArray = cloudletList.get(geneIndex).executioncost;
			computationCost += executionCostArray[individual.chromosome[geneIndex]];
		}
		double communicationCost = 0;

		ArrayList<Integer> edgesFrom = new ArrayList<Integer>();
		ArrayList<Integer> edgesTo = new ArrayList<Integer>();
		
		edgesFrom.add(1); edgesTo.add(2);
		edgesFrom.add(1); edgesTo.add(3);
		edgesFrom.add(1); edgesTo.add(4);
		edgesFrom.add(2); edgesTo.add(5);
		edgesFrom.add(3); edgesTo.add(5);
		edgesFrom.add(4); edgesTo.add(5);
		
		for(int i=0; i< edgesFrom.size();i++) {
			int edgeFrom = edgesFrom.get(i), edgeTo = edgesTo.get(i);
			int processorI = individual.chromosome[edgeFrom - 1];
			int processorJ = individual.chromosome[edgeTo - 1];
			
			int dataSize = 10;
			double[] communicationCostArray = vmlist.get(processorI).comcost;
			communicationCost += dataSize * communicationCostArray[processorJ];
		}
		cost = computationCost + communicationCost;

		double fitness =  cost;
		individual.setFitness(fitness);

		return fitness;
	}

	public void evalPopulation(Population population) {
		double populationFitness=0;

		for (Individual individual : population.getIndividuals()) {

			double individualFitness = calcFitness(individual); 
			individual.setFitness(individualFitness);
			populationFitness+=individualFitness;
		
		}
		population.setPopulationFitness(populationFitness);

	}

	public Individual selectParent(Population population) {
		Individual individuals[] = population.getIndividuals();
		double populationFitness = population.getPopulationFitness();
		double rouletteWheelPosition = Math.random() * populationFitness;

		double spinWheel = 0;
		for (Individual individual : individuals) {
			spinWheel += individual.getFitness();
			if (spinWheel >= rouletteWheelPosition) {
				return individual;
			}
		}
		return individuals[population.size() - 1];
	}


	public Population crossoverPopulation(Population population) {
		Population newPopulation = new Population(population.size());

		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual parent1 = population.getFittest(populationIndex);

			if (this.crossoverRate > Math.random()&& populationIndex > this.elitismCount ) {

				Individual offspring = new Individual(parent1.getChromosomeLength());
				Individual parent2 = selectParent(population);

				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					if (0.5 > Math.random()) {
						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
					} else {
						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
					}
				}

				newPopulation.setIndividual(populationIndex, offspring);
			} else {
				newPopulation.setIndividual(populationIndex, parent1);
			}
		}

		return newPopulation;
	}

	public Population mutatePopulation(Population population) {
		Population newPopulation = new Population(this.populationSize);

		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);
			for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
				
				if (populationIndex > this.elitismCount) {
				
					if (this.mutationRate > Math.random()) {
				
						int newGene=0;
						if (individual.getGene(geneIndex) == 0) {
							double r=Math.random();
							if(r>0.5)
							{
								newGene=1;
							}
							else
								newGene=2;
						}
						else if (individual.getGene(geneIndex) == 1) {
							double r=Math.random();
							if(r>0.5)
							{
								newGene=2;
							}
							else
								newGene=0;
						}
						else if (individual.getGene(geneIndex) == 2) {
							double r=Math.random();
							if(r>0.5)
							{
								newGene=0;
							}
							else
								newGene=1;
						}
				
						individual.setGene(geneIndex, newGene);
					}
				}
			}

			newPopulation.setIndividual(populationIndex, individual);
		}

		return newPopulation;
	}

}
