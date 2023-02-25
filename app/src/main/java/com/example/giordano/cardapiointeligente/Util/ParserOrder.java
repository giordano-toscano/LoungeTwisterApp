package com.example.giordano.cardapiointeligente.Util;

import com.example.giordano.cardapiointeligente.Model.Sabor;
import com.example.giordano.cardapiointeligente.Model.Pedido;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParserOrder {

    private int numero;
    private String str;

    public ParserOrder(int numero) {
        this.numero = numero;
    }

    public String orderParser(Pedido pedido) {

        numero = 1 + numero;
        str += "\n";
        str += "LOUNGE TWISTER";
        str += "\n";
        str = "PEDIDO: " + numero + "\n";
        str += "Data:" + dataParser(pedido) + "\n";
        str += "Consumo:" + pedido.getLocal().name() + "\n";
        //str += "Nome do Cliente:" + pedido.getNome().toString() + "\n";
        str += "Tamanho:" + pedido.getDrink().getTam().toString() + "\n";
        str += "Tipo da bebida:\n" + removerAcentos(pedido.getDrink().getSabor().getDescricao())+"\n";

        if(pedido.getDrink().getAcomp().size()!=0)
            str += "Acompanhamentos:\n" + removerAcentos(acompanhamentosParser(pedido));

//        str += "\nTotal: " + "R$" + String.format("%.2f",pedido.getTotal()) + "\n\n\n"+" "+"\n";
        str += "\n\n\n\n";
        return str;
    }

    public String acompanhamentosParser(Pedido pedido){
        String string="";
        ArrayList<Sabor> saborArrayList = pedido.getDrink().getAcomp();
        int acompanhamentos=0;

        for (int i = 0; i < saborArrayList.size(); i++) {
            if(saborArrayList.get(i).isComprado()){
                string+="-"+ saborArrayList.get(i).getDescricao()+";";
                string+="\n";
                acompanhamentos++;
            }

        }
        if(acompanhamentos==14)
            return "- Todos\n";
        else
            return string;

    }


    public String dataParser(Pedido pedido){
        Date data = pedido.getData();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        return formato.format(data);

    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }



}
