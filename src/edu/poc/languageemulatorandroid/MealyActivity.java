package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import mmACHINE.MealyMachine;
import transicoes.Transicoes;
import transicoes.TransicoesMealy;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import conversor.ConversorMM;
import estado.Estado;

public class MealyActivity extends MActivity {
	@Override
	public void criaEspecifico(){
		mm=new MealyMachine();
		super.criaEspecifico();
	}
	@Override
	protected Estado newEstado(float x, float y, RelativeLayout vg){
		return new Estado(x,y,vg);
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void editaTransicoes(String s) {
		if(s.equals(""))return;
		String origemNome = inicioMove.pegaNome(), destinoNome=fimMove.pegaNome();
		int origem = estados.indexOf(inicioMove), destino = estados.indexOf(fimMove);
		String atual = planoDeFundo.pegaTexto(origem, destino);
		if(atual.equals(s)) return;
		StringTokenizer st = new StringTokenizer(atual, ","), aux;
		while(st.hasMoreTokens()){
			aux=new StringTokenizer(st.nextToken(), "/");
			mm.removeTransicao(origemNome, destinoNome, aux.nextToken());
		}
		planoDeFundo.removeuTransicao(estados.indexOf(inicioMove), estados.indexOf(fimMove));
		st = new StringTokenizer(s, ",");
		StringBuilder sb = new StringBuilder("");
		boolean primeiro = true;
		if(st.hasMoreTokens()){
			while(st.hasMoreTokens()){
				aux=new StringTokenizer(st.nextToken(), "/");
				if(aux.countTokens()<2)continue;
				String tempEntrada = aux.nextToken();
				String tempSaida = aux.nextToken();
				ArrayList<Integer> tempArray = mm.pegaDestino(origemNome, tempEntrada);
				for(int i = 0; i < tempArray.size();i++){
					planoDeFundo.removeTransicaoEspecifica(origem, tempArray.get(i), tempEntrada+"/"+tempSaida);
					mm.removeTransicao(origemNome, destinoNome, tempEntrada);
				}
				mm.adicionaTransicao(origemNome, destinoNome, tempEntrada, tempSaida);
				if((!primeiro)) sb.append(",");
				sb.append(tempEntrada);
				sb.append("/");
				sb.append(tempSaida);
				primeiro=false;
			}
		}
		if(sb.length()!=0){
			planoDeFundo.novaTransicao(origem, destino, sb.toString());
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			    vg.setBackgroundDrawable(planoDeFundo);
			} else {
			    vg.setBackground(planoDeFundo);
			}
			vg.invalidate();
		}
	}
	@Override
	protected void geraEstado(String s) {
		mm.adicionaEstado(s, null);
	}
	@Override
	protected Transicoes newPlanodeFundo(int width, int height){
		return new TransicoesMealy(width, height, getResources());
	}
	@Override
	protected String pegaTipo() {
		return "Mealy";
	}
	@Override
	public void geramealy(View v) {}
	@Override
	public void geramoore(View v) {
		arquivo = ((ConversorMM)(c)).toMoore(planoDeFundo, estados, w, h);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), MooreActivity.class);
	    startActivity(intent);
	}
	@Override
	public Salvavel pegaContexto() {
		return MealyActivity.this;
	}
	@Override
	protected String pegaNomeAtividade() {
		return lingua.mmealy;
	}
	@Override
	protected View pegaAApagarEspecifico() {
		return findViewById(R.id.aflikegeramealy);
	}
}
