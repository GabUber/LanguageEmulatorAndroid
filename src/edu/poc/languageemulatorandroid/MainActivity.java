package edu.poc.languageemulatorandroid;

import mensageiro.Mensageiro;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import autogeneretadeanduglylanguagethingsdonottouch.MainTextiveis;
import autogeneretadeanduglylanguagethingsdonottouch.SalvavelTextiveis;

public class MainActivity extends Salvavel {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		new MenuInflater(this).inflate(R.menu.main, menu);
		m=menu;
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends FragmentoGeral {
		public PlaceholderFragment() {
			super();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return ligaFragmentoGeral((ViewGroup) rootView, inflater, container);
		}
	}
	public void gotoAFD(View v){
		esvaziaMensageiro();
	    Intent intent = new Intent(MainActivity.this, AFDActivity.class);
	    startActivity(intent);
	}
	private void esvaziaMensageiro() {
		// TODO Auto-generated method stub
		Mensageiro.arquivo=null;
		Mensageiro.lingua = linguas.indexOf(lingua);
		Mensageiro.linguas=linguas;
		Mensageiro.nomeArquivo=null;
	}

	public void gotoAFN(View v){
		esvaziaMensageiro();
	    Intent intent = new Intent(pegaContexto(), AFNActivity.class);
	    startActivity(intent);
	}
	public void gotoAFNL(View v){
		esvaziaMensageiro();
	    Intent intent = new Intent(pegaContexto(), AFNLActivity.class);
	    startActivity(intent);
	}
	public void gotoER(View v){
		esvaziaMensageiro();
		Intent intent = new Intent(pegaContexto(), ERActivity.class);
	    startActivity(intent);
	}
	public void gotoGR(View v){
		esvaziaMensageiro();
		Intent intent = new Intent(pegaContexto(), GRActivity.class);
	    startActivity(intent);
	}
	public void gotoMMealy(View v){
		esvaziaMensageiro();
		Intent intent = new Intent(pegaContexto(), MealyActivity.class);
	    startActivity(intent);
	}
	public void gotoMMOORE(View v){
		esvaziaMensageiro();
		Intent intent = new Intent(pegaContexto(), MooreActivity.class);
	    startActivity(intent);
	}

	@Override
	public String toString(View v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parse(String s) throws MalformedFileException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Salvavel pegaContexto() {
		return MainActivity.this;
	}

	@Override
	protected void setaLingua() {
		MainTextiveis.setaLingua(lingua);
	    super.setaLingua();
	}
	@Override
	public void criaEspecifico(){
		inicial.setVisibility(View.GONE);
	}

	@Override
	protected void carregaTextiveisMenu() {
		SalvavelTextiveis.carregaTextiveisMenuMain(m);
	}
	@Override
	protected void setaLinguaMenus() {
		SalvavelTextiveis.setaLinguaMenusMain(lingua);
	}
	@Override
	protected void trazOutroATona() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void carregaTextiveisEspecifico() {
		MainTextiveis.carregaTextiveis((Activity) pegaContexto());
	}

	@Override
	protected String pegaNomeAtividade() {
		return lingua.app_name;
	}
}
