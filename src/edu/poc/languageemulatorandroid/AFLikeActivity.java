package edu.poc.languageemulatorandroid;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public abstract class AFLikeActivity extends ActionBarActivity {
	protected ViewGroup vg;
	protected LinearLayout menu1;
	protected LinearLayout menu2[];
	protected LinearLayout menu3[];
	protected LinearLayout menu4[];
	protected LinearLayout menu5[];
	protected LinearLayout menu6;
	protected LinearLayout menu7[];
	protected static final int dimensao = 10;
	protected int dimensaomm;
	protected boolean tentouMover;
	protected boolean tentouCriar;
	protected PlanodeFundo planoDeFundo;
	protected ArrayList<Estado> estados;
	protected ArrayList<String> nomeEstados;
	protected Estado inicioMove, fimMove;
	protected class PlanodeFundo extends Drawable{
		ArrayList<ArrayList<Flecha>> flechas;
		ArrayList<ArrayList<TextView>> textos;
		ArrayList<ArrayList<Boolean>> ativos;
		ArrayList<Float> x;
		ArrayList<Float> y;
		Drawable fundo;
		Paint p;
		protected static final int cons=10;
		protected int estadoSize=0;
		public void setaDimensao(int px) {
			estadoSize=px;
		}
		public PlanodeFundo(int maxWidth, int maxHeight){
			this.setBounds(0, 0, maxWidth-1, maxHeight-1);
			Flecha.setaCorrecao(cons);
			flechas=new ArrayList<ArrayList<Flecha>>();
			textos =new ArrayList<ArrayList<TextView>>();
			ativos=new ArrayList<ArrayList<Boolean>>();
			x=new ArrayList<Float>();
			y=new ArrayList<Float>();
			fundo=getResources().getDrawable(R.drawable.fundo);
			fundo.setBounds(getBounds());
			p=new Paint();
			p.setColor(Color.WHITE);
		}
		@Override
		public void draw(Canvas canvas) {
			fundo.draw(canvas);
			for(int i=0; i<flechas.size();i++){
				for(int j = 0; j < flechas.get(i).size(); j++){
					if(ativos.get(i).get(j)){
						flechas.get(i).get(j).desenha(canvas, p);
					}
				}
			}
			
		}

		@Override
		public void setAlpha(int alpha) {			
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
		}

		@Override
		public int getOpacity() {
			return 0;
		}
		public String pegaTexto(int origem, int destino){
			return textos.get(origem).get(destino).getText().toString();
		}
		public void novaTransicao(int origem, int destino, String s){
			TextView tv = textos.get(origem).get(destino);
			tv.setText(s);
			if(origem!=destino)tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tv.setVisibility(View.VISIBLE);
			ativos.get(origem).set(destino, true);
		}
		public void removeuEstado(int indice, ViewGroup vg) {
			for(int i = 0; i < textos.size(); i++){
				vg.removeView(textos.get(indice).get(i));
				vg.removeView(flechas.get(indice).get(i));
			}
			flechas.remove(indice);
			textos.remove(indice);
			ativos.remove(indice);
			x.remove(indice);
			y.remove(indice);
			for(int i = 0; i < flechas.size(); i++){
				vg.removeView(textos.get(i).get(indice));
				vg.removeView(flechas.get(i).get(indice));
				flechas.get(i).remove(indice);
				textos.get(i).remove(indice);
				ativos.get(i).remove(indice);
			}
		}
		public void adicionouEstado(float x, float y, Context context, ViewGroup vg) {
			this.x.add(x);
			this.y.add(y);
			Flecha F;
			for(int i = 0; i < flechas.size(); i++){
				float[]var=Flecha.calculaCorrecao(this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, x+estadoSize/2, y+estadoSize/2);
				F=new Flecha(this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, x+estadoSize/2, y+estadoSize/2, context);
				flechas.get(i).add(F);
				TextView tv = new TextView(context);
				vg.addView(tv);
				vg.addView(F);
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				tv.setMinimumHeight(10);
				tv.setMinimumWidth(10);
				tv.setX((this.x.get(i)+x+estadoSize)/2+var[0]*2);
				tv.setY((this.y.get(i)+y+estadoSize)/2+var[1]*2+(var[1]<0?-tv.getHeight():tv.getHeight()));
				tv.setVisibility(View.INVISIBLE);
				textos.get(i).add(tv);
				ativos.get(i).add(false);
			}
			flechas.add(new ArrayList<Flecha>());
			textos.add(new ArrayList<TextView>());
			ativos.add(new ArrayList<Boolean>());
			int n = flechas.size()-1;
			for(int i = 0; i <=n; i++){
				float[] var = Flecha.calculaCorrecao(x+estadoSize/2, y+estadoSize/2, this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2);
				F=new Flecha(x+estadoSize/2, y+estadoSize/2, this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, context);
				flechas.get(n).add(F);
				TextView tv = new TextView(context);
				vg.addView(tv);
				vg.addView(F);
				tv.setMinimumHeight(10);
				tv.setMinimumWidth(10);
				if(i!=n){
					tv.setX((this.x.get(i)+x+estadoSize)/2 +var[0]*2);
					tv.setY((this.y.get(i)+y+estadoSize)/2 +var[1]*2+(var[1]<0?-tv.getHeight():tv.getHeight()));
					tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}else{
					tv.setLayoutParams(new LayoutParams(estadoSize, estadoSize*3/5));
					tv.setX(x);
					tv.setY(y-estadoSize*4/5);
					tv.setGravity(Gravity.CENTER);
				}
				tv.setVisibility(View.INVISIBLE);
				textos.get(n).add(tv);
				ativos.get(n).add(false);
			}
		}
		public void moveuEstado(int id, float x, float y) {
			this.x.set(id, x);
			this.y.set(id, y);
			for(int i = 0; i < flechas.size(); i++){
				TextView tv = textos.get(i).get(id);
				flechas.get(i).get(id).moveFlecha(this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, x+estadoSize/2, y+estadoSize/2);
				if(i!=id){
					float[] var = Flecha.calculaCorrecao(this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, x+estadoSize/2, y+estadoSize/2);
					tv.setX((this.x.get(i)+x+estadoSize)/2 + var[0]*2);
					tv.setY((this.y.get(i)+y+estadoSize)/2 + var[1]*2-(var[1]<0?tv.getHeight():0));
					tv = textos.get(id).get(i);
					flechas.get(id).get(i).moveFlecha(x+estadoSize/2, y+estadoSize/2, this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2);
					tv.setX((this.x.get(i)+x+estadoSize)/2 - var[0]);
					tv.setY((this.y.get(i)+y+estadoSize)/2 - var[1]-(var[1]<0?0:tv.getHeight()));
				}
				else{
					tv.setX(x);
					tv.setY(y-estadoSize*4/5);
				}
			}
		}
		public void removeuTransicao(int inicio, int fim) {
			textos.get(inicio).get(fim).setText("");
			textos.get(inicio).get(fim).setVisibility(View.INVISIBLE);
			ativos.get(inicio).set(fim, false);
			flechas.get(inicio).get(fim).setVisibility(View.INVISIBLE);
		}
		public void removeTransicaoEspecifica(int origem, int destino, String entrada) {
			StringBuilder s = new StringBuilder("");
			StringTokenizer st = new StringTokenizer (textos.get(origem).get(destino).getText().toString(), ",");
			boolean primeira = true;
			while(st.hasMoreTokens()){
				String temp =st.nextToken();
				if(!entrada.equals(temp)){
					if(primeira){
						primeira=false;
						s.append(temp);
					}
					else{
						s.append(", ");
						s.append(temp);
					}
				}
			}
			if(s.length()==0) removeuTransicao(origem, destino);
			else textos.get(origem).get(destino).setText(s);
		}

		
	}
	protected boolean menuaberto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aflike);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		menuaberto=false;
		tentouMover=false;
		tentouCriar=false;
	}
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_aflike,
					container, false);
			return rootView;
		}
	}
	@SuppressWarnings("deprecation")
	public void cria(View v){
		vg=(ViewGroup) findViewById(R.id.aflikerelativeleiaute);
		Resources r = getResources();
		dimensaomm = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, dimensao, r.getDisplayMetrics());
		Estado.setaDimensao(dimensaomm, r, this);
		iniciaMenus();
		estados=new ArrayList<Estado>();
		invizibilizaTudo();
		planoDeFundo = newPlanodeFundo(vg.getWidth(), vg.getHeight());
		planoDeFundo.setaDimensao(dimensaomm);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    vg.setBackgroundDrawable(planoDeFundo);
		} else {
		    vg.setBackground(planoDeFundo);
		}
		vg.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	        	if(menuaberto) return false;
	            if (event.getAction() == MotionEvent.ACTION_DOWN){
	            	invizibilizaTudo();
	            	menuaberto=true;
	            	localizaESeta(menu4, event.getX(), event.getY());
	        		inicioMove = newEstado(event.getX(), event.getY(), vg);
	        		vg.addView(inicioMove);
	        		tentouCriar=true;
	                ((EditText)findViewById(R.id.aflikeentradanomeestadocimaesquerda)).setText("");
	        		((EditText)findViewById(R.id.aflikenomeestadobaixodireita)).setText("");
	        		((EditText)findViewById(R.id.aflikenomeestadocimadireita)).setText("");
	        		((EditText)findViewById(R.id.aflikenomeestadobaixoesquerda)).setText("");
	                return true;
	            }
	            else return false;
	        }
	    });
		vg.setOnDragListener(new OnDragListener(){

			@Override
			public boolean onDrag(View view, DragEvent dragEvent) {
		        if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
		        	menuaberto=true;
		        	localizaESeta(menu2, dragEvent.getX(), dragEvent.getY());
	                inicioMove.move(dragEvent.getX(), dragEvent.getY());
	                tentouMover=true;
		        }
		        return true;
			}
			
		});
		nomeEstados=new ArrayList<String>();
		inicioMove=null;
		fimMove=null;
		Flecha.setaTamanhoEstado(dimensaomm);
		Flecha.pegaRecurso(r);
		menu1.setVisibility(View.GONE);
	}


	protected abstract PlanodeFundo newPlanodeFundo(int width, int height);


	protected abstract Estado newEstado(float x, float y, ViewGroup vg);
	protected void localizaESeta(LinearLayout[] menu, float x, float y) {
		if(x>vg.getWidth()/2){
			if(y>vg.getHeight()/2){
				menu[2].setVisibility(View.VISIBLE);
				menu[2].setX(x-menu[2].getWidth());
				menu[2].setY(y-dimensaomm/2-menu[2].getHeight());
				menu[2].bringToFront();
			}
			else {
				menu[3].setVisibility(View.VISIBLE);
				menu[3].setX(x-menu[2].getWidth());
				menu[3].setY(y+dimensaomm/2);
				menu[3].bringToFront();
			}
		}
		else{
			if(y>vg.getHeight()/2){
				menu[1].setVisibility(View.VISIBLE);
				menu[1].setX(x);
				menu[1].setY(y-dimensaomm/2-menu[1].getHeight());
				menu[1].bringToFront();
			}
			else{
				menu[0].setVisibility(View.VISIBLE);
				menu[0].setX(x);
				menu[0].setY(y+dimensaomm/2);
				menu[0].bringToFront();
			}
		}
	}

	protected void invizibilizaTudo() {
		menu1.setVisibility(View.GONE);
		menu2[0].setVisibility(View.GONE);
		menu2[1].setVisibility(View.GONE);
		menu2[2].setVisibility(View.GONE);
		menu2[3].setVisibility(View.GONE);
		menu3[0].setVisibility(View.GONE);
		menu3[1].setVisibility(View.GONE);
		menu3[2].setVisibility(View.GONE);
		menu3[3].setVisibility(View.GONE);
		menu4[0].setVisibility(View.GONE);
		menu4[1].setVisibility(View.GONE);
		menu4[2].setVisibility(View.GONE);
		menu4[3].setVisibility(View.GONE);
		menu5[0].setVisibility(View.GONE);
		menu5[1].setVisibility(View.GONE);
		menu5[2].setVisibility(View.GONE);
		menu5[3].setVisibility(View.GONE);
		menu6.setVisibility(View.GONE);
		menuaberto=false;
		menu7[0].setVisibility(View.GONE);
		menu7[1].setVisibility(View.GONE);
		menu7[2].setVisibility(View.GONE);
		menu7[3].setVisibility(View.GONE);
	}
	protected void iniciaMenus() {
		menu1=(LinearLayout)findViewById (R.id.aflikeleiauteinicial);
		menu2 = new LinearLayout[4];
		menu2[0]=(LinearLayout)findViewById (R.id.aflikeleiaute2cimaesquerda);
		menu2[1]=(LinearLayout)findViewById (R.id.aflikeleiaute2baixoesquerda);
		menu2[2]=(LinearLayout)findViewById (R.id.aflikeleiaute2cimadireita);
		menu2[3]=(LinearLayout)findViewById (R.id.aflikeleiaute2baixodireita);
		menu3 = new LinearLayout[4];
		menu3[0]=(LinearLayout)findViewById (R.id.aflikeleiaute3cimaesquerda);
		menu3[1]=(LinearLayout)findViewById (R.id.aflikeleiaute3baixoesquerda);
		menu3[2]=(LinearLayout)findViewById (R.id.aflikeleiaute3cimadireita);
		menu3[3]=(LinearLayout)findViewById (R.id.aflikeleiaute3baixodireita);
		menu4 = new LinearLayout[4];
		menu4[0]=(LinearLayout)findViewById (R.id.aflikeleiaute4cimaesquerda);
		menu4[1]=(LinearLayout)findViewById (R.id.aflikeleiaute4baixoesquerda);
		menu4[2]=(LinearLayout)findViewById (R.id.aflikeleiaute4cimadireita);
		menu4[3]=(LinearLayout)findViewById (R.id.aflikeleiaute4baixodireita);
		menu5 = new LinearLayout[4];
		menu5[0]=(LinearLayout)findViewById (R.id.aflikeleiaute5cimaesquerda);
		menu5[1]=(LinearLayout)findViewById (R.id.aflikeleiaute5baixoesquerda);
		menu5[2]=(LinearLayout)findViewById (R.id.aflikeleiaute5cimadireita);
		menu5[3]=(LinearLayout)findViewById (R.id.aflikeleiaute5baixodireita);
		menu6=(LinearLayout)findViewById (R.id.aflikeleiaute6);
		menu7 = new LinearLayout[4];
		menu7[0]=(LinearLayout)findViewById (R.id.aflikeleiaute7cimaesquerda);
		menu7[1]=(LinearLayout)findViewById (R.id.aflikeleiaute7baixoesquerda);
		menu7[2]=(LinearLayout)findViewById (R.id.aflikeleiaute7cimadireita);
		menu7[3]=(LinearLayout)findViewById (R.id.aflikeleiaute7baixodireita);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aflike, menu);
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

	public void initEmul(View v){
		if(!menuaberto){
			findViewById(R.id.aflikeiniciaemul).setVisibility(View.GONE);
			menuaberto=true;
			menu6.setVisibility(View.VISIBLE);
        	menu6.bringToFront();
			((EditText)findViewById(R.id.aflikeentradasimula)).setText("");
			preparaEmulacao();
		}
	}
	protected abstract void preparaEmulacao();
	public void cancel(View v){
		invizibilizaTudo();
		if(tentouMover){
			tentouMover=false;
			inicioMove.retorna();
		}
		if(tentouCriar){
			tentouCriar=false;
			inicioMove.setaExiste(false, vg);
			vg.removeView(inicioMove);
		}
	}
	@SuppressWarnings("deprecation")
	public void moveEstado(View v){
		invizibilizaTudo();
		menuaberto=false;
		tentouMover=false;
		planoDeFundo.moveuEstado(estados.indexOf(inicioMove), inicioMove.getX(), inicioMove.getY());
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    vg.setBackgroundDrawable(planoDeFundo);
		} else {
		    vg.setBackground(planoDeFundo);
		}
		vg.invalidate();
	}
	public void criaTrans(View v){
		invizibilizaTudo();
		menuaberto=true;
		localizaESeta(menu5, fimMove.getX()+dimensaomm/2, fimMove.getY()+dimensaomm/2);
		((EditText)findViewById(R.id.aflikeentradatransicoescimaesquerda)).setText(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)));
		((EditText)findViewById(R.id.aflikeentradatransicoesbaixoesquerda)).setText(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)));
		((EditText)findViewById(R.id.aflikeentradatransicoescimadireita)).setText(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)));
		((EditText)findViewById(R.id.aflikeentradatransicoesbaixodireita)).setText(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)));
	}
	@SuppressWarnings("deprecation")
	public void estadoDeleta(View v){
		menuaberto=false;
		invizibilizaTudo();
		inicioMove.setaExiste(false, vg);
		vg.removeView(inicioMove);
		planoDeFundo.removeuEstado(estados.indexOf(inicioMove), vg);
		estados.remove(inicioMove);
		inicioMove=null;
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    vg.setBackgroundDrawable(planoDeFundo);
		} else {
		    vg.setBackground(planoDeFundo);
		}
		vg.invalidate();
	}
	public void inicialToggle(View v){
		menuaberto=false;
		invizibilizaTudo();
		toggleInicial(inicioMove);
	}
	protected abstract void toggleInicial(Estado e);
	public void finalToggla(View v){
		menuaberto=false;
		invizibilizaTudo();
		toggleFinal(inicioMove);
	}
	protected abstract void toggleFinal(Estado inicioMove2);
	public void criaEstado(View v){
		String s = ((EditText) findViewById(R.id.aflikeentradanomeestadocimaesquerda)).getText().toString();
		if(s.equals("")) s = ((EditText) findViewById(R.id.aflikenomeestadobaixoesquerda)).getText().toString();
		if(s.equals("")) s = ((EditText) findViewById(R.id.aflikenomeestadocimadireita)).getText().toString();
		if(s.equals("")) s = ((EditText) findViewById(R.id.aflikenomeestadobaixodireita)).getText().toString();
		if(s==null||s.equals("")||nomeEstados.contains(s)){
			invizibilizaTudo();
			menuaberto=false;
			inicioMove.setaExiste(false, vg);
			vg.removeView(inicioMove);
			return;
		}
		planoDeFundo.adicionouEstado(inicioMove.getX(), inicioMove.getY(), this, vg);
		inicioMove.setaNome(s);
		setaEscutador(inicioMove);
		estados.add(inicioMove);
		nomeEstados.add(s);
		geraEstado(s);
		invizibilizaTudo();
		menuaberto=false;
		inicioMove.setVisibility(View.VISIBLE);
		vg.invalidate();
		tentouCriar=false;
	}
	protected void setaEscutador(Estado e) {
		e.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
		    public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
		        	if(menuaberto) return false;
					ClipData clipData = ClipData.newPlainText("", "");
					inicioMove=(Estado) view;
					View.DragShadowBuilder dsb = new View.DragShadowBuilder(view);
					view.startDrag(clipData, dsb, view, 0);
					return true;
				}else return false;
			}
		});
		e.setOnDragListener(new OnDragListener(){
			@Override
			public boolean onDrag(View view, DragEvent dragEvent) {
		        if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
		        	fimMove=(Estado)view;
		        	if(menuaberto) return false;
		        	if(fimMove.equals(inicioMove)) localizaESeta(menu3, fimMove.getX()+dimensaomm/2, fimMove.getY()+dimensaomm/2);
		        	else localizaESeta(menu7, fimMove.getX()+dimensaomm/2, fimMove.getY()+dimensaomm/2);
		        	menuaberto=true;
		        }
		        return true;
			}
			
		});
		// TODO Auto-generated method stub
		
	}
	public void criaTransicoes(View v){
		menuaberto=false;
		invizibilizaTudo();
		String s=null;
		String s1 = ((EditText)findViewById(R.id.aflikeentradatransicoescimaesquerda)).getText().toString();
		String s2 = ((EditText)findViewById(R.id.aflikeentradatransicoesbaixoesquerda)).getText().toString();
		String s3 = ((EditText)findViewById(R.id.aflikeentradatransicoesbaixodireita)).getText().toString();
		String s4 = ((EditText)findViewById(R.id.aflikeentradatransicoescimadireita)).getText().toString();
		if(s1.length()==0&&s2.length()==0&&s3.length()==0&&s4.length()==0)editaTransicoes("");
		if(!s1.equals(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)))) s=s1;
		if(!s2.equals(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)))) s=s2;
		if(!s3.equals(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)))) s=s3;
		if(!s4.equals(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)))) s=s4;
		if(s==null) return;
		else editaTransicoes(s);
	}
	protected abstract void editaTransicoes(String s);
	protected abstract void geraEstado(String string);
	@SuppressWarnings("deprecation")
	public void removeTransicao(View v){
		menuaberto=false;
		invizibilizaTudo();
		removeTransicaoDeFato(planoDeFundo.pegaTexto(estados.indexOf(inicioMove), estados.indexOf(fimMove)));
		planoDeFundo.removeuTransicao(estados.indexOf(inicioMove), estados.indexOf(fimMove));
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    vg.setBackgroundDrawable(planoDeFundo);
		} else {
		    vg.setBackground(planoDeFundo);
		}
		vg.invalidate();
	}
	public void simula(View v){
		String s = ((EditText)findViewById(R.id.aflikeentradasimula)).getText().toString();
		simula(s);
	}
	public void paraSimul(View v){
		findViewById(R.id.aflikeiniciaemul).setVisibility(View.VISIBLE);
		menuaberto=false;
		menu6.setVisibility(View.GONE);
		paraSimul();
	}
	protected void setaAtivos(ArrayList<Integer> lista){
		for(int i = 0; i < lista.size(); i++){
			estados.get(lista.get(i)).setaAtivo();
		}
	}
	protected void setaTodosEstadosInativos(){
		for (int i = 0; i < estados.size();i++){
			estados.get(i).setaIAtivo();
		}
	}
	protected abstract void paraSimul();
	protected abstract void simula(String s);
	protected abstract void removeTransicaoDeFato(String s);
}
