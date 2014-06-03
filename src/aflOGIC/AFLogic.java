package aflOGIC;

import java.util.ArrayList;

public class AFLogic {
	protected ArrayList<ArrayList<ArrayList<Integer>>> tabela;
	protected ArrayList<Integer> ativos;
	public AFLogic(){
		tabela=new ArrayList<ArrayList<ArrayList<Integer>>>();
		ativos=new ArrayList<Integer>();
	}
	public void adicionaEstados(int n){//adiciona n estados
		ArrayList<ArrayList<Integer>> temp1;
		int k = (tabela.size() == 0) ? 0 : tabela.get(0).size();
		for(int i = 0; i < n; i++){
			temp1=new ArrayList<ArrayList<Integer>>();
			for(int j = 0; j < k; j++){
				temp1.add (new ArrayList<Integer>());
			}
			tabela.add(temp1);
		}
	}
	public void adicionaLetra(int n){//adiciona n letras ao alfabeto, precisa de haver pelo menos um estado
		ArrayList<ArrayList<Integer>> temp1;
		int k = tabela.size();
		for(int i=0; i<k;i++){
			temp1=tabela.get(i);
			for(int j = 0; j < n; j++){
				temp1.add(new ArrayList<Integer>());
			}
		}
	}
	public void adicionaTransicoes(ArrayList<Integer> estadoOrigem, ArrayList<Integer> letra, ArrayList<Integer> estadoDestino){
		int n = letra.size();
		for (int i = 0; i < n; i++){
			tabela.get(estadoOrigem.get(i)).get(letra.get(i)).add(estadoDestino.get(i));
		}
	}
	public void setaEstadosAtivos(ArrayList<Integer> estados){
		ativos.addAll(estados);
	}
	@SuppressWarnings("unchecked")
	public void setaEstadosAtivosExclusivo(ArrayList<Integer> estados){
		ativos=(ArrayList<Integer>)estados.clone();
	}
	public final ArrayList<Integer> retornaAtivos(){
		return ativos;
	}
	public void simulaPasso(int entrada){
		int k = ativos.size();
		for(int j = 0; j < k; j++){
			ativos.addAll(tabela.get(ativos.remove(0)).get(entrada));
		}
	}
	public final ArrayList<Integer> pegaDestino(int origem, int entrada) {
		return tabela.get(origem).get(entrada);
	}
}
