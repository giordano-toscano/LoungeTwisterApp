package com.example.giordano.cardapiointeligente.Model;

public enum Tamanho {
	P("P",0),
	M("M",0),
	G("G",0);

	private double valor;
	private boolean checado;
	private String descricao;
		
	Tamanho( String descricao, double valor){
		this.descricao = descricao;
		this.valor = valor;
		this.checado = false;
	}

	public String getDescricao() {
		return descricao;
	}

	public double getValor(){
		return valor; 
	}

	public void setChecado(boolean checado) {
		this.checado = checado;
	}

	public boolean isChecado() {
		return checado;
	}
	
}
