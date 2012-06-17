import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Main {
	/**
	 * @param 
	 * nombreArchivo: Nombre o ruta del archivo. 
	 * tipoArchivo: P (Postaggged) o C (Chunked)
	 */
	static BufferedWriter out;
	static String tipoSalida;
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("Se esperan dos argumentos. El primero el nombre del archivo a pasear. " +
								"El segundo 'P' para obtener la salida de un archivo de Postagging o 'C' de Chunking. ");
			return;
		} 
		
		String nombreArchivo = args[0];
		tipoSalida = args[1];
		
		Scanner lector;
		try {
			lector = new Scanner(new FileReader(nombreArchivo));
		} catch (FileNotFoundException e1) {
			System.out.println("Archivo no encontrado.");
			return;
		}
		try {
			out = new BufferedWriter(new FileWriter(nombreArchivo.replace(".txt", "") + ".out"));
		} catch (IOException e) {
			System.out.println("No se pudo crear el archivo de salida.");
			return;
		}
		if(!tipoSalida.matches("(P|C)") ){
			System.out.println("Formato de entrada inválido, ingrese 'P' o 'C'.");
			return;
		}
		try {
			if(tipoSalida.equals("P"))
				while(lector.hasNext()){
					//Lee la siguiente linea. 
					String linea = lector.nextLine();
					//Separa la linea por espacios.
					String[] palabras = linea.split("\\s");
					if(!linea.isEmpty())
						for (String palabra : palabras) {
							write(palabra);
						}
				}
			else
				while(lector.hasNext()){
					//Lee la siguiente linea. 
					String linea = lector.nextLine();
					if(!linea.isEmpty()){
						if(!linea.matches("(\\s)*\\[(.*)\\](\\s)*")){
//						System.out.println("El archivo no se encontraba correctamente chunkeado en el formato [X A_Y B_Z]");
							int separator = linea.lastIndexOf("_");
							String label = linea.substring(separator+1, linea.length());
							write(transform(linea, label));
						}
						else{
//							linea = linea.replaceFirst("(\\s)*\\[", "");
//							linea = linea.replace("(\\s)*\\](\\s)*","");
							int open = linea.indexOf("[");
							int close = linea.lastIndexOf("]");
							linea = linea.substring(open+1,close);
							//linea = (String) linea.subSequence(1, linea.length()-2);
							//Separa la linea por espacios.
							String[] palabras = linea.split("\\s");
							if(!linea.isEmpty()){
								
								String label = palabras[0];
								
								write(transform(palabras[1],"B-"+label));		
								
								for (int i = 2; i < palabras.length ; i++) {
									write(transform(palabras[i],"I-"+label));
								}
							}
						}
					}
					}
				
		} catch (IOException e) {
			System.out.println("Error al de lectura/escritura.");
			return;
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("ERROR: Uno de los archivos de salida no pudo ser cerrado correctamente.");
			}
			lector.close();
		}
	}
	
	
	static String transform(String palabra, String label){
		int separator_palabra_beggin = palabra.lastIndexOf("_");
		if(separator_palabra_beggin!=-1)
			return (palabra.substring(0,separator_palabra_beggin) + " " + palabra.substring(separator_palabra_beggin+1,palabra.length()) + " " + label);
		else
			return palabra + " " + label + " ERROR";
	}
	
	static void write(String palabra)throws IOException{
		out.write(palabra);
		out.newLine();
		out.flush();
	}
}
