
package neuroevolucion.agent;

import neuroevolucion.madeline.Madeline;
import neuroevolucion.madeline.Adeline;
import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;


import ch.idsia.benchmark.mario.engine.sprites.Mario;
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
	
	public int zlevelScene;
    
	//Mario Status
	int marioStatus;
	int marioMode;
	int isMarioOnGround;
	int isMarioAbleToJump;
	int isMarioAbleToShoot;
	int isMarioCarrying;
	//int getTimeLeft;
	
	int zLevelScene;
		
    public NeuronalAgent() {
    	super("NeuronalAgent");
    	reset();
    }
    
    public void reset() {
        action = new boolean[Environment.numberOfKeys];
    }
    
	public void integrateObservation(Environment environment) {
	    
		mergedObservation = environment.getMergedObservationZZ(this.zLevelScene, this.zLevelScene);
		int[] marioState = environment.getMarioState();
		
	    marioStatus = marioState[0];
	    marioMode = marioState[1];
	    isMarioOnGround = marioState[2];
	    isMarioAbleToJump = marioState[3];
	    isMarioAbleToShoot = marioState[4];
	    isMarioCarrying = marioState[5];
	    //getKillsTotal = marioState[6];
	    //getKillsByFire = marioState[7];
	    //getKillsByStomp = marioState[8];
	    //getKillsByShell = marioState[9];
		//getTimeLeft = marioState[10];

	    receptiveFieldWidth = environment.getReceptiveFieldWidth();
	    receptiveFieldHeight = environment.getReceptiveFieldHeight();
	
		double[] entradas = new double[receptiveFieldWidth * receptiveFieldHeight + 11];
		
		for(int i = 0; i < mergedObservation.length; i++) {
			int pos = mergedObservation[i].length * i;
			for(int j = 0; j < mergedObservation[i].length; j++) {
				entradas[pos+j] = mergedObservation[i][j];
			}
		}
		
		int length = entradas.length;
		
		entradas[length - 1] = marioStatus;
		entradas[length - 2] = marioMode;
		entradas[length - 3] = isMarioOnGround;
		entradas[length - 4] = isMarioAbleToJump;
		entradas[length - 5] = isMarioAbleToShoot;
		entradas[length - 6] = isMarioCarrying;
		//entradas[length - 7] = getKillsTotal;
		//entradas[length - 8] = getKillsByFire;
		//entradas[length - 9] = getKillsByStomp;
		//entradas[length - 10] = getKillsByShell;
		//entradas[length - 11] = getTimeLeft;
		
		double[] salidas = this.madeline.procesa(entradas);
	
		//System.out.println("\n\tSalidas:");
		for(int i = 0; i < Environment.numberOfKeys; i++) {
			//System.out.println("\t" + salidas[i]);
			action[i] = salidas[i] > 0.5;
		}
		if (salidas[Mario.KEY_LEFT] < salidas[Mario.KEY_RIGHT]) {
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_LEFT] = false;
		} else {
			action[Mario.KEY_LEFT] = true;
			action[Mario.KEY_RIGHT] = false;
		}
		if (salidas[Mario.KEY_JUMP] >= 0.5) {
			boolean jump = isMarioAbleToJump == 1;
			boolean ground = isMarioOnGround == 1;
			action[Mario.KEY_JUMP] = jump || !ground;
			//action[Mario.KEY_JUMP] = true;
		} else {
			//action[Mario.KEY_JUMP] = false;
		}
		action[Mario.KEY_UP] = false;
	}
	
	
    /*
	Regreso las salidas que en el metodo anterior genero.
     */
    public boolean[] getAction() {
		return this.action;
    }
    
    /*
      Si el Agente que contrui debe de tener una red neuronal que
      no sea random, aqui la agrego.
     */
    
    public void setMadeline(Madeline madeline) {
        this.madeline = madeline;
    }
	
	public void setZLevel(int i) {
		this.zlevelScene = i;
	}

    /*
      Creo una copia de la red neuronal, por si la altero, no afecte a la red
      neuronal actual.
     */
    public Madeline getMadeline() {
        Madeline copy = this.madeline.copy();
        return copy;
    }

	public NeuronalAgent copy() {
		NeuronalAgent agent = new NeuronalAgent();
		agent.setMadeline(this.getMadeline());
		return agent;
		
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
        
        int numberInputs = network1.capaOculta[0].weights.length + 1;
        int numberHidenLayer = network1.capaSalida[0].weights.length + 1;

        int position = 0;
        
        for(int i = 0; i < pointsHideLayer.length; i++) {
            
            whichSon = whichSon ? false : true;
            
            for (int j = position; j < pointsHideLayer[i]; j++,position++) {
                int adelinePosition = j / numberInputs;
                int weightPosition = j - adelinePosition*numberInputs;
                
                if (whichSon) {
					
					if (((j + 1)/numberInputs) == adelinePosition) {
                   
                    	son1.capaOculta[adelinePosition].weights[weightPosition] = 
                        	network1.capaOculta[adelinePosition].weights[weightPosition];
                    
                    	son2.capaOculta[adelinePosition].weights[weightPosition] = 
                        	network2.capaOculta[adelinePosition].weights[weightPosition];
					} else {
						son1.capaOculta[adelinePosition].theta = 
							network1.capaOculta[adelinePosition].theta;
						
						son2.capaOculta[adelinePosition].theta = 
							network2.capaOculta[adelinePosition].theta;
					}
                } else {
					
					if (((j + 1)/numberInputs) == adelinePosition) {
					
                    	son1.capaOculta[adelinePosition].weights[weightPosition] = 
                        	network2.capaOculta[adelinePosition].weights[weightPosition];
                    	son2.capaOculta[adelinePosition].weights[weightPosition] = 
                        	network1.capaOculta[adelinePosition].weights[weightPosition];
					} else {
						son1.capaOculta[adelinePosition].theta = 
							network2.capaOculta[adelinePosition].theta;
						
						son2.capaOculta[adelinePosition].theta = 
							network1.capaOculta[adelinePosition].theta;
					
					}
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
                    
					if (((j + 1)/numberHidenLayer) == adelinePosition) {
                    	son1.capaSalida[adelinePosition].weights[weightPosition] = 
                        	network1.capaSalida[adelinePosition].weights[weightPosition];

                    	son2.capaSalida[adelinePosition].weights[weightPosition] =
                        	network2.capaSalida[adelinePosition].weights[weightPosition];
					} else {
						son1.capaSalida[adelinePosition].theta = 
							network1.capaSalida[adelinePosition].theta;
						
						son2.capaSalida[adelinePosition].theta = 
							network2.capaSalida[adelinePosition].theta;
					}

                } else {
                    if (((j + 1)/numberHidenLayer) == adelinePosition) {
                   		son1.capaSalida[adelinePosition].weights[weightPosition] =
                     		network2.capaSalida[adelinePosition].weights[weightPosition];

                    	son2.capaSalida[adelinePosition].weights[weightPosition] = 
                        	network1.capaSalida[adelinePosition].weights[weightPosition];
					} else {
						son1.capaSalida[adelinePosition].theta = 
							network2.capaSalida[adelinePosition].theta;
						
						son2.capaSalida[adelinePosition].theta = 
							network1.capaSalida[adelinePosition].theta;
					}
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

    public NeuronalAgent mutation(double probability) {
       
        Madeline madeline = this.madeline;
        for (int i = 0; i < madeline.capaOculta.length;i++) {
            for (int j = 0; j < madeline.capaOculta[i].weights.length;j++) {
                Random random = new Random();
                if (random.nextDouble() <= probability) {
                    int n = madeline.capaOculta[i].weights.length;
                    madeline.capaOculta[i].weights[j] = random.nextBoolean() ?
			         	//(-1 * random.nextInt(RANGO)) :
						//random.nextInt(RANGO);
						((-2.4 * random.nextDouble())/n) :
			            ((2.4 * random.nextDouble())/n);
                }
			
            }
			Random random = new Random();
            if (random.nextDouble() <= probability) {
                int n = madeline.capaOculta[i].weights.length;
                madeline.capaOculta[i].theta = random.nextBoolean() ?
		         	//(-1 * random.nextInt(RANGO)) :
					//random.nextInt(RANGO);
					((-2.4 * random.nextDouble())/n) :
		            ((2.4 * random.nextDouble())/n);
            }
        }

        for (int i = 0; i < madeline.capaSalida.length;i++) {
            for (int j = 0; j < madeline.capaSalida[i].weights.length;j++) {
                Random random = new Random();
                if (random.nextDouble() <= probability) {
                    int n = madeline.capaSalida[i].weights.length;
                    madeline.capaSalida[i].weights[j] =  random.nextBoolean() ?
			         	//(-1 * random.nextInt(RANGO)) :
						//random.nextInt(RANGO);
						((-2.4 * random.nextDouble())/n) :
			            ((2.4 * random.nextDouble())/n);
                }
            }
			Random random = new Random();
            if (random.nextDouble() <= probability) {
                int n = madeline.capaSalida[i].weights.length;
                madeline.capaSalida[i].theta =  random.nextBoolean() ?
		         	//(-1 * random.nextInt(RANGO)) :
					//random.nextInt(RANGO);
					((-2.4 * random.nextDouble())/n) :
		            ((2.4 * random.nextDouble())/n);
            }
        }
        return this;
    }
}
