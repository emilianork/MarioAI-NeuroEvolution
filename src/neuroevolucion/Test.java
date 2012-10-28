package neuroevolucion;

import neuroevolucion.madeline.Madeline;
import neuroevolucion.agent.NeuronalAgent;

public class Test{
    public static void main(String[] args) {
        
        NeuronalAgent parent1 = new NeuronalAgent();
        NeuronalAgent parent2 = new NeuronalAgent();
        
        Madeline madeline1 = new Madeline(15,15,1);
        Madeline madeline2 = new Madeline(15,15,1);

        madeline1 = madeline1.cargaMadeline("Madeline1.txt");
        madeline2 = madeline2.cargaMadeline("Madeline2.txt");

        //madeline1.guardaMadeline("Madeline1");
        //madeline2.guardaMadeline("Madeline2");

        parent1.setMadeline(madeline1);
        parent2.setMadeline(madeline2);
        
        parent1.mutate(0.05);
        parent1.madeline.guardaMadeline("Mutado.txt");
        /*

        int[] points1 = parent1.generatePoints(4,15);
        int[] points2 = parent2.generatePoints(4,15);
        int[][] pointers = new int[2][4];
        pointers[0] = points1;
        pointers[1] = points2;
        
        for (int i = 0; i < points1.length; i++) {
            System.out.println(points1[i]);
        }
        
        System.out.println();
        for (int i = 0; i < points2.length; i++) {
            System.out.println(points2[i]);
        }
        
        NeuronalAgent[] sons = parent1.crossover(parent2,pointers);
        
        sons[0].madeline.guardaMadeline("Hijo1.txt");
        sons[1].madeline.guardaMadeline("Hijo2.txt");

        */
    }
}

