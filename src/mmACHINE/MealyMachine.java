package mmACHINE;

import java.util.ArrayList;

public class MealyMachine extends MMachine {
	ArrayList<String> origens;
	ArrayList<String> destinos;
	ArrayList<String> entradas;
	ArrayList<String> saidas;
	public MealyMachine(){
		super();
		origens=new ArrayList<String>();
		destinos=new ArrayList<String>();
		entradas=new ArrayList<String>();
		saidas=new ArrayList<String>();
	}
	@Override
	public void removeEstado(String nome){
		super.removeEstado(nome);
		for(int i = 0; i < origens.size();i++){
			if(nome.equals(origens.get(i)) || nome.equals(destinos.get(i))){
				origens.remove(i);
				destinos.remove(i);
				entradas.remove(i);
				saidas.remove(i);
			}
		}
	}
	@Override
	public void adicionaTransicao(String origem, String destino, String entrada, String saida){
		super.adicionaTransicao(origem, destino, entrada, saida);
		for(int i = 0; i < origens.size();i++){
			if(origem.equals(origens.get(i)) && entrada.equals(entradas.get(i))){
				origens.remove(i);
				destinos.remove(i);
				entradas.remove(i);
				saidas.remove(i);
				break;
			}
		}
		origens.add(origem);
		destinos.add(destino);
		entradas.add(entrada);
		saidas.add(saida);
	}
	@Override
	public void removeTransicao(String origem, String destino, String entrada){
		super.removeTransicao(origem, destino, entrada);
		for(int i = 0; i < origens.size();i++){
			if(origem.equals(origens.get(i)) && entrada.equals(entradas.get(i))){
				origens.remove(i);
				destinos.remove(i);
				entradas.remove(i);
				saidas.remove(i);
				break;
			}
		}
	}
	@Override
	public String simula(String entrada) {
		try{
			String s = afd.retornaNome(afd.retornaAtivos().get(0));
			for (int i = 0; i < origens.size(); i++){
				if(s.equals(origens.get(i)) && entrada.equals(entradas.get(i))){
					ultimaResposta=saidas.get(i);
					afd.simula(entrada);
					return ultimaResposta;
				}
			}
		}
		catch(RuntimeException rt){}
		ultimaResposta=null;
		return null;
	}

}
