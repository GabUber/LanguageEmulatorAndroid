package transicoes;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import edu.poc.languageemulatorandroid.R;

public class Transicoes extends Drawable{
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
	public Transicoes(int maxWidth, int maxHeight, Resources r){
		this.setBounds(0, 0, maxWidth-1, maxHeight-1);
		Flecha.setaCorrecao(cons);
		flechas=new ArrayList<ArrayList<Flecha>>();
		textos =new ArrayList<ArrayList<TextView>>();
		ativos=new ArrayList<ArrayList<Boolean>>();
		x=new ArrayList<Float>();
		y=new ArrayList<Float>();
		fundo=r.getDrawable(R.drawable.fundo);
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
	public void setAlpha(int alpha) {}

	@Override
	public void setColorFilter(ColorFilter cf) {}
	@Override
	public int getOpacity() {
		return 0;
	}
	public String pegaTexto(int origem, int destino){
		return textos.get(origem).get(destino).getText().toString();
	}
	public void novaTransicao(int origem, int destino, String s){
		if( s==null || s.length()==0) removeuTransicao(origem, destino);
		else{
			TextView tv = textos.get(origem).get(destino);
			tv.setText(s);
			if(origem!=destino)tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tv.setVisibility(View.VISIBLE);
			ativos.get(origem).set(destino, true);
			}
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
	public void adicionouEstado(float x, float y, Context context, RelativeLayout vg) {
		this.x.add(x);
		this.y.add(y);
		Flecha F;
		for(int i = 0; i < flechas.size(); i++){
			float[]var=Flecha.calculaCorrecao(this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, x+estadoSize/2, y+estadoSize/2);
			F=new Flecha(this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, x+estadoSize/2, y+estadoSize/2, context, false);
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
			F=new Flecha(x+estadoSize/2, y+estadoSize/2, this.x.get(i)+estadoSize/2, this.y.get(i)+estadoSize/2, context, i==n);
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