package com.example.giordano.cardapiointeligente.Model;

public enum Sabor {
	OVOMALTINE("Ovomaltine", (float)1.5 ),
	OREO("Oreo",(float)1.5),
	ACAI("Açaí",(float)1.5),
	MARACUJA("Maracujá",(float)1.5),
	MORANGO("Morango",(float)1.5),
	BAUNILHA("Baunilha",(float)1.5),
	CHOCOLATE("Chocolate",(float)1.5),
	ABACAXI("Abacaxi",(float)1.5),
	MENTA("Menta",(float)1.5),
	LACTEA("Láctea",(float)1.5),
	PACOCA("Paçoca",(float)1.5),
	CUPUACU("Cupuaçu",(float)1.5),
	DORGO("Dorgo",(float)1.5),
	DOCE_DE_LEITE("Doce de Leite",(float)1.5),

	// JUICE FLAVORS

	LARANJA("Laranja", (float)1.5 ),
	ACEROLA("Acerola",(float)1.5),
	//ABACAXI("Açaí",(float)1.5),
	LIMAO("Limão",(float)1.5),
	//MARACUJA("Morango",(float)1.5),
	MELANCIA("Melancia",(float)1.5),
	CAJU("Caju",(float)1.5),
	CAJA("Cajá",(float)1.5),
	GOIABA("Goiaba",(float)1.5),
	MANGA("Manga",(float)1.5),
	MELAO("Melão",(float)1.5),
	TAMARINDO("Tamarindo",(float)1.5),
	UVA("Uva",(float)1.5),
	GRAVIOLA("Graviola",(float)1.5),
	//CUPUACU("Doce de Leite",(float)1.5);

	// VITAMINA FLAVORS

	BANANA("Banana",(float)1.5),
	ABACATE("Abacate",(float)1.5),
	MAMAO_BANANA_MACA("Mamão/Banana/Maçã",(float)1.5),
	GUARANA_DO_AMAZONAS("Guaraná do Amazonas",(float)1.5),
	GUARACAI("Guaraçaí",(float)1.5);


	private float valor;
	private String descricao;
	private boolean comprado;

	public boolean isComprado() {
		return comprado;
	}

	public void setComprado(boolean comprado) {
		this.comprado = comprado;
	}

	public String getDescricao() {
		return descricao;
	}

	public float getValor() {
		return valor;
	}

	private Sabor(String descricao, float valor) {
		this.valor = valor;
		this.descricao = descricao;
		this.comprado = false;
	}
	
	
}
