import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class CompararPostaggedCONNL {
	/*
	 * Este archivo toma dos archivos de postagged en formato CONNL 
	 * y calcula el postagging accuracy y los cinco valores de error más frecuente, 
	 * teniendo en cuenta que el primer archivo es el orginal. 
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
			out = new BufferedWriter(new FileWriter(nombreArchivoOriginal + ".stats"));
		} catch (IOException e) {
			System.out.println("No se pudo crear el archivo de salida.");
			return;
		}
		try {
			Integer cantTotal =  0;
			Integer cantOK = 0;
			HashMap<String, Integer> cantidades = new HashMap<String, Integer>();
			
			while(lectorOriginal.hasNext() && lectorProcesado.hasNext()){
				//Lee la siguiente linea. 
				String lineaOriginal = lectorOriginal.nextLine();
				String lineaProcesado = lectorProcesado.nextLine();
				if(!lineaOriginal.isEmpty()){
					String []partesOriginal = lineaOriginal.split("\\s");
					String []partesProcesado = lineaProcesado.split("\\s");
					if(partesOriginal[1].equals(partesProcesado[1]))
						cantOK++;
					else
						agregarError(partesOriginal[1],partesProcesado[1], cantidades);
					cantTotal++;				
				}
			}
			
			out.write("Posttaging Accuracy: " + new Double(cantOK) / new Double(cantTotal));
			out.newLine();
			out.write("---------------------------------------------------------------");
			out.newLine();
			
			writeTopFive(cantidades);
			
			
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
	
	private static void writeTopFive(HashMap<String, Integer> cantidades) throws IOException {
		ArrayList<Integer> ordenadas = new ArrayList<Integer>(cantidades.values());
		Collections.sort(ordenadas);
		Integer erroresTotales = 0;
		for(String key: cantidades.keySet()){
			erroresTotales += cantidades.get(key);
		}
		Integer erroresTopSubtotal = 0;
		for(int i=0 ; i<5 ;i++){
			for(String key : cantidades.keySet()){
				if(cantidades.get(key).equals(ordenadas.get(ordenadas.size()-1-i))){
					out.write(key + " " + cantidades.get(key) + " " + new Double(cantidades.get(key))/new Double(erroresTotales));
					out.newLine();
					erroresTopSubtotal += cantidades.get(key);
				}
			}
		}
		out.write("Subtotal " + erroresTopSubtotal + " " + new Double(erroresTopSubtotal)/new Double(erroresTotales));
		out.newLine();

	}

	private static void agregarError(String original, String procesado, HashMap<String, Integer> cantidades) {
		if(cantidades.containsKey(procesado + "|" + original) || cantidades.containsKey(original + "|" + procesado)){
			if(cantidades.containsKey(procesado + "|" + original))
				cantidades.put( procesado + "|" + original , cantidades.get(procesado + "|" + original) + 1);
			else
				cantidades.put( original + "|" + procesado , cantidades.get(original + "|" + procesado) + 1);
		}
		else
			cantidades.put(original + "|" + procesado, 1);
	}

}
