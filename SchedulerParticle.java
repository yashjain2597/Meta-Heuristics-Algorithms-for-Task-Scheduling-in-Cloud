package Fitness_Function;

import net.sourceforge.jswarm_pso.Particle;



import java.util.Random;



public class SchedulerParticle extends Particle {

    SchedulerParticle() {

        super(Constant.NO_OF_TASKS);

        double[] position = new double[Constant.NO_OF_TASKS];

        double[] velocity = new double[Constant.NO_OF_TASKS];



        for (int i = 0; i < Constant.NO_OF_TASKS; i++) {

            Random randObj = new Random();

            position[i] = randObj.nextInt(Constant.NO_OF_DATA_CENTERS);

            velocity[i] = Math.random();

        }

        setPosition(position);

        setVelocity(velocity);

    }

    

    @Override

    public String toString() {

        String output = "";

        for (int i = 0; i < Constant.NO_OF_DATA_CENTERS; i++) {

            String tasks = "";

            int no_of_tasks = 0;

            for (int j = 0; j < Constant.NO_OF_TASKS; j++) {

                if (i == (int) getPosition()[j]) {

                    tasks += (tasks.isEmpty() ? "" : " ") + j;

                    ++no_of_tasks;

                }

            }

            if (tasks.isEmpty()) output += "There is no tasks associated to Data Center " + i + "\n";

            else

                output += "There are " + no_of_tasks + " tasks associated to Data Center " + i + " and they are " + tasks + "\n";

        }

        return output;

    }

}
