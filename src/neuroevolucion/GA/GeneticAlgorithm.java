package neuroevolucion.GA;

import neuroevolucion.agent.NeuronalAgent;
import neuroevolucion.madeline.Madeline;
import neuroevolucion.GA.Fitness;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;


import java.util.Random;
import java.util.Arrays;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.ConcurrentModificationException;

public class GeneticAlgorithm {

    //Variables de la poblacion.
    public int numberOfPopulation;
    public NeuronalAgent[] populationOld;
    public NeuronalAgent[] populationNew;

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
	public int pointsCrossover;
    
    //Variables de probabilidad;
    public double proMutation;
    public double proCrossover;

    //Variables Elitismo;
    public NeuronalAgent[] elitism;
    public int numberOfElitism;
    public double[] fitnessElitism;
    

    //Variables el mejor de todas las combinaciones
    public NeuronalAgent theBest;
    public double theBestFitness;

    //Variable de los entornos del juego;
    public MarioAIOptions[][] options;

	public Fitness[] fitnessThread;

    public GeneticAlgorithm(int numberOfPopulation, int inputs, int hiddenLayer,
                            int outputLayer,int numberParticipants,
                            int numberOfElitism, int maxGenerations, 
                            double proMutation, double proCrossover,
                            int pointsCrossover,
							String dificultad,String[] semillas,String malla) {
								
        this.numberOfPopulation = numberOfPopulation;
        this.inputs = inputs;
        this.hiddenLayer = hiddenLayer;
        this.outputLayer = outputLayer;
        this.numberParticipants = numberParticipants;
        this.numberOfElitism = numberOfElitism;
        this.maxGenerations = maxGenerations;
        this.proMutation = proMutation;
        this.proCrossover = proCrossover;
		this.pointsCrossover = pointsCrossover;
		this.generation = 0;
		
		
		// Genero los niveles de cada individuo 
		options = new MarioAIOptions[numberOfPopulation][semillas.length];
		
		
		for (int i = 0; i < numberOfPopulation; i++) {
			for(int j = 0;j < semillas.length;j++) {
				String[] nivel = {"-ls",semillas[j],"-ld",dificultad,"-vis","off","-rfw", malla,
				"-rfh",malla};
				options[i][j] = new MarioAIOptions(nivel);
			}
		}
		
        // Inicializamos los arreglos de todo.
        
        populationOld = new NeuronalAgent[numberOfPopulation];
        populationNew = new NeuronalAgent[numberOfPopulation];
        
        fitnessOld = new double[numberOfPopulation];
        fitnessNew = new double[numberOfPopulation];
        
		theBestFitness = -1.0;
		
        data = new double[maxGenerations][numberOfPopulation];
      
		fitnessElitism = new double[numberOfElitism];
        
        initializePopulation();
        
    }
    
	/*
	Corre el algoritmo genetico.
	*/
	public void run() {
		int index;
		for (int i = 1; i < this.maxGenerations; i++) {
			index = 0;
			for (int j = 0; j < this.numberOfPopulation/2; j++) {
				boolean crossover = true;
				NeuronalAgent parent1 = null;
				NeuronalAgent parent2 = null;
				NeuronalAgent son1 = null;
				NeuronalAgent son2 = null;
				
				while(crossover) {
					parent1 = tournament();
					parent2 = tournament();
					Random rand = new Random();
					if (this.proCrossover <= rand.nextDouble()){
						crossover = false;
						
						// Puede que este mal, por que no me acuerdo de como
						// funciona el metodo generatePoints(int numberOfPoints, int lengthLayer) 
						int[] pointsOfHiddenLayer = generatePoints(pointsCrossover,hiddenLayer*inputs);
						int[] pointsOfOutputLayer = generatePoints(pointsCrossover,hiddenLayer*outputLayer);
						
						int[][] points = new int[2][pointsCrossover + 1];
						points[0] = pointsOfHiddenLayer;
						points[1] = pointsOfOutputLayer;
						
						NeuronalAgent[] sons = parent1.crossover(parent2,points);
						son1 = sons[0];
						son2 = sons[1];
						
						son1 = son1.mutation(proMutation);
						son2 = son2.mutation(proMutation);
					}
				}
				populationNew[index] = son1;
				populationNew[index + 1] = son2;
				//fitnessNew[index] = fitness(son1,index);
				//fitnessNew[index + 1] = fitness(son2,index+1);
				index += 2;
			}
			evaluaPoblacion();
			setElitism();
			changePopulation();
			getElitism();
			getBest();
		}
		System.out.println("ACA PASO");
		saveData();
		return;
	}
	
	public void evaluaPoblacion() {
		fitnessThread = new Fitness[numberOfPopulation];
		fitnessNew =  new double[numberOfPopulation];
		for (int i = 0; i < numberOfPopulation; i++) {
			fitnessThread[i] = new Fitness(populationNew[i],options[i]); 
		}
		for (int i = 0; i < numberOfPopulation; i++) {
			try {
				fitnessThread[i].start();
			} catch (ConcurrentModificationException cme) {
				System.out.println("La evaluacion fallo:");
				System.out.println("Generacion = " + generation);
				System.out.println("Individuo = " + i);
				System.exit(0);	
			}
		}
		for (int i = 0; i < numberOfPopulation; i++) {
			try {
				fitnessThread[i].join();
				fitnessNew[i] = fitnessThread[i].getFitness();
			} catch (InterruptedException ie) {
				System.out.println("La evaluacion fallo:");
				System.out.println("Generacion = " + generation);
				System.out.println("Individuo = " + i);
				System.exit(0);
			}
		}
	}
	
	/*
	Coloca a los mejores de la generacion pasada al principio de la
	nueva generacion
	*/
	public void setElitism() {
		for(int i = 0; i < numberOfElitism; i++) {
			// Puede que aqui este mal, no estoy seguro.
			// se arregla con elitism[i].copy();
			populationNew[i] = elitism[i];
			fitnessNew[i] = fitnessElitism[i];
		}
	}
	
	/*
	Calcula el promedio de fitness de un individuo
	*/
   	
 	public double fitness(NeuronalAgent agent,int index) {
		int fitness = 0;
		for(int i = 0; i < options[index].length; i++) {
			(options[index][i]).setAgent(agent);
			BasicTask task = new BasicTask(options[index][i]);
			task.doEpisodes(1,false,1);
			fitness += task.fitness();
		}
		double length = options.length;
        return (fitness/length);
    }
    

	/*
	Genero la primera poblacion y la evaluo
	*/
    public void initializePopulation() {
        for(int i = 0; i < numberOfPopulation;i++) {
            NeuronalAgent agent = new NeuronalAgent();
            Madeline madeline = new Madeline(inputs,hiddenLayer,outputLayer);
            agent.setMadeline(madeline);
            populationNew[i] = agent;
        }
		/*
        for(int i = 0; i < numberOfPopulation;i++) {
            fitnessNew[i] = fitness(populationNew[i],i);
        }
		*/
		evaluaPoblacion();
        changePopulation();
		getElitism();
		getBest();
        return;
    }


	/*
	Agrega al arreglo elitism[] los mejores Agentes de la
	generacion anterior.
	*/
	public void getElitism() {
		int[] mejores = getMejores(numberOfElitism);
		
		elitism = new NeuronalAgent[numberOfElitism];
		fitnessElitism = new double[numberOfElitism];
 		
		for (int i = 0; i < numberOfElitism; i++) {
			elitism[i] = populationOld[mejores[i]].copy();
			fitnessElitism[i] = fitnessOld[mejores[i]];
		}
		return;
	}
	
	/*
	Obtiene los indices de los mejores individuos de la poblacion.
	*/
	public int[] getMejores(int numberOfElitism) {
		int[] mejores = new int[numberOfElitism];
		double[] fitnessPopulation = new double[numberOfPopulation];
		//Copio el arreglo para trabajar con el
		for (int i = 0; i < numberOfPopulation; i++) {
			fitnessPopulation[i] = fitnessOld[i]; 
		}
		//Ordeno
		Arrays.sort(fitnessPopulation);
		
		for(int i = 0; i < mejores.length; i++) {
			for(int j = 0; j < fitnessPopulation.length; j++) {
				if (fitnessPopulation[fitnessPopulation.length - (i + 1)] == fitnessOld[j]) {
					mejores[i] = j;
				}
			}
		}
		return mejores;
	}
	
	/*
	Si existe un mejor individuo entonces lo agrego a la poblacion.
	*/
	public void getBest() {
		int index = -1; 
		for(int i = 0; i < numberOfPopulation; i++) {
			if (fitnessOld[i] > theBestFitness) {
				index = i;
				theBestFitness = fitnessOld[i];
			}
		}
		if (index != -1) {
			theBest = populationOld[index].copy();
		}
		return;
	}


	/*
	Por medio de torneo obtengo a un padre;
	*/
	public NeuronalAgent tournament() {
		int[] participantsIndex = new int[numberParticipants];
		Random random = new Random();
		
		for(int i = 0; i < numberParticipants;i++) {
			participantsIndex[i] = random.nextInt(numberOfPopulation);
		}
		
		int indexWinner = participantsIndex[0];
		for(int i = 1; i < numberParticipants; i++) {
			if (fitnessOld[indexWinner] < fitnessOld[participantsIndex[i]]) {
				indexWinner = participantsIndex[i];
			}
		}
		return populationOld[indexWinner];
	}
	/*
	La nueva poblacion ahora es la vieja poblacion
	Agrego su fitness al arreglo de datos de todas las
	generaciones y reinicio los arreglos del fitness
	y la poblacion de la nueva.
	
	*/
    public void changePopulation() {
        populationOld = populationNew;
   		fitnessOld = new double[numberOfPopulation];
        for(int i = 0; i < numberOfPopulation;i++) {
			fitnessOld[i] = fitnessNew[i]; 
			data[generation][i] = fitnessNew[i];
		}
        fitnessNew = new double[numberOfPopulation];
 		populationNew = new NeuronalAgent[numberOfPopulation];
		generation += 1;
    }

    /*
      numberOfPoints => son el numero de puntos de cruza que se desean generar.
      lengthLayer => es la longitud de la capa Oculta o la de Salida.
     */
    public int[] generatePoints(int numberOfPoints, int lengthLayer) {
        
        int[] points = new int[numberOfPoints + 1];
        
        int actualPosition = 0;
        
        for(int i = 0 ; i < numberOfPoints; i++) {
            Random random = new Random();
            int newPoint = lengthLayer - actualPosition;
            points[i] = newPoint > 0 ? random.nextInt(lengthLayer - actualPosition) 
                + actualPosition : lengthLayer;
            actualPosition = points[i] + 1;
        }
        points[numberOfPoints] = lengthLayer;
        
        return points;
    }

	public void saveData() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("MarioAI.data"));
			for (int i = 0; i < maxGenerations; i++) {
				String generationFitness = "";
				for (int j = 0; j < numberOfPopulation;j++) {
					generationFitness += data[i][j] + " ";
				}
				writer.write(generationFitness+"\n");
			} 
			writer.close();
		} catch (IOException ioe) {
			System.out.println("No se guardo nada");
		}
	
	}
    
}
