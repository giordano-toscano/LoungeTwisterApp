package com.example.giordano.cardapiointeligente.Model;

import java.util.Date;

public class Pedido {
	
	private String nome;
	private Date data;
	private Drink drink;
	private Local local;
	private double total;

	public Pedido (Drink drink) {
		this.drink = drink;
	}
	
	
	public Drink getDrink() {
		return drink;
	}
	
	public double getTotal() {		
		return this.total;
	}
	
	public String getNome() {
		return nome;
	}

	public Date getData() {
		return data;
	}	
	
	public Local getLocal() {
		return local;
	}	
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setDrink(Drink drink) {
		this.drink = drink;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void CalcularPedido() {
		double total = 0;

		total += this.local.equals(Local.VIAGEM)?1:0;

		if(this.drink.getAcomp()!= null)
			for(int i = 0; i<this.drink.getAcomp().size(); i++){
				if(i>=4){
					total += 1.5;
				}
			}

		if(this.drink.tam != null)
			total += this.drink.getTam().getValor();
		if(this.drink.drinkType != null)
			total +=this.drink.drinkType.getValor();

			this.total = total;
	}


}
