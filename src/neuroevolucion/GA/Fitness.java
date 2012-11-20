package neuroevolucion.GA;

import neuroevolucion.GA.GeneticAlgorithm;
import neuroevolucion.agent.NeuronalAgent;
import neuroevolucion.madeline.Madeline;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import java.util.ConcurrentModificationException;

public class Fitness extends Thread {
	
	int index;
	double[] fitnessTable;
	NeuronalAgent agent;
	MarioAIOptions[] options;
	
	public Fitness(int index, double[] fitnessTable, NeuronalAgent agent,
					MarioAIOptions[] options) {
		this.index = index;
		this.fitnessTable = fitnessTable;
		this.agent = agent;
		this.options = options;
	}
	
	public void run() {
		try {
			int fitness = 0;
			for (int i = 0; i < options.length; i++) {
				options[i].setAgent(agent);
				BasicTask task = new BasicTask(options[i]);
				task.doEpisodes(1,false,1);
				fitness += task.fitness();
			}
			double length = options.length;
			fitnessTable[index] = fitness/length;
		} catch(ConcurrentModificationException cme) {
			System.exit(0);
		}
		return;
	}
	
}