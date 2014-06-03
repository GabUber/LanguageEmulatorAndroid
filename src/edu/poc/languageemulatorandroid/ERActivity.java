package edu.poc.languageemulatorandroid;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import er.ER;

public class ERActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_er);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		er=new ER();
		r = getResources();
		existeEr=false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.er, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_er, container,
					false);
			return rootView;
		}
	}

	ER er;
	Resources r;
	boolean existeEr;
	public void novaExpresao(View V){
		EditText et = (EditText)findViewById(R.id.erimputer);
		String s = et.getText().toString();
		et.setText("");
		try{er.novaExpress�o(s);
			existeEr=true;}
		catch(RuntimeException r){}
		TextView saida = (TextView)findViewById(R.id.ertextoexpressao);
		saida.setText(er.toString());
	}
	public void testaPadrao(View V){
		if(!existeEr) return;
		EditText et = (EditText)findViewById(R.id.erimputpadrao);
		String s = et.getText().toString();
		TextView padrao = (TextView)findViewById(R.id.ertextopadrao);
		padrao.setText(s);
		et.setText("");
		boolean aceito=er.testaExpress�o(s);
		TextView saida = (TextView)findViewById(R.id.ertextosaida);
		saida.setText(aceito ? r.getString(R.string.aceito) : r.getString(R.string.recusado));
	}
}
