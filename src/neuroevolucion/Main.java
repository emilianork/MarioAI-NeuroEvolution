package neuroevolucion;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import neuroevolucion.agent.NeuronalAgent;
import neuroevolucion.madeline.Madeline;

public final class Main {
	
	public static void main(String[] args) {
		
		String dificultad = "1";
		String[] semillas = {"1","2","3","4","5","6","7","8","9","10"};
	
		String[][] niveles = new String[semillas.length][6];

		MarioAIOptions[] options = new MarioAIOptions[semillas.length];
		
		// Genero los arreglos de argumentos de los niveles;
		for(int i = 0; i < semillas.length;i++){
			String[] nivel = {"-ls",semillas[i],"-ld",dificultad,"-vis","off"};
			niveles[i] = nivel;
		}
		
		for(int i = 0; i < niveles.length;i++) {
			options[i] = new MarioAIOptions(niveles[i]);
		}
	}
}