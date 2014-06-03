package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import mmACHINE.MealyMachine;
import android.os.Bundle;
import android.view.ViewGroup;

public class MealyActivity extends MActivity {
	protected class PlanodeFundoMealy extends PlanodeFundo{

		public PlanodeFundoMealy(int maxWidth, int maxHeight) {
			super(maxWidth, maxHeight);
		}
		@Override
		public void removeTransicaoEspecifica(int origem, int destino, String se){
			StringBuilder s = new StringBuilder("");
			String entrada = new StringTokenizer(se,"/").nextToken();
			StringTokenizer st = new StringTokenizer (textos.get(origem).get(destino).getText().toString(), ",");
			boolean primeira = true;
			while(st.hasMoreTokens()){
				String temp=st.nextToken(); 
				String t =new StringTokenizer (temp, "/").nextToken();
				if(!entrada.equals(t)){
					if(primeira){
						primeira=false;
						s.append(temp);
					}
					else{
						s.append(", ");
						s.append(temp);
					}
				}
			}
			if(s.length()==0) removeuTransicao(origem, destino);
			else textos.get(origem).get(destino).setText(s);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mm=new MealyMachine();
	}
	@Override
	protected Estado newEstado(float x, float y, ViewGroup Vg){
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
	protected PlanodeFundo newPlanodeFundo(int width, int height){
		return new PlanodeFundoMealy(width, height);
	}
}
