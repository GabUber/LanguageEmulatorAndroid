package lingua;

import java.util.ArrayList;

import android.content.res.Resources;

public abstract class Lingua {
	public String app_name ;
	public String af;
	public String afn;
	public String afnl;
	public String ER;
	public String gr;
	public String mmoore;
	public String mmealy;
	public String emulInit;
	public String criar_estado;
	public String cancel;
	public String setInit;
	public String unsetInit;
	public String setfim;
	public String unsetfim;
	public String statedelete;
	public String newTrans;
	public String move;
	public String expressao;
	public String ok;
	public String padrao;
	public String esquerda;
	public String direita;
	public String novaRegra;
	public String removerRegra;
	public String avisoGR;
	public String novaRegraDireita;
	public String aceito;
	public String recusado;
	public String estadonome;
	public String transtext;
	public String removeTrans;
	public String emulfim;
	public String entrada;
	public String start;
	public String erexplicacao;
	public String grexplicacao;
	public String aflikeexplicacao;
	public String moveRegra;
	public String Gerar;
	public String AFNL;
	public String Simplificar;
	public String resetar;
	public String passo;
	public String AF;
	public String GR;
	public String er;
	public String MEALY;
	public String MOORE;
	public String AFD;
	public String AFN;
	public String GRE;
	public String GRD;
	public String Arquivo;
	public String Salvar;
	public String Salvarcomo;
	public String Abrir;
	public String SalvaEnvia;
	public String Lingua;
	public String deleta;
	public String NomeArquivo;
	public String sobreescrever;
	public String textoEscolhedor;
	private static final int numL = 2;
	public static final int portugues = 0;
	public static final int english = 1;
	public static ArrayList<Lingua> criaLinguas(Resources r) {
		ArrayList<Lingua> l = new ArrayList<Lingua>(numL);
		l.add(new Portugues(r));
		l.add(new English(r));
		return l;
	}
	public static int pegaL(String s) {
		if(s==null) return 0;
		if(s.equals("English")) return 1;
		return 0;
	}
}
