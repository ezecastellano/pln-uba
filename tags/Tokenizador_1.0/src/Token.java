
public class Token {
	String valor;
	Regla regla;
	
	public Token(String unValor, Regla unaRegla) {
		valor = unValor;
		regla = unaRegla;
	}
	
	@Override
	public String toString() {
		return "<"+regla.name()+">"+valor+"</"+regla.name()+">";
	}
	
	public String valor(){
		return valor;
	}
}
