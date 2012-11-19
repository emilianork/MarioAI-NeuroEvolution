package neuroevolucion;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import neuroevolucion.agent.NeuronalAgent;
import neuroevolucion.madeline.Madeline;
import neuroevolucion.GA.GeneticAlgorithm;

public final class Main {
	
	public static void main(String[] args) {
		
		String dificultad = "1";
		String[] semillas = {"1","2","3","4","5","6","7","8","9","10"};
	
		String[][] niveles = new String[semillas.length][6];

		MarioAIOptions[] options = new MarioAIOptions[semillas.length];
		
		int numberOfPopulation = 100;
		int inputs = 19*19 + 6;
		int hiddenLayer = 4;
		int outputLayer = 6;
		int numberParticipants = 6; 
		int numberOfElitism = 4;
		int maxGenerations = 10;
		double proMutation = 0.01;
		double proCrossover = 0.85;
		int pointsCrossover = 1;
		
		// Genero los arreglos de argumentos de los niveles;
		for(int i = 0; i < semillas.length;i++){
			String[] nivel = {"-ls",semillas[i],"-ld",dificultad,"-vis","off"};
			niveles[i] = nivel;
		}
		
		for(int i = 0; i < niveles.length;i++) {
			options[i] = new MarioAIOptions(niveles[i]);
		}
		
		GeneticAlgorithm genetic = new GeneticAlgorithm(numberOfPopulation,inputs,hiddenLayer,
														outputLayer, numberParticipants,
														numberOfElitism, maxGenerations,
														proMutation, proCrossover, options,
														pointsCrossover);
		genetic.run();
	}
}