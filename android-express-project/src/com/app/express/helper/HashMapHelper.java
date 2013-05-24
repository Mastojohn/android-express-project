package com.app.express.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.util.Log;

public class HashMapHelper {
	/**
	 * Display a HashMap.
	 * 
	 * @param H - HashMap - The HashMap to display.
	 * @param nameHashMap - String - The HashMap's label.
	 */
	@SuppressWarnings("rawtypes")
	public static void displayHashMap(HashMap H, String nameHashMap) {
		// Set est l'interface d'une liste sans doublon
		// Cette liste contient l'ensemble des pairs clef-valeur
		@SuppressWarnings("unchecked")
		Set<Entry<Object, Object>> entrySet = H.entrySet();

		// On crée un itérateur sur la "Set"
		Iterator<Entry<Object, Object>> it = entrySet.iterator();

		String content = "Contenu de " + nameHashMap + ":\n";
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();

			content += entry.getKey() + "(" + entry.getKey().getClass().getSimpleName() + ")" + " = ";
			if (entry.getValue().getClass().isPrimitive()) {
				content += entry.getValue() + "(" + entry.getValue().getClass().getSimpleName() + ")\n";
			} else if (entry.getValue().getClass().isArray()) {
				content += "Array" + Arrays.deepToString((Object[]) entry.getValue()) + "\n";
			}

		}
		
		Log.i("HashMapHelper", content);
	}
	
	/**
	 * Return the content of an HashMap.
	 * 
	 * @param H - HashMap - The HashMap to display.
	 * @param nameHashMap - String - The HashMap's label.
	 */
	@SuppressWarnings("rawtypes")
	public static String getHashMapContent(HashMap H, String nameHashMap) {
		// Set est l'interface d'une liste sans doublon
		// Cette liste contient l'ensemble des pairs clef-valeur
		@SuppressWarnings("unchecked")
		Set<Entry<Object, Object>> entrySet = H.entrySet();

		// On crée un itérateur sur la "Set"
		Iterator<Entry<Object, Object>> it = entrySet.iterator();

		String content = "Contenu de " + nameHashMap + ":\n";
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();

			content += entry.getKey() + "(" + entry.getKey().getClass().getSimpleName() + ")" + " = ";
			if (entry.getValue().getClass().isPrimitive()) {
				content += entry.getValue() + "(" + entry.getValue().getClass().getSimpleName() + ")\n";
			} else if (entry.getValue().getClass().isArray()) {
				content += "Array" + Arrays.deepToString((Object[]) entry.getValue()) + "\n";
			}else if(entry instanceof HashMap){
				content += getHashMapContent((HashMap)entry, "subHashMap");
			}

		}
		
		return content;
	}
}
