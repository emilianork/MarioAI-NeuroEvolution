package neuroevolucion;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import neuroevolucion.agent.NeuronalAgent;
import neuroevolucion.madeline.Madeline;


public final class Test1 {
	public static void main(String[] args) {
		MarioAIOptions marioAIOptions = new MarioAIOptions(args);
		NeuronalAgent agent = new NeuronalAgent();
		Madeline madeline = new Madeline(19*19 + 6,4,6);
		agent.setMadeline(madeline);
		agent.setZLevel(0);
		marioAIOptions.setAgent(agent);
    	BasicTask basicTask = new BasicTask(marioAIOptions);
    	basicTask.doEpisodes(1,false,1);
		System.out.println(basicTask.fitness());
	}
}