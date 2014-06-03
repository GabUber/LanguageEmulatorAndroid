package mmACHINE;

import java.util.ArrayList;

public class MooreMachine extends MMachine {
	ArrayList<String> estados;
	ArrayList<String> saidas;
	public MooreMachine(){
		super();
		estados=new ArrayList<String>();
		saidas=new ArrayList<String>();
	}
	@Override
	public void adicionaEstado(String nome, String saida){
		super.adicionaEstado(nome, saida);
		estados.add(nome);
		saidas.add(saida);
	}
	@Override
	public void removeEstado(String nome){
		super.removeEstado(nome);
		int n = estados.indexOf(nome);
		estados.remove(n);
		saidas.remove(n);
	}
	@Override
	public String simula(String entrada) {
		afd.simula(entrada);
		try{ultimaResposta=saidas.get(afd.retornaAtivos().get(0));}
		catch(RuntimeException re){ultimaResposta="";}
		return ultimaResposta;
	}
	@Override
	public String preparaSimulacao(){
		super.preparaSimulacao();
		try{ultimaResposta=saidas.get(afd.retornaAtivos().get(0));}
		catch(RuntimeException re){ultimaResposta="";}
		return ultimaResposta;
	}
}
