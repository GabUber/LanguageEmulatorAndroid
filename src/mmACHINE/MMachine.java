package mmACHINE;

import java.util.ArrayList;
import java.util.StringTokenizer;

import af.AFD;

public abstract class MMachine {
	protected AFD afd;
	String ultimaResposta;
	public MMachine(){
		afd=new AFD();
	}
	public void adicionaEstado(String nome, String saida){
		afd.adicionaEstado(nome);
	}
	public void togglaInicial(String nome){
		afd.togglaInicial(nome);
	}
	public ArrayList<Integer> retornaIniciais(){
		return afd.retornaIniciais();
	}
	public ArrayList<Integer> retornaFinais(){
		return afd.retornaFinais();
	}

	public void removeEstado(String nome){
		afd.removeEstado(nome);
	}
	public void adicionaTransicao(String origem, String destino, String entrada, String saida){
		afd.adicionaTransicao(origem, destino, entrada);
	}
	public void removeTransicao(String origem, String destino, String entrada){
		afd.removeTransicao(origem, destino, entrada);
	}
	public String preparaSimulacao(){
		afd.preparaSimulacao();
		ultimaResposta="";
		return "";
	}
	public void paraSimulacao(){
		afd.paraSimulacao();
	}
	public abstract String simula(String entrada);
	public String simula(StringTokenizer st){
		String resposta=ultimaResposta;
		while(st.hasMoreTokens()){
			resposta=simula(st.nextToken());
		}
		ultimaResposta=resposta;
		return resposta;
	}
	public ArrayList<Integer> retornaAtivos(){
		return afd.retornaAtivos();
	}
	public ArrayList<Integer> pegaDestino(String estadoOrigem, String transicao) {
		return afd.pegaDestino(estadoOrigem, transicao);
	}
}
