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
	 * tipoSalida: P (Postaggged) T (Tokenized) o C (Chunked)
	 * formatoSalida: NLP (OpenNLP) o BIO.
	 */
	static BufferedWriter out;
	static String tipoSalida;
	static String formatoSalida;
	public static void main(String[] args) {
		if(args.length != 3){
			System.out.println("Se esperan dos argumentos. El primero el nombre del archivo a pasear. " +
								"El segundo 'P' para obtener la salida de un archivo de Postagging, 'T' de Tokenizing o 'C' de Chunking. " +
								"El tercero 'NLP para obtenerlo en formato OpenNLP o 'BIO' para formato BIO");
			return;
		} 
		
		String nombreArchivo = args[0];
		tipoSalida = args[1];
		formatoSalida = args[2];
		
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
		if(!tipoSalida.matches("(P|C|T)") ){
			System.out.println("Formato de entrada inválido, ingrese 'P' o 'C' o 'T'.");
			return;
		}
		if(!formatoSalida.matches("NLP|BIO")){
			System.out.println("Formato de entrada inválido, ingrese 'NLP' o 'BIO'.");
			return;
		}
		try {
			if(formatoSalida.equals("NLP")){
				if(tipoSalida.equals("P")){
					String oracion = "";
					while(lector.hasNext()){
						//Lee la siguiente linea. 
						String linea = lector.nextLine();
						if(linea.isEmpty()){
							write(oracion);
							writeNewLine();
							oracion = "";
						}else{
							String[] palabras = linea.split("\\s");
							if(oracion.length()>0)
								oracion += " ";
							oracion +=  palabras[0] + "_" + palabras[1];
						}
					}
					if(!oracion.isEmpty())
						write(oracion);
					
				}else if(tipoSalida.equals("C")){
					String chunk = "[";
					while(lector.hasNext()){
						//Lee la siguiente linea. 
						String linea = lector.nextLine();
						
						if(!linea.isEmpty()){
							String[] palabras = linea.split("\\s");
							if(palabras[palabras.length-1].matches("I-.*")){
								chunk += " " + palabras[0] + "_" + palabras[1];
							}
							else if(palabras[palabras.length-1].equals("O")){
								if(chunk.length()>1){
									write(chunk + " ]");
								}
								write(" " + palabras[0] + "_" + palabras[1] + " ");
								chunk = "[" ;							
							}
							else{
								if(chunk.length()>1){
									write(chunk + " ]");
								}
								chunk = "["+ palabras[2].replace("B-","") + " " +palabras[0] + "_" + palabras[1];
							}
						}else{
							writeNewLine();
						}
					}
					if(chunk.length()>1)
						write(chunk + " ]");
				}else{
					String oracion = "";
					while(lector.hasNext()){
						//Lee la siguiente linea. 
						String linea = lector.nextLine();
						if(linea.isEmpty()){
							write(oracion);
							writeNewLine();
							oracion = "";
						}else{
							String[] palabras = linea.split("\\s");
							if(oracion.length()>0)
								oracion += " ";
							oracion += palabras[0];
						}
					}
					if(!oracion.isEmpty())
						write(oracion);
				}
					
			}else{
				if(tipoSalida.equals("P")){
					/*
					//Lee la siguiente linea. 
					String linea = lector.nextLine();
					//Separa la linea por espacios.
					String[] palabras = linea.split("\\s");
					if(!linea.isEmpty())
						for (String palabra : palabras) {
							write(palabra);
						}
					 */
				}else{
					/*
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
					}*/
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
		out.flush();
	}
	static void writeNewLine()throws IOException{
		out.newLine();
		out.flush();
	}
}
