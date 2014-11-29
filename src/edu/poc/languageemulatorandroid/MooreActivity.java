package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import mmACHINE.MooreMachine;
import transicoes.Transicoes;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import conversor.ConversorMM;
import estado.Estado;
import estado.EstadoMoore;

public class MooreActivity extends MActivity {
	@Override
	public void criaEspecifico(){
		mm=new MooreMachine();
		super.criaEspecifico();
	}
	@Override
	protected Estado newEstado(float x, float y, RelativeLayout vg){
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
		String s = entrada.getText().toString();
		StringTokenizer st = new StringTokenizer(s, "/");
		String nome;
		if(s==null||s.equals("")||st.countTokens()<2){
			aInvizibilizar.setVisibility(View.GONE);
			menuaberto=false;
			inicioMove.setaExiste(false, vg);
			vg.removeView(inicioMove);
			return;
		}
		try {
			geraEstado(s);
		}
		catch (RuntimeException re){
			aInvizibilizar.setVisibility(View.GONE);
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
		aInvizibilizar.setVisibility(View.GONE);
		menuaberto=false;
		inicioMove.setVisibility(View.VISIBLE);
		vg.invalidate();
		tentouCriar=false;
	}
	@Override
	protected Transicoes newPlanodeFundo(int width, int height){
		return new Transicoes(width, height, getResources());
	}
	@Override
	protected String pegaTipo() {
		return "Moore";
	}
	@Override
	public void geramealy(View v) {
		arquivo = ((ConversorMM)(c)).toMealy(planoDeFundo, estados, w, h);
		preparaMensageiro(false);
	    Intent intent = new Intent(pegaContexto(), MealyActivity.class);
	    startActivity(intent);
	}
	@Override
	public void geramoore(View v) {}
	@Override
	public Salvavel pegaContexto() {
		return MooreActivity.this;
	}
	@Override
	protected void trataEstados(StringTokenizer st, double fatorW, double fatorH) throws AFLikeMalformedFileException{
		int n = Integer.parseInt(st.nextToken());
		for (int i = 0; i < n;i++){
			StringTokenizer temp =new StringTokenizer(te.estado(st.nextToken()), "/");
			String s1=temp.nextToken();
			String s2=temp.nextToken();
			if(s1.length()==0) throw new AFLikeMalformedFileException();
			geraEstado(s1+'/'+s2);
			float x = (float) ((Float.parseFloat(st.nextToken())) * fatorW);
			float y = (float) ((Float.parseFloat(st.nextToken())) * fatorH);
			inicioMove=newEstado(x+dimensaomm/2, y+dimensaomm/2, vg);
			inicioMove.setVisibility(View.VISIBLE);
			vg.addView(inicioMove);
			planoDeFundo.adicionouEstado(x, y, this, vg);
			inicioMove.setaNome(s1);
			((EstadoMoore)inicioMove).setaSaida(s2);
			estados.add(inicioMove);
			setaEscutador(inicioMove);
			nomeEstados.add(s1);
			vg.invalidate();
		}
	}
	@Override
	protected String pegaNomeAtividade() {
		return lingua.mmoore;
	}
	@Override
	protected View pegaAApagarEspecifico() {
		return findViewById(R.id.aflikegeramoore);
	}
}
