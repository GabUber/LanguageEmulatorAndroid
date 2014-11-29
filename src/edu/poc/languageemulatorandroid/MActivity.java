package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import mmACHINE.MMachine;
import tratadorEntrada.MTratadorEntrada;
import tratadorEntrada.TratadorEntrada;
import android.os.Bundle;
import android.view.View;
import conversor.Conversor;
import conversor.ConversorMM;
import estado.Estado;

public abstract class MActivity extends AFLikeActivity {
	protected MMachine mm;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	protected void preparaEmulacao() {
		String resposta;
		if(!emulacaoPreparada){
			try{ resposta = mm.preparaSimulacao();}
			catch(RuntimeException re){resposta="";}
			emulacaoPreparada=true;
		}
		else resposta="";
		setaTodosEstadosInativos();
		setaAtivos(mm.retornaAtivos());
		saidaEmul.setText(resposta);
		emulacaoPreparada=true;
	}
	@Override
	protected TratadorEntrada criaTE(){
		return new MTratadorEntrada();
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
		emulacaoPreparada=false;
		mm.paraSimulacao();
		setaTodosEstadosInativos();
	}
	@Override
	protected void simula(String s) {
		if(!emulacaoPreparada){
			mm.preparaSimulacao();
			emulacaoPreparada=true;
		}
		StringTokenizer entrada = new StringTokenizer(s);
		String resposta =mm.simula(entrada);
		setaTodosEstadosInativos();
		setaAtivos(mm.retornaAtivos());
		saidaEmul.setText(resposta);
		mm.paraSimulacao();
		emulacaoPreparada=false;
	}
	@Override
	protected void deletaEstadoDeFato(String s){
		mm.removeEstado(s);
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
	protected final void apagaEspecificos(){
		apagaFilhos(true, findViewById(R.id.aflikegeragr), findViewById(R.id.aflikegeraer), findViewById(R.id.aflikegeraaf), pegaAApagarEspecifico());
		apagaFilhos(false, findViewById(R.id.afliketogglafinalcimaesquerda));
		apagaFilhos(false, findViewById(R.id.afliketogglafinalcimadireita));
		apagaFilhos(false, findViewById(R.id.afliketogglafinalbaixoesquerda));
		apagaFilhos(false, findViewById(R.id.afliketogglafinalbaixodireita));
	}
	protected abstract View pegaAApagarEspecifico();
	@Override
	protected Conversor criaC() {
		return new ConversorMM();
	}
	@Override
	protected int trataFinais(String s)throws AFLikeMalformedFileException {
		if(Integer.parseInt(s)!=0)throw new AFLikeMalformedFileException();
		return 0;
	}
	@Override
	protected int trataIniciais(String s) throws AFLikeMalformedFileException {
		int n = Integer.parseInt(s);
		if(n>1)throw new AFLikeMalformedFileException();
		return n;
	}
	@Override
	protected String simulaPasso(String s){
		if(!emulacaoPreparada){
			mm.preparaSimulacao();
			emulacaoPreparada=true;
		}
		String resposta =mm.simula(s);
		setaTodosEstadosInativos();
		setaAtivos(mm.retornaAtivos());
		return resposta;
	}
	@Override
	public void geragre(View v){}
	@Override
	public void geragrd(View v){}
	@Override
	public void geraafd(View v){}
	@Override
	public void geraafn(View v){}
	@Override
	public void geraafnl(View v){}
	@Override
	public void geraer(View v){}
	@Override
	public void simplifica(View v){
		if(menuaberto)return;
		ArrayList<Integer> e = c.simplifica(planoDeFundo, estados, null, mm, nomeEstados);
		int max = e.size();
		for(int i = 0; i < max; i++){
			inicioMove=estados.get(e.get(i));
			estadoDeleta(null);
		}
	}
}
