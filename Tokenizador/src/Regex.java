
public class Regex {

	//static String url2	 = "(ht|f)tps?:\/\/\w+([\.\-\w]+)?\.([a-z]{2,4}|travel)(:\d{2,5})?(\/.*)?$/i";
	static String nombres = "((ht|f)tps?://)?\\w+([\\.\\-\\w]+)?\\.([a-zA-Z]{2,4}|travel)(:\\d{2,5})?(/[\\.\\-\\w]*)*";
	
//	static String generic_domain = ".*\\.(com|net|org|aero|edu|gob|gov|uba).*";
//	static String http = "http://.*";
//	static String www = "www\\..+";
//	static String url = www+"|"+generic_domain+"|"+http;
//	Ojo que no sea un double...
//	static String file = ".+\\..+";
//	static String nombres = file +"|"+ url ;
	
	static String digito = "\\d";
	static String entero = digito + "+";
	static String decimal = entero+"\\."+entero;
	static String numero = decimal + "|" + entero;
	static String porcentaje = numero + "%";
	static String caracter = "[a-zA-Z]";
	static String palabra = caracter+"+";
	static String caracter_digito = "("+digito + "|" + caracter+")";
	static String palabra_numero = caracter_digito + "+";
	static String guion = palabra_numero+"-"+palabra_numero;
	static String barra = palabra_numero+"/"+palabra_numero;
	static String apostrofe = palabra +"'"+palabra;
	static String palabra_capital_con_punto = "[A-Z]+[a-z]*\\.";
	static String abreviatura = "("+palabra_capital_con_punto + ")+";
	static String simbolo = "\\W";
	static String simbolos = simbolo + "+";
	static String espacio = "\\s";

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
				
		DiccionarioDeContracciones diccionarioContracciones = new DiccionarioDeContracciones();
		
		System.out.println("http://cnn.com/US/9701/30/wall.fall.texas/index.html".matches(nombres));
		
		//Primero hago el split y me quedo con los parrafos. 
		String word = "http://cnn.com/US/9701/30/wall.fall.texas/index.html";
		if(word.matches(palabra))
			System.out.println("Palabra...");
		else if(word.matches(numero))
			System.out.println("Numero...");
		else if(word.matches(guion))
			System.out.println("Palabra con guion intermedio...");
		else if(word.matches(barra))
			System.out.println("Palabra con barra...");
		else if(word.matches(porcentaje))
			System.out.println("Porcentaje...");
		else if(diccionarioContracciones.esUnaContraccion(word))
			System.out.println("Es una contraccion...");
		else if(word.matches(apostrofe))
			System.out.println("Es un posesivo o algo raro...");
		else if(word.matches(abreviatura))
			System.out.println("Es una abreviatura");
		else if(word.matches(simbolo))
			System.out.println("Es un simbolo");
		else if(word.matches(palabra_numero+simbolo))
			System.out.println("Es una palabra que termina en un simbolo");
		else if(word.matches(nombres))
			System.out.println("Es un nombre...");
		else 
			System.out.println("Algo más complejo que una palabra...");
		
	}

}