package edu.poc.languageemulatorandroid;

import java.util.StringTokenizer;

import af.AFNL;
import estado.Estado;

public class AFNLActivity extends AFActivity {
	@Override
	public void criaEspecifico(){
		af=new AFNL();
		super.criaEspecifico();
	}
	@Override
	protected void toggleInicial(Estado e) {
		e.toggleInicial();
		af.togglaInicial(e.pegaNome());
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void editaTransicoes(String s) {
		if(s.equals("") || s.length()==0) s = "λ";
		String origemNome = inicioMove.pegaNome(), destinoNome=fimMove.pegaNome();
		int origem = estados.indexOf(inicioMove), destino = estados.indexOf(fimMove);
		String atual = planoDeFundo.pegaTexto(origem, destino);
		if(atual.equals(s)) return;
		StringTokenizer st = new StringTokenizer(atual, ",");
		boolean lambda = false;
		while(st.hasMoreTokens()){
			String stnextToken = st.nextToken();
			if(stnextToken.equals("λ") && !lambda){
				lambda=true;
			}
			af.removeTransicao(origemNome, destinoNome, stnextToken);
		}
		planoDeFundo.removeuTransicao(estados.indexOf(inicioMove), estados.indexOf(fimMove));
		st = new StringTokenizer(s, ",");
		StringBuilder sb = new StringBuilder("");
		boolean primeiro = true;
		lambda=false;
		if(st.hasMoreTokens()){
			while(st.hasMoreTokens()){
				String tempString = st.nextToken();
				if(tempString.equals("λ") && !lambda){
					af.adicionaTransicao(origemNome, destinoNome, null);
					lambda = true;
				}
				else af.adicionaTransicao(origemNome, destinoNome, tempString);
				if((!primeiro)&&tempString.length()!=0) sb.append(",");
				sb.append(tempString);
				primeiro=false;
			}
		}
		if(s.charAt(s.length()-1)==','){
			sb.append(",λ");
			af.adicionaTransicao(origemNome, destinoNome, null);
		}
		planoDeFundo.novaTransicao(origem, destino, sb.toString());
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    vg.setBackgroundDrawable(planoDeFundo);
		} else {
		    vg.setBackground(planoDeFundo);
		}
		vg.invalidate();
	}
	@Override
	protected int trataIniciais(String s) throws AFLikeMalformedFileException {
		return Integer.parseInt(s);
	}
	@Override
	protected String pegaTipo() {
		return "AFNL";
	}
	@Override
	protected void apagaEspecificos(){
		super.apagaEspecificos();
		apagaFilhos(true, findViewById(R.id.aflikegeraafnl));
		apagaFilhos(true, findViewById(R.id.aflikesimplifica));
	}
	@Override
	protected Boolean temLambda() {
		return true;
	}
	@Override
	public Salvavel pegaContexto() {
		return AFNLActivity.this;
	}
	@Override
	protected String pegaNomeAtividade() {
		return lingua.afnl;
	}
}