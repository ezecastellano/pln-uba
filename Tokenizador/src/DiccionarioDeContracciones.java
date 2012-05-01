import java.util.Map;
import java.util.TreeMap;


public class DiccionarioDeContracciones {
		Map<String,String> contracciones;
		
		DiccionarioDeContracciones(){
			contracciones = new TreeMap<String, String>();
			contracciones.put( "aren't","are not");
			contracciones.put( "can't","can not");
			contracciones.put( "couldn't","could not");
			contracciones.put( "didn't","did not");
			contracciones.put( "doesn't","does not");
			contracciones.put( "don't","do not");
			contracciones.put( "hadn't","had not");
			contracciones.put( "hasn't","has not");
			contracciones.put( "haven't","have not");
			contracciones.put( "he'd","he would");//he had
			contracciones.put( "he'll","he will");//he shall
			contracciones.put( "he's","he is");//he has
			contracciones.put( "I'd","I would");//I had 
			contracciones.put( "I'll","I will");//I shall
			contracciones.put( "I'm","I am");
			contracciones.put( "I've","I have");
			contracciones.put( "isn't","is not");
			contracciones.put( "let's","let us");
			contracciones.put( "mightn't","might not");
			contracciones.put( "mustn't","must not");
			contracciones.put( "shan't","shall not");
			contracciones.put( "she'd","she would");//she had
			contracciones.put( "she'll","she will");//she shall
			contracciones.put( "she's","she is; she has");
			contracciones.put( "shouldn't","should not");
			contracciones.put( "that's","that is");//that has
			contracciones.put( "there's","there is"); //there has
			contracciones.put( "they'd","they would");//they would
			contracciones.put( "they'll","they will");//they shall
			contracciones.put( "they're","they are");
			contracciones.put( "they've","they have");
			contracciones.put( "we'd","we would");//we had
			contracciones.put( "we're","we are");
			contracciones.put( "we've","we have");
			contracciones.put( "weren't","were not");
			contracciones.put( "what'll","what will");//what sall
			contracciones.put( "what're","what are");
			contracciones.put( "what's","what is");//what has
			contracciones.put( "what've","what have");
			contracciones.put( "where's","where is");//where has
			contracciones.put( "who'd","who would");//who had
			contracciones.put( "who'll","who will");//who shall
			contracciones.put( "who're","who are");
			contracciones.put( "who's","who is");//who has
			contracciones.put( "who've","who have");
			contracciones.put( "won't","will not");
			contracciones.put( "wouldn't","would not");
			contracciones.put( "you'd","you would");//you had 
			contracciones.put( "you'll","you will");//you shall
			contracciones.put( "you're","you are");
			contracciones.put( "you've","you have");

		}
		
		public boolean esUnaContraccion(String unaPalabra){
			return contracciones.containsKey(unaPalabra);
		}
		
		public String obtenerFormaNormal(String unaContraccion){
			assert(esUnaContraccion(unaContraccion));
			return contracciones.get(unaContraccion);
		}
		
}
