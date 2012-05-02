import java.util.ArrayList;
import java.util.List;


public class Analizador {
	static DiccionarioDeContracciones diccionarioContracciones = new DiccionarioDeContracciones();
	
	public static List<Token> obtenerTokens(String palabra) {
		List<Token> tokens = new ArrayList<Token>();
		List<Token> tokensTraseros = new ArrayList<Token>();
		String resto = palabra;
		while (!resto.isEmpty()) {
			if(resto.matches(Regex.palabra)){
				System.out.println("Palabra...");
				tokens.add(new Token(resto,Regla.palabra));
				resto = "";
			}
			else if(resto.matches(Regex.espacio)){
				System.out.println("Esacio...");
				resto = "";
			}
			else if(resto.matches(Regex.numero)){
				System.out.println("Numero...");
				tokens.add(new Token(resto,Regla.numero));
				resto = "";
			}
			else if(resto.matches(Regex.guion)){
				System.out.println("Palabra con guion intermedio...");
				tokens.add(new Token(resto,Regla.palabra_compuesta));
				resto = "";
			}
			else if(resto.matches(Regex.barra)){
				System.out.println("Palabra con barra...");
				tokens.add(new Token(resto,Regla.palabra_barra));
				resto = "";
			}
			else if(resto.matches(Regex.porcentaje)){
				System.out.println("Porcentaje...");
				tokens.add(new Token(resto,Regla.porcentaje));
				resto = "";
			}
			else if(diccionarioContracciones.esUnaContraccion(resto)){
				//OJO: Si mis contracciones fueran otra cosa el stack me vuela por todos lados. 
				System.out.println("Es una contraccion...");
				String formaNormal = diccionarioContracciones.obtenerFormaNormal(resto);
				for (String parte : formaNormal.split(" ")) {
					tokens.addAll(Analizador.obtenerTokens(parte));
				}
				resto = "";
			}
			else if(resto.matches(Regex.apostrofe)){
				System.out.println("Es un posesivo o algo raro...");
				String[] partes = resto.split("'");
				assert(partes.length == 2);
				tokens.add(new Token(partes[0], Regla.palabra));
				tokens.add(new Token("'"+partes[1], Regla.apostrofe));
				resto = "";
			}
			else if(resto.matches(Regex.abreviatura)){
				System.out.println("Es una abreviatura");
				tokens.add(new Token(resto,Regla.abreviatura));
				resto = "";
			}
			else if(resto.matches(Regex.simbolo)){
				System.out.println("Es un simbolo");
				tokens.add(new Token(resto,Regla.simbolo));
				resto = "";
			}
			else if(resto.matches(Regex.palabra_numero+Regex.simbolos)){
				System.out.println("Es una palabra que termina en un simbolo");
				tokensTraseros.add(0, new Token(resto.substring(resto.length()-1), Regla.simbolo));
				resto = resto.substring(0,resto.length()-1);
			}else if(resto.matches(Regex.simbolo+".+")){
				System.out.println("Empieza en un simbolo");
				tokens.add(new Token(resto.substring(0,1), Regla.simbolo));
				resto = resto.substring(1);
			}
			else if(resto.matches(Regex.nombres)){
				System.out.println("Es un nombre...");
				tokens.add(new Token(resto, Regla.nombres));
				resto = "";
			}
			else if(resto.matches(Regex.nombres+Regex.simbolos)){
				System.out.println("Es un nombre termina en un simbolo");
				tokensTraseros.add(0, new Token(resto.substring(resto.length()-1), Regla.simbolo));
				resto = resto.substring(0,resto.length()-1);
			}	
			else {
				System.out.println("Algo más complejo que una palabra...");
				tokens.add(new Token(resto, Regla.raro));
				resto = "";
				
			} 
		}
		tokens.addAll(tokensTraseros);
		return tokens;
	}
	
	
}
