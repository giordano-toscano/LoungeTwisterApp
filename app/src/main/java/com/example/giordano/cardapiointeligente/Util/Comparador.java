package com.example.giordano.cardapiointeligente.Util;

import com.example.giordano.cardapiointeligente.Model.Sabor;

import java.util.Comparator;

public class Comparador implements Comparator<Sabor> {

    @Override
    public int compare(Sabor objeto1, Sabor objeto2){
        int retorno = 0;
        retorno = objeto1.getDescricao().compareTo(objeto2.getDescricao());
        return retorno;
    }
}
