package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.Random;

class Particle {

    public Vector position;        
    public Vector velocity;
    public Vector bestPosition;    
    public double bestEval;        

    
    
    Particle (int a,int b,int c,int d,int e) {
        
        position = new Vector(a,b,c,d,e);
        velocity = new Vector();
        
        bestPosition = position.clone();
        bestEval = eval();
    }

    private double eval () {
    	
    	double cost = 0.0;
		double computationCost = 0;
		double[] executionCostArray = Swarm.cloudletList.get(0).executioncost;
		computationCost += executionCostArray[position.a];
		executionCostArray = Swarm.cloudletList.get(1).executioncost;
		computationCost += executionCostArray[position.b];
		executionCostArray = Swarm.cloudletList.get(2).executioncost;
		computationCost += executionCostArray[position.c];
		executionCostArray = Swarm.cloudletList.get(3).executioncost;
		computationCost += executionCostArray[position.d];
		executionCostArray = Swarm.cloudletList.get(4).executioncost;
		computationCost += executionCostArray[position.e];
		
		double communicationCost = 0;
		int[] data = Swarm.cloudletList.get(1).datasize;
		
		double[] communicationCostArray = Swarm.vmlist.get(position.a).comcost;
		communicationCost+=data[0]*communicationCostArray[position.b];
		data = Swarm.cloudletList.get(2).datasize;
		
		communicationCostArray = Swarm.vmlist.get(position.b).comcost;
		communicationCost+=data[0]*communicationCostArray[position.c];
		data = Swarm.cloudletList.get(3).datasize;
		
		communicationCostArray = Swarm.vmlist.get(position.c).comcost;
		communicationCost+=data[0]*communicationCostArray[position.d];
		data = Swarm.cloudletList.get(4).datasize;
		
		communicationCostArray = Swarm.vmlist.get(position.d).comcost;
		communicationCost+=data[0]*communicationCostArray[position.e];
		
		cost = computationCost + communicationCost;
		return cost;
    }


    void updatePersonalBest () {
        double eval = eval();
        if (eval < bestEval) {
            bestPosition = position.clone();
            bestEval = eval;
        }
    }

    Vector getPosition () {
        return position.clone();
    }

    Vector getVelocity () {
        return velocity.clone();
    }

    Vector getBestPosition() {
        return bestPosition.clone();
    }

    double getBestEval () {
        return bestEval;
    }

    void updatePosition () {
        this.position.add(velocity);
    }

    void setVelocity (Vector velocity) {
        this.velocity = velocity.clone();
    }

    
}
