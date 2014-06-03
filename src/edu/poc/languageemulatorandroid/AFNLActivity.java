package edu.poc.languageemulatorandroid;

import java.util.StringTokenizer;

import af.AFNL;
import android.os.Bundle;

public class AFNLActivity extends AFActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		af=new AFNL();
	}
	@Override
	protected void toggleInicial(Estado e) {
		e.toggleInicial();
		af.togglaInicial(e.pegaNome());
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void editaTransicoes(String s) {
		if(s.equals("")) s = "λ";
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
				if(tempString.equals("λ"))af.adicionaTransicao(origemNome, destinoNome, null);
				else af.adicionaTransicao(origemNome, destinoNome, tempString);
				if((!primeiro)&&tempString.length()!=0) sb.append(",");
				sb.append(tempString);
				primeiro=false;
			}
		}
		if(s.charAt(s.length()-1)==','){
			sb.append(",λ");
			af.adicionaTransicao(origemNome, destinoNome, null);
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
