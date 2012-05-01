
public class Regex {

	static String generic_domain = ".*\\.(com|net|org|aero|edu|gob|gov|uba).*";
	static String http = "http://*.";
	static String www = "www\\..+";
	static String url = www+"|"+generic_domain+"|"+http;
	//Ojo que no sea un double...
	static String file = ".+\\..+";
	
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
	static String simbolo = "[^("+caracter+"|"+digito+")]";
	static String simbolos = simbolo + "+";
	static String nombres = file +"|"+ url ;
	static String espacio = "\\s";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
				
		DiccionarioDeContracciones diccionarioContracciones = new DiccionarioDeContracciones();
		
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

//		System.out.println("test.html".matches(nombres));
//		System.out.println("www.dc.uba.ar".matches(nombres));
//		System.out.println("google.com".matches(nombres));
//		System.out.println("http://wiki.net".matches(nombres));
//		System.out.println("dc.uba.ar".matches(nombres));
//		String generic_domain = ".*\\.(com|net|org|aero|edu|gob|gov|uba).*";
//		String http = "http://*.";
//		String www = "www\\..+";
//		String url = www+"|"+generic_domain+"|"+http;
//		//Ojo que no sea un double...
//		String file = ".+\\..+";


//		String digito = "\\d";
//		String entero = digito + "+";
//		String decimal = entero+"\\."+entero;
//		String numero = decimal + "|" + entero;
//		String porcentaje = numero + "%";
//		String caracter = "[a-zA-Z]";
//		String palabra = caracter+"+";
//		String caracter_digito = "("+digito + "|" + caracter+")";
//		String palabra_numero = caracter_digito + "+";
//		String guion = palabra_numero+"-"+palabra_numero;
//		String barra = palabra_numero+"/"+palabra_numero;
//		String apostrofe = palabra +"'"+palabra;
//		String palabra_capital_con_punto = "[A-Z]+[a-z]*\\.";
//		String abreviatura = "("+palabra_capital_con_punto + ")+";
//		String simbolo = "[^("+caracter+"|"+digito+")]";
//		String nombres = file +"|"+ url ;