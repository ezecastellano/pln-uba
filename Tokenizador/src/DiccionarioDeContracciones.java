import java.util.Map;
import java.util.TreeMap;


public class DiccionarioDeContracciones {
		Map<String,String> contracciones;
		
		DiccionarioDeContracciones(){
			contracciones = new TreeMap<String, String>();
			contracciones.put( "aren't","are n't");
			contracciones.put( "can't","can n't");
			contracciones.put( "couldn't","could n't");
			contracciones.put( "didn't","did n't");
			contracciones.put( "doesn't","does n't");
			contracciones.put( "don't","do n't");
			contracciones.put( "hadn't","had n't");
			contracciones.put( "hasn't","has n't");
			contracciones.put( "haven't","have n't");
			contracciones.put( "he'd","he 'd");
			contracciones.put( "he'll","he 'll");
			contracciones.put( "he's","he 's");
			contracciones.put( "I'd","I 'd"); 
			contracciones.put( "I'll","I 'll");
			contracciones.put( "I'm","I 'm");
			contracciones.put( "I've","I 've");
			contracciones.put( "isn't","is n't");
			contracciones.put( "let's","let 's");
			contracciones.put( "mightn't","might n't");
			contracciones.put( "mustn't","must n't");
			contracciones.put( "shan't","shall n't");
			contracciones.put( "she'd","she 'd");
			contracciones.put( "she'll","she 'll");
			contracciones.put( "she's","she 's");
			contracciones.put( "shouldn't","should n't");
			contracciones.put( "that's","that 's");
			contracciones.put( "there's","there 's");
			contracciones.put( "they'd","they 'd");
			contracciones.put( "they'll","they 'll");
			contracciones.put( "they're","they 're");
			contracciones.put( "they've","they 've");
			contracciones.put( "we'd","we 'd");
			contracciones.put( "we're","we 're");
			contracciones.put( "we've","we 've");
			contracciones.put( "weren't","were n't");
			contracciones.put( "what'll","what 'll");
			contracciones.put( "what're","what 're");
			contracciones.put( "what's","what 's");
			contracciones.put( "what've","what 've");
			contracciones.put( "where's","where 's");
			contracciones.put( "who'd","who 'd");
			contracciones.put( "who'll","who 'll");
			contracciones.put( "who're","who 're");
			contracciones.put( "who's","who 's");
			contracciones.put( "who've","who 've");
			contracciones.put( "won't","will n't");
			contracciones.put( "wouldn't","would n't");
			contracciones.put( "you'd","you 'd"); 
			contracciones.put( "you'll","you 'll");
			contracciones.put( "you're","you 're");
			contracciones.put( "you've","you 've");

		}
		
		public boolean esUnaContraccion(String unaPalabra){
			return contracciones.containsKey(unaPalabra);
		}
		
		public String obtenerFormaNormal(String unaContraccion){
			assert(esUnaContraccion(unaContraccion));
			return contracciones.get(unaContraccion);
		}
		
}
