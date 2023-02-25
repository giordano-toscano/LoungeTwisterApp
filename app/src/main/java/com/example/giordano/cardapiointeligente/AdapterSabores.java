package com.example.giordano.cardapiointeligente;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.giordano.cardapiointeligente.Model.Sabor;

import java.util.ArrayList;

public class AdapterSabores extends ArrayAdapter<Sabor> {

    private final Context context;
    private final ArrayList<Sabor> saborArrayList;
    private static String CHAVE_ACOMPANHAMENTOS = "precoAcompanhamentos";
    private static String QTD_SELECIONADA = "quantidadeAcompanhamentos";


    public AdapterSabores(Context context, ArrayList<Sabor> acompanhamentosArrayList) {
        super(context, R.layout.custom_layout_list_frutas,acompanhamentosArrayList);
        this.context = context;
        this.saborArrayList = acompanhamentosArrayList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_layout_list_acompanhamentos, parent , false);

        final CheckBox rdbtn_listaTipos = rowView.findViewById(R.id.chckbtn_listaAcompanhamentos);

        rdbtn_listaTipos.setText(saborArrayList.get(position).getDescricao());
        rdbtn_listaTipos.setChecked(saborArrayList.get(position).isComprado());

        rdbtn_listaTipos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences preferences = new Preferences(PreferenceManager.getDefaultSharedPreferences(context));
                //int quantidadeAcompanhamentos = preferences.carregarPreferenciasInt(QTD_SELECIONADA);

                float preco=0;
                saborArrayList.get(position).setComprado(b);
                if(b){
                    //preco = acompanhamentoArrayList.get(position).getValor();

                }else{
                    //preco = (-1) * acompanhamentoArrayList.get(position).getValor();

                }

                preferences.salvarPreferencias(CHAVE_ACOMPANHAMENTOS,preco);
                //preferences.salvarPreferencias(QTD_SELECIONADA, quantidadeAcompanhamentos);
            }
        });


        return rowView;
    }



    public void restaurarLista(ArrayList<Sabor> sabors){
        this.saborArrayList.clear();
        for(Sabor a : sabors){
            a.setComprado(false);
        }
        this.saborArrayList.addAll(sabors);

        notifyDataSetChanged();
    }

}
