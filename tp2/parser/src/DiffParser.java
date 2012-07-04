import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class DiffParser {
	/**
	 * @param 
	 * nombreArchivo1: Nombre o ruta del archivo. 
	 * nombreArchivo2: Nombre o ruta del archivo. 
	 */
	static BufferedWriter out;
	static BufferedWriter stats;
	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("ERROR ARGS!");
			return;
		} 
		
		String nombreArchivo1 = args[0];
//		String nombreArchivo2 = args[1];
		
		Scanner lector1;
//		Scanner lector2;
		try {
			lector1 = new Scanner(new FileReader(nombreArchivo1));
		} catch (FileNotFoundException e1) {
			System.out.println("Archivo no encontrado.");
			return;
		}
		
//		try {
//			lector2 = new Scanner(new FileReader(nombreArchivo2));
//		} catch (FileNotFoundException e1) {
//			System.out.println("Archivo2 no encontrado, no pasa naranja.");
//		}
		try {
			out = new BufferedWriter(new FileWriter(nombreArchivo1 + ".diffout"));
			stats = new BufferedWriter(new FileWriter(nombreArchivo1 + ".diffstats"));
		} catch (IOException e) {
			System.out.println("No se pudo crear el archivo de salida.");
			return;
		}
		try {
			
			int state = 0;
			int countDiff = 0;
			boolean firstLine = true;
			ArrayList<String> left = new ArrayList<String>();
			ArrayList<String> right = new ArrayList<String>();
			
			while(lector1.hasNext()){
				//Lee la siguiente linea. 
				String linea1 = lector1.nextLine();
				
				if(state==0 && linea1.matches("\\d*c\\d*")){
					state = 1;
					firstLine = true;
				}else if(state ==1 && linea1.equals("---")){
					state = 2;
					firstLine = true;
				}else if(state == 1){
					if(firstLine && linea1.matches("> .*")){
						linea1 = linea1.substring(2);
					}
					left.add(getTag(linea1));
					countDiff++;
				}else if(state == 2){
					if(firstLine && linea1.matches("< .*")){
						linea1 = linea1.substring(2);
					}
					right.add(getTag(linea1));
					countDiff--;
					if(countDiff == 0)
						state = 0;
				}
			}
			HashMap<String, Integer> cantidades = new HashMap<String, Integer>();
			for (int i=0; i< left.size() ; i++){
				if(!right.get(i).matches("\\(|\\)")){
					write(left.get(i) + " " + right.get(i));
					if(cantidades.containsKey(left.get(i)+" "+right.get(i))){
						cantidades.put(left.get(i)+" "+right.get(i), cantidades.get(left.get(i)+" "+right.get(i))+1);
					}else{
						cantidades.put(left.get(i)+" "+right.get(i), 1);
					}
				}
			}
			for(String key : cantidades.keySet()){
				writeStats(key + " " +  cantidades.get(key) );
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
			lector1.close();
//			lector2.close();
		}
	}
	
	
	static String getTag(String palabra){
		int underscore = palabra.lastIndexOf("_");
		return palabra.substring(underscore+1);
	}
	
	static void writeStats(String palabra)throws IOException{
		stats.write(palabra);
		stats.newLine();
		stats.flush();
	}
	
	static void write(String palabra)throws IOException{
		out.write(palabra);
		out.newLine();
		out.flush();
	}
	static void writeNewLine()throws IOException{
		out.newLine();
		out.flush();
	}
}
