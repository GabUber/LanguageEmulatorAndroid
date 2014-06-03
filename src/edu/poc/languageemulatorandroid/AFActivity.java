package edu.poc.languageemulatorandroid;

import java.util.StringTokenizer;

import af.AF;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class AFActivity extends AFLikeActivity {
	AF af;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		r=getResources();
	}
	@Override
	protected Estado newEstado(float x, float y, ViewGroup vg){
		return new Estado(x,y,vg);
	}
	@Override
	protected void preparaEmulacao() {
		boolean resposta = af.preparaSimulacao();
		setaTodosEstadosInativos();
		setaAtivos(af.retornaAtivos());
		((TextView)findViewById(R.id.aflikesaida)).setText(resposta ? r.getString(R.string.aceito) : r.getString(R.string.recusado));
	}
	Resources r;
	@Override
	protected void toggleFinal(Estado inicioMove2) {
		af.togglaFinal(inicioMove2.pegaNome());
		inicioMove2.toggleFinal();
	}
	@Override
	protected void geraEstado(String string) {
		af.adicionaEstado(string);
	}

	@Override
	protected void paraSimul() {
		af.paraSimulacao();
		setaTodosEstadosInativos();
	}

	@Override
	protected void simula(String s) {
		af.preparaSimulacao();
		StringTokenizer entrada = new StringTokenizer(s);
		boolean resposta =af.simula(entrada);
		setaTodosEstadosInativos();
		setaAtivos(af.retornaAtivos());
		((TextView)findViewById(R.id.aflikesaida)).setText(resposta ? r.getString(R.string.aceito) : r.getString(R.string.recusado));
		af.paraSimulacao();
	}

	@Override
	protected void removeTransicaoDeFato(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		String token;
		while(st.hasMoreTokens()){
			token=st.nextToken();
			if(token.equals("")) af.removeTransicao(inicioMove.pegaNome(), fimMove.pegaNome(), null);
			else af.removeTransicao(inicioMove.pegaNome(), fimMove.pegaNome(), token);
		}
	}
	@Override
	protected PlanodeFundo newPlanodeFundo(int width, int height){
		return new PlanodeFundo(width, height);
	}
}
