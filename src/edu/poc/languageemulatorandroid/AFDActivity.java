package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import af.AFD;
import android.content.Intent;
import android.view.View;
import conversor.ConversorAF;
import estado.Estado;

public class AFDActivity extends AFActivity {
	@Override
	public void criaEspecifico(){
		af=new AFD();
		super.criaEspecifico();
	}
	@Override
	protected void toggleInicial(Estado e) {
		if(!e.ehInicial()){
			for (int i=0;i<estados.size();i++){
				if(estados.get(i).ehInicial()){
					estados.get(i).toggleInicial();
					break;
				}
			}
		}
		e.toggleInicial();
		af.togglaInicial(e.pegaNome());
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void editaTransicoes(String s) {
		if(s.equals(""))return;
		String origemNome = inicioMove.pegaNome(), destinoNome=fimMove.pegaNome();
		int origem = estados.indexOf(inicioMove), destino = estados.indexOf(fimMove);
		String atual = planoDeFundo.pegaTexto(origem, destino);
		if(atual.equals(s)) return;
		StringTokenizer st = new StringTokenizer(atual, ",");
		while(st.hasMoreTokens()){
			af.removeTransicao(origemNome, destinoNome, st.nextToken());
		}
		planoDeFundo.removeuTransicao(estados.indexOf(inicioMove), estados.indexOf(fimMove));
		st = new StringTokenizer(s, ",");
		StringBuilder sb = new StringBuilder("");
		boolean primeiro = true;
		if(st.hasMoreTokens()){
			while(st.hasMoreTokens()){
				String tempString = st.nextToken();
				ArrayList<Integer> tempArray = af.pegaDestino(origemNome, tempString);
				for(int i = 0; i < tempArray.size();i++){
					planoDeFundo.removeTransicaoEspecifica(origem, tempArray.get(i), tempString);
					af.removeTransicao(origemNome, destinoNome, tempString);
				}
				af.adicionaTransicao(origemNome, destinoNome, tempString);
				if((!primeiro)&&tempString.length()!=0) sb.append(",");
				sb.append(tempString);
				primeiro=false;
			}
		}
		planoDeFundo.novaTransicao(origem, destino, sb.toString());
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    vg.setBackgroundDrawable(planoDeFundo);
		} else {
		    vg.setBackground(planoDeFundo);
		}
		vg.invalidate();
	}
	@Override
	protected String pegaTipo() {
		return "AFD";
	}
	@Override
	protected int trataIniciais(String s) throws AFLikeMalformedFileException {
		int n = Integer.parseInt(s);
		if (n>1) throw (new AFLikeMalformedFileException());
		return n;
	}
	@Override
	protected Boolean temLambda() {
		return false;
	}
	@Override
	protected void apagaEspecificos() {
		super.apagaEspecificos();
		apagaFilhos(true, findViewById(R.id.aflikegeraafd));
	}

	@Override
	public Salvavel pegaContexto() {
		return AFDActivity.this;
	}
	@Override
	public void geraafn(View v){
		arquivo = ((ConversorAF) (c)).toString(planoDeFundo, estados, "AFN", w, h);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), AFNActivity.class);
	    startActivity(intent);
	}
	@Override
	protected String pegaNomeAtividade() {
		return lingua.af;
	}
}