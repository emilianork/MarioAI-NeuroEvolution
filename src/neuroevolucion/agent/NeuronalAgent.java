
package neuroevolucion.agent;

import neuroevolucion.madeline.Madeline;
import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;

/*
  Agente que controla al persona de Mario Bros. Debe de implementar
  la interface de Agent  que indica que metodos debe de tener
  y heredo de BasicMarioAIAgent para no tener que escribir tantos
  metodos vacios.
 */

public class NeuronalAgent extends BasicMarioAIAgent implements Agent {
    
    //Objeto que representa la red neuronal.
    public Madeline madeline;
    
    public NeuronalAgent() {
    	super("NeuronalAgent");
    	reset();
    }
    
    public void reset() {
        // Reseto lo que haga falta.
    }
    
    /*
      Dada las entradas, genero una salida.
      Es aqui donde la red neuronal funciona.
     */
    public boolean[] getAction() {
	boolean[] ret = new boolean[Environment.numberOfKeys];
        return ret;
    }
    
    /*
      Si el Agente que contrui debe de tener una red neuronal que
      no sea random, aqui la agrego.
     */
    
    public void setMadeline(Madeline madeline) {
        this.madeline = madeline;
    }

    /*
      Creo una copia de la red neuronal, por si la altero, no afecte a la red
      neuronal actual.
     */
    public Madeline getMadeline() {
        Madeline copy = this.madeline.copy();
        return copy;
    }
    
    /*
      Dado un arreglo de puntos genero dos nuevos hijos
     */
    public NeuronalAgent[] crossover(NeuronalAgent parent2,int[] points) {
        NeuronalAgent parent1 = this;
        NeuronalAgent[] sons = new NeuronalAgent[2];
        return sons;
    }
}
