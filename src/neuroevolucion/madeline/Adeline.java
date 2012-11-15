package neuroevolucion.madeline;

import java.util.Random;


/*
  Clase que simula un Adeline.
  
 */
public class Adeline {
    
    public int[] weights;
	public int theta;
    
	public final int RANGO = 91;
	
    /*Guardo su salida por si la necesito en algun momento*/
    public double salida;
    
    /*
      Contruye un nuevo Adeline con n entradas, y los pesos los
      inicializa entre los intervalos [-2.4/n,2.4/n]
     */
    public Adeline(int n) {
        this.weights = new int[n];
        
        for(int i = 0; i < n; i++)  {
            Random random = new Random();
            this.weights[i] = random.nextBoolean() ? 
                (-1 * random.nextInt(RANGO)) :
				random.nextInt(RANGO);
				//((-2.4 * random.nextDouble())/n) :
                //((2.4 * random.nextDouble())/n);		
        }
        Random random = new Random();
         this.theta = random.nextBoolean() ? 
         	(-1 * random.nextInt(RANGO)) :
			random.nextInt(RANGO);
			//((-2.4 * random.nextDouble())/n) :
             //((2.4 * random.nextDouble())/n);	
    }
    
    /*
      Contruye un Adeline con los pesos ya hechos.
      Este constructor sirve principalmente para el operador de cruza
     */
    public Adeline(int[] weights, int theta){
        this.weights = weights;
		this.theta = theta;
    } 
    
    /*
      Dado que no le puedo asignar un arreglo de pesos a otro madeline
      por que solamente compartiria el apuntador y a ambos los modificaria
      Utilizo este metodo para copiar cada peso en un nuevo arreglo y asi
      entregar el nuevo.
     */
    public Adeline copy() {
		int[] copy = new int[this.weights.length];
		
        for (int i = 0; i < copy.length; i++) {
            copy[i] = this.weights[i];
        }
		Adeline newAdeline = new Adeline(copy,this.theta);
        return newAdeline;
    }
    
    /*
      Calculo la suma de los pesos por las entradas.
     */
    public double salida(double[] entradas) {
        this.salida = 0;
        for(int i = 0; i < this.weights.length; i++) {
            this.salida += (this.weights[i] * entradas[i]);
        }
		
		this.salida += this.theta * (-1);
			
        this.salida = this.disparo(this.salida);
        return this.salida;
    }
    
    /*
      Calcula la sigmoidal del resultado y lo regreso,
      guardando con esto la salida.
     */
    public double disparo(double y) {
        //Sigmoid
        return (1.0/(1.0 + Math.pow(Math.E,(-1.0)*y)));
    }
    
}

