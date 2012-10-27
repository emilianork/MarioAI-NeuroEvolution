package neuroevolucion;

import neuroevolucion.madeline.Madeline;
import neuroevolucion.agent.NeuronalAgent;

public class Test{
    public static void main(String[] args) {
        
        NeuronalAgent parent1 = new NeuronalAgent();
        NeuronalAgent parent2 = new NeuronalAgent();
        
        Madeline madeline1 = new Madeline(1,1,1);
        Madeline madeline2 = new Madeline(1,1,1);

        madeline1 = madeline1.cargaMadeline("Madeline1.txt");
        madeline2 = madeline2.cargaMadeline("Madeline2.txt");

        madeline1.guardaMadeline("Madeline1");
        madeline2.guardaMadeline("Madeline2");

        parent1.setMadeline(madeline1);
        parent2.setMadeline(madeline2);

        int[][]  points = new int[2][2];
        points[0][0] = 3;
        points[0][1] = 4;
        points[1][0] = 1;
        points[1][1] = 2;
        NeuronalAgent[] sons = parent1.crossover(parent2,points);
        
        sons[0].madeline.guardaMadeline("Hijo1.txt");
        sons[1].madeline.guardaMadeline("Hijo2.txt");

        System.out.println("ASD");
    }
}

