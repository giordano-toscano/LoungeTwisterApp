package com.example.giordano.cardapiointeligente.Model;

import java.util.ArrayList;

public class Drink {
	
	DrinkType drinkType;
	private ArrayList<Sabor> sabors;
	Tamanho tam;
	
	public Drink() {
		
	}
	
	public DrinkType getSabor() {
		return drinkType;
	}

	public void setSabor(DrinkType drinkType) {
		this.drinkType = drinkType;
	}
	
	public void setTam(Tamanho tam) {
		this.tam = tam;
	}

	public Tamanho getTam() {
		return tam;
	}

	public ArrayList<Sabor> getAcomp() {
		return sabors;
	}

	public void setAcomp(ArrayList<Sabor> sabors) {
		this.sabors = sabors;
	}

}
