package af;

import java.util.ArrayList;

public class AFNL extends AFN {
	
	ArrayList<Integer> transicoesLambdaOrigem;
	ArrayList<Integer> transicoesLambdaDestino;
	public AFNL(){
		super();
		transicoesLambdaOrigem=new ArrayList<Integer>();
		transicoesLambdaDestino=new ArrayList<Integer>();
	}
	@Override
	public void adicionaTransicao(String origem, String destino, String entrada){
		if (entrada!=null) super.adicionaTransicao(origem, destino, entrada);
		else{
			modificado=true;
			int n = transicoesLambdaOrigem.size();
			for (int i=0;i<n;i++){
				if (origem.equals(estados.get(transicoesLambdaOrigem.get(i))) && destino.equals(estados.get(transicoesLambdaDestino.get(i)))){
					return;
				}
			}
			transicoesLambdaOrigem.add(estados.indexOf(origem));
			transicoesLambdaDestino.add(estados.indexOf(destino));
		}
	}
	@Override
	public void removeEstado(String nome){
		int temp = (estados.indexOf(nome));
		for (int i = 0; i<transicoesLambdaOrigem.size();i++){
			if(temp==transicoesLambdaOrigem.get(i) || temp==transicoesDestino.get(i)){
				transicoesLambdaOrigem.remove((int)i);
				transicoesLambdaDestino.remove((int)i);
				i--;
			}
			else{
				if(temp<transicoesLambdaOrigem.get(i)) transicoesLambdaOrigem.set(i, transicoesLambdaOrigem.get(i)-1);
				if(temp<transicoesLambdaDestino.get(i)) transicoesLambdaDestino.set(i, transicoesLambdaDestino.get(i)-1);
			}
		}
		super.removeEstado(nome);
	}
	@Override
	public void removeTransicao(String origem, String destino, String entrada){
		if(entrada!=null) super.removeTransicao(origem, destino, entrada);
		else{
			modificado=true;
			int n=transicoesLambdaOrigem.size();
			for (int i=0;i<n;i++){
				if (origem.equals(estados.get(transicoesLambdaOrigem.get(i))) && destino.equals(estados.get(transicoesLambdaDestino.get(i)))){
					transicoesLambdaOrigem.remove((int)i);
					transicoesLambdaDestino.remove((int)i);
				}
			}
		}
	}
	@Override
	public boolean preparaSimulacao(){
		boolean resposta = super.preparaSimulacao();
		ArrayList<Integer> transitados = new ArrayList<Integer>();
		ArrayList<Integer> aux1 = iniciais;
		ArrayList<Integer> aux2 = new ArrayList<Integer>();
		int n = transicoesLambdaOrigem.size();
		boolean inserido=true;
		while(inserido){
			inserido=false;
			for(int i = 0; i < n; i++){
				if(aux1.contains(transicoesLambdaOrigem.get(i))&&(!iniciais.contains(transicoesLambdaDestino.get(i)))&&!transitados.contains(transicoesLambdaDestino.get(i))){
					aux2.add(transicoesLambdaDestino.get(i));
					resposta=resposta||finais.contains(transicoesLambdaDestino.get(i));
					inserido=true;
				}
			}
			transitados.addAll(aux2);
			aux1=aux2;
			aux2=new ArrayList<Integer>();
		}
		afl.setaEstadosAtivos(transitados);
		return resposta;
	}
	@Override
	public boolean simula(String entrada){
		if (afl==null)return false;
		boolean respostaInicial = super.simula(entrada);
		boolean respostaSecundaria = false;
		ArrayList<Integer> transitados = new ArrayList<Integer>();
		ArrayList<Integer> ativos = afl.retornaAtivos();
		ArrayList<Integer> aux1 = ativos;
		ArrayList<Integer> aux2 = new ArrayList<Integer>();
		int n = transicoesLambdaOrigem.size();
		boolean inserido=true;
		while(inserido){
			inserido=false;
			for(int i = 0; i < n; i++){
				if(aux1.contains(transicoesLambdaOrigem.get(i))&&(!ativos.contains(transicoesLambdaDestino.get(i)))&&!transitados.contains(transicoesLambdaDestino.get(i))){
					aux2.add(transicoesLambdaDestino.get(i));
					inserido=true;
					respostaSecundaria=respostaSecundaria||finais.contains(transicoesLambdaDestino.get(i));
				}
			}
			transitados.addAll(aux2);
			aux1=aux2;
			aux2=new ArrayList<Integer>();
		}
		afl.setaEstadosAtivos(transitados);
		return (respostaInicial || respostaSecundaria);
	}
}
