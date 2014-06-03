package edu.poc.languageemulatorandroid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

public class Flecha extends View{
	private boolean autoFlecha;
	private float linhaComecoX, linhaComecoY, linhaFimX, linhaFimY;
	private static int correcao, tamanhoEstado, tamanhoTriangulo;
	private static Resources r;
	private static BitmapDrawable autoFlechaBase;
	private static BitmapDrawable triangulo;
	float angulo;
	@SuppressWarnings("deprecation")
	public Flecha(float inicioX, float inicioY, float fimX, float fimY, Context context){
		super(context);
		if((inicioX==fimX)&&(inicioY==fimY)){
			setLayoutParams(new LayoutParams(tamanhoEstado, tamanhoEstado*3/5));
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				setBackgroundDrawable(autoFlechaBase);
			}else{
				setBackground(autoFlechaBase);
			}
			autoFlecha=true;
		}
		else{
			setLayoutParams(new LayoutParams(tamanhoTriangulo, tamanhoTriangulo));
			autoFlecha=false;

		}
		moveFlecha(inicioX, inicioY, fimX, fimY);
		setVisibility(View.INVISIBLE);
	}
	@SuppressWarnings("deprecation")
	void desenha(Canvas canvas, Paint p){
		if(!autoFlecha){
			float[] var = calculaCorrecao(linhaComecoX, linhaComecoY,linhaFimX,linhaFimY);
			canvas.drawLine(linhaComecoX+var[0], linhaComecoY+var[1], linhaFimX+var[0], linhaFimY+var[1], p);
			float[] cor = calculaPosicaoSeta();
			if(linhaComecoX>linhaComecoY)setX(linhaFimX+var[0]+cor[0]);
			else setX(linhaFimX+var[0]+cor[0]+getWidth());
			if(linhaComecoY>linhaComecoY)setY(linhaFimY+var[1]-getHeight()/2+cor[1]);
			else setY(linhaFimY+var[1]-getHeight()/2+cor[1]);
			Matrix matrix = new Matrix();
			matrix.postRotate(angulo);
		    Bitmap b =triangulo.getBitmap();
			Bitmap bMapRotate = Bitmap.createBitmap(b, 0, 0, triangulo.getIntrinsicWidth(), triangulo.getIntrinsicHeight(), matrix, true);
			BitmapDrawable n = new BitmapDrawable(r,bMapRotate);
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				setBackgroundDrawable(null);
			}else{
				setBackground(n);
			}
		}
		desenhaFlecha(linhaComecoX, linhaComecoY);
	}
	private float[] calculaPosicaoSeta(){
		float[] resp = new float[2];
		double l = Math.sqrt((linhaFimX-linhaComecoX)*(linhaFimX-linhaComecoX)+(linhaFimY-linhaComecoY)*(linhaFimY-linhaComecoY));
		resp[0]=(float) ((tamanhoEstado/2)*(linhaComecoX-linhaFimX)/l);
		resp[1]=(float) ((tamanhoEstado/2)*(linhaComecoY-linhaFimY)/l);
		return resp;
	}
	public static float[] calculaCorrecao(float linhaComecoX, float linhaComecoY,	float linhaFimX, float linhaFimY) {
		float[] var = new float[2];
		double l = Math.sqrt(  ( linhaFimX - linhaComecoX ) * ( linhaFimX - linhaComecoX ) + ( linhaFimY - linhaComecoY )  *  ( linhaFimY - linhaComecoY ) );
		var[0]=(float) (((linhaFimY - linhaComecoY)/l)*correcao);
		var[1]=(float) (((linhaFimX - linhaComecoX)/l)*correcao);
		return var;
	}
	public static void setaCorrecao(int correcao){
		Flecha.correcao=correcao;
	}
	public static void setaTamanhoEstado(int tamanhoEstado){
		Flecha.tamanhoEstado=tamanhoEstado;
		tamanhoTriangulo=tamanhoEstado/5;
	}
	private void desenhaFlecha(float x, float y) {
		setVisibility(View.VISIBLE);
	}
	public static void pegaRecurso(Resources r){
		autoFlechaBase = (BitmapDrawable) r.getDrawable(R.drawable.setaretorno);
		triangulo=(BitmapDrawable) r.getDrawable(R.drawable.seta);
		Flecha.r=r;
	}
	public void moveFlecha(float inicioX, float inicioY, float fimX, float fimY) {
		// TODO Auto-generated method stub
		if(autoFlecha){
			setX(fimX-tamanhoEstado/2);
			setY(fimY-tamanhoEstado*4/5);
		}
		else{
			atualizaTriangulo(inicioX, inicioY, fimX, fimY);
		}
		linhaComecoX=inicioX;
		linhaComecoY=inicioY;
		linhaFimX=fimX;
		linhaFimY=fimY;
		
		//TODO calcular correção tamanho estado
	}
	private void atualizaTriangulo(float inicioX, float inicioY, float fimX, float fimY) {
		// TODO Auto-generated method stub
		if(inicioX==fimX) angulo = (fimY>inicioY)?90:270;
		else {
			angulo=(float) Math.toDegrees(Math.atan((double)(fimY-inicioY)/(double)(fimX-inicioX)));
			if(fimX<inicioX) angulo+=180;
		}
		
	}
}
