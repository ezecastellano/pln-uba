import java.util.ArrayList;
import java.util.List;


public class Analizador {
	static DiccionarioDeContracciones diccionarioContracciones = new DiccionarioDeContracciones();
	
	public static List<Token> obtenerTokens(String palabra) {
		List<Token> tokens = new ArrayList<Token>();
		List<Token> tokensTraseros = new ArrayList<Token>();
		String resto = palabra.trim();
		while (!resto.isEmpty()) {
			if(resto.matches(Regex.palabra +"|"+ Regex.numero +"|"+ Regex.guion +"|"+ Regex.barra +"|"+ Regex.porcentaje
					+"|"+ Regex.simbolo +"|"+ Regex.abreviatura +"|"+ Regex.nombres)){
				tokens.add(obtenerTokenEspecifico(resto));
				resto = "";
			}
			else if(diccionarioContracciones.esUnaContraccion(resto)){
				//Es una contraccion...
				for (String parte : diccionarioContracciones.obtenerPartes(resto)) {
					tokens.addAll(Analizador.obtenerTokens(parte));
				}
				resto = "";
			}
			else if(resto.matches(Regex.apostrofe)){
				//Es un posesivo o algo raro...
				String[] partes = resto.split("'");
				assert(partes.length == 2);
				tokens.add(new Token(partes[0], Regla.palabra));
				tokens.add(new Token("'"+partes[1], Regla.apostrofe));
				resto = "";
			}
			else if(resto.matches(".+"+Regex.simbolo)){
				//Es una palabra que termina en un simbolo.
				tokensTraseros.add(0, new Token(resto.substring(resto.length()-1), Regla.simbolo));
				resto = resto.substring(0,resto.length()-1);
			}else if(resto.matches(Regex.simbolo+".+")){
				//Empieza en un simbolo.
				tokens.add(new Token(resto.substring(0,1), Regla.simbolo));
				resto = resto.substring(1);
			}
			else {
				//Algo más complejo que una palabra...
				tokens.add(new Token(resto, Regla.raro));
				resto = "";
			} 	
		}
		tokens.addAll(tokensTraseros);
		return tokens;
	}
	
	private static Token obtenerTokenEspecifico(String palabra){
		Token token = null;
		
		if(palabra.matches(Regex.palabra)){
			token = (new Token(palabra,Regla.palabra));
		}
		else if(palabra.matches(Regex.numero)){
			token = (new Token(palabra,Regla.numero));
		}
		else if(palabra.matches(Regex.guion)){
			token = (new Token(palabra,Regla.palabra_compuesta));
		}
		else if(palabra.matches(Regex.barra)){
			token = (new Token(palabra,Regla.palabra_barra));
		}
		else if(palabra.matches(Regex.porcentaje)){
			token = (new Token(palabra,Regla.porcentaje));
		}
		else if(palabra.matches(Regex.abreviatura)){
			token = (new Token(palabra,Regla.abreviatura));
		}
		else if(palabra.matches(Regex.simbolo)){
			token = (new Token(palabra,Regla.simbolo));
		}
		else if(palabra.matches(Regex.nombres)){
			token = (new Token(palabra, Regla.nombres));
		}
		
		return token;
	}
	
	
}
