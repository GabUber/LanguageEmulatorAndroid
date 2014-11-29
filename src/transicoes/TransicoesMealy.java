package transicoes;

import java.util.StringTokenizer;

import android.content.res.Resources;

public class TransicoesMealy extends Transicoes{
	public TransicoesMealy(int maxWidth, int maxHeight, Resources r) {
		super(maxWidth, maxHeight, r);
	}
	@Override
	public void removeTransicaoEspecifica(int origem, int destino, String se){
		StringBuilder s = new StringBuilder("");
		String entrada = new StringTokenizer(se,"/").nextToken();
		StringTokenizer st = new StringTokenizer (textos.get(origem).get(destino).getText().toString(), ",");
		boolean primeira = true;
		while(st.hasMoreTokens()){
			String temp=st.nextToken(); 
			String t =new StringTokenizer (temp, "/").nextToken();
			if(!entrada.equals(t)){
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