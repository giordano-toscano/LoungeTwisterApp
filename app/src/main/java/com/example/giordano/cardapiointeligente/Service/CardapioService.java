package com.example.giordano.cardapiointeligente.Service;

import com.example.giordano.cardapiointeligente.Model.Drink;
import com.example.giordano.cardapiointeligente.Model.Sabor;
import com.example.giordano.cardapiointeligente.Model.DrinkType;
import com.example.giordano.cardapiointeligente.Model.Tamanho;

import java.util.ArrayList;

/**
 * Created by mathe on 11/10/2018.
 */

public class CardapioService {



    public Drink generateDrink(ArrayList<Tamanho> tamanhoArrayList, ArrayList<DrinkType> drinkTypeArrayList, ArrayList<Sabor> saborLista1ArrayList, ArrayList<Sabor> saborLista2ArrayList){
        Drink acai = new Drink();
        acai.setSabor(montarSabor(drinkTypeArrayList));
        acai.setTam(montarTamanho(tamanhoArrayList));
        acai.setAcomp(montarAcompanhamentos(saborLista1ArrayList, saborLista2ArrayList));
        return acai;
    }

    public Tamanho montarTamanho(ArrayList<Tamanho> tamanhoArrayList){
        Tamanho tamanho = null;
        for(Tamanho t : tamanhoArrayList){
            if(t.isChecado()){
                tamanho = t;
            }
        }
        return tamanho;
    }

    public DrinkType montarSabor(ArrayList<DrinkType> drinkTypeArrayList){
        DrinkType drinkType = null;
        for(DrinkType s : drinkTypeArrayList){
            if(s.isChecado()){
                drinkType = s;
            }
        }
        return drinkType;
    }

    public ArrayList<Sabor> montarAcompanhamentos(ArrayList<Sabor> saborLista1ArrayList, ArrayList<Sabor> saborLista2ArrayList){

        ArrayList<Sabor> comprados = new ArrayList<Sabor>();
        for(Sabor sabor : saborLista1ArrayList){
            if(sabor.isComprado()){
                comprados.add(sabor);
            }
        }
        for(Sabor sabor : saborLista2ArrayList) {
            if (sabor.isComprado()) {
                comprados.add(sabor);
            }
        }
        return comprados;
    }


}
