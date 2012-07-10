import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Concatenar {
	/**
	 * @param 
	 * pathDirectorio: Nombre o ruta del directorio. 
	 */
	static BufferedWriter out;
	static BufferedWriter stats;
	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("ERROR ARGS!");
			return;
		} 
		
		String pathDirectorio = args[0];
		
		File directorio = new File(pathDirectorio);
		if(!directorio.isDirectory()){
			System.out.println("No es un directorio.");
			return;
		}
		String nombreSalida = "all.train";
		try {
			out = new BufferedWriter(new FileWriter(pathDirectorio + "/"+ nombreSalida));
		} catch (IOException e) {
			System.out.println("No se pudo crear el archivo de salida.");
			return;
		}
		
		String []archivos = directorio.list();
		
		for (String archivo : archivos) {
			if(!archivo.equals(nombreSalida) && !archivo.equals("\\..*")){
				Scanner lector;
				try {
					lector = new Scanner(new FileReader(pathDirectorio + archivo));
					
				} catch (FileNotFoundException e) {
					System.out.println("Archivo no encontrado.");
					return;
				}
				while(lector.hasNext()){
					try {
						String linea = lector.nextLine();
						String []partes = linea.split("\\s");
						out.write(partes[0]+ " " + partes[1] +" "+partes[2]);
						out.newLine();
					} catch (IOException e) {
						System.out.println("ERROR: Lectura/escritura.");
						return;
					}
				}
				try {
					out.flush();
					lector.close();
				} catch (IOException e) {
					System.out.println("ERROR: Lectura/escritura.");
					return;
				}
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			System.out.println("ERROR: Uno de los archivos de salida no pudo ser cerrado correctamente.");
		}
	}
}
