import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class UnirChunkedCONNL {
	/*
	 * Este programa toma dos archivos de chunking en formato CONNL 
	 * y deja en las tres primeras columnas los valores del original, 
	 * mientras que en la última el valor a verificar. 
	 * Las estadísticas de esta comparación pueden verse usando el
	 * script conlleval.pl.
	 * 
	 * @param 
	 * nombreArchivoOriginal: Nombre o ruta del archivo. 
	 * nombreArchivoProcesado: Nombre o ruta del archivo. 
	 */
	static BufferedWriter out;
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("ERROR ARGS!");
			return;
		} 
		
		String nombreArchivoOriginal = args[0];
		String nombreArchivoProcesado = args[1];
		
		Scanner lectorOriginal = null;
		Scanner lectorProcesado = null;
		try {
			lectorOriginal = new Scanner(new FileReader(nombreArchivoOriginal));
		} catch (FileNotFoundException e1) {
			System.out.println("Archivo no encontrado.");
			return;
		}
		
		try {
			lectorProcesado = new Scanner(new FileReader(nombreArchivoProcesado));
		} catch (FileNotFoundException e1) {
			System.out.println("Archivo2 no encontrado.");
		}
		try {
			out = new BufferedWriter(new FileWriter(nombreArchivoOriginal + ".compare"));
		} catch (IOException e) {
			System.out.println("No se pudo crear el archivo de salida.");
			return;
		}
		try {
			while(lectorOriginal.hasNext() && lectorProcesado.hasNext()){
				//Lee la siguiente linea. 
				String lineaOriginal = lectorOriginal.nextLine();
				String lineaProcesado = lectorProcesado.nextLine();
				if(!lineaOriginal.isEmpty()){
					String []partesOriginal = lineaOriginal.split("\\s");
					String []partesProcesado = lineaProcesado.split("\\s");
					out.write(partesOriginal[0] + " " + partesOriginal[1] + " " + partesOriginal[2] + " " + partesProcesado[2]);
				}
				out.newLine();
			}
			
		} catch (IOException e) {
			System.out.println("ERROR: Lectura/escritura.");
			return;
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("ERROR: Uno de los archivos de salida no pudo ser cerrado correctamente.");
			}
			lectorOriginal.close();
			lectorProcesado.close();
		}
	}
}
