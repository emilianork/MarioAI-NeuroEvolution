package neuroevolucion.GA;

import neuroevolucion.GA.GeneticAlgorithm;
import neuroevolucion.agent.NeuronalAgent;
import neuroevolucion.madeline.Madeline;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import java.util.ConcurrentModificationException;

public class Fitness extends Thread {
	
	NeuronalAgent agent;
	MarioAIOptions[] options;
	double eval;
	
	public Fitness(NeuronalAgent agent,
					MarioAIOptions[] options) {
		this.agent = agent;
		this.options = options;
		this.eval = 0.0;
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
			this.eval = fitness/length;
		} catch(ConcurrentModificationException cme) {
			System.exit(0);
		}
		return;
	}
	
	public double getFitness() {
		return eval;
	}
	
}