import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class ChunkingStats {
	/**
	 * @param 
	 * nombreArchivo1: Nombre o ruta del archivo. 
	 * nombreArchivo2: Nombre o ruta del archivo. 
	 */
	static BufferedWriter out;
	static BufferedWriter stats;
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
			out = new BufferedWriter(new FileWriter(nombreArchivoOriginal + ".diffout"));
			stats = new BufferedWriter(new FileWriter(nombreArchivoOriginal + ".diffstats"));
		} catch (IOException e) {
			System.out.println("No se pudo crear el archivo de salida.");
			return;
		}
		try {
			
			Integer countVerbalProcesado = 0;
			Integer countVerbalOK = 0;
			Integer countVerbalGold = 0;
			
			Integer countNominalProcesado = 0;
			Integer countNominalOK = 0;
			Integer countNominalGold = 0;
			
			while(lectorOriginal.hasNext()){
				//Lee la siguiente linea. 
				String lineaOriginal = lectorOriginal.nextLine();
				String lineaProcesado = lectorProcesado.nextLine();

				ArrayList<String> chunksOriginal = obtenerChunks(lineaOriginal);
				ArrayList<String> chunksProcesado = obtenerChunks(lineaProcesado);
				
				String oracionOriginal = "";
				String oracionProcesado = "";
				String label_vp  = "VP";
				String label_np  = "NP";
				
				int j = 0;
				for (int i = 0; i < chunksOriginal.size(); i++) {
					int offset = 0;
					String chunkActualOriginal = chunksOriginal.get(i);
					String chunkActualProcesado = chunksProcesado.get(j);
					oracionProcesado += oracion(chunksProcesado.get(j));
					oracionOriginal += oracion(chunksOriginal.get(i));
					boolean coincideOracion = oracionOriginal.equals(oracionProcesado);
					if(!coincideOracion){
						//Vamos mal...
						while(oracionOriginal.length() > oracionProcesado.length()){
							offset++;
							oracionProcesado += " " + oracion(chunksProcesado.get(j+offset));
							if(label(chunksProcesado.get(j+offset)).equals(label_vp)){
								countVerbalProcesado++;
							}
							else if(label(chunksProcesado.get(j+offset)).equals(label_np)){
								countNominalProcesado++;
							}
						}
					}else{
						offset = 1;
					}
					if(label(chunkActualOriginal).equals(label_vp)){
						countVerbalGold++;
						if(coincideOracion && label(chunkActualProcesado).equals(label_vp)){
							countVerbalOK++;
						}
					}
					else if(label(chunkActualOriginal).equals(label_np)){
						countNominalGold++;
						if(coincideOracion && label(chunkActualProcesado).equals(label_np)){
							countNominalOK++;
						}
					}
					//Sino avance porque estaba más largo el chunk procesado lo veré en la próxima iteración?
					if(offset != 0){
						if(label(chunksProcesado.get(j)).equals(label_vp)){
							countVerbalProcesado++;
						}
						else if(label(chunksProcesado.get(j)).equals(label_np)){
							countNominalProcesado++;
						}					
					}
					
					j+=offset;
				}
				
				//Nos pueden sobrar chunks de nuestro procesamiento tenemos que contarlos.
				for(j+=1; j < chunksProcesado.size();j++){
					if(label(chunksProcesado.get(j)).equals(label_vp)){
						countVerbalProcesado++;
					}
					else if(label(chunksProcesado.get(j)).equals(label_np)){
						countNominalProcesado++;
					}					
				
				}
				
				
				
			}
			
			writeStats("Verbal" );
			writeStats("Recall: " +  new Double(countVerbalOK)/new Double(countVerbalProcesado));
			writeStats("Precision: " + new Double(countVerbalOK)/new Double(countVerbalGold));
			writeStats("-------------------------");
			writeStats("Nominal" );
			writeStats("Recall: " +  new Double(countNominalOK) /new Double(countNominalProcesado) );
			writeStats("Precision: " + new Double(countNominalOK) / new Double(countNominalGold));

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
	
	static String oracion(String chunk){
		String oracion = "";
		int i = 0;
		if(chunk.startsWith("[") && chunk.endsWith("]")){
			chunk = chunk.substring(1,chunk.length()-2);
			i = 1;
		}
		String[] partes = chunk.split("\\s");
		//Dependiendo si usa llaves o no tengo una etiqueta en la primera parte.
		for(; i < partes.length ; i++){
			String parte = partes[i];
			int separator_index = parte.lastIndexOf("_");
			if(separator_index!=-1)
				oracion += " " + parte.substring(0,separator_index);
			else
				System.out.println("ERROR: Obteniendo la palabra del chunk " + chunk + ".");
		}
		return oracion;
	}
	
	//Revisar el tema de que cierre con la llave... para mi estoy metiendo la pata con asummir que no hay espacios..
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




//Idea Original por si las moscas....				

//String[] pedazos1 = linea1.replace("\\W_\\W", "").replace("._.", "").replace(":_:", "").replace(",_,", "").split("\\s{0,1}\\[");
//String[] pedazos2 = linea2.replace("\\W_\\W", "").replace("._.", "").replace(":_:", "").replace(",_,", "").split("\\s{0,1}\\[");
//Tenemos un problemita... que hacemos cuando algo chunkea diferente?

//String[] pedazosOriginal = lineaOriginal.split("\\[");
//String[] pedazosProcesado = lineaProcesado.split("\\[");

//if(pedazosOriginal.length != pedazosProcesado.length)
//	System.out.println("Se viene la noche...");
//	for (int i = 0; i < pedazosProcesado.length; i++) {
//	if(pedazosOriginal[i].split(" ")[0].equals("VP")){
//		countVerbal++;
//		if(pedazosProcesado[i].split(" ")[0].equals("VP"))
//			countVerbalOK++;
//	}
//	if(pedazosProcesado[i].split(" ")[0].equals("VP"))
//		countVerbalGold++;
//	
//	if(pedazosOriginal[i].split(" ")[0].equals("NP")){
//		countNominal++;
//		if(pedazosProcesado[i].split(" ")[0].equals("NP"))
//			countNominalOK++;
//	}
//	if(pedazosProcesado[i].split(" ")[0].equals("NP"))
//		countNominalGold++;
//}