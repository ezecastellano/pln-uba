import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	/**
	 * @param 
	 * nombreArchivo: Nombre o ruta del archivo. 
	 * tipoSalida: P (Postagged) T (Tokenized) o C (Chunked)
	 * formatoSalida: NLP (OpenNLP) o BIO (Palabra, Postag, Chunktag).
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
					String chunk = " [";
					while(lector.hasNext()){
						//Lee la siguiente linea. 
						String linea = lector.nextLine();
						
						if(!linea.isEmpty()){
							String[] palabras = linea.split("\\s");
							if(palabras[palabras.length-1].matches("I-.*")){
								chunk += " " + palabras[0] + "_" + palabras[1];
							}
							else if(palabras[palabras.length-1].equals("O")){
								if(!chunk.equals(" [")){
									write(chunk + " ]");
								}
								write(palabras[0] + "_" + palabras[1] + " ");
								chunk = " [" ;							
							}
							else{
								if(!chunk.equals(" [")){
									write(chunk + " ]");
								}
								chunk = " ["+ palabras[2].replace("B-","") + " " +palabras[0] + "_" + palabras[1];
							}
						}else{
							writeNewLine();
						}
					}
					if(!chunk.equals(" ["))
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
					while(lector.hasNext()){
						String linea = lector.nextLine();
						String []partes = linea.split("\\s");
						for (String parte : partes) {
							write(getPalabra(parte) + " " + getCONNLTag(parte));
							writeNewLine();
						}
						writeNewLine();
					}
					
				}else if (tipoSalida.equals("C")){
					
					while(lector.hasNext()){
						String linea = lector.nextLine();
						ArrayList<String> chunks = obtenerChunks(linea);
						for (String chunk : chunks) {
							writeChunk(chunk);
						}
						writeNewLine();
					}
				}else{
					System.out.println("Advertencia: No hay diferencias de formato...");
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
	
	static void writeChunk(String chunk) throws IOException {
		String label = label(chunk);
		int i = 0;
		if(chunk.startsWith("[") && chunk.endsWith("]")){
			chunk = chunk.substring(1,chunk.length()-2);
			i = 1;
		}
		String[] partes = chunk.split("\\s");
		//Dependiendo si usa llaves o no tengo una etiqueta en la primera parte.
		for(; i < partes.length ; i++){
			String parte = partes[i];
			String lineaCONNL = getPalabra(parte) + " " + getTag(parte) + " " + getCONNLLabel((parte.length()>1 && i==1) || parte.length()==1, parte, label);
			write(lineaCONNL);
			writeNewLine();
		}
	}
	
	static String getCONNLTag(String parte){
		if(getTag(parte).matches("\\W|-(L|R)RB-"))
			return "O";
		else
			return getTag(parte);
	}
	
	static String getCONNLLabel(boolean esPrimero, String parte, String label){
		if(getTag(parte).matches("\\W|-(L|R)RB-"))
			return "O";
		else
			if(esPrimero)
				return "B-"+label;
			else
				return "I-"+label;
	}
	
	static String getPalabra(String parte){
		String palabra = "";
		int separator_index = parte.lastIndexOf("_");
		if(separator_index!=-1)
			palabra = parte.substring(0,separator_index);
		else
			System.out.println("ERROR: Obteniendo la palabra de la parte del chunk " + parte + ".");
		return palabra;
	}
	
	static String label(String chunk){
		String label = "";
		if(chunk.startsWith("[") && chunk.endsWith("]")){
			chunk = chunk.substring(1,chunk.length()-2);
			String[] partes = chunk.split("\\s");
			label =  partes[0];
		}else{
			int separator_index = chunk.lastIndexOf("_");
			if(separator_index!=-1)
				label =  chunk.substring(separator_index + 1);
			else
				System.out.println("ERROR: Obteniendo etiqueta de una parte del chunk " + chunk +".");
		}
		return label;
	}
	
	static ArrayList<String> obtenerChunks(String oracionDeChunks){
		ArrayList<String> chunks = new ArrayList<String>();
		String chunk = "";
		boolean abrioLlave = false;
		boolean empezoPalabraComun = false;
		char[] chars = oracionDeChunks.toCharArray();
		String charAnterior = " ";
		for(int i = 0; i < oracionDeChunks.length();i++){
			Character unaCharacter = chars[i];
			String charActual = unaCharacter.toString();
			//Chunks que van entre llaves. 
			if(charActual.equals("[") && charAnterior.equals(" ")){
				abrioLlave = true;
				chunk += charActual;
			}else if(charActual.equals("]") && charAnterior.equals(" ")){
				abrioLlave = false;
				chunk += charActual;
				chunks.add(chunk);
				chunk = "";
			}else if(abrioLlave){
				chunk += charActual;
			}//Chunks que no van entre llaves.
			else if(!abrioLlave && !empezoPalabraComun && !charActual.equals(" ")){
				empezoPalabraComun = true;
				chunk += charActual;
			}else if(empezoPalabraComun && charActual.equals(" ")){
				empezoPalabraComun = false;
				chunks.add(chunk);
				chunk = "";
			}else if(empezoPalabraComun){
				chunk += charActual;
			}
			charAnterior = charActual;
		}
		
		if(!chunk.isEmpty())
			chunks.add(chunk);
		
		return chunks;
	}
	
	
	static String getTag(String palabra){
		int underscore = palabra.lastIndexOf("_");
		return palabra.substring(underscore+1);
	}
}




