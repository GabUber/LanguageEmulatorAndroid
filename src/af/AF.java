package af;


import java.util.ArrayList;
import java.util.StringTokenizer;

import aflOGIC.AFLogic;

public abstract class AF {
	protected AFLogic afl=null;
	ArrayList<String> alfabeto;
	ArrayList<String> estados;
	ArrayList<Integer> iniciais;
	ArrayList<Integer> finais;
	ArrayList<Integer> transicoesOrigem;
	ArrayList<Integer> transicoesDestino;
	ArrayList<Integer> transicoesEntrada;
	protected boolean modificado;
	public AF(){
		afl=new AFLogic();
		alfabeto=new ArrayList<String>();
		estados=new ArrayList<String>();
		iniciais=new ArrayList<Integer>();
		finais=new ArrayList<Integer>();
		transicoesOrigem=new ArrayList<Integer>();
		transicoesDestino=new ArrayList<Integer>(); 
		transicoesEntrada=new ArrayList<Integer>();
		modificado=true;
	}
	public void adicionaEstado(String nome){
		modificado=true;
		if (estados.contains(nome)) throw(new RuntimeException());
		estados.add(nome);
	}
	public abstract void togglaInicial(String nome);
	public final ArrayList<Integer> retornaIniciais(){
		return iniciais;
	}
	public final ArrayList<String> retornaEstados(){
		return estados;
	}
	public final ArrayList<Integer> retornaFinais(){
		return finais;
	}
	public void togglaFinal(String nome){
		modificado=true;
		int n = estados.indexOf(nome);
		if (finais.contains(n)) finais.remove((Integer) n);
		else finais.add(n);
	}
	public void removeEstado(String nome){
		modificado=true;
		int temp = (estados.indexOf(nome));
		iniciais.remove((Integer)temp);
		finais.remove((Integer)temp);
		for (int i = 0; i<transicoesOrigem.size();i++){
			if(temp==transicoesOrigem.get(i) || temp==transicoesDestino.get(i)){
				int entrada=transicoesEntrada.get(i);
				transicoesOrigem.remove((int)i);
				transicoesDestino.remove((int)i);
				transicoesEntrada.remove((int)i);
				if(!transicoesEntrada.contains(entrada)){
					alfabeto.remove(entrada);
					for(int j=0;j<transicoesEntrada.size();j++){
						if(transicoesEntrada.get(j)>entrada) transicoesEntrada.set(j, transicoesEntrada.get(j)-1);
					}
				}
				i--;
			}
			else{
				if(temp<transicoesOrigem.get(i)) transicoesOrigem.set(i, transicoesOrigem.get(i)-1);
				if(temp<transicoesDestino.get(i)) transicoesDestino.set(i, transicoesDestino.get(i)-1);
			}
		}
		estados.remove(nome);
	}
	public String retornaNome(int estado){
		return estados.get(estado);
	}
	public abstract void adicionaTransicao(String origem, String destino, String entrada);
	public void removeTransicao(String origem, String destino, String entrada){
		modificado=true;
		int n = transicoesEntrada.size();
		for(int i = 0; i < n; i++){
			if(destino.equals(estados.get(transicoesDestino.get(i))) && origem.equals(estados.get(transicoesOrigem.get(i))) && entrada.equals(alfabeto.get(transicoesEntrada.get(i)))){
				transicoesDestino.remove(i);
				transicoesOrigem.remove(i);
				transicoesEntrada.remove(i);
				i=n;
			}
		}
		int ent = alfabeto.indexOf(entrada);
		if(!transicoesEntrada.contains(ent)){
			alfabeto.remove(entrada);
			for(int j=0;j<transicoesEntrada.size();j++){
				if(transicoesEntrada.get(j)>ent) transicoesEntrada.set(j, transicoesEntrada.get(j)-1);
			}
		}
	}
	private void constroiAF(){
		afl=new AFLogic();
		afl.adicionaEstados(estados.size());
		afl.adicionaLetra(alfabeto.size());
		afl.adicionaTransicoes(transicoesOrigem, transicoesEntrada, transicoesDestino);
		afl.setaEstadosAtivos(iniciais);
	}
	public boolean preparaSimulacao(){
		boolean resposta = false;
		if(modificado) constroiAF();
		else afl.setaEstadosAtivosExclusivo(iniciais);
		int n = iniciais.size();
		for(int i = 0; i < n; i++){
			resposta = resposta || finais.contains(iniciais.get(i));
		}
		return resposta;
	}
	public void paraSimulacao(){
		modificado=false;
	}
	public boolean simula(String entrada){
		if (afl==null)return false;
		ArrayList<Integer> temp;
		if(!entrada.equals("")){
			int entradaNum = alfabeto.indexOf(entrada);
			if(entradaNum==-1) afl.setaEstadosAtivosExclusivo(new ArrayList<Integer>());
			else afl.simulaPasso(entradaNum);
		}
		temp=afl.retornaAtivos();
		for (int i=0; i<temp.size(); i++){
			if(finais.contains(temp.get(i))) return true;
		}
		return false;
	}
	public boolean simula(StringTokenizer entrada){
		if(afl==null) return false;
		while(entrada.hasMoreTokens()){
			simula(entrada.nextToken());
		}
		return simula("");
	}
	public final ArrayList<Integer> retornaAtivos(){
		return afl.retornaAtivos();
	}
	public final ArrayList<Integer> pegaDestino(String estadoOrigem, String transicao){
		ArrayList <Integer> al = new ArrayList<Integer>();
		for(int i = 0; i < transicoesOrigem.size();i++){
			if(estadoOrigem.equals(estados.get(transicoesOrigem.get(i)))&&transicao.equals(alfabeto.get(transicoesEntrada.get(i)))) al.add(transicoesDestino.get(i));
		}
		return al;
	}
	public ArrayList<ArrayList<ArrayList<String>>> geraTabela(){
		if(modificado){
			constroiAF();
			modificado=false;
		}
		ArrayList<ArrayList<ArrayList<String>>> tabela = new ArrayList<ArrayList<ArrayList<String>>>();
		int max=estados.size();
		ArrayList<ArrayList<String>> temp;
		ArrayList<Integer> entradas;
		for (int i = 0; i<max; i++) {
			temp=new ArrayList<ArrayList<String>>();
			for (int j = 0; j < max; j++)temp.add(new ArrayList<String>());
			tabela.add(temp);
		}
		int max2 = alfabeto.size();
		for(int i=0;i<max;i++) for (int j = 0; j<max2;j++){
			entradas = afl.pegaDestino(i, j);
			int max3 = entradas.size();
			for (int k = 0; k < max3; k++) tabela.get(i).get(entradas.get(k)).add(alfabeto.get(j));
		}
		return tabela;
	}
	public void inverte() {
		ArrayList<Integer> temp = iniciais;
		iniciais=finais;
		finais=temp;
		temp=transicoesOrigem;
		transicoesOrigem=transicoesDestino;
		transicoesDestino=temp;
		constroiAF();
		modificado=false;
	}
}
