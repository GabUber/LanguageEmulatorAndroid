package conversor;

import java.util.ArrayList;
import java.util.StringTokenizer;

import transicoes.Transicoes;
import af.AF;
import android.content.Context;
import android.widget.RelativeLayout;
import estado.Estado;

public class ConversorAF extends Conversor{
	public String toER(Transicoes pf, ArrayList<Estado> estados){
		int max = estados.size();
		int maxMais2 = max+2;
		ArrayList<Integer> iniciais = new ArrayList<Integer>(), finais = new ArrayList<Integer>();
		for (int i = 0; i < max; i++){
			Estado e = estados.get(i);
			if(e.ehInicial()) iniciais.add(i);
			if(e.ehFinal()) finais.add(i);
		}
		if(finais.size()==0 || iniciais.size()==0) return "";
		ArrayList<ArrayList<Boolean>> temTransicao = new ArrayList<ArrayList<Boolean>>(maxMais2);
		for (int i = 0; i < maxMais2; i++){
			ArrayList<Boolean> linha = new ArrayList<Boolean>(maxMais2);
			temTransicao.add(linha);
			if(i<max)for(int j = 0; j < max; j++) linha.add(!pf.pegaTexto(i, j).equals(""));
		}
		ArrayList<Integer> grauEntrada = new ArrayList<Integer>(maxMais2), grauSaida = new ArrayList<Integer>(maxMais2);
		for(int i = 0; i < maxMais2; i++){
			grauEntrada.add(0);
			grauSaida.add(0);
		}
		ArrayList<ArrayList<StringBuilder>> tabela = transformaTabela(pf, grauEntrada, grauSaida, iniciais, finais, temTransicao);
		for (int i = 0; i < max; i++) removeEstado(tabela, temTransicao, grauEntrada, grauSaida);
		String s = tabela.get(0).get(1).toString();
		return "er "+ s;
	}
	private void removeEstado(ArrayList<ArrayList<StringBuilder>> tabela, ArrayList<ArrayList<Boolean>> temTransicao, ArrayList<Integer> grauEntrada, ArrayList<Integer> grauSaida) {
		int aRemover = pegaMenor(grauEntrada, grauSaida);
		atualizaVetores(temTransicao, grauEntrada, grauSaida, aRemover);
		preparaRemocao(tabela, temTransicao, aRemover);
		temTransicao.remove(aRemover);
		tabela.remove(aRemover);
		int max = tabela.size();
		for (int i = 0; i < max; i++){
			temTransicao.get(i).remove(aRemover);
			tabela.get(i).remove(aRemover);
		}
	}
	private void atualizaVetores(ArrayList<ArrayList<Boolean>> temTransicao, ArrayList<Integer> grauEntrada, ArrayList<Integer> grauSaida, int aRemover) {
		int max = temTransicao.size();
		ArrayList<Boolean> linhaARemover = temTransicao.get(aRemover);
		for (int i = 0; i<max; i++){
			if(i==aRemover) continue;
			ArrayList<Boolean> linhaI = temTransicao.get(i);
			if(linhaARemover.get(i)) grauEntrada.set(i, grauEntrada.get(i)-1);
			if(linhaI.get(aRemover)){ 
				grauSaida.set(i, grauSaida.get(i)-1);
				for (int j = 0; j < max; j++){
					if(j==aRemover) continue;
					if(linhaARemover.get(j)&&!linhaI.get(j)){
						grauEntrada.set(j, grauEntrada.get(j)+1);
						grauSaida.set(i, grauSaida.get(i)+1);
					}
				}
			}
		}
		grauEntrada.remove(aRemover);
		grauSaida.remove(aRemover);
	}
	private void preparaRemocao(ArrayList<ArrayList<StringBuilder>> tabela, ArrayList<ArrayList<Boolean>> temTransicao, int aRemover) {
		int max = tabela.size();
		String s = temTransicao.get(aRemover).get(aRemover) ? tabela.get(aRemover).get(aRemover).toString() : " ";
		if(!s.equals(" "))s="( "+s+" )*";
		for (int i = 0; i < max; i++){
			if(i==aRemover) continue;
			boolean temTransicaoDeIParaARemover = temTransicao.get(i).get(aRemover);
			if(!temTransicaoDeIParaARemover) continue;
			ArrayList<Boolean> temTransicaoDeARemoverPara = temTransicao.get(aRemover);
			ArrayList<Boolean> temTransicaoDeIPara = temTransicao.get(i);
			ArrayList<StringBuilder> linhaI = tabela.get(i);
			String sARemover = linhaI.get(aRemover).toString();
			ArrayList<StringBuilder> linhaARemover = tabela.get(aRemover);
			for (int j = 0; j < max; j++){
				if(j==aRemover) continue;
				if(temTransicaoDeARemoverPara.get(j)) {
					temTransicaoDeIPara.set(j, true);
					linhaI.set(j, alteraSB(linhaI.get(j).toString(), sARemover, s, linhaARemover.get(j).toString()));
				}
			}
		}
	}
	private StringBuilder alteraSB(String original, String entrada, String loop, String saida) {
		boolean primeiroLambda = original.equals(" ");
		boolean primeiroVazio = original.length() == 0;
		boolean entradaLambda = entrada.equals(" ");
		boolean loopLambda = loop.equals(" ");
		boolean saidaLambda = saida.equals(" ");
		boolean segundoLambda = entradaLambda && loopLambda && saidaLambda;
		StringBuilder sb = new StringBuilder(entrada).append(loop).append(saida);
		if(primeiroLambda){
			if(segundoLambda) return new StringBuilder(' ');
			else return new StringBuilder(" ( ").append(sb).append(" ) ? ");
		}
		else if (segundoLambda) return new StringBuilder(" ( ").append(original).append(" ) ? ");
		if(primeiroVazio) return sb;
		return new StringBuilder(" ( ").append(original).append(" ) | ").append(sb);
	}
	private int pegaMenor(ArrayList<Integer> grauEntrada, ArrayList<Integer> grauSaida) {
		int menor = grauEntrada.get(0)*grauSaida.get(0);
		int resposta = 0;
		int max = grauEntrada.size()-2;
		for (int i = 1; i < max; i++){
			int temp = grauEntrada.get(i)*grauSaida.get(i);
			if (temp < menor){
				menor = temp;
				resposta = i;
			}
		}
		return resposta;
	}
	private ArrayList<ArrayList<StringBuilder>> transformaTabela(Transicoes pf, ArrayList<Integer> grauEntrada, ArrayList<Integer> grauSaida, ArrayList<Integer> iniciais, ArrayList<Integer> finais, ArrayList<ArrayList<Boolean>> temTransicao) {
		int max = grauEntrada.size();
		int maxMenos2 = max - 2;
		ArrayList<Boolean> B = new ArrayList<Boolean>(1);
		B.add(false);
		ArrayList<ArrayList<StringBuilder>> tabela = new ArrayList<ArrayList<StringBuilder>>(max);
		for (int i = 0; i < maxMenos2; i++){
			ArrayList<StringBuilder> linha = new ArrayList<StringBuilder>(max);
			tabela.add(linha);
			for(int j = 0; j < maxMenos2; j++){
				B.set(0, temTransicao.get(i).get(j));
				linha.add(transforma(pf.pegaTexto(i, j), B));
				if(B.get(0)){
					grauEntrada.set(j, grauEntrada.get(j)+1);
					grauSaida.set(i, grauSaida.get(i)+1);
				}
			}
		}
		setaNovoInicial(tabela, temTransicao, iniciais, maxMenos2);
		setaNovoFinal(tabela, temTransicao, finais, max-1);
		return tabela;
	}
	private StringBuilder transforma(String s, ArrayList<Boolean> temTransicao) {
		ArrayList<Boolean> ehLambda = new ArrayList<Boolean>(1);
		ehLambda.add(false);
		boolean temLambda = false;
		StringTokenizer st = new StringTokenizer(s, ",");
		temTransicao.set(0, st.hasMoreTokens());
		StringBuilder sb = new StringBuilder();
		int cont = 0;
		boolean repete = true;
		while(st.hasMoreTokens() && repete){
			sb.append(testaLambda(st.nextToken(), ehLambda));
			if(ehLambda.get(0))temLambda = true;
			else{
				cont++;
				repete = false;
			}
		}
		while (st.hasMoreTokens()){
			String s2 = testaLambda(st.nextToken(), ehLambda);
			if(ehLambda.get(0))temLambda = true;
			else{
				cont++;
				sb.append(" | ").append(s2);
			}
		}
		if(temLambda && cont!=0) sb = new StringBuilder(" ( ").append(sb).append(" ) ? ");
		return sb;
	}
	private String testaLambda(String s, ArrayList<Boolean> ehLambda) {
		ehLambda.set(0, s.equals("λ"));
		if(ehLambda.get(0)) return " ";
		return s;
	}
	private void setaNovoFinal(ArrayList<ArrayList<StringBuilder>> tabela, ArrayList<ArrayList<Boolean>> temTransicao, ArrayList<Integer> finais, int indice) {
		int max = indice+1;
		ArrayList<StringBuilder> linhaSB = new ArrayList<StringBuilder>(max);
		tabela.add(linhaSB);
		ArrayList<Boolean> linhaB = temTransicao.get(indice);
		for (int i = 0; i < max; i++){
			linhaSB.add(new StringBuilder());
			linhaB.add(false);
			tabela.get(i).add(new StringBuilder());
			temTransicao.get(i).add(false);
		}
		max = finais.size();
		for(int i = 0; i < max; i++){
			int fin = finais.get(i);
			tabela.get(fin).get(indice).append(' ');
			temTransicao.get(fin).set(indice, true);
		}
	}
	private void setaNovoInicial(ArrayList<ArrayList<StringBuilder>> tabela, ArrayList<ArrayList<Boolean>> temTransicao, ArrayList<Integer> iniciais, int indice) {
		int max = indice+1;
		ArrayList<StringBuilder> linhaSB = new ArrayList<StringBuilder>(max+1);
		tabela.add(linhaSB);
		ArrayList<Boolean> linhaB = temTransicao.get(indice);
		for (int i = 0; i < max; i++){
			linhaSB.add(new StringBuilder());
			linhaB.add(false);
			tabela.get(i).add(new StringBuilder());
			temTransicao.get(i).add(false);
		}
		max = iniciais.size();
		for(int i = 0; i < max; i++){
			int init = iniciais.get(i);
			linhaSB.get(init).append(' ');
			linhaB.set(init, true);
		}
	}
	public String toAFD(Transicoes pf, ArrayList<Estado> estados, boolean temLambda, RelativeLayout vg, Context c, int d, int w, int h){
		if(estados.size()!=0) {
			if(temLambda)removeLambda(pf,estados);
			removeIndeterminismo(pf, estados, vg, c, d);
		}
		return toString(pf, estados, "AFD", w, h);
	}
	private static void removeIndeterminismo(Transicoes pf, ArrayList<Estado> estados, RelativeLayout vg, Context c, int d) {
		int max = estados.size();
		ArrayList<String> nomeEstados = new ArrayList<String>(max);
		ArrayList<Integer> iniciais = new ArrayList<Integer>();
		ArrayList<Boolean> finais = new ArrayList<Boolean>(max);
		for (int i = 0; i < max; i++){
			Estado e = estados.get(i);
			nomeEstados.add(e.pegaNome());
			if(e.ehInicial()) iniciais.add(i);
			finais.add(e.ehFinal());
		}
		ArrayList<String > alfabeto= new ArrayList<String>();
		ArrayList<ArrayList<ArrayList<Integer>>> tabela = criaTabelaEstadoXAlfabeto(pf, iniciais, nomeEstados.size(), alfabeto);
		ArrayList<Integer> novoInicial = new ArrayList<Integer>(1);
		ArrayList<ArrayList<Integer>> novosEstados = cresceTabela(tabela, iniciais, novoInicial);
		ArrayList<Integer> novosFinais = casoFinais(finais, novosEstados);
		tabela = transformaTabela(tabela);
		atualizaOriginal(tabela, iniciais, novoInicial, novosFinais, novosEstados, pf, estados, alfabeto, nomeEstados, vg, c, d);
	}
	private static ArrayList<ArrayList<Integer>> cresceTabela( ArrayList<ArrayList<ArrayList<Integer>>> tabela, ArrayList<Integer> iniciais, ArrayList<Integer> novoInicial) {
		int max = tabela.size()-1;
		int numEstadosOriginais = tabela.size();
		int max2 = tabela.get(0).size();
		int numEstados = 0;
		ArrayList<ArrayList<ArrayList<Integer>>> novosEstadosOrdenado = new ArrayList<ArrayList<ArrayList<Integer>>>(max);
		ArrayList<ArrayList<ArrayList<Integer>>> novosEstados = new ArrayList<ArrayList<ArrayList<Integer>>>(max);
		ArrayList<ArrayList<Integer>> indiceDeTabela = new ArrayList<ArrayList<Integer>>(max);
		ArrayList<ArrayList<Integer>> indiceDeIndices = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < max; i++){
			novosEstadosOrdenado.add(new ArrayList<ArrayList<Integer>>(i+2));
			novosEstados.add(new ArrayList<ArrayList<Integer>>(i+2));
			indiceDeTabela.add(new ArrayList<Integer>());
		}
		int isize = iniciais.size();
		int isizeMenos2 = isize - 2;
		if(isize == 1) novoInicial.add(iniciais.get(0));
		else if(isize>1){
			insercaoBinaria(iniciais, novosEstadosOrdenado.get(isizeMenos2), novosEstados.get(isizeMenos2), indiceDeTabela.get(isizeMenos2), indiceDeIndices, isizeMenos2, numEstados); 
			numEstados++;
			insereLinha(tabela, iniciais);
			novoInicial.add(max + numEstados);
		}
		for(int i = 0; i < tabela.size(); i++){
			ArrayList<ArrayList<Integer>> linha = tabela.get(i);
			for (int j = 0; j < max2; j++){
				ArrayList<Integer> entrada = linha.get(j);
				int sizeMenos2 = entrada.size()-2;
				if(sizeMenos2>=0){
					int indice =insercaoBinaria(entrada, novosEstadosOrdenado.get(sizeMenos2), novosEstados.get(sizeMenos2), indiceDeTabela.get(sizeMenos2), indiceDeIndices, sizeMenos2, numEstados); 
					if (indice==-1){
						indice = numEstados;
						numEstados++;
						insereLinha(tabela, entrada);
					}
					entrada.clear();
					entrada.add(numEstadosOriginais + indice);
				}
			}
		}
		return ordenaPorIndice(novosEstados, indiceDeIndices);
	}
	private static int insercaoBinaria(ArrayList<Integer> entrada, ArrayList<ArrayList<Integer>> ordenado, ArrayList<ArrayList<Integer>> porChegada, ArrayList<Integer> indiceOrdenado, ArrayList<ArrayList<Integer>> indiceDeIndices, int sizeMenos2, int numEstados) {
		ArrayList<Integer> saida = new ArrayList<Integer>(1);
		boolean achado = procuraBinaria(ordenado, entrada, saida);
		int local = saida.remove(0);
		if(!achado){
			ArrayList<Integer> copia = copia(entrada);
			ordenado.add(local, copia);
			indiceOrdenado.add(local, numEstados);
			ArrayList<Integer> indice = new ArrayList<Integer>(2);
			indice.add(sizeMenos2);
			indice.add(porChegada.size());
			indiceDeIndices.add(indice);
			porChegada.add(copia);
		}
		return achado ? indiceOrdenado.get(local) : -1;
	}
	private static boolean procuraBinaria(ArrayList<ArrayList<Integer>> ordenado, ArrayList<Integer> entrada, ArrayList<Integer> saida) {
		int min  = 0, max = ordenado.size()-1;
		boolean encontrado = false;
		int media=0;
		if(max>=0)while(!encontrado){
			media = (min+max)/2;
			int resultado = compara (ordenado.get(media), entrada);
			if(resultado<0) max = media-1;
			else if(resultado>0) min=media+1;
			else encontrado=true;
			if(min>=max) break;
		}
		if(!encontrado && max==ordenado.size()-1) saida.add(ordenado.size());
		else saida.add(media);
		return encontrado;
	}
	private static int compara(ArrayList<Integer> gabarito, ArrayList<Integer> entrada) {
		int max = gabarito.size();
		for (int i = 0; i < max; i++){
			int gab = gabarito.get(i), ent = entrada.get(i);
			if(ent<gab) return -1;
			else if (ent > gab) return 1;
		}
		return 0;
	}
	private static ArrayList<Integer> copia(ArrayList<Integer> entrada) {
		int max = entrada.size();
		ArrayList<Integer> copia = new ArrayList<Integer>(max);
		for(int i = 0; i < max; i++) copia.add(entrada.get(i));
		return copia;
	}
	private static void insereLinha(ArrayList<ArrayList<ArrayList<Integer>>> tabela, ArrayList<Integer> entrada) {
		int max = tabela.get(0).size();
		ArrayList<ArrayList<Integer>> novaLinha =new ArrayList<ArrayList<Integer>>(max);
		for(int i = 0; i < max; i++) novaLinha.add(new ArrayList<Integer>());
		int max2= entrada.size();
		for(int i = 0; i < max2; i++){
			ArrayList<ArrayList<Integer>> linha = tabela.get(entrada.get(i));
			for(int j = 0; j < max; j++) {
				ArrayList<Integer> entrada0 = linha.get(j);
				int max3=entrada0.size();
				ArrayList<Integer> entrada1 = novaLinha.get(j);
				for(int k = 0; k < max3; k++){
					int numero = entrada0.get(k);
					if(!entrada1.contains(numero)) entrada1.add(numero);
				}
			}
		}
		tabela.add(novaLinha);
	}
	private static ArrayList<ArrayList<Integer>> ordenaPorIndice(ArrayList<ArrayList<ArrayList<Integer>>> novosEstadosOrdenado, ArrayList<ArrayList<Integer>> indiceDeIndices) {
		ArrayList<ArrayList<Integer>> resposta = new ArrayList<ArrayList<Integer>>();
		int max = indiceDeIndices.size();
		for(int i = 0; i < max; i++){
			ArrayList<Integer> indice = indiceDeIndices.get(i);
			resposta.add(novosEstadosOrdenado.get(indice.get(0)).get(indice.get(1)));
		}
		return resposta;
	}
	private static ArrayList<Integer> casoFinais(ArrayList<Boolean> finais,	ArrayList<ArrayList<Integer>> novosEstados) {
		ArrayList<Integer> resposta = new ArrayList<Integer>();
		int max = novosEstados.size();
		int offset = finais.size();
		for(int i = 0; i < max; i++){
			ArrayList<Integer> linha = novosEstados.get(i);
			int max2 = linha.size();
			for(int j = 0; j < max2; j++) if(finais.get(linha.get(j))){
				resposta.add(offset+i);
				break;
			}
		}
		return resposta;
	}
	private static ArrayList<ArrayList<ArrayList<Integer>>> transformaTabela(ArrayList<ArrayList<ArrayList<Integer>>> tabela) {
		int max = tabela.size();
		ArrayList<ArrayList<ArrayList<Integer>>> resposta = new ArrayList<ArrayList<ArrayList<Integer>>>(max);
		for(int i = 0; i < max; i++){
			ArrayList<ArrayList<Integer>> linha = new ArrayList<ArrayList<Integer>>(max);
			for(int j = 0; j < max; j++) linha.add(new ArrayList<Integer>());
			resposta.add(linha);
		}
		int max2 = tabela.get(0).size();
		for (int i=0;i<max;i++){
			ArrayList<ArrayList<Integer>> linha = tabela.get(i);
			ArrayList<ArrayList<Integer>> linhaResposta = resposta.get(i);
			for (int j = 0; j < max2; j++){
				ArrayList<Integer> entrada = linha.get(j);
				int max3 = entrada.size();
				for(int k = 0; k < max3; k++) linhaResposta.get(entrada.get(k)).add(j);
			}
		}
		return resposta;
	}
	private static void atualizaOriginal(ArrayList<ArrayList<ArrayList<Integer>>> tabela, ArrayList<Integer> desIniciais, ArrayList<Integer> novoInicial, ArrayList<Integer> novosFinais, ArrayList<ArrayList<Integer>> novosEstados, Transicoes pf, ArrayList<Estado> estados, ArrayList<String> alfabeto, ArrayList<String> nomeEstados, RelativeLayout vg, Context c, int d) {
		int max = novosEstados.size();
		for(int i = 0; i < max; i++){
			ArrayList<Integer> atual = novosEstados.get(i);
			StringBuilder sb = new StringBuilder(nomeEstados.get(atual.get(0)));
			int max2 = atual.size();
			for(int j = 1; j < max2; j++) sb.append('∪').append(nomeEstados.get(atual.get(j)));
			String s = sb.toString();
			while(nomeEstados.contains(s)) s+="'";
			nomeEstados.add(s);
			Estado e = new Estado(d/2, d/2, vg);
			e.setaNome(s);
			estados.add(e);
			pf.adicionouEstado(0, 0, c, vg);
		}
		max=desIniciais.size();
		for(int i = 0; i < max; i++) estados.get(desIniciais.get(i)).toggleInicial();
		int init = novoInicial.size()!=0 ? novoInicial.get(0) : -1;
		if(init>=0)estados.get(init).toggleInicial();
		max=novosFinais==null ? 0 : novosFinais.size();
		for(int i = 0; i < max; i++) estados.get(novosFinais.get(i)).toggleFinal();
		max = estados.size();
		for(int i = 0; i < max; i++) for (int j = 0; j < max; j++){
			ArrayList<Integer> linha = tabela.get(i).get(j);
			int max2 = linha.size();
			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < max2; k++) sb.append(alfabeto.get(linha.get(k))).append(k+1<max2 ? "," : "");
			pf.novaTransicao(i, j, sb.toString());
		}
	}
	private static ArrayList<ArrayList<ArrayList<Integer>>> criaTabelaEstadoXAlfabeto(Transicoes pf, ArrayList<Integer> iniciais, int max, ArrayList<String> alfabeto) {
		ArrayList<ArrayList<ArrayList<Integer>>> tabela = new ArrayList<ArrayList<ArrayList<Integer>>>(max);
		for(int i = 0; i < max; i++){
			tabela.add(new ArrayList<ArrayList<Integer>>());
		}
		for(int i = 0; i < max; i++) for (int j = 0; j < max; j++){
			StringTokenizer st = new StringTokenizer(pf.pegaTexto(i, j), ",");
			while(st.hasMoreTokens()){
				String s = st.nextToken();
				int k = alfabeto.indexOf(s);
				if (k==-1){
					k=alfabeto.size();
					alfabeto.add(s);
					for(int l = 0; l < max; l++) tabela.get(l).add(new ArrayList<Integer>());
				}
				tabela.get(i).get(k).add(j);
			}
		}
		return tabela;
	}
	public String toAFN(Transicoes pf, ArrayList<Estado> estados, boolean temLambda, int w, int h){
		if(temLambda)removeLambda(pf,estados);
		return toString(pf, estados, "AFN", w, h);
	}
	private static void removeLambda(Transicoes pf, ArrayList<Estado> estados) {
		int max = estados.size();
		ArrayList<Integer> lambdaOrigens = new ArrayList<Integer>(), lambdaDestinos = new ArrayList<Integer>();
		for (int i = 0; i < max; i++){
			for (int j = 0; j < max; j++){
				String s = pf.pegaTexto(i, j);
				if(s.contains("λ")){
					pf.novaTransicao(i, j, removeSubstringLambda(s));
					if(estados.get(i).ehInicial() && (! estados.get(j).ehInicial())) estados.get(j).toggleInicial();
					if (i != j){
						lambdaOrigens.add(i);
						lambdaDestinos.add(j);
					}
				}
			}
		}
		geraTransitividade(lambdaOrigens, lambdaDestinos, estados);
		ArrayList<ArrayList<ArrayList<String>>> tabela = criaTabelaEstadoXEstado(pf, max);
		adicionaEquivalentesALambda(lambdaOrigens, lambdaDestinos, max, tabela);
		atualizaPF(pf, tabela, max);
	}
	private static void geraTransitividade(ArrayList<Integer> lambdaOrigens,
			ArrayList<Integer> lambdaDestinos, ArrayList<Estado> estados) {
		boolean modifica = true;
		while(modifica){
			modifica=false;
			for(int i = 0; i < lambdaOrigens.size(); i++){
				for(int j=0;j<lambdaOrigens.size();j++){
					if(i==j)continue;
					int d = lambdaDestinos.get(i);
					int o = lambdaOrigens.get(j);
					if(o==d) if(tentaAdicionar(lambdaOrigens.get(i),lambdaDestinos.get(j),lambdaOrigens, lambdaDestinos, estados))modifica=true;
				}
			}
		}
		// TODO Auto-generated method stub
		
	}
	private static boolean tentaAdicionar(int origem, int destino,	ArrayList<Integer> lambdaOrigens, ArrayList<Integer> lambdaDestinos, ArrayList<Estado> estados) {
		int max = lambdaOrigens.size();
		for(int i = 0; i < max; i++) if(lambdaOrigens.get(i)==origem && lambdaDestinos.get(i)==destino)return false;
		lambdaOrigens.add(origem);
		lambdaDestinos.add(destino);
		if(estados.get(origem).ehInicial() && ! estados.get(destino).ehInicial()) estados.get(destino).toggleInicial();
		return true;
	}
	private static void adicionaEquivalentesALambda(ArrayList<Integer> lambdaOrigens, ArrayList<Integer> lambdaDestinos, int numEstados, ArrayList<ArrayList<ArrayList<String>>> tabela) {
		int max = lambdaOrigens.size();
		ArrayList<ArrayList<ArrayList<String>>> tabelaAux = new ArrayList<ArrayList<ArrayList<String>>>(numEstados);
		ArrayList<ArrayList<ArrayList<String>>> novaTabela = new ArrayList<ArrayList<ArrayList<String>>>(numEstados);
		for(int i = 0; i < numEstados; i++){
			ArrayList<ArrayList<String>> linhaOriginal = tabela.get(i);
			ArrayList<ArrayList<String>> linhaNova = new ArrayList<ArrayList<String>>(numEstados);
			ArrayList<ArrayList<String>> linhaAux = new ArrayList<ArrayList<String>>(numEstados);
			novaTabela.add(linhaNova);
			tabelaAux.add(linhaAux);
			for(int j = 0; j < numEstados; j++){
				linhaNova.add(new ArrayList<String>());
				ArrayList<String> entrada = linhaOriginal.get(j);
				ArrayList<String> entradaAux = new ArrayList<String>(entrada.size());
				linhaAux.add(entradaAux);
				entradaAux.addAll(entrada);
			}
		}
		boolean algumModificado = true;
		while(algumModificado){
			algumModificado = false;
			for (int i = 0; i < max; i++){
				int destino = lambdaDestinos.get(i);
				int origem = lambdaOrigens.get(i);
				for (int j=0;j<numEstados;j++){
					ArrayList<String> entrada= tabelaAux.get(j).get(origem);
					ArrayList<String> atual = tabela.get(j).get(destino);
					ArrayList<String>novaEntrada = novaTabela.get(j).get(destino);
					int max2 = entrada.size();
					for(int k = 0; k < max2; k++){
						String s = entrada.get(k);
						if(!atual.contains(s)){
							algumModificado = true;
							atual.add(s);
							novaEntrada.add(s);
						}
					}
				}
			}
			for(int i = 0; i < numEstados; i++){
				ArrayList<ArrayList<String>> linhaAux = tabelaAux.get(i);
				for (int j = 0; j < numEstados; j++) linhaAux.get(j).clear();
			}
			ArrayList<ArrayList<ArrayList<String>>> aux = novaTabela;
			novaTabela=tabelaAux;
			tabelaAux=aux;
		}
	}
	private static ArrayList<ArrayList<ArrayList<String>>> criaTabelaEstadoXEstado(
			Transicoes pf, int max) {
		ArrayList<ArrayList<ArrayList<String>>> tabela = new ArrayList<ArrayList<ArrayList<String>>>(max);
		for(int i = 0; i < max; i++){
			ArrayList<ArrayList<String>> linha = new ArrayList<ArrayList<String>>(max);
			tabela.add(linha);
			for(int j = 0; j < max; j++){
				ArrayList<String> entrada = new ArrayList<String>();
				linha.add(entrada);
				StringTokenizer st = new StringTokenizer(pf.pegaTexto(i, j), ",");
				while(st.hasMoreTokens()) entrada.add(st.nextToken());
			}
		}
		return tabela;
	}
	private static void atualizaPF(Transicoes pf, ArrayList<ArrayList<ArrayList<String>>> tabela, int numEstados) {
		for (int i = 0; i < numEstados; i++){
			ArrayList<ArrayList<String>> atual1 = tabela.get(i);
			for(int j = 0; j < numEstados; j++){
				ArrayList<String> atual2 = atual1.get(j);
				StringBuilder sb = new StringBuilder();
				while(atual2.size()!=0) sb.append(atual2.remove(0)).append(atual2.size()==0 ? "" : ",");
				pf.novaTransicao(i, j, sb.toString());
			}
		}
	}
	private static String removeSubstringLambda(String s) {
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(s, ",");
		String s1;
		while(st.hasMoreTokens()){
			s1=st.nextToken();
			if (!s1.equals("λ")) sb.append(s1).append(st.hasMoreTokens() ? "," : "");
		}
		return sb.toString();
	}
	public String toGRE(Transicoes pf, ArrayList<Estado> estados){
		ArrayList<String> estadoNomes = new ArrayList<String>();
		StringBuilder sb = new StringBuilder("GRE ");
		int max = estados.size();
		ArrayList<Integer> finais = new ArrayList<Integer>();
		for (int i = 0; i < max; i++){
			if(estados.get(i).ehFinal()) finais.add(i);
			estadoNomes.add(estados.get(i).pegaNome());
		}
		int max2 = finais.size();
		if(max2==0) return sb.toString();
		String s = "S";
		while(estadoNomes.contains(s)) s+="'";
		for (int i = 0; i < max2; i++) sb.append(s).append(" -> ").append(estadoNomes.get(finais.get(i))).append(System.getProperty("line.separator"));
		for (int i = 0; i < max; i++){
			for (int j = 0; j < max; j++){
				StringTokenizer st = new StringTokenizer(pf.pegaTexto(j, i), ",");
				while(st.hasMoreTokens()){
					s=st.nextToken();
					sb.append(estadoNomes.get(i)).append(" -> ").append(estadoNomes.get(j)).append(' ').append(s.equals("λ") ? "" : s).append(System.getProperty("line.separator"));
				}
			}
			if(estados.get(i).ehInicial()){
				sb.append(estadoNomes.get(i)).append(" -> ").append("λ").append(System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}
	public String toGRD(Transicoes pf, ArrayList<Estado> estados){
		ArrayList<String> estadoNomes = new ArrayList<String>();
		StringBuilder sb = new StringBuilder("GRD ");
		int max = estados.size();
		ArrayList<Integer> iniciais = new ArrayList<Integer>();
		for (int i = 0; i < max; i++){
			if(estados.get(i).ehInicial()) iniciais.add(i);
			estadoNomes.add(estados.get(i).pegaNome());
		}
		int max2 = iniciais.size();
		if(max2==0) return sb.toString();
		String s = "S";
		while(estadoNomes.contains(s)) s+="'";
		for (int i = 0; i < max2; i++) sb.append(s).append(" -> ").append(estadoNomes.get(iniciais.get(i))).append(System.getProperty("line.separator"));
		for (int i = 0; i < max; i++){
			for (int j = 0; j < max; j++){
				StringTokenizer st = new StringTokenizer(pf.pegaTexto(i, j), ",");
				while(st.hasMoreTokens()){
					s=st.nextToken();
					sb.append(estadoNomes.get(i)).append(" -> ").append(s.equals("λ") ? "" : s).append(' ').append(estadoNomes.get(j)).append(System.getProperty("line.separator"));
				}
			}
			if(estados.get(i).ehFinal()) sb.append(estadoNomes.get(i)).append(" -> ").append("λ").append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	@Override
	public String toString(Transicoes pf, ArrayList<Estado> estados, String tipo, int w, int h){
		int max = estados.size();
		Estado em;
		StringBuilder sb = new StringBuilder(tipo).append(' ').append(w).append(' ').append(h).append(' ').append(max).append(' ');
		ArrayList<Integer> init= new ArrayList<Integer>();
		ArrayList<Integer> fin = new ArrayList<Integer>();
		for (int i = 0; i < max; i++){
			em=estados.get(i);
			sb.append(em.pegaNome()).append(' ').append(em.getX()).append(' ').append(em.getY()).append(' ');
			if (em.ehInicial()) init.add(i);
			if (em.ehFinal  ()) fin .add(i);
		}
		int p = init.size();
		sb.append(p).append(' ');
		for(int i = 0; i < p;i++)sb.append(init.get(i)).append(' ');
		p=fin.size();
		sb.append(p).append(' ');
		for(int i = 0; i < p; i++)sb.append(fin.get(i)).append(' ');
		for (int i = 0; i < max; i++){
			for (int j = 0; j < max; j++){
				StringTokenizer st = new StringTokenizer(pf.pegaTexto(i, j), ",");
				while(st.hasMoreTokens()){
					sb.append(i).append(' ').append(j).append(' ').append(st.nextToken()).append(' ');
				}
			}
		}
		return sb.toString();
	}
	public String afToAF(AF af, int w, int h, boolean inverte){
		if(inverte) af.inverte();
		ArrayList<String> estados = af.retornaEstados();
		int max = estados.size();
		StringBuilder sb = new StringBuilder("AFNL ").append(w).append(' ').append(h).append(' ').append(max).append(' ');
		final ArrayList<Integer> init = af.retornaIniciais();
		final ArrayList<Integer> fin  = af.retornaFinais();
		for (int i = 0; i < max; i++)sb.append(estados.get(i)).append(' ').append(0).append(' ').append(0).append(' ');
		int p = init.size();
		sb.append(p).append(' ');
		for(int i = 0; i < p;i++)sb.append(init.get(i)).append(' ');
		p=fin.size();
		sb.append(p).append(' ');
		for(int i = 0; i < p; i++)sb.append(fin.get(i)).append(' ');
		ArrayList<ArrayList<ArrayList<String>>> tabela = af.geraTabela();
		for (int i = 0; i < max; i++){
			for (int j = 0; j < max; j++){
				ArrayList<String> linha = tabela.get(i).get(j);
				int max2 = linha.size();
				for(int k = 0; k < max2; k++){
					sb.append(i).append(' ').append(j).append(' ').append(linha.get(k)).append(' ');
				}
			}
		}
		return sb.toString();
	}
}
