package org.cloudbus.cloudsim.examples;

import java.util.List;
import java.util.Random;

public class Swarm {

    private int numOfParticles, epochs;
    private double inertia, cognitiveComponent, socialComponent;
    public Vector bestPosition;
    private double bestEval;
    private int[][] particles;
    public static final double DEFAULT_INERTIA = 0.729844;
    public static final double DEFAULT_COGNITIVE = 1.496180; // Cognitive component.
    public static final double DEFAULT_SOCIAL = 1.496180; // Social component.
    public static List<cloudlet3> cloudletList;
	public static List<vm3> vmlist;


    public Swarm (int[][] particles, int epochs, int numOfParticles, List<cloudlet3> cloudletList, List<vm3> vmlist) {
    	this.numOfParticles = numOfParticles;
    	this.particles=particles;
    	this.epochs=epochs;
        this.cloudletList = cloudletList;
    	this.vmlist = vmlist;
        bestPosition = new Vector(0, 0, 0, 0, 0);
        bestEval = Double.POSITIVE_INFINITY;
        
    }

    public void run (int[][] individuals) {
        Particle[] particle=new Particle[numOfParticles];
        for(int i=0;i<numOfParticles;i++)
        {
        	int a = particles[i][0];
        	int b = particles[i][1];
        	int c = particles[i][2];
        	int d = particles[i][3];
        	int e = particles[i][4];
        	particle[i]=new Particle(a,b,c,d,e);
        	System.out.println("particle " + i + " =>" + (particle[i].position).toString());
        }

        double oldEval = bestEval;
        System.out.println("--------------------------EXECUTING-------------------------");
        

        for (int i = 0; i < epochs; i++) {

            if (bestEval < oldEval) {
                
                oldEval = bestEval;
            }
            

            for (int j=0;j<numOfParticles;j++) {
            	particle[j].updatePersonalBest();
                updateGlobalBest(particle[j]);
            }

            for (int j=0;j<numOfParticles;j++) {
                updateVelocity(particle[j]);
                particle[j].updatePosition();
            }
        }

        
        
        
        System.out.println("Final Best Evaluation: " + bestEval);
        System.out.println("---------------------------COMPLETE-------------------------");

    }


    private void updateGlobalBest (Particle particle) {
        if (particle.getBestEval() < bestEval) {
            bestPosition = particle.getBestPosition();
            bestEval = particle.getBestEval();
        }
    }


    private void updateVelocity (Particle particle) {
        Vector oldVelocity = particle.getVelocity();
        Vector pBest = particle.getBestPosition();
        Vector gBest = bestPosition.clone();
        Vector pos = particle.getPosition();

        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        Vector newVelocity = oldVelocity.clone();
        newVelocity.mul(inertia);

        pBest.sub(pos);
        pBest.mul(cognitiveComponent);
        pBest.mul(r1);
        newVelocity.add(pBest);

        gBest.sub(pos);
        gBest.mul(socialComponent);
        gBest.mul(r2);
        newVelocity.add(gBest);

        particle.setVelocity(newVelocity);
    }

}
