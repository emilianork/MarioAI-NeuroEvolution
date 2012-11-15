package neuroevolucion.GA;

import neuroevolucion.agent.NeuronalAgent;
import neuroevolucion.madeline.Madeline;
import java.util.ArrayList;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

public class GeneticAlgorithm {

    //Variables de la poblacion.
    public int numberOfPopulation;
    public ArrayList<NeuronalAgent> populationOld;
    public ArrayList<NeuronalAgent> populationNew;

    public double[] fitnessOld;
    public double[] fitnessNew;
   
    public double[][] data;
    
    public int maxGenerations;
    public int generation;
    
    //Variables de los agentes
    public int inputs;
    public int hiddenLayer;
    public int outputLayer;
    
    //Variables del Torneo;
    public int numberParticipants;
    public ArrayList<NeuronalAgent> participants;
    
    //Variables de probabilidad;
    public double proMutation;
    public double proCrossover;
    public double proMutationInside;

    //Variables Elitismo;
    public ArrayList<NeuronalAgent> elitism;
    public int numberOfElitism;
    public double[] fitnessElitism;
    

    //Variables el mejor de todas las combinaciones
    public NeuronalAgent theBest;
    public double theBestFitness;

    //Variable de los entornos del juego;
    public ArrayList<MarioAIOptions> options;

    public GeneticAlgorithm(int numberOfPopulation, int inputs, int hiddenLayer,
                            int outputLayer,int numberParticipants,
                            int numberOfElitism, int maxGenerations, 
                            double proMutation, double proCrossover,
                            double proMutationInside,
                            ArrayList<MarioAIOptions> options) {
        this.numberOfPopulation = numberOfPopulation;
        this.inputs = inputs;
        this.hiddenLayer = hiddenLayer;
        this.outputLayer = outputLayer;
        this.numberParticipants = numberParticipants;
        this.numberOfElitism = numberOfElitism;
        this.maxGenerations = maxGenerations;
        this.proMutation = proMutation;
        this.proCrossover = proCrossover;
        this.proMutationInside = proMutationInside;
        this.options = options;
        // Inicializamos los arreglos de todo.
        
        populationOld = new ArrayList<NeuronalAgent>(numberOfPopulation);
        populationNew = new ArrayList<NeuronalAgent>(numberOfPopulation);
        
        fitnessOld = new double[numberOfPopulation];
        fitnessNew = new double[numberOfPopulation];
        
        data = new double[maxGenerations][numberOfPopulation];
        
        participants = new ArrayList<NeuronalAgent>(numberParticipants);
        
        elitism = new ArrayList<NeuronalAgent>(elitism);
        
        initializePopulation();
        
    }
    
    public double fitness(NeuronalAgent agent) {
		
        return 0;
    }
    
    public void initializePopulation() {
        for(int i = 0; i < numberOfPopulation;i++) {
            NeuronalAgent agent = new NeuronalAgent();
            Madeline madeline = new Madeline(inputs,hiddenLayer,outputLayer);
            agent.setMadeline(madeline);
            populationNew.add(i,agent);
        }

        for(int i = 0; i < numberOfPopulation;i++) {
            fitnessNew[i] = fitness(populationNew.get(i));
        }
        changePopulation();
        return;
    }

    public void changePopulation() {
        
    }
    
}
