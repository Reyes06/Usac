package Servicios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedOutputStream;

/**
 *
 * @author Eddy
 */
public class Archivo {

    //Leer archivo-------------------------------------------------------------
    FileInputStream entrada;
    BufferedReader bufferentrada;
    InputStreamReader lector;
    
    //Escribir archivo--------------------------------------------------------
    FileOutputStream salida;
    OutputStreamWriter escritor;
    BufferedOutputStream buffersalida;
    
    //Archivo------------------------------------------------------------------
    File archivo;

    public String leer(String ruta){
        String contenido = "";
        try {
            archivo = new File(ruta);
            entrada = new FileInputStream(archivo);
            lector = new InputStreamReader(entrada);
            bufferentrada = new BufferedReader(lector);
            String linea;
            while ((linea = bufferentrada.readLine()) != null) {
                contenido += linea;
                contenido += "\n";
            }
            bufferentrada.close();

        } catch (IOException e) {
            System.out.println("Error al leer  :'v");
            System.out.println("Mensaje: " + e.getMessage() + "Ubicacion: " + e.getLocalizedMessage());
        }
        return contenido;
    }

    public boolean escribir(String ruta, String contenido){
        try {
            archivo = new File(ruta);
            salida = new FileOutputStream(archivo);
            escritor = new OutputStreamWriter(salida);
            
            escritor.write(contenido);
            
            escritor.close();
            
            return true;
        } catch (IOException e) {
            System.out.println("Error al escribir :'v");
            System.out.println("Mensaje: " + e.getMessage() + "Ubicacion: " + e.getLocalizedMessage());
        }
        return false;
    }
}
