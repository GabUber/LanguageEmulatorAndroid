package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import transicoes.Transicoes;
import tratadorEntrada.ATratadorEntrada;
import tratadorEntrada.TratadorEntrada;
import af.AF;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import conversor.Conversor;
import conversor.ConversorAF;
import estado.Estado;

public abstract class AFActivity extends AFLikeActivity {
	AF af;
	boolean resposta;
	@Override
	protected Estado newEstado(float x, float y, RelativeLayout vg){
		return new Estado(x,y,vg);
	}
	@Override
	protected void preparaEmulacao() {
		resposta = af.preparaSimulacao();
		setaTodosEstadosInativos();
		setaAtivos(af.retornaAtivos());
		saidaEmul.setText(resposta ? lingua.aceito : lingua.recusado);
		emulacaoPreparada=true;
	}
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
	protected void deletaEstadoDeFato(String s){
		af.removeEstado(s);
	}
	@Override
	protected void paraSimul() {
		af.paraSimulacao();
		setaTodosEstadosInativos();
	}
	@Override
	protected void simula(String s) {
		if(!emulacaoPreparada){
			af.preparaSimulacao();
			emulacaoPreparada=true;
		}
		StringTokenizer entrada = new StringTokenizer(s);
		resposta =af.simula(entrada);
		setaTodosEstadosInativos();
		setaAtivos(af.retornaAtivos());
		saidaEmul.setText(resposta ? lingua.aceito : lingua.recusado);
		af.paraSimulacao();
		emulacaoPreparada=false;
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
	protected Transicoes newPlanodeFundo(int width, int height){
		return new Transicoes(width, height, getResources());
	}
	@Override
	protected Conversor criaC(){
		return new ConversorAF();
	}
	@Override
	protected TratadorEntrada criaTE() {
			return new ATratadorEntrada();
	}
	protected int trataFinais(String s){
		return Integer.parseInt(s);
	}
	@Override
	public void geragre(View v){
		arquivo = ((ConversorAF) (c)).toGRE(planoDeFundo, estados);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), GRActivity.class);
	    startActivity(intent);
	}
	@Override
	public void geragrd(View v){
		arquivo = ((ConversorAF) (c)).toGRD(planoDeFundo, estados);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), GRActivity.class);
	    startActivity(intent);
	}
	@Override
	public void geraafd(View v){
		arquivo = ((ConversorAF) (c)).toAFD(planoDeFundo, estados, temLambda(), vg, pegaContexto(), dimensaomm, w, h);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), AFDActivity.class);
	    startActivity(intent);
	}
	@Override
	public void geraafn(View v){
		arquivo = ((ConversorAF) (c)).toAFN(planoDeFundo, estados, temLambda(), w, h);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), AFNActivity.class);
	    startActivity(intent);
	}
	@Override
	public void geraafnl(View v){
		arquivo = ((ConversorAF) (c)).toString(planoDeFundo, estados, "AFNL", w, h);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), AFNLActivity.class);
	    startActivity(intent);
	}
	@Override
	public void geraer(View v){
		arquivo = ((ConversorAF) (c)).toER(planoDeFundo, estados);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), ERActivity.class);
	    startActivity(intent);
	}
	@Override
	public void geramealy(View v){}
	@Override
	public void geramoore(View v){}
	@Override
	protected String simulaPasso(String s){
		if(!emulacaoPreparada){
			af.preparaSimulacao();
			emulacaoPreparada=true;
		}
		resposta =af.simula(s);
		setaTodosEstadosInativos();
		setaAtivos(af.retornaAtivos());
		return resposta ? lingua.aceito : lingua.recusado;
	}
	protected abstract Boolean temLambda();
	@Override
	protected void apagaEspecificos(){
		apagaFilhos(true, findViewById(R.id.aflikegeramealy), findViewById(R.id.aflikegeramoore));
	}
	@Override
	public void simplifica(View v){
		if(menuaberto)return;
		ArrayList<Integer> e = c.simplifica(planoDeFundo, estados, af, null, nomeEstados);
		int max = e.size();
		for(int i = 0; i < max; i++){
			inicioMove=estados.get(e.get(i));
			estadoDeleta(null);
		}
	}
	@Override
	public void setaLingua(){
		saidaEmul.setText(resposta ? lingua.aceito : lingua.recusado);
		super.setaLingua();
	}
}