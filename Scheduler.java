package Fitness_Function;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class Scheduler extends FitnessFunction {
    private static double[][] execTimeMatrix, communTimeMatrix;

    Scheduler() {
        super(false);
        initMatrices();
    }

    @Override
    public double evaluate(double[] position) {
        return calcMakespan(position);
    }

    private double calcTotalTime(double[] position) {
        double totalCost = 0;
        for (int i = 0; i < Constant.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            totalCost += execTimeMatrix[i][dcId] + communTimeMatrix[i][dcId];
        }
        return totalCost;
    }

    public double calcMakespan(double[] position) {
        double makespan = 0;
        double[] dcWorkingTime = new double[Constant.NO_OF_DATA_CENTERS];

        for (int i = 0; i < Constant.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            if(dcWorkingTime[dcId] != 0) --dcWorkingTime[dcId];
            dcWorkingTime[dcId] += execTimeMatrix[i][dcId] + communTimeMatrix[i][dcId];
            makespan = Math.max(makespan, dcWorkingTime[dcId]);
        }
        return makespan;
    }
    public double[][] getExecTimeMatrix()  { return execTimeMatrix; }
    public double[][] getCoumnTimeMatrix() { return communTimeMatrix; }
    
    private void initMatrices() {
        System.out.println("Initializing input matrices (e.g. exec time & communication time matrices");
        execTimeMatrix = new double[Constant.NO_OF_TASKS][Constant.NO_OF_DATA_CENTERS];
        communTimeMatrix = new double[Constant.NO_OF_TASKS][Constant.NO_OF_DATA_CENTERS];

        for (int i = 0; i < Constant.NO_OF_TASKS; i++) {
            for (int j = 0; j < Constant.NO_OF_DATA_CENTERS; j++) {
                execTimeMatrix[i][j] = Math.random() * 500;
                communTimeMatrix[i][j] = Math.random() * 500 + 20;
            }
        }
    }
}