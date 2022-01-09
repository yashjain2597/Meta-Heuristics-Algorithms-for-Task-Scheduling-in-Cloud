package Fitness_Function;

import net.sourceforge.jswarm_pso.Swarm;

import java.text.DecimalFormat;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;

import FitnessFunction.Scheduler;

public class PSO {
    
private static SchedulerParticle particles[];


    public static Scheduler ff = new Scheduler();

    private static Swarm swarm = new Swarm(Constant.POPULATION_SIZE, new SchedulerParticle(), ff);

    public PSO() 
    {

        initParticles();

    }

    public double[] run() {



        swarm.setMinPosition(0);

        swarm.setMaxPosition(Constant.NO_OF_DATA_CENTERS - 1);

        swarm.setMaxMinVelocity(0.5);

        swarm.setParticles(particles);

        swarm.setParticleUpdate(new SchedulerParticleUpdate(new SchedulerParticle()));



        for (int i = 0; i < 500; i++) {

            swarm.evolve();

            if (i % 10 == 0) {

                System.out.printf("Gloabl best at iteration (%d): %f\n", i, swarm.getBestFitness());

            }

        }



        System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + " Best makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));

         

        System.out.println("The best solution is: ");

        SchedulerParticle bestParticle = (SchedulerParticle) swarm.getBestParticle();

        System.out.println(bestParticle.toString());

        

        return swarm.getBestPosition();

    }

    

    public void printBestFitness() {

    	System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + " Best makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));

    }

    public double[][] getCommunTimeMatrix() { return ff.getCoumnTimeMatrix(); }

    public double[][] getExecTimeMatrix() { return ff.getExecTimeMatrix(); }

    private static void initParticles() {

        particles = new SchedulerParticle[Constant.POPULATION_SIZE];

        for (int i = 0; i < Constant.POPULATION_SIZE; ++i)

            particles[i] = new SchedulerParticle();

    }
}
