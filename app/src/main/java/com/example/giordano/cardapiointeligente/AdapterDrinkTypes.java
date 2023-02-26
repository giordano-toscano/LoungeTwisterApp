package com.example.giordano.cardapiointeligente;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.giordano.cardapiointeligente.Model.DrinkType;
import com.example.giordano.cardapiointeligente.Model.Sabor;
import com.example.giordano.cardapiointeligente.View.ListaDinamica;

import java.util.ArrayList;

public class AdapterDrinkTypes extends ArrayAdapter<DrinkType> {

    private final Context context;
    private final ArrayList<DrinkType> saboresList;

    float preco = 0;

    public AdapterDrinkTypes(Context context, ArrayList<DrinkType> saboresList) {
        super(context, R.layout.custom_layout_list_sabores,saboresList);
        this.context = context;
        this.saboresList = saboresList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_layout_list_sabores, parent , false);

        final RadioButton rdbtn_listaSabores = (RadioButton) rowView.findViewById(R.id.rdbtn_listaSabores);
        TextView preco_listaSabores = (TextView)rowView.findViewById(R.id.preco_listaSabores);

        rdbtn_listaSabores.setText(saboresList.get(position).getDescricao());

        if(saboresList.get(position).isChecado() )
            rdbtn_listaSabores.setChecked(true);
        else
            rdbtn_listaSabores.setChecked(false);

        if(saboresList.get(position).getValor()==0){
            preco_listaSabores.setText("");
        }else {
            preco_listaSabores.setText(" R$" + String.format("%.2f",saboresList.get(position).getValor()) + "");
        }

        rdbtn_listaSabores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(context));

                setarLista(saboresList);
                saboresList.get(position).setChecado(b);

                if(saboresList.get(position).getDescricao().equalsIgnoreCase("milkshake") && b == true){
                    ArrayList<Sabor> listA = new ArrayList<Sabor>();
                    ArrayList<Sabor> listB = new ArrayList<Sabor>();
                    Cardapio.addMilkshakeFlavors(listA,listB);
                    Cardapio.adapterSabores1.restaurarLista(listA);
                    Cardapio.adapterSabores2.restaurarLista(listB);
                    Cardapio.saboresTile.setText("Sabores");
                    Cardapio.listViewAcompanhamentos1.setVisibility(View.VISIBLE);
                    Cardapio.listViewAcompanhamentos2.setVisibility(View.VISIBLE);
                    ListaDinamica.setListViewHeighBasedOnItems(Cardapio.listViewAcompanhamentos1);
                    ListaDinamica.setListViewHeighBasedOnItems(Cardapio.listViewAcompanhamentos2);
                }else if(saboresList.get(position).getDescricao().equalsIgnoreCase("suco") && b == true){
                    ArrayList<Sabor> listA = new ArrayList<Sabor>();
                    ArrayList<Sabor> listB = new ArrayList<Sabor>();
                    Cardapio.addJuiceFlavors(listA,listB);
                    Cardapio.adapterSabores1.restaurarLista(listA);
                    Cardapio.adapterSabores2.restaurarLista(listB);
                    Cardapio.saboresTile.setText("Sabores");
                    Cardapio.listViewAcompanhamentos1.setVisibility(View.VISIBLE);
                    Cardapio.listViewAcompanhamentos2.setVisibility(View.VISIBLE);
                    ListaDinamica.setListViewHeighBasedOnItems(Cardapio.listViewAcompanhamentos1);
                    ListaDinamica.setListViewHeighBasedOnItems(Cardapio.listViewAcompanhamentos2);
                }else if(saboresList.get(position).getDescricao().equalsIgnoreCase("vitamina") && b == true){
                    ArrayList<Sabor> listA = new ArrayList<Sabor>();
                    ArrayList<Sabor> listB = new ArrayList<Sabor>();
                    Cardapio.addVitaminaFlavors(listA,listB);
                    Cardapio.adapterSabores1.restaurarLista(listA);
                    Cardapio.adapterSabores2.restaurarLista(listB);
                    Cardapio.saboresTile.setText("Sabores");
                    Cardapio.listViewAcompanhamentos1.setVisibility(View.VISIBLE);
                    Cardapio.listViewAcompanhamentos2.setVisibility(View.VISIBLE);
                    ListaDinamica.setListViewHeighBasedOnItems(Cardapio.listViewAcompanhamentos1);
                    ListaDinamica.setListViewHeighBasedOnItems(Cardapio.listViewAcompanhamentos2);
                }else{
                    ArrayList<Sabor> listA = new ArrayList<Sabor>();
                    ArrayList<Sabor> listB = new ArrayList<Sabor>();
                    Cardapio.addJuiceFlavors(listA,listB);
                    Cardapio.adapterSabores1.restaurarLista(listA);
                    Cardapio.adapterSabores2.restaurarLista(listB);
                    Cardapio.saboresTile.setText("");
                    Cardapio.listViewAcompanhamentos1.setVisibility(View.INVISIBLE);
                    Cardapio.listViewAcompanhamentos2.setVisibility(View.INVISIBLE);
                }

                p.salvarPreferencias("precoSabores",saboresList.get(position).getValor());

            }
        });

        return rowView;
    }

    public void restaurarLista(ArrayList<DrinkType> sabores){
        this.saboresList.clear();
        for(DrinkType s : sabores){
            s.setChecado(false);
        }
        this.saboresList.addAll(sabores);

        notifyDataSetChanged();
    }

    public void setarLista(ArrayList<DrinkType> saboresList){

        for (int i = 0; i <saboresList.size() ; i++) {
            saboresList.get(i).setChecado(false);
        }

    }

}
