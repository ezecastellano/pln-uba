public enum Regla {
	numero, palabra, palabra_compuesta, palabra_barra, abreviatura/*Elaborar estrategia: Siguiente con mayúscula? No me parece..*/,
	nombres/*url, archivos, compañías, etc...*/, porcentaje, puntuacion, parrafo, apostrofe, simbolo, raro;
}


/*
 Asuma que el léxico contiene todas las formas con un sufijo parte de una contracción.
 Por ejemplo, contiene "not" pero también "n't" (don't), "will" pero también "'ll" (we will, we'll);
 Sin embargo este léxico no contiene variaciones del prefijo que es parte de la contracción: contiene will, pero no wo  (e.g., won't);
 contiene can, pero no ca (e.g., can't). Así el tokenizer debería expandir todas las contracciones:
can't ==> ['can' ,"n't"]
wouldn't ==> ['would', "n't"]
I'll ==> ['I', "'ll"]
won't ==> ['will', "n't"]
etc.
Considere que la forma 's (genitivo) no es una contracción en frases como John's book.  En estos casos su tokenizador debe separar 's de la palabra en la que aparece.
*/