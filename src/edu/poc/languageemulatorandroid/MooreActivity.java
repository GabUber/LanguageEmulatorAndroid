package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import mmACHINE.MooreMachine;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MooreActivity extends MActivity {
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mm=new MooreMachine();
	}
	@Override
	protected Estado newEstado(float x, float y, ViewGroup vg){
		return new EstadoMoore(x,y,vg);
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
			mm.removeTransicao(origemNome, destinoNome, st.nextToken());
		}
		planoDeFundo.removeuTransicao(estados.indexOf(inicioMove), estados.indexOf(fimMove));
		st = new StringTokenizer(s, ",");
		StringBuilder sb = new StringBuilder("");
		boolean primeiro = true;
		if(st.hasMoreTokens()){
			while(st.hasMoreTokens()){
				String tempString = st.nextToken();
				ArrayList<Integer> tempArray = mm.pegaDestino(origemNome, tempString);
				for(int i = 0; i < tempArray.size();i++){
					planoDeFundo.removeTransicaoEspecifica(origem, tempArray.get(i), tempString);
					mm.removeTransicao(origemNome, destinoNome, tempString);
				}
				mm.adicionaTransicao(origemNome, destinoNome, tempString, null);
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
	protected void geraEstado(String s) {
		StringTokenizer st = new StringTokenizer(s, "/");
		String entrada=st.nextToken();
		String saida=st.nextToken();
		mm.adicionaEstado(entrada, saida);
	}
	@Override
	public void criaEstado(View v){
		EstadoMoore es = (EstadoMoore)inicioMove;
		String s = ((EditText) findViewById(R.id.aflikeentradanomeestadocimaesquerda)).getText().toString();
		if(s.equals("")) s = ((EditText) findViewById(R.id.aflikenomeestadobaixoesquerda)).getText().toString();
		if(s.equals("")) s = ((EditText) findViewById(R.id.aflikenomeestadocimadireita)).getText().toString();
		if(s.equals("")) s = ((EditText) findViewById(R.id.aflikenomeestadobaixodireita)).getText().toString();
		StringTokenizer st = new StringTokenizer(s, "/");
		String nome;
		if(s==null||s.equals("")||st.countTokens()<2){
			invizibilizaTudo();
			menuaberto=false;
			inicioMove.setaExiste(false, vg);
			vg.removeView(inicioMove);
			return;
		
		}
		try {
			geraEstado(s);
		}
		catch (RuntimeException re){
			invizibilizaTudo();
			menuaberto=false;
			inicioMove.setaExiste(false, vg);
			vg.removeView(inicioMove);
			return;
		}
		nome=st.nextToken();
		planoDeFundo.adicionouEstado(inicioMove.getX(), inicioMove.getY(), this, vg);
		inicioMove.setaNome(nome);
		es.setaSaida(st.nextToken());
		setaEscutador(inicioMove);
		estados.add(inicioMove);
		nomeEstados.add(nome);
		invizibilizaTudo();
		menuaberto=false;
		inicioMove.setVisibility(View.VISIBLE);
		vg.invalidate();
		tentouCriar=false;
	}	
	@Override
	protected PlanodeFundo newPlanodeFundo(int width, int height){
		return new PlanodeFundo(width, height);
	}
}
