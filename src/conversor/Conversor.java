package conversor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import mmACHINE.MMachine;
import transicoes.Transicoes;
import af.AF;
import estado.Estado;
import estado.EstadoMoore;

public abstract class Conversor {
	public abstract String toString(Transicoes pf, ArrayList<Estado> estados, String tipo, int w, int h);
	public ArrayList<Integer> simplifica(Transicoes pv, ArrayList<Estado> estados, AF af, MMachine m, ArrayList<String> estadosNomes){
		int max = estados.size();
		ArrayList<Boolean> iniciais = new ArrayList<Boolean>();
		ArrayList<Boolean> finais = new ArrayList<Boolean>();
		achaIniciaisEFinais(estados, iniciais, finais);
		isolaEquivalentes(pv, iniciais, finais, max, estados, af, m, estadosNomes);
		return removeInatingiveis(pv, iniciais, max);
	}
	private static void achaIniciaisEFinais(ArrayList<Estado> estados, ArrayList<Boolean> iniciais, ArrayList<Boolean> finais) {
		int max = estados.size();
		for(int i = 0; i < max; i++){
			Estado e = estados.get(i);
			iniciais.add(e.ehInicial());
			finais.add(e.ehFinal());
		}
	}
	private static void isolaEquivalentes(Transicoes tabela, ArrayList<Boolean> inicial, ArrayList<Boolean> finais, int max, ArrayList<Estado> estados, AF af, MMachine m, ArrayList<String> estadosNomes) {
		for (int  i = 0; i < max; i++) for (int j = i+1; j < max; j++) if (equivalentes(tabela, i,j, max, finais, estados.get(i), estados.get(j))) isola(i,j,inicial,tabela, max, af, m, estadosNomes);
	}
	private static boolean equivalentes(Transicoes tabela, int i, int j, int max, ArrayList<Boolean> finais, Estado ei, Estado ej) {
		if(ei.getClass()==EstadoMoore.class){
			if( !((EstadoMoore)(ei)).pegaSaida().equals(((EstadoMoore)(ej)).pegaSaida()))return false;
		}
		boolean fi = finais.get(i), fj = finais.get(j);
		
		if( ( fi && ! fj ) || ( ! fi && fj) ) return false;
		boolean resposta = equivalente(tabela.pegaTexto(i, i), tabela.pegaTexto(i, j), tabela.pegaTexto(j, i), tabela.pegaTexto(j, j));
		
		if(resposta)for (int k = 0; k < max; k++) if( k != i && k != j && !equivalente(tabela.pegaTexto(i, k), tabela.pegaTexto(j, k))){
			resposta = false;
			break;
		}
		return resposta;
	}
	private static boolean equivalente(String si1, String si2,
			String sj1, String sj2) {
		StringTokenizer sti1 = new StringTokenizer(si1, ",");
		StringTokenizer sti2 = new StringTokenizer(si2, ",");
		StringTokenizer stj1 = new StringTokenizer(sj1, ",");
		StringTokenizer stj2 = new StringTokenizer(sj2, ",");
		ArrayList<String> i = new ArrayList<String>();
		while(sti1.hasMoreTokens()) i.add(sti1.nextToken());
		while(sti2.hasMoreTokens()){
			String s = sti2.nextToken();
			if(!i.contains(s))i.add(s);
		}
		ArrayList<String> j = new ArrayList<String>();
		while(stj1.hasMoreTokens()) j.add(stj1.nextToken());
		while(stj2.hasMoreTokens()){
			String s = stj2.nextToken();
			if(!j.contains(s))j.add(s);
		}
		int max = i.size();
		if(max!=j.size()) return false;
		Collections.sort(i);
		Collections.sort(j);
		for(int k = 0; k < max; k++) if(!i.get(k).equals(j.get(k))) return false;
		return true;
	}
	private static boolean equivalente(String si, String sj) {
		StringTokenizer sti = new StringTokenizer(si, ",");
		int max = sti.countTokens();
		StringTokenizer stj = new StringTokenizer(sj, ",");
		if(stj.countTokens()!=max) return false;
		ArrayList<String> i = new ArrayList<String>();
		ArrayList<String> j = new ArrayList<String>();
		while(sti.hasMoreTokens()){
			i.add(sti.nextToken());
			j.add(stj.nextToken());
		}
		Collections.sort(i);
		Collections.sort(j);
		for(int k = 0; k < max; k++) if(!i.get(k).equals(j.get(k))) return false;
		return true;
	}
	private static void isola(int i, int j, ArrayList<Boolean> inicial, Transicoes tabela, int max, AF af, MMachine m, ArrayList<String> estadoNomes) {
		int isolar = inicial.get(i) ? j : i;
		inicial.set(isolar, false);
		int equivalente = i==isolar ? j : i;
		for(int k = 0; k < max; k++){
			String s = mescla(tabela.pegaTexto(k, isolar), tabela.pegaTexto(k, equivalente));
			tabela.novaTransicao(k, equivalente, s);
			tabela.novaTransicao(k, isolar, "");
			if(af!=null) isola(af, k, equivalente, s, estadoNomes);
			if(m!=null) isola (m, k, equivalente,s, estadoNomes);
		}
	}
	private static void isola(MMachine m, int origem, int destino, String s,
			ArrayList<String> estadoNomes) {
		StringTokenizer st = new StringTokenizer(s, ",");
		String origemNome = estadoNomes.get(origem);
		String destinoNome = estadoNomes.get(destino);
		while(st.hasMoreTokens()){
			String s1 = st.nextToken(), s2=null;
			StringTokenizer st2 = new StringTokenizer(s1, "/");
			s1 = st2.nextToken();
			if(st2.hasMoreTokens()) s2=st2.nextToken();
			m.adicionaTransicao(origemNome, destinoNome, s1, s2);
		}
		
	}
	private static void isola(AF af, int origem, int destino, String s, ArrayList<String> estadoNomes) {
		StringTokenizer st = new StringTokenizer(s, ",");
		String origemNome = estadoNomes.get(origem);
		String destinoNome = estadoNomes.get(destino);
		while(st.hasMoreTokens()) af.adicionaTransicao(origemNome, destinoNome, st.nextToken());
	}
	private static String mescla(String s1, String s2) {
		StringTokenizer st = new StringTokenizer(s1, ",");
		ArrayList<String> al = new ArrayList<String>();
		while(st.hasMoreTokens()) al.add(st.nextToken());
		st = new StringTokenizer(s2, ",");
		while(st.hasMoreTokens()){
			String s = st.nextToken();
			if(!al.contains(s)) al.add(s);
		}
		int max = al.size();
		if(max==0) return "";
		StringBuilder sb = new StringBuilder(al.get(0));
		for(int i = 1; i < max; i++) sb.append(',').append(al.get(i));
		return sb.toString();
	}
	private static ArrayList<Integer> removeInatingiveis( Transicoes tabela, ArrayList<Boolean> inicial, int max) {
		ArrayList<Boolean> isolado = new ArrayList<Boolean>(max);
		for (int i = 0; i < max; i++) isolado.add(true);
		ArrayList<Integer> fila = new ArrayList<Integer>();
		for(int i = 0; i < max; i++) if(inicial.get(i)){
			fila.add(i);
			isolado.set(i, false);	
		}
		while(fila.size()!=0){
			int atual = fila.remove(0);
			for (int i = 0; i < max; i++) if(tabela.pegaTexto(atual, i).length()!=0 &&(isolado.get(i))){
				isolado.set(i, false);
				fila.add(i);
			}
		}
		ArrayList<Integer> aRemover = new ArrayList<Integer>();
		for (int i = max-1; i >= 0; i--) if(isolado.get(i)) aRemover.add(i);
		return aRemover;
	}
}
