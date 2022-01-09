package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class Population {
	public Individual population[];
	public double populationFitness = -1;

	public Population(int populationSize) {
		this.population = new Individual[populationSize];
	}

	public Population(int populationSize, int chromosomeLength) {
		this.population = new Individual[populationSize];

		for (int individualCount = 0; individualCount < populationSize; individualCount++) {
			Individual individual = new Individual(chromosomeLength);
			this.population[individualCount] = individual;
		}

		System.out.println("Population");
		for(int i=0;i<populationSize;i++) {
			System.out.print("Indi "+i+" -> ");
			for(int j=0;j<5;j++) {
				System.out.print(population[i].chromosome[j] + " ");
			}
			System.out.println();
		}

		
	}

	public Individual[] getIndividuals() {
		return this.population;
	}

	public Individual getFittest(int offset) {
		Arrays.sort(this.population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return 1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return -1;
				}
				return 0;
			}
		});
		return this.population[offset];
	}

	public void setPopulationFitness(double fitness) {
		this.populationFitness = fitness;
	}

	public double getPopulationFitness() {
		return this.populationFitness;
	}

	public int size() {
		return this.population.length;
	}

	public Individual setIndividual(int offset, Individual individual) {
		return population[offset] = individual;
	}

	public Individual getIndividual(int offset) {
		return population[offset];
	}

	public void shuffle() {
		Random rnd = new Random();
		for (int i = population.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Individual a = population[index];
			population[index] = population[i];
			population[i] = a;
		}
	}
}