package neuroevolucion.madeline;

import java.util.Random;

/*
  Clase que simula un Adeline.
  
 */
public class Adeline {
    
    public double weights[];
    
    /*Guardo su salida por si la necesito en algun momento*/
    public double salida;
    
    /*
      Contruye un nuevo Adeline con n entradas, y los pesos los
      inicializa entre los intervalos [-2.4/n,2.4/n]
     */
    public Adeline(int n) {
        this.weights = new double[n];
        
        for(int i = 0; i < n; i++)  {
            Random random = new Random();
            //this.weights[i] = random.nextBoolean() ? 
            //    ((-2.4 * random.nextDouble())/n) :
            //    ((2.4 * random.nextDouble())/n);
            this.weights[i] = 0.5;
        }
    }
    
    /*
      Contruye un Adeline con los pesos ya hechos.
      Este constructor sirve principalmente para el operador de cruza
     */
    public Adeline(double[] weights){
        this.weights = weights;
    } 
    
    /*
      Dado que no le puedo asignar un arreglo de pesos a otro madeline
      por que solamente compartiria el apuntador y a ambos los modificaria
      Utilizo este metodo para copiar cada peso en un nuevo arreglo y asi
      entregar el nuevo.
     */
    public Adeline copy() {
        Adeline copy = new Adeline(this.weights.length);
        for (int i = 0; i < this.weights.length; i++) {
            copy.weights[i] = this.weights[i];
        }
        return copy;
    }
    
    /*
      Calculo la suma de los pesos por las entradas.
     */
    public double salida(double[] entradas) {
        this.salida = 0;
        for(int i = 0; i < this.weights.length; i++) {
            this.salida += (this.weights[i] * entradas[i]);
        }
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

