
public class Regex {

	static String nombres = "((ht|f)tps?://)?\\w+([\\.\\-\\w]+)?" +
			"\\.([a-zA-Z]{2,4}|travel)(:\\d{2,5})?(/[\\.\\-\\w]*)*";
	static String digito = "\\d";
	static String entero = digito + "+";
	static String decimal = entero+"\\."+entero;
	static String numero = decimal + "|" + entero;
	static String porcentaje = numero + "%";
	static String caracter = "[a-zA-Z]";
	static String palabra = caracter+"+";
	//Agregué o un caracter, porque sino este string no me lo aceptaba. 
	static String no_entre_llaves = "[^{(\\[]+.*[^\\]})]+|.";
	static String entre_llaves = "[{(\\[]+.*[\\]})]";
	static String caracter_digito = "("+digito + "|" + caracter+")";
	static String palabra_numero = caracter_digito + "+";
	static String guion = palabra_numero+"-"+palabra_numero;
	static String barra = palabra_numero+"/"+palabra_numero;
	static String apostrofe = palabra +"'"+palabra;
	static String palabra_capital_con_punto = "[A-Z]+[a-z]*\\.";
	static String abreviatura_minuscula_mas_de_dos_siglas = "([a-z]+\\.([a-z]+\\.)+)";
	static String abreviatura = "("+palabra_capital_con_punto + ")+|" +  
			abreviatura_minuscula_mas_de_dos_siglas;
	static String simbolo = "\\W";
	static String simbolos = simbolo + "+";
	static String espacio = "\\s";
}