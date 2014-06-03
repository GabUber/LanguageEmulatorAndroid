package er;

import java.util.ArrayList;
import java.util.Stack;

import af.AFNL;

public class ER {
	protected class Bloco{
		public String penultimoIncluido;
		public String ultimoIncluido;
		public String primeiroIncluido;
	}
	protected String expressao;
	protected Stack<Bloco> pilha;
	protected Stack<Integer> pilhaPendencias;
	protected Bloco blocoAtual;
	protected AFNL afnl;
	protected int contadorEstados;
	protected char ultimoLido, atual;
	protected int contadorPendencias;
	public ER(){
		expressao = "#invalid";
		pilha = new Stack<Bloco>();
		pilhaPendencias=new Stack<Integer>();
		blocoAtual=new Bloco();
	}
	public void novaExpressão(String s)throws RuntimeException{//constroi afnl seguindo algoritmo de construção de thompson
		if (s.length()==0) throw (new RuntimeException());
		AFNL aux =new AFNL();
		aux.adicionaEstado("inicio");
		aux.adicionaEstado("fim");
		aux.togglaInicial("inicio");
		aux.togglaFinal("fim");
		blocoAtual.primeiroIncluido="inicio";
		blocoAtual.ultimoIncluido="inicio";
		blocoAtual.penultimoIncluido=null;
		contadorEstados=0;
		contadorPendencias=0;
		atual='*';
		for(int i = 0; i < s.length(); i++){
			ultimoLido=atual;
			atual=s.charAt(i);
			if(atual=='(') abreParenteses(aux);
			else if(atual==')') fechaParenteses(aux);
			else if(atual=='*') asterisco(aux);
			else if(atual=='+') mais(aux);
			else if(atual=='?') interrogacao(aux);
			else if(atual=='|') ou(aux);
			else caractere(aux);
		}
		resolvePendencias(aux);
		aux.adicionaTransicao(blocoAtual.ultimoIncluido, "fim", null);
		if(!pilha.isEmpty()) throw (new RuntimeException());
		else{expressao=s;
			afnl=aux;
		}
	}
	@Override
	public String toString(){
		return expressao;
	}
	private void empilhaELiga(AFNL a, String s){
		Bloco aux = new Bloco();
		aux.primeiroIncluido= ((Integer)(contadorEstados)).toString();
		aux.ultimoIncluido = aux.primeiroIncluido;
		aux.penultimoIncluido=null;
		contadorEstados++;
		a.adicionaEstado(aux.ultimoIncluido);
		a.adicionaTransicao(s, aux.primeiroIncluido, null);
		pilha.push(blocoAtual);
		blocoAtual=aux;
	}
	private void resolvePendencias(AFNL aux){
		ArrayList<String> a = new ArrayList<String>();
		while(contadorPendencias!=0){
			if(pilha.empty()) throw (new RuntimeException());
			a.add(blocoAtual.ultimoIncluido);
			blocoAtual=pilha.pop();
			contadorPendencias--;
		}
		a.add(blocoAtual.ultimoIncluido);
		blocoAtual.penultimoIncluido=blocoAtual.ultimoIncluido;
		blocoAtual.ultimoIncluido=((Integer)(contadorEstados)).toString();
		contadorEstados++;
		aux.adicionaEstado(blocoAtual.ultimoIncluido);
		for(int i = 0; i < a.size(); i++){
			aux.adicionaTransicao(a.get(i), blocoAtual.ultimoIncluido, null);
		}
	}
	private void ou(AFNL aux) {
		empilhaELiga(aux, blocoAtual.primeiroIncluido);
		contadorPendencias++;
		
	}
	private void caractere(AFNL aux) {
		blocoAtual.penultimoIncluido=blocoAtual.ultimoIncluido;
		blocoAtual.ultimoIncluido=((Integer)(contadorEstados)).toString();
		contadorEstados++;
		aux.adicionaEstado(blocoAtual.ultimoIncluido);
		aux.adicionaTransicao(blocoAtual.penultimoIncluido, blocoAtual.ultimoIncluido,  ((Character)atual).toString());
	}
	private void interrogacao(AFNL aux) {
		if(ultimoLido=='('||ultimoLido=='*'||ultimoLido=='+'||ultimoLido=='?') throw (new RuntimeException());
		aux.adicionaTransicao(blocoAtual.penultimoIncluido, blocoAtual.ultimoIncluido, null);
		
	}
	private void mais(AFNL aux) {
		if(ultimoLido=='('||ultimoLido=='*'||ultimoLido=='+'||ultimoLido=='?') throw (new RuntimeException());
		aux.adicionaTransicao(blocoAtual.ultimoIncluido, blocoAtual.penultimoIncluido, null);
		
	}
	private void asterisco(AFNL aux) {
		if(ultimoLido=='('||ultimoLido=='*'||ultimoLido=='+'||ultimoLido=='?') throw (new RuntimeException());
		aux.adicionaTransicao(blocoAtual.ultimoIncluido, blocoAtual.penultimoIncluido, null);
		aux.adicionaTransicao(blocoAtual.penultimoIncluido, blocoAtual.ultimoIncluido, null);
		
	}
	private void fechaParenteses(AFNL aux) {
		contadorPendencias++;
		resolvePendencias(aux);
		contadorPendencias=pilhaPendencias.pop();
		
	}
	private void abreParenteses(AFNL aux) {
		empilhaELiga(aux, blocoAtual.ultimoIncluido);
		pilhaPendencias.push(contadorPendencias);
		contadorPendencias=0;
		
	}
	public boolean testaExpressão(String s){
		boolean resposta=afnl.preparaSimulacao();
		int i;
		if(s==null||s.equals("")) return resposta;
		else for(i=0;i<s.length();i++){
			resposta=afnl.simula(((Character)s.charAt(i)).toString());
		}
		afnl.paraSimulacao();
		return resposta;
	}
}
