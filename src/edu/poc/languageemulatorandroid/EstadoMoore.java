package edu.poc.languageemulatorandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class EstadoMoore extends Estado {
	TextView saida;
	Paint p;
	public EstadoMoore(Context context){
		super(context);
	}
	public EstadoMoore(float x, float y, ViewGroup vg){
		super(x,y,vg);
		saida=new TextView(context);
		vg.addView(saida);
		saida.setText("");
		saida.setTextColor(Color.WHITE);
		saida.setTextSize(dimensao*3/8);
		saida.setTypeface(null, Typeface.BOLD);
		saida.setX(x-dimensao/2);
		saida.setY(y);
//		tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
		saida.setVisibility(VISIBLE);
		p=new Paint();
		p.setColor(Color.WHITE);
	}
	@Override
	public void setaExiste(Boolean b, ViewGroup vg){
		vg.removeView(saida);
		if(b) vg.addView(saida);
		super.setaExiste(b, vg);
	}
	@Override
	public void move(float x, float y){
		super.move(x, y);
		saida.setX(x-dimensao/2);
		saida.setY(y);
	}
	public String pegaSaida(){
		return saida.getText().toString();
	}
	@Override
	public void toggleFinal(){}
	@Override
	public void retorna(){
		super.retorna();
		saida.setX(ultimoX);
		saida.setY(ultimoY+dimensao/2);
	}
	public void setaSaida(String s){
		saida.setText(s);
		tv.setLayoutParams(new LayoutParams(dimensao, dimensao/2));
		saida.setLayoutParams(new LayoutParams(dimensao, dimensao/2));
		saida.setGravity(Gravity.CENTER);
		saida.bringToFront();
	}
}
