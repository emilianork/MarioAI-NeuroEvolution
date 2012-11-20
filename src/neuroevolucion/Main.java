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
		
		int numberOfPopulation = 2;
		int inputs = 5*5 + 6;
		int hiddenLayer = 4;
		int outputLayer = 6;
		int numberParticipants = 6; 
		int numberOfElitism = 4;
		int maxGenerations = 3;
		double proMutation = 0.01;
		double proCrossover = 0.85;
		int pointsCrossover = 2;

		
		GeneticAlgorithm genetic = new GeneticAlgorithm(numberOfPopulation,inputs,hiddenLayer,
														outputLayer, numberParticipants,
														numberOfElitism, maxGenerations,
														proMutation, proCrossover,
														pointsCrossover,dificultad,semillas);
		genetic.run();
	}
}