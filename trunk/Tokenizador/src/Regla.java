public enum Regla {
	numero, palabra, palabra_compuesta, palabra_barra, abreviatura/*Elaborar estrategia: Siguiente con may�scula? No me parece..*/,
	nombres/*url, archivos, compa��as, etc...*/, porcentaje, puntuacion, parrafo, apostrofe, simbolo, raro;
}


/*
 Asuma que el l�xico contiene todas las formas con un sufijo parte de una contracci�n.
 Por ejemplo, contiene "not" pero tambi�n "n't" (don't), "will" pero tambi�n "'ll" (we will, we'll);
 Sin embargo este l�xico no contiene variaciones del prefijo que es parte de la contracci�n: contiene will, pero no wo  (e.g., won't);
 contiene can, pero no ca (e.g., can't). As� el tokenizer deber�a expandir todas las contracciones:
can't ==> ['can' ,"n't"]
wouldn't ==> ['would', "n't"]
I'll ==> ['I', "'ll"]
won't ==> ['will', "n't"]
etc.
Considere que la forma 's (genitivo) no es una contracci�n en frases como John's book.  En estos casos su tokenizador debe separar 's de la palabra en la que aparece.
*/