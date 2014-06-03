package edu.poc.languageemulatorandroid;

import java.util.StringTokenizer;

import mmACHINE.MMachine;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public abstract class MActivity extends AFLikeActivity {
	protected MMachine mm;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	protected void preparaEmulacao() {
		String resposta;
		try{ resposta = mm.preparaSimulacao();}
		catch(RuntimeException re){
			resposta="";
		}
		setaTodosEstadosInativos();
		setaAtivos(mm.retornaAtivos());
		((TextView)findViewById(R.id.aflikesaida)).setText(resposta);
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
		mm.togglaInicial(e.pegaNome());
	}

	@Override
	protected void toggleFinal(Estado inicioMove2) {}

	@Override
	protected void paraSimul() {
		mm.paraSimulacao();
		setaTodosEstadosInativos();
	}

	@Override
	protected void simula(String s) {
		mm.preparaSimulacao();
		StringTokenizer entrada = new StringTokenizer(s);
		String resposta =mm.simula(entrada);
		setaTodosEstadosInativos();
		setaAtivos(mm.retornaAtivos());
		((TextView)findViewById(R.id.aflikesaida)).setText(resposta);
		mm.paraSimulacao();
	}

	@Override
	protected void removeTransicaoDeFato(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		String token;
		while(st.hasMoreTokens()){
			token=st.nextToken();
			if(token.equals("")) mm.removeTransicao(inicioMove.pegaNome(), fimMove.pegaNome(), null);
			else mm.removeTransicao(inicioMove.pegaNome(), fimMove.pegaNome(), token);
		}
	}
	@Override
	public void cria(View v){
		super.cria(v);
		findViewById(R.id.afliketogglafinalcimaesquerda).setVisibility(View.GONE);
		findViewById(R.id.afliketogglafinalbaixoesquerda).setVisibility(View.GONE);
		findViewById(R.id.afliketogglafinalcimadireita).setVisibility(View.GONE);
		findViewById(R.id.afliketogglafinalbaixodireita).setVisibility(View.GONE);
		//TODO atualizar tamanhos de menu3;
		LayoutParams params = new LayoutParams(menu3[0].getWidth(), (int) (menu3[0].getHeight()*0.835));
		menu3[0].setLayoutParams(params);
		menu3[0].setVisibility(View.VISIBLE);menu3[0].setVisibility(View.INVISIBLE);
		menu3[1].setLayoutParams(params);
		menu3[1].setVisibility(View.VISIBLE);menu3[1].setVisibility(View.INVISIBLE);
		menu3[2].setLayoutParams(params);
		menu3[2].setVisibility(View.VISIBLE);menu3[2].setVisibility(View.INVISIBLE);
		menu3[3].setLayoutParams(params);
		menu3[3].setVisibility(View.VISIBLE);menu3[3].setVisibility(View.INVISIBLE);
	}

}
