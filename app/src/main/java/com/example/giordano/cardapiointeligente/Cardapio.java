package com.example.giordano.cardapiointeligente;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;

//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giordano.cardapiointeligente.Database.BancoController;
import com.example.giordano.cardapiointeligente.Model.Drink;
import com.example.giordano.cardapiointeligente.Model.Sabor;
import com.example.giordano.cardapiointeligente.Model.DrinkType;
import com.example.giordano.cardapiointeligente.Model.Local;
import com.example.giordano.cardapiointeligente.Model.Pedido;
import com.example.giordano.cardapiointeligente.Model.PrecoFinal;
import com.example.giordano.cardapiointeligente.Model.Tamanho;
import com.example.giordano.cardapiointeligente.Service.CardapioService;
import com.example.giordano.cardapiointeligente.Util.BluetoothPrinter;
import com.example.giordano.cardapiointeligente.Util.Comparador;
import com.example.giordano.cardapiointeligente.Util.ParserOrder;
import com.example.giordano.cardapiointeligente.View.ListaDinamica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Cardapio extends AppCompatActivity {

    static int NUMERO_MINIMO_ACOMPANHAMENTO = 4;

    View decorView;
    ArrayList<DrinkType> arraySabores;
    ArrayList<Tamanho> arrayTamanhos;
    ArrayList<Sabor> arrayAcompanhamentos1;
    ArrayList<Sabor> arrayAcompanhamentos2;
    ListView listViewSabores;

    public static TextView saboresTile;
    public static ListView listViewAcompanhamentos1;
    public static ListView listViewAcompanhamentos2;

    Button btn_confirmarPedido;

    //Componentes Local/Viagem
    RadioButton rdbtn_inLoco;
    RadioButton rdbtn_viagem;

    //Componentes Estaticos//
    RadioButton rdbtn_tamanhoP;
    RadioButton rdbtn_tamanhoM;
    RadioButton rdbtn_tamanhoG;

    //Marcar todos os Acompanhamentos
    CheckBox rdbtn_MarcarTodos;

    Pedido pedido;
    Drink drink;
    PrecoFinal precoFinal = new PrecoFinal(0,0,0,0,0,0,0);
    int qtdClickHistorico;

    AdapterDrinkTypes adapterDrinkTypes;
    public static AdapterSabores adapterSabores1;
    public static AdapterSabores adapterSabores2;

    BluetoothPrinter btPrinter = new BluetoothPrinter();
    ParserOrder po = new ParserOrder(0);


    Intent startIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startIntent = getIntent();
        setContentView(R.layout.activity_cardapio);

        final BancoController bancoController = new BancoController(getApplicationContext());
        bancoController.deletaRegistroDoDiaAnterior();

        final CardapioService cardapioService = new CardapioService();
        final Preferences preferences = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));


        inicializarComponentes();

        qtdClickHistorico = 0;

        //Deixar o app fullscreen
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //Alterações na Action Bar//
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cd3065"))); //#f29418
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(5);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setTitle(Html.fromHtml("<b><big>&emsp;Lounge Twister</big></b>"));
        actionBar.setTitle(Html.fromHtml("<font color='#5F3177'><b><big>&emsp;Lounge Twister</big></b></font>"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.twister_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fff6ed")));

        //Impressora//
        conectarImpressora(getResources().getString(R.string.nome_impressora));


        montarCardapio();

        rdbtn_MarcarTodos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for(Sabor sabor : arrayAcompanhamentos1){
                    sabor.setComprado(b);
                }
                for(Sabor sabor : arrayAcompanhamentos2){
                    sabor.setComprado(b);
                }
                adapterSabores1.notifyDataSetChanged();
                adapterSabores2.notifyDataSetChanged();
            }
        });

        /*listViewAcompanhamentos1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                adapterSabores1.resetAllExcept(-1);
                limparAdapter();
                adapterSabores1.notifyDataSetChanged();
                // Display the selected item in a Toast
                Toast.makeText(view.getContext(), selectedItem, Toast.LENGTH_LONG).show();
            }


        });*/

        btn_confirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drink = cardapioService.generateDrink(arrayTamanhos, arraySabores, arrayAcompanhamentos1, arrayAcompanhamentos2);

                RadioButton rdbtninLocal = findViewById(R.id.rdbtn_InLoco);
                RadioButton rdbtnViagem = findViewById(R.id.rdbtn_Viagem);

                if (drink.getTam() != null && drink.getSabor() != null && (drink.getSabor() == DrinkType.MILKSHAKE || drink.getSabor() == DrinkType.SUCO || drink.getSabor() == DrinkType.VITAMINA) && (rdbtninLocal.isChecked() || rdbtnViagem.isChecked())) {



                    SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                    Date data = new Date();

                    pedido = new Pedido(drink);
                    pedido.setData(new Date());

                    if (rdbtninLocal.isChecked()) {
                        pedido.setLocal(Local.LOCAL);
                    } else if (rdbtnViagem.isChecked()) {
                        pedido.setLocal(Local.VIAGEM);
                    }


                    pedido.CalcularPedido();
                    bancoController.inserirPedidoNoBanco(pedido);

                    try {

                        final String pedidoStringfy = po.orderParser(pedido);

                        btPrinter.sendData(pedidoStringfy);
                        Log.d("IMPRIMINDO",pedidoStringfy);

                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        try {
                                            btPrinter.sendData(pedidoStringfy);
                                            Log.d("IMPRIMINDO",pedidoStringfy);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                5000
                        );
                        //Log.d("IMPRIMINDO", po.orderParser(p));
                        alertDialogCustom(pedido);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (drink.getTam() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), Html.fromHtml("<b><big>" + "Escolha um Tamanho" + "</big></b>"), Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 0);
                    toast.show();
                } else if (drink.getSabor() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), Html.fromHtml("<b><big>" + "Escolha um tipo de bebida" + "</big></b>"), Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 0);
                    toast.show();
                } else if (rdbtninLocal.isChecked() == false && rdbtnViagem.isChecked() == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), Html.fromHtml("<b><big>" + "Escolha um Local para Consumo" + "</big></b>"), Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 0);
                    toast.show();
                }

            }
        });

        //Componentes Estaticos//
        rdbtn_tamanhoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarTamanhoEstatico(view);
            }
        });
        rdbtn_tamanhoM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarTamanhoEstatico(view);
            }
        });
        rdbtn_tamanhoG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarTamanhoEstatico(view);
            }
        });

        rdbtn_inLoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdbtn_viagem.setChecked(false);
                preferences.salvarPreferencias("precoEmbalagem", (float)(0));
                precoFinal.setPrecoEmbalagem(preferences.carregarPreferencias("precoEmbalagem"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));
//                + "<br />" +
//                        "<big>" + getString(R.string.preco) + String.format("%.2f", precoFinal.getPrecoTotal()) + "</big>" + "<br />"
            }
        });

        rdbtn_viagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdbtn_inLoco.setChecked(false);
                preferences.salvarPreferencias("precoEmbalagem", (float)(1.0));
                precoFinal.setPrecoEmbalagem(preferences.carregarPreferencias("precoEmbalagem"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                qtdClickHistorico ++;
                if(qtdClickHistorico>5){
                    Intent i = new Intent(getApplicationContext(), Historico.class);
                    startActivity(i);
                    qtdClickHistorico = 0;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean conectarImpressora(String nomeImpressora){

        try {
            btPrinter.setPrinterName(nomeImpressora);
            btPrinter.findBT();
            btPrinter.openBT();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void inicializarComponentes() {

        listViewSabores = findViewById(R.id.list_Sabores);
        saboresTile = findViewById(R.id.textView8);
        listViewAcompanhamentos1 = findViewById(R.id.list1_acompanhamentos);
        listViewAcompanhamentos2 = findViewById(R.id.list2_acompanhamentos);
        saboresTile.setText("");
        listViewAcompanhamentos1.setVisibility(View.INVISIBLE);
        listViewAcompanhamentos2.setVisibility(View.INVISIBLE);

        rdbtn_MarcarTodos = findViewById(R.id.rdbtn_MarcarTodos);
        btn_confirmarPedido = findViewById(R.id.btn_confirmarPedido);
        btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        //Componentes Estaticos//
        rdbtn_tamanhoP = (RadioButton)findViewById(R.id.rdbtn_tamanhoP);
        rdbtn_tamanhoM = (RadioButton)findViewById(R.id.rdbtn_tamanhoM);
        rdbtn_tamanhoG = (RadioButton)findViewById(R.id.rdbtn_tamanhoG);

        rdbtn_inLoco = (RadioButton) findViewById(R.id.rdbtn_InLoco);
        rdbtn_viagem = (RadioButton) findViewById(R.id.rdbtn_Viagem);
        //radioGroupLocal = (RadioGroup) findViewById(R.id.radio_groupLocal);

    }

    public ArrayList adicionarTamanhos(){
        ArrayList<Tamanho> listaTamanhos = new ArrayList<Tamanho>();

        //Adicionar Frutas//
        listaTamanhos.add(Tamanho.P);
        listaTamanhos.add(Tamanho.M);
        listaTamanhos.add(Tamanho.G);

        return listaTamanhos;
    }

    public ArrayList addDrinkTypes(){
        ArrayList<DrinkType> listaDrinkTypes = new ArrayList<DrinkType>();

        //Adicionar Sabores//
        listaDrinkTypes.add(DrinkType.MILKSHAKE);
        listaDrinkTypes.add(DrinkType.VITAMINA);
        listaDrinkTypes.add(DrinkType.SUCO);

        return listaDrinkTypes;
    }

    public static ArrayList<Sabor> addMilkshakeFlavors(ArrayList lista1, ArrayList lista2) {
        ArrayList<Sabor> listaSabores = new ArrayList<>();

        //Adicionar Acompanhamentos//
        listaSabores.add(Sabor.OVOMALTINE);
        listaSabores.add(Sabor.OREO);
        listaSabores.add(Sabor.ACAI);
        listaSabores.add(Sabor.MARACUJA);
        listaSabores.add(Sabor.MORANGO);
        listaSabores.add(Sabor.BAUNILHA);
        listaSabores.add(Sabor.CHOCOLATE);
        listaSabores.add(Sabor.ABACAXI);
        listaSabores.add(Sabor.MENTA);
        listaSabores.add(Sabor.LACTEA);
        listaSabores.add(Sabor.PACOCA);
        listaSabores.add(Sabor.CUPUACU);
        listaSabores.add(Sabor.DORGO);
        listaSabores.add(Sabor.DOCE_DE_LEITE);

        Collections.sort(listaSabores, new Comparador());

        for (int i = 0; i < listaSabores.size(); i++) {
            if(i%2==0){
                lista1.add(listaSabores.get(i));
            }else{
                lista2.add(listaSabores.get(i));
            }
        }

        return listaSabores;
    }

    public static ArrayList<Sabor> addJuiceFlavors(ArrayList lista1, ArrayList lista2) {
        ArrayList<Sabor> listaSabores = new ArrayList<>();

        //Adicionar Acompanhamentos//
        listaSabores.add(Sabor.LARANJA);
        listaSabores.add(Sabor.ACEROLA);
        listaSabores.add(Sabor.ABACAXI);
        listaSabores.add(Sabor.LIMAO);
        listaSabores.add(Sabor.MARACUJA);
        listaSabores.add(Sabor.MELANCIA);
        listaSabores.add(Sabor.CAJU);
        listaSabores.add(Sabor.CAJA);
        listaSabores.add(Sabor.GOIABA);
        listaSabores.add(Sabor.MANGA);
        listaSabores.add(Sabor.MELAO);
        listaSabores.add(Sabor.TAMARINDO);
        listaSabores.add(Sabor.UVA);
        listaSabores.add(Sabor.GRAVIOLA);
        listaSabores.add(Sabor.CUPUACU);

        Collections.sort(listaSabores, new Comparador());

        for (int i = 0; i < listaSabores.size(); i++) {
            if(i%2==0){
                lista1.add(listaSabores.get(i));
            }else{
                lista2.add(listaSabores.get(i));
            }
        }

        return listaSabores;
    }

    public static ArrayList<Sabor> addVitaminaFlavors(ArrayList lista1, ArrayList lista2) {
        ArrayList<Sabor> listaSabores = new ArrayList<>();

        //Adicionar Acompanhamentos//
        listaSabores.add(Sabor.BANANA);
        listaSabores.add(Sabor.ABACATE);
        listaSabores.add(Sabor.MAMAO_BANANA_MACA);
        listaSabores.add(Sabor.GUARANA_DO_AMAZONAS);
        listaSabores.add(Sabor.GUARACAI);

        Collections.sort(listaSabores, new Comparador());

        for (int i = 0; i < listaSabores.size(); i++) {
            if(i%2==0){
                lista1.add(listaSabores.get(i));
            }else{
                lista2.add(listaSabores.get(i));
            }
        }

        return listaSabores;
    }

    public void buttonChecked(View v){

        v.getId();
        Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        switch(v.getId()) {
            case R.id.rdbtn_listaAdicional:
                precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                break;

            case R.id.rdbtn_listaTamanhos:
                precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
                precoFinal.notifyChange();
                break;

            case R.id.chckbtn_listaFrutas:
                precoFinal.setPrecoFrutas(precoFinal.getPrecoFrutas() + p.carregarPreferencias("precoFrutas"));
                precoFinal.notifyChange();
                break;

            case R.id.rdbtn_listaSabores:
                precoFinal.setPrecoSabores(p.carregarPreferencias("precoSabores"));
                precoFinal.notifyChange();
                adapterDrinkTypes.notifyDataSetChanged();
                break;

            case R.id.chckbtn_listaAcompanhamentos:
                precoFinal.setPrecoAcompanhamentos(getPrecoAcompanhamentos());
                precoFinal.notifyChange();
                break;
        }

        btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


    }

    public float getPrecoAcompanhamentos(){
        float precoAcompanhamentos = 0;
        int totalAcompanhamentos = 0;
        for(Sabor a: arrayAcompanhamentos1){
            if(a.isComprado()){
                totalAcompanhamentos ++;
            }
        }
        for(Sabor a: arrayAcompanhamentos2){
            if(a.isComprado()){
                totalAcompanhamentos ++;
            }
        }
        precoAcompanhamentos = totalAcompanhamentos > NUMERO_MINIMO_ACOMPANHAMENTO ? (float)( (totalAcompanhamentos-NUMERO_MINIMO_ACOMPANHAMENTO) * 1.5):0F;
        return precoAcompanhamentos;
    }

    public void alertDialogCustom(Pedido p) {

        final Pedido pedido = p;
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_layout_alert_dialog, null);
        TextView txtLista = alertLayout.findViewById(R.id.txtalert_pedido);

        txtLista.setText("Obrigado pela Preferência! \nRetire seu pedido na impressora e dirija-se ao caixa.");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        //Identificar enquanto está em Run - AlertDialog//
        final Dialog dialog = alert.show();

        dialog.getWindow().setBackgroundDrawableResource(R.color.sectionColor);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // verificar se a caixa de diálogo está visível
                if (dialog.isShowing()) {
                    // fecha a caixa de diálogo
                    dialog.dismiss();
                }
            }
        };

        //Quando Dialog fechar//
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });



        handler.postDelayed(runnable, 7000);
        montarCardapio();
        limparAdapter();



    }

    public void limparAdapter(){

        //Clear BowlSize RadioButtons
        clearCheckeBowlSizedButtons(0);

        // Clear Local Of Cunsumption RadioButtons
        rdbtn_inLoco = (RadioButton) findViewById(R.id.rdbtn_InLoco);
        rdbtn_viagem = (RadioButton) findViewById(R.id.rdbtn_Viagem);
        rdbtn_inLoco.setChecked(false);
        rdbtn_viagem.setChecked(false);


        for (int i = 0; i < arrayTamanhos.size() ; i++) {
            arrayTamanhos.get(i).setChecado(false);
        }

        adapterDrinkTypes.restaurarLista(addDrinkTypes());

        ArrayList<Sabor> listA = new ArrayList<Sabor>();
        ArrayList<Sabor> listB = new ArrayList<Sabor>();
        addMilkshakeFlavors(listA,listB);
        adapterSabores1.restaurarLista(listA);
        adapterSabores2.restaurarLista(listB);
        rdbtn_MarcarTodos.setChecked(false);
        saboresTile.setText("");
        listViewAcompanhamentos1.setVisibility(View.INVISIBLE);
        listViewAcompanhamentos2.setVisibility(View.INVISIBLE);

        Preferences preferences = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        preferences.salvarPreferencias("precoFrutas",(float)0);
        preferences.salvarPreferencias("precoAcompanhamentos", (float)0);
        preferences.salvarPreferencias("precosTamanhos",(float)0);
        preferences.salvarPreferencias("precoSabores", (float)0);
        preferences.salvarPreferencias("precoAdicional",(float)0);
        preferences.salvarPreferencias("precoEmbalagem", (float)(0));


        precoFinal = new PrecoFinal(0,0,0,0,0,0,0);
        btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

    }

    public void montarCardapio(){

        arrayTamanhos = adicionarTamanhos();

        //Montando Lista - Sabores//
        if(arraySabores!=null){
            arraySabores.clear();
        }
        arraySabores = addDrinkTypes();
        adapterDrinkTypes = new AdapterDrinkTypes(this, arraySabores);
        listViewSabores.setAdapter(adapterDrinkTypes);
        //adicionarSabores();
        ListaDinamica.setListViewHeighBasedOnItems(listViewSabores);


        //Montando Listas - Acompanhamento//
        if(arrayAcompanhamentos1 !=null){
            arrayAcompanhamentos1.clear();
        }if(arrayAcompanhamentos2 !=null){
            arrayAcompanhamentos2.clear();
        }

        arrayAcompanhamentos1 = new ArrayList<Sabor>();
        arrayAcompanhamentos2 = new ArrayList<Sabor>();
        //addMilkshakeFlavors(arrayAcompanhamentos1, arrayAcompanhamentos2);
        adapterSabores1 = new AdapterSabores(this, arrayAcompanhamentos1);
        listViewAcompanhamentos1.setAdapter(adapterSabores1);
        ListaDinamica.setListViewHeighBasedOnItems(listViewAcompanhamentos1);

        adapterSabores2 = new AdapterSabores(this, arrayAcompanhamentos2);
        listViewAcompanhamentos2.setAdapter(adapterSabores2);
        ListaDinamica.setListViewHeighBasedOnItems(listViewAcompanhamentos2);

    }

    public void montarTamanhoEstatico(View v) {

        Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        for (int i = 0; i < arrayTamanhos.size(); i++) {
            arrayTamanhos.get(i).setChecado(false);
        }

        if(v.getId() == R.id.rdbtn_tamanhoP){

            clearCheckeBowlSizedButtons(v.getId());

            arrayTamanhos.get(0).setChecado(true);
            p.salvarPreferencias("precoTamanhos",(float)(arrayTamanhos.get(0).getValor()));
            precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

        }
        else if(v.getId() == R.id.rdbtn_tamanhoM) {

            clearCheckeBowlSizedButtons(v.getId());

            arrayTamanhos.get(1).setChecado(true);
            p.salvarPreferencias("precoTamanhos", (float) (arrayTamanhos.get(1).getValor()));
            precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

        }
        else if(v.getId() == R.id.rdbtn_tamanhoG){

            clearCheckeBowlSizedButtons(v.getId());

            arrayTamanhos.get(2).setChecado(true);
            p.salvarPreferencias("precoTamanhos",(float)(arrayTamanhos.get(2).getValor()));
            precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

        }

    }

    public void clearCheckeBowlSizedButtons(int id){
        rdbtn_tamanhoP = (RadioButton)findViewById(R.id.rdbtn_tamanhoP);
        rdbtn_tamanhoM = (RadioButton)findViewById(R.id.rdbtn_tamanhoM);
        rdbtn_tamanhoG = (RadioButton)findViewById(R.id.rdbtn_tamanhoG);
        if (R.id.rdbtn_tamanhoP != id) rdbtn_tamanhoP.setChecked(false);
        if (R.id.rdbtn_tamanhoM != id) rdbtn_tamanhoM.setChecked(false);
        if (R.id.rdbtn_tamanhoG != id) rdbtn_tamanhoG.setChecked(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onDestroy(){
        try{
            btPrinter.closeBT();
        } catch(Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

}



