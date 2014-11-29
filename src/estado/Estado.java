package estado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import edu.poc.languageemulatorandroid.R;

public class Estado extends View{
	static Drawable eInativo;
	static Drawable eAtivo;
	static Drawable eInicialAtivo;
	static Drawable eInicialInativo;
	static Drawable eFinalAtivo;
	static Drawable eFinalInativo;
	static Drawable eFinalInicialInativo;
	static Drawable eFinalInicialAtivo;
	static Context context;
	static int dimensao;
	private boolean ehInicial, ehFinal, ativo;
	boolean existe;
	float ultimoX, ultimoY;
	TextView tv;
	public Estado(Context context){
		super(context);
	}
	public Estado(float x, float y, RelativeLayout vg){
		super(context);
		setLayoutParams(new LayoutParams(dimensao, dimensao));
		setX(x-dimensao/2);
		setY(y-dimensao/2);
		this.setMinimumHeight((int)(dimensao));
		this.setMinimumWidth(dimensao);
		tv = new TextView(context);
		vg.addView(tv);
		tv.setText("");
		tv.setTextColor(Color.WHITE);
		tv.setMinimumHeight(dimensao);
		tv.setMinimumWidth(dimensao);
		//tv.setTextSize(dimensao*3/8);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setX(x-dimensao/2);
		tv.setY(y-dimensao/2);
//		tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
		tv.setVisibility(VISIBLE);
		ehInicial=false;
		ehFinal=false;
		ativo=false;
		existe=true;
		atualizaEstado();
	}
	public void setaExiste(Boolean b, RelativeLayout vg){
		vg.removeView(tv);
		if(b){
			vg.addView(tv);
		}
		existe=b;
		invalidate();
	}
	public void move(float x, float y){
		ultimoX=getX();
		ultimoY=getY();
		setX(x-dimensao/2);
		setY(y-dimensao/2);
		tv.setX(x-dimensao/2);
		tv.setY(y-dimensao/2);
	}
	public String pegaNome(){
		return tv.getText().toString();
	}
	public void setaAtivo(){
		ativo=true;
		atualizaEstado();
	}
	public void setaIAtivo(){
		ativo=false;
		atualizaEstado();
	}
	public void toggleInicial(){
		ehInicial=!ehInicial;
		atualizaEstado();
	}
	public void toggleFinal(){
		ehFinal=!ehFinal;
		atualizaEstado();
	}
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void atualizaEstado() {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(existe){
			if (ativo){
				if(ehInicial){
					setLayoutParams(new LayoutParams((int) (dimensao*1.54), dimensao));
					if(ehFinal){
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eFinalInicialAtivo);
						} else {
						    setBackground(eFinalInicialAtivo);
						}
					}
					else{
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eInicialAtivo);
						} else {
						    setBackground(eInicialAtivo);
						}
					}
				}
				else{
					setLayoutParams(new LayoutParams(dimensao, dimensao));
					if(ehFinal){
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eFinalAtivo);
						} else {
						    setBackground(eFinalAtivo);
						}
					}
					else{
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eAtivo);
						} else {
						    setBackground(eAtivo);
						}
					}
				}
			}
			else{
				
				if(ehInicial){
					setLayoutParams(new LayoutParams((int) (dimensao*1.54), dimensao));
					if(ehFinal){
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eFinalInicialInativo);
						} else {
						    setBackground(eFinalInicialInativo);
						}
					}
					else{
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eInicialInativo);
						} else {
						    setBackground(eInicialInativo);
						}
					}
				}
				else{
					setLayoutParams(new LayoutParams(dimensao, dimensao));
					if(ehFinal){
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eFinalInativo);
						} else {
						    setBackground(eFinalInativo);
						}
					}
					else{
						if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						    setBackgroundDrawable(eInativo);
						} else {
						    setBackground(eInativo);
						}
					}
				}
			}
		}
	}
	public boolean ehInicial(){
		return ehInicial;
	}
	public boolean ehFinal(){
		return ehFinal;
	}		
	public static void setaDimensao(int px, Resources r, Context c) {
		dimensao=px;
		context=c;
		eInativo=r.getDrawable(R.drawable.estadonaoativo);
		eAtivo=r.getDrawable(R.drawable.estadoativo);
		eInicialAtivo=r.getDrawable(R.drawable.estadoinicialativo);
		eInicialInativo=r.getDrawable(R.drawable.estadoinicialnaoativo);
		eFinalAtivo=r.getDrawable(R.drawable.estadofinalativo);
		eFinalInativo=r.getDrawable(R.drawable.estadofinalnaoativo);
		eFinalInicialInativo=r.getDrawable(R.drawable.estadoinicialfinalnaoativo);
		eFinalInicialAtivo=r.getDrawable(R.drawable.estadoinicialfinalativo);
	}
	public void retorna() {
		setX(ultimoX);
		setY(ultimoY);
		tv.setX(ultimoX);
		tv.setY(ultimoY);
	}
	public void setaNome(String s) {
		tv.setText(s);
		tv.setLayoutParams(new LayoutParams(dimensao, dimensao));
		tv.setGravity(Gravity.CENTER);
		tv.bringToFront();
	}
}
