package com.example.giordano.cardapiointeligente.Model;

public enum DrinkType {
	
	MILKSHAKE("Milkshake",0),
	SUCO("Suco",0),
	VITAMINA("Vitamina",0);
	
	private String descricao;
	private float valor;
	private boolean checado;

	DrinkType(String descricao, float valor) {
		this.setDescricao(descricao);
		this.setValor(valor);
		this.checado=false;
	}

	public boolean isChecado() {
		return checado;
	}

	public void setChecado(boolean checado) {
		this.checado = checado;
	}

	public String getDescricao() {
		return descricao;
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

}
