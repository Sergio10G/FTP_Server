package com.campusfp.hitoftp.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
	// ATTRIBUTES
	private Map<String, List<String>> menuMap;

	// CONSTRUCTOR
	public Menu() {
		this.menuMap = new HashMap<>();
		//menuMap.put("main", Arrays.asList("Listar ficheros", "Descargar fichero", "Subir fichero"));
		menuMap.put("main", Arrays.asList("Descargar fichero", "Subir fichero"));
	}

    // METHDOS
    private void printHeader() {
        System.out.println("o==============================o");
        System.out.println("o=            MENU            =o");
        System.out.println("o==============================o");
    }

    private void printSeparator() {
        System.out.println("o==============================o");
    }
    
    private void printEmptyLine() {
        System.out.println("o                              o");
    }

	private String embedOption(int num, String option) {
		String out = "o  ";
		out += num + ". " + option;
		while (out.length() < 30) {
			out += " ";
		}
		out += " o";
		return out;
	}

	public void optionListFromText(String text) {
		List<String> optionList;
		String[] textParts = text.split("/");
		String category = textParts[0];
		if (textParts.length > 1)
			optionList = Arrays.asList(textParts[1].split(":"));
		else
			optionList = new ArrayList<>();
		refreshOptionsList(category, optionList);
	}

	public String refreshOptionsList(String category, List<String> optionList) {
		if (menuMap.containsKey(category))
			menuMap.remove(category);
		menuMap.put(category, optionList);
		return optionListToText(category);
	}

	public void printMenu(String category) {
		if (!menuMap.containsKey(category)) {
			System.out.println("ERROR DE MENU: No existe la categoria " + category);
			return;
		}
		List<String> options = menuMap.get(category);

		printHeader();
		for (int i = 0; i < options.size(); i++) {
			System.out.println(embedOption(i + 1, options.get(i)));
		}
		printEmptyLine();
		System.out.println(embedOption(0, "Salir"));
		printSeparator();
	}

	public void printMenuFromText(String text) {
		optionListFromText(text);
		printMenu(text.split("/")[0]);
	}

	public String optionListToText(String category) {
		List<String> optionList = menuMap.get(category);
		String out = category + "/";
		for (int i = 0; i < optionList.size(); i++) {
			out += optionList.get(i);
			if (i != optionList.size() - 1)
				out += ":";
		}
		return out;
	}

	public static String optionListToText(String category, List<String> optionList) {
		String out = category + "/";
		for (int i = 0; i < optionList.size(); i++) {
			out += optionList.get(i);
			if (i != optionList.size() - 1)
				out += ":";
		}
		return out;
	}

	public static void clearTerminal() {
		System.out.print("\033[H\033[2J");
    	System.out.flush();
	}

    // GETTERS AND SETTERS
    
    
}
