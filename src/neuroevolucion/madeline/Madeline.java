package neuroevolucion.madeline;

// Clases que sirve para guarda y escribir Madelines en la memoria.
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.BufferedWriter;

import java.io.IOException;

public class Madeline {
    
    /*
      Variables que me guardan la capa oculta de neuronas,
      la capa de salida de las neuronas, y en numero de entradas
      de la capa oculta.
     */
    public int entradas;
    public Adeline[] capaOculta;
    public Adeline[] capaSalida;
    
    /*
      Construyo una nueva Madeline a partir del numero de neuronas
      en la capa escondida, el numero de neuronas en la capa de salida
      y el numero de entradas de la capa oculta.
      
      Crea un Madeline con pesos randoms.
      
     */
    public Madeline(int entradas, int capaOculta, int capaSalida) {
        
        this.entradas = entradas;
        this.capaOculta = new Adeline[capaOculta];
        this.capaSalida = new Adeline[capaSalida];
        
        for (int i = 0; i < capaOculta; i++) {
            this.capaOculta[i] = new Adeline(entradas);
        }
        for (int i = 0; i < capaSalida; i++) {
            this.capaSalida[i] = new Adeline(capaOculta);
        }
    }
    
    /*
      Dado un arreglo de entradas, genero las salidas.
     */
    public double[] procesa(double[] entradas) {
        double[] salidaOculta = new double[capaOculta.length];
        double[] salidaSalida = new double[capaSalida.length];
        for (int i = 0; i < capaOculta.length;i++) {
            salidaOculta[i] = capaOculta[i].salida(entradas);
        }
	
        for (int i = 0; i < capaSalida.length; i++) {
            salidaSalida[i] = capaSalida[i].salida(salidaOculta);
        }
	
        return salidaSalida;
    }

    /*
      Clase para copiar un madeline, dado que si lo asigno solamente este
      tendra problemas por que se modificaran ambos al intentar modificar
      solamente uno.
     */
    public Madeline copy() {
        Madeline copy = new Madeline(this.entradas,this.capaOculta.length,
                                     this.capaSalida.length);
        
        for (int i = 0; i < this.capaOculta.length; i++) {
            copy.capaOculta[i] = (this.capaOculta[i]).copy();
        }

        for (int i = 0; i < this.capaSalida.length; i++) {
            copy.capaSalida[i] = (this.capaSalida[i]).copy();
        }
        return copy;
    }
    
    public void guardaMadeline(String nombreArchivo) {
        try {
            BufferedWriter writer = 
                new BufferedWriter(new FileWriter(nombreArchivo));
            
            //Guardo el Numero de las entradas
            writer.write((capaOculta[0].weights).length + "\n");

            //Guardo el Numero de neuronas en la capa oculta
            writer.write(capaOculta.length + "\n");

            //Guardo el numero de neuronas en la capa de salida
            writer.write(capaSalida.length + "\n");
            writer.newLine();
            
            for (int i = 0; i < capaOculta.length; i++) {
                
                String pesosCapaOculta = "";
                
                for(int j = 0; j < (capaOculta[0].weights).length; j++) {
                    pesosCapaOculta += (capaOculta[i].weights)[j] + " ";
                }
				pesosCapaOculta += capaOculta[i].theta + " ";
                
                pesosCapaOculta += "\n";
                writer.write(pesosCapaOculta);
            }
            
            writer.newLine();
            
            for (int i = 0; i < capaSalida.length; i++) {
                String pesosCapaSalida = "";
                
                for(int j = 0; j < (capaSalida[0].weights).length; j++) {
                    pesosCapaSalida += (capaSalida[i].weights)[j] + " ";
                }
                
				pesosCapaSalida += capaSalida[i].theta + " ";
                pesosCapaSalida += "\n";
                writer.write(pesosCapaSalida);
            }
            
            writer.close();

        } catch (IOException ioe) {
            System.out.println("Algo malo paso");
        }
    }
    
    public Madeline cargaMadeline(String nombreArchivo) {
        try {
            
            BufferedReader reader = 
                new BufferedReader(new FileReader(nombreArchivo));
            
            int entradas = Integer.parseInt(((reader.readLine()).split("\n"))[0]);
            int numOculta = Integer.parseInt(((reader.readLine()).split("\n"))[0]);
            int numSalida =  Integer.parseInt(((reader.readLine()).split("\n"))[0]);
            
            //Salto una linea
            reader.readLine();
            
            Madeline madelineNueva = new Madeline(entradas,numOculta,numSalida);
            
            Adeline[] capaOculta = madelineNueva.capaOculta;
            Adeline[] capaSalida = madelineNueva.capaSalida;
            
            for (int i = 0; i < capaOculta.length; i++) {
                
                String[] pesosCapaOculta = (reader.readLine()).split(" ");
		
                for(int j = 0; j < (capaOculta[0].weights).length; j++) {
                    (capaOculta[i].weights)[j] = 
                        Double.parseDouble(pesosCapaOculta[j]);
                }
				
				capaOculta[i].theta = 
						Double.parseDouble(pesosCapaOculta[(capaOculta[0].weights).length]);
            }
            
            reader.readLine();
            for (int i = 0; i < capaSalida.length; i++) {
                String[] pesosCapaSalida = (reader.readLine()).split(" ");
		
                for(int j = 0; j < (capaSalida[0].weights).length; j++) {
                    (capaSalida[i].weights)[j] = 
                        Double.parseDouble(pesosCapaSalida[j]);
                }
				
				capaSalida[i].theta = 
						Double.parseDouble(pesosCapaSalida[(capaSalida[0].weights).length]);
                
            }
            
            reader.close();
            return madelineNueva;
        } catch (IOException ioe) {
            System.out.println("Algo malo paso");
            return new Madeline(1,1,1);
        }
    }
    
    public void entradaTexto(String nombreArchivo, String salidaArchivo) {
        try {
            BufferedReader reader = 
                new BufferedReader(new FileReader(nombreArchivo));
            BufferedWriter writer = 
                new BufferedWriter(new FileWriter(salidaArchivo));
            
            String linea = reader.readLine();
            String salida  = "";
            
            while (linea != null) {
                
                String[] entradasTexto = linea.split(" ");
                double[] entradas = new double[entradasTexto.length];
		
                for (int i = 0; i < entradasTexto.length - 1; i++) {
                    entradas[i] = Double.parseDouble(entradasTexto[i]);
                }
                
                String ultimaEntrada = 
                    (entradasTexto[entradas.length - 1].split("\n"))[0];
                
                entradas[entradas.length - 1] = 
                    Double.parseDouble(ultimaEntrada);
                
                double[] salidas = procesa(entradas);
                
                writer.write(linea + " & ");
                
                for (int i = 0; i < salidas.length;i++) {
                    writer.write(salidas[i] + " ");
                }
                
                writer.write("\n");
                linea = reader.readLine();
            }
            writer .close();
        } catch (IOException ioe) {
            System.out.println("Algo Paso");
            
        }
    }
    
}
