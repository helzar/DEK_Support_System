package com.nulp.dss.util;

public class Transliterator {

	public static String transliterate(String message) {
		char[] abcCyrillic = { ' ', 'à', 'á', 'â', 'ã', 'ä', 'å', '¸', 'æ', 'ç',
				'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó',
				'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ',
				'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', '¨', 'Æ', 'Ç', 'È', 'É', 'Ê',
				'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö',
				'×', 'Ø', 'Ù', 'Ú', 'Û', 'Á', 'Ý', 'Þ', 'ß', 'a', 'b', 'c',
				'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
				'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
				'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		String[] abcLatin = { " ", "a", "b", "v", "g", "d", "e", "e", "zh", "z",
				"i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u",
				"f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju",
				"ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y",
				"K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H",
				"Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a",
				"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
				"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
				"z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
				"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
				"X", "Y", "Z" };
		StringBuilder builder = new StringBuilder();
		boolean isChanged;
		for (int i = 0; i < message.length(); i++) {
			isChanged = false;
			for (int x = 0; x < abcCyrillic.length; x++){
				if (message.charAt(i) == abcCyrillic[x]) {
					builder.append(abcLatin[x]);
					isChanged = true;
					break;
				}
			}
			if (!isChanged){
				builder.append(message.charAt(i));
			}
		}
		return builder.toString();
	}
}
