
package neuroevolucion.agent;

import neuroevolucion.madeline.Madeline;
import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;

//Para generar puntos de forma random.
import java.util.Random;
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
      Dado un arreglo de puntos genero dos nuevos hijos.
      Siempre deben de terminar con la ultima posicion.
     */
    public NeuronalAgent[] crossover(NeuronalAgent parent2,int[][] points) {
        Madeline network1 = this.getMadeline();
        Madeline network2 = parent2.getMadeline();
        
        int[] pointsHideLayer = points[0];
        int[] pointsOutputLayer = points[1];

        Madeline son1 = new Madeline(network1.entradas,
                                     network1.capaOculta.length,
                                     network1.capaSalida.length);
        
        Madeline son2 = new Madeline(network2.entradas,
                                     network2.capaOculta.length,
                                     network2.capaSalida.length);
        
        boolean whichSon = false;
        
        int numberInputs = network1.capaOculta[0].weights.length;
        int numberHidenLayer = network1.capaOculta.length;

        int position = 0;
        
        for(int i = 0; i < pointsHideLayer.length; i++) {
            
            whichSon = whichSon ? false : true;
            
            for (int j = position; j < pointsHideLayer[i]; j++,position++) {
                int adelinePosition = j / numberInputs;
                int weightPosition = j - adelinePosition*numberInputs;
                
                if (whichSon) {
                   
                    son1.capaOculta[adelinePosition].weights[weightPosition] = 
                        network1.capaOculta[adelinePosition].weights[weightPosition];
                    
                    son2.capaOculta[adelinePosition].weights[weightPosition] = 
                        network2.capaOculta[adelinePosition].weights[weightPosition];
                } else {
                    son1.capaOculta[adelinePosition].weights[weightPosition] = 
                        network2.capaOculta[adelinePosition].weights[weightPosition];
                    son2.capaOculta[adelinePosition].weights[weightPosition] = 
                        network1.capaOculta[adelinePosition].weights[weightPosition];
                }
            }
        } 

        whichSon = false;
        position = 0;

        for (int i = 0; i < pointsOutputLayer.length; i++) {
            whichSon = whichSon ? false : true;
            
            for (int j = position; j < pointsOutputLayer[i]; j++, position++) {
                int adelinePosition = j / numberHidenLayer;
                int weightPosition = j - adelinePosition * numberHidenLayer;
                
                if (whichSon) {
                    
                    son1.capaSalida[adelinePosition].weights[weightPosition] = 
                        network1.capaSalida[adelinePosition].weights[weightPosition];

                    son2.capaSalida[adelinePosition].weights[weightPosition] =
                        network2.capaSalida[adelinePosition].weights[weightPosition];

                } else {
                    
                    son1.capaSalida[adelinePosition].weights[weightPosition] =
                        network2.capaSalida[adelinePosition].weights[weightPosition];

                    son2.capaSalida[adelinePosition].weights[weightPosition] = 
                        network1.capaSalida[adelinePosition].weights[weightPosition];
                }

            }
        }
        

        NeuronalAgent[] sons = new NeuronalAgent[2];
        NeuronalAgent result1 = new NeuronalAgent();
        result1.setMadeline(son1);
        NeuronalAgent result2 = new NeuronalAgent();
        result2.setMadeline(son2);
        sons[0] = result1;
        sons[1] = result2;
        return sons;
    }

    public NeuronalAgent mutate(double probability) {
       
        Madeline madeline = this.madeline;
        for (int i = 0; i < madeline.capaOculta.length;i++) {
            for (int j = 0; j < madeline.capaOculta[i].weights.length;j++) {
                Random random = new Random();
                if (random.nextDouble() <= probability) {
                    int n = madeline.capaOculta[i].weights.length;
                    madeline.capaOculta[i].weights[j] = random.nextBoolean() ?
                        ((-2.4 * random.nextDouble())/n) :
                        ((2.4 * random.nextDouble())/n);
                }
            }
        }

        for (int i = 0; i < madeline.capaSalida.length;i++) {
            for (int j = 0; j < madeline.capaSalida[i].weights.length;j++) {
                Random random = new Random();
                if (random.nextDouble() <= probability) {
                    int n = madeline.capaSalida[i].weights.length;
                    madeline.capaSalida[i].weights[j] =  random.nextBoolean() ?
                        ((-2.4 * random.nextDouble())/n) :
                        ((2.4 * random.nextDouble())/n); 
                }
            }
        }
        return this;
    }


    // COPIAR METODO A LA CLASE DEL ALGORITMO GENETICO.
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
}
