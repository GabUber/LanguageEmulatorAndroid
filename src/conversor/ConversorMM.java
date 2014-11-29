package conversor;

import java.util.ArrayList;
import java.util.StringTokenizer;

import transicoes.Transicoes;
import estado.Estado;
import estado.EstadoMoore;

public class ConversorMM extends Conversor{
	@Override
	public String toString(Transicoes pf, ArrayList<Estado> estados, String tipo, int w, int h){
		StringBuilder sb = new StringBuilder(tipo).append(' ').append(w).append(' ').append(h).append(' ');
		int max = estados.size();
		sb.append(max).append(' ');
		int inicial = -1;
		for (int i = 0; i < max; i++){
			Estado e = estados.get(i);
			if (e.ehInicial()) inicial = i;
			sb.append(e.pegaNome()).append(e.getClass() == EstadoMoore.class ? "/"+((EstadoMoore) (e)).pegaSaida() : "" ).append(' ').append(e.getX()).append(' ').append(e.getY()).append(' ');
		}
		if(inicial == -1) sb.append("0 0 ");
		else sb.append("1 ").append(inicial).append(" 0 ");
		for (int j = 0; j < max; j++){
			for (int i = 0; i < max; i++){
				StringTokenizer st = new StringTokenizer(pf.pegaTexto(i, j), ",");
				while(st.hasMoreTokens()){
					sb.append(i).append(' ').append(j).append(' ').append(st.nextToken()).append(' ');
				}
			}
		}
		return sb.toString();
	}
	public String toMealy(Transicoes pf, ArrayList<Estado> estados, int w, int h){
		int max = estados.size();
		EstadoMoore em;
		StringBuilder sb = new StringBuilder ("Mealy ").append(w).append(' ').append(h).append(' ').append(max).append(' ');
		int p=-1;
		for (int i = 0; i < max; i++){
			em=(EstadoMoore) estados.get(i);
			sb.append(em.pegaNome()).append(' ').append(em.getX()).append(' ').append(em.getY()).append(' ');
			if (em.ehInicial()) p=i;
		}
		if (p!=-1)sb.append("1 ").append(p).append(" 0 ");
		else sb.append("0 0 ");
		for (int j = 0; j < max; j++){
			String s=((EstadoMoore) estados.get(j)).pegaSaida();
			for (int i = 0; i < max; i++){
				StringTokenizer st = new StringTokenizer(pf.pegaTexto(i, j), ",");
				while(st.hasMoreTokens()){
					sb.append(i).append(' ').append(j).append(' ').append(st.nextToken()).append ('/').append(s).append(' ');
				}
			}
		}
		return sb.toString();
	}
	public String toMoore(Transicoes pf, ArrayList<Estado> estados, int w, int h){
		int max = estados.size();
		ArrayList<String> estadoNomes = new ArrayList<String>(max);
		ArrayList<String> saidas = new ArrayList<String>(max);
		ArrayList<Float> x = new ArrayList<Float>(max);
		ArrayList<Float> y = new ArrayList<Float>(max);
		int inicial = -1;
		for (int i = 0; i < max; i++){
			Estado e = estados.get(i);
			saidas.add(null);
			estadoNomes.add(e.pegaNome());
			x.add(e.getX());
			y.add(e.getY());
			if(e.ehInicial())inicial = i;
		}
		ArrayList<ArrayList<ArrayList<String>>> tabela = criaTabela(estadoNomes, pf, saidas, x, y);
		return toMoore(estadoNomes, saidas, x, y, tabela, inicial, w, h);
	}
	private static ArrayList<ArrayList<ArrayList<String>>> criaTabela(ArrayList<String> estadoNomes, Transicoes pf, ArrayList<String> saidas, ArrayList<Float> x, ArrayList<Float> y) {
		int max = estadoNomes.size();
		ArrayList<ArrayList<ArrayList<String>>> tabela = new ArrayList<ArrayList<ArrayList<String>>>(max);
		for(int i = 0; i < max; i++){
			ArrayList<ArrayList<String>> linha = new ArrayList<ArrayList<String>>(max);
			tabela.add(linha);
			for (int j = 0; j < max; j++) linha.add(new ArrayList<String>());
		}
		ArrayList<String> fila = new ArrayList<String>();
		ArrayList<ArrayList<String>> entradas = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Integer>> origens = new ArrayList<ArrayList<Integer>>();
		for (int i = 0;  i < max; i++){
			for (int j = 0; j < max; j++){
				encheFila(fila, entradas, origens, pf.pegaTexto(j, i), j);
			}
			consomeFila(tabela, fila, entradas, origens, estadoNomes, saidas, x, y, i);
		}
		return tabela;
	}
	private static void consomeFila(ArrayList<ArrayList<ArrayList<String>>> tabela, ArrayList<String> fila, ArrayList<ArrayList<String>> entradas, ArrayList<ArrayList<Integer>> origens, ArrayList<String> estadoNomes, ArrayList<String> saidas, ArrayList<Float> x, ArrayList<Float> y, int i) {
		boolean primeiro = true;
		StringBuilder s = null;
		int indice;
		while(fila.size()!=0){
			String saida = fila.remove(0);
			ArrayList<Integer> origem = origens.remove(0);
			ArrayList<String> entrada = entradas.remove(0);
			int max = origem.size();
			if (primeiro){
				primeiro = false;
				s = new StringBuilder(estadoNomes.get(i));
				indice = i;
				saidas.set(i, saida);
			}
			else{
				indice = estadoNomes.size();
				String string = s.append("'").toString();
				while(estadoNomes.contains(string)) string = s.append("'").toString();
				estadoNomes.add(string);
				saidas.add(saida);
				adicionaLinha(tabela);
				x.add((float)(0));
				y.add((float)(0));
			}
			for (int j = 0; j < max; j++) tabela.get(origem.get(j)).get(indice).add(entrada.get(j));
		}
		if(primeiro) saidas.add(i, "#invalid");
	}
	private static void adicionaLinha(ArrayList<ArrayList<ArrayList<String>>> tabela) {
		int max = tabela.size();
		ArrayList<ArrayList<String>> novaLinha = new ArrayList<ArrayList<String>>(max+1);
		tabela.add(novaLinha);
		for (int i = 0; i < max; i++){
			novaLinha.add(new ArrayList<String>());
			tabela.get(i).add(new ArrayList<String>());
		}
		novaLinha.add(new ArrayList<String>());
	}
	private static void encheFila(ArrayList<String> fila, ArrayList<ArrayList<String>> entradas, ArrayList<ArrayList<Integer>> origens, String s, int j) {
		StringTokenizer st = new StringTokenizer(s, ",");
		while(st.hasMoreTokens()){
			StringTokenizer st2 = new StringTokenizer(st.nextToken(),"/");
			String entrada = st2.nextToken();
			String saida = st2.nextToken();
			int indice= fila.indexOf(saida);
			if(indice == -1){
				indice = fila.size();
				fila.add(saida);
				entradas.add(new ArrayList<String>());
				origens.add(new ArrayList<Integer>());
			}
			entradas.get(indice).add(entrada);
			origens.get(indice).add(j);
		}
	}
	private static String toMoore(ArrayList<String> estadoNomes, ArrayList<String> saidas, ArrayList<Float> x, ArrayList<Float> y, ArrayList<ArrayList<ArrayList<String>>> tabela, int inicial, int w, int h) {
		int max = estadoNomes.size();
		StringBuilder sb = new StringBuilder("Moore ").append(w).append(' ').append(h).append(' ').append(max).append(' ');
		for (int i = 0; i < max; i++) sb.append(estadoNomes.get(i)).append('/').append(saidas.get(i)).append(' ').append(x.get(i)).append(' ').append(y.get(i)).append(' ');
		if (inicial==-1) sb.append("0 0 ");
		else sb.append("1 ").append(inicial).append(" 0 ");
		for (int i = 0; i < max; i++){
			ArrayList<ArrayList<String>> linha = tabela.get(i);
			for (int j = 0; j < max; j++){
				ArrayList<String> entrada = linha.get(j);
				int max2 = entrada.size();
				for (int k=0;k<max2;k++) sb.append(i).append(' ').append(j).append(' ').append(entrada.get(k)).append(' ');
			}
		}
		return sb.toString();
	}
}
