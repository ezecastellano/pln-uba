import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Main {
	/**
	 * @param 
	 * nombreArchivo: Nombre o ruta del archivo. 
	 * tipoSalida: "A", un archivo con una línea por cada token. "P", debe salir por pantalla una línea por cada token.
	 */
	static BufferedWriter out;
	static String tipoSalida;
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("Se esperan dos argumentos. El primero el nombre del archivo a pasear. " +
								"El segundo 'A' para obtener la salida en un archivo o 'P' por pantalla. ");
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
		if(tipoSalida.equals("A")){
			try {
				out = new BufferedWriter(new FileWriter(nombreArchivo.replace(".txt", "") + ".out"));
			} catch (IOException e) {
				System.out.println("No se pudo crear el archivo de salida.");
				return;
			}
		}else if(!tipoSalida.equals("P")){
			System.out.println("Formato de salida inválido, ingrese 'A' o 'P'.");
			return;
		}
		try {
			boolean anteriorEraParrafo = true;
			while(lector.hasNext()){
				//Lee la siguiente linea. 
				String linea = lector.nextLine();
				//Separa la linea por espacios.
				String[] palabras = linea.split(" ");
				if(linea.isEmpty())
				{
					if(!anteriorEraParrafo)	
						write(new Token("", Regla.parrafo));
					anteriorEraParrafo = true;
				}
				else{
					for (String palabra : palabras) {
						//Analiza cada una de las palabras especiales.
						List<Token> tokens = Analizador.obtenerTokens(palabra);
						for (Token token : tokens) {
							write(token);
						}
					}
					anteriorEraParrafo = false;
				}
			}
		} catch (IOException e) {
			System.out.println("Error al de lectura/escritura.");
			return;
		}
	}
	
	static void write(Token token) throws IOException{
		String texto = token.toString();
		if(tipoSalida.equals("A")){
			out.write(texto);
			out.newLine();
			out.flush();
		}else{
			System.out.println(texto);
		}
	}
	

}
