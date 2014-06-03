package edu.poc.languageemulatorandroid;

import java.util.StringTokenizer;

import af.AFN;
import android.os.Bundle;

public class AFNActivity extends AFActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		af=new AFN();
	}
	@Override
	protected void toggleInicial(Estado e) {
		e.toggleInicial();
		af.togglaInicial(e.pegaNome());
		
	}

	@SuppressWarnings( "deprecation" )
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

}
