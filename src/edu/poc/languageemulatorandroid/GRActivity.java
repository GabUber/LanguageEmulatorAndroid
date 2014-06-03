package edu.poc.languageemulatorandroid;

import gr.GR;
import gr.GRDireita;
import gr.GREsquerda;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GRActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gr);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		existeGR=false;
		menuAtivo=false;
		r=getResources();
		gr=null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gr, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_gr, container,
					false);
			return rootView;
		}
	}

	private String s;
	boolean existeGR,menuAtivo;
	Resources r ;
	GR gr;
	public void mostraMenu(View v){
		s=((Button)v).getText().toString();
		if(existeGR){
			View menu = findViewById(R.id.grleiaute);
			menu.setVisibility(View.VISIBLE);
			menu.setX(v.getX()+v.getWidth()/2+menu.getWidth());
			menu.setY(v.getY()+v.getHeight()*3/2);
			menu.bringToFront();
			menuAtivo=true;
		}
		else{
			existeGR=true;
			if(s.equals(r.getString(R.string.esquerda))) gr=new GREsquerda();
			else gr=new GRDireita();
		}
	}
	public void cancela(View v){
		((LinearLayout)findViewById(R.id.grleiaute)).setVisibility(View.GONE);
		menuAtivo=false;
	}
	public void confirmaCriacao(View v){
		((View)v.getParent()).setVisibility(View.GONE);
		existeGR=true;
		if(s.equals(r.getString(R.string.esquerda))) gr=new GREsquerda();
		else gr=new GRDireita();
		((TextView) findViewById(R.id.grtextoimprimeregras)).setText("");
		menuAtivo=false;
	}
	public void insereNovaRegra(View v){
		if(!existeGR||menuAtivo) return;
		TextView tvEsquerda = (TextView) findViewById(R.id.grimputregraesquerda);
		TextView tvDireita = (TextView) findViewById(R.id.grimputregradireita);
		TextView mostraRegras = (TextView) findViewById(R.id.grtextoimprimeregras);
		String parteEsquerda = tvEsquerda.getText().toString();
		String parteDireita = tvDireita.getText().toString();
		if(parteEsquerda==null||parteEsquerda.equals("")) return;
		if(parteDireita==null||parteDireita.equals("")) parteDireita="";
		gr.novaRegra(parteEsquerda.toUpperCase(), parteDireita);
		mostraRegras.setText(gr.toString());
	}
	public void removeRegra(View v){
		if(!existeGR||menuAtivo)return;
		TextView tv = (TextView) findViewById(R.id.grimputremoveregra);
		TextView mostraRegras = (TextView) findViewById(R.id.grtextoimprimeregras);
		int n;
		try{n = Integer.parseInt(tv.getText().toString());
		gr.removeRegra(n);
		mostraRegras.setText(gr.toString());}
		catch(NumberFormatException nfe){}
	}
	public void testaPadrao(View v){
		if(!existeGR||menuAtivo)return;
		gr.preparaSimulacao();
		TextView entrada = (TextView) findViewById(R.id.grimputpadrao);
		TextView saida = (TextView) findViewById(R.id.grtextoresposta);
		boolean aceito=gr.simula(entrada.getText().toString());
		gr.paraSimulacao();
		saida.setText(aceito ? r.getString(R.string.aceito) : r.getString(R.string.recusado));
	}
}
