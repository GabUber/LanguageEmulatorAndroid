package gerenciaArquivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import lingua.Lingua;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import edu.poc.languageemulatorandroid.R;
import edu.poc.languageemulatorandroid.Salvavel;

public abstract class GerenciaArquivo {
	static private final String terminacao = ".langemul";
	static private File dirInterno, dirExterno;
	static private Resources r;
	static public String pegaLingua(Context c){
		try {
			FileReader a;
			a = new FileReader(dirInterno.getAbsolutePath()+"/lingua");
			String s = leArquivo(a);
			a.close();
			return s;
		} catch (FileNotFoundException e) {
			salvaLingua("Português", c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Português";
	}

	private static String leArquivo(FileReader a) throws IOException {
		StringBuilder sb = new StringBuilder(512);
		int c = a.read();
		while(c!=-1){
			sb.append((char)c);
			c=a.read();
		}
		return sb.toString();
	}

	public static void deleta(String nome) {//TODO fix
		File f = new File(dirExterno.getAbsolutePath(), nome+terminacao);
		f.delete();
	}

	public static void salva(String arquivo, String nome, boolean aenviar,
			Context c, Lingua l) {
		String s = dirExterno.getAbsolutePath()+"/"+nome+terminacao;
		PrintWriter w;
		try {
			w = new PrintWriter(s, "UTF-8");
			w.print(arquivo);
			w.close();
			if(aenviar) envia(nome, c, l);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void envia(String nome, Context c, Lingua l) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent .setType("message/rfc822");
	    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+dirExterno.getAbsolutePath()+"/"+nome+terminacao));
		((Activity)c).startActivity(Intent.createChooser(intent, l.textoEscolhedor));
		
	}

	public static boolean contem(String nome) {
		String s1 = nome+terminacao;
		String nomes[] = dirExterno.list(new FiltroLangEmul());
		for (String s2 : nomes) if(s2.equals(s1))return true;
		return false;
	}

	public static String abre(String nomeArquivo, Context c) {
		try {
			FileReader a;
			a = new FileReader(dirExterno.getAbsolutePath()+"/"+nomeArquivo+terminacao);
			String s = leArquivo(a);
			a.close();
			return s;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void salvaLingua(String lingua, Context c) {
		PrintWriter s;
		try {
			s = new PrintWriter(c.openFileOutput("lingua", Context.MODE_PRIVATE));
			s.print(lingua);
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void setaNomes(LinearLayout ll, Context c, int h) {
		ll.removeAllViews();
		String nomes[]=dirExterno.list(new FiltroLangEmul());
		int max = nomes.length;
		ArrayList<String> nomesAL = new ArrayList<String>(max);
		for (int i = 0; i < max; i++) nomesAL.add(nomes[i]);
		Collections.sort(nomesAL);
		int sdk = android.os.Build.VERSION.SDK_INT;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, h);
		for(String s : nomesAL){
			Button b = new Button(c);
			ll.addView(b);
			b.setLayoutParams(params);
			b.setText(s.substring(0, s.length()-9));
			setaFundo(b, sdk);
			setaEscutador(b, (Salvavel)c);
		}
	}

	private static void setaEscutador(final Button b, final Salvavel c) {
		b.setOnTouchListener(new View.OnTouchListener() {
			@Override
		    public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
		        	c.seleciona(view);
					return true;
				}else return false;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private static void setaFundo(Button b, int sdk) {
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    b.setBackgroundDrawable(r.getDrawable(R.drawable.fundo));
		} else {
		    b.setBackground(r.getDrawable(R.drawable.fundo));
		}
	}

	public static void setaDiretorio(Context c, Resources r) {
		dirInterno = c.getFilesDir();
		dirExterno = c.getExternalFilesDir(null);
		GerenciaArquivo.r=r;
	}
}
