package af;

public class AFN extends AF {

	@Override
	public void togglaInicial(String nome) {
		modificado=true;
		int n = estados.indexOf(nome);
		if (iniciais.contains(n)) iniciais.remove((Integer) n);
		else iniciais.add(n);
	}
	@Override
	public void adicionaTransicao(String origem, String destino, String entrada) {
		modificado=true;
		if (entrada==null) throw (new RuntimeException());
		int n = transicoesOrigem.size();
		if(!alfabeto.contains(entrada)) alfabeto.add(entrada);
		else for (int i=0;i<n;i++){
			if (origem.equals(estados.get(transicoesOrigem.get(i))) && entrada.equals(alfabeto.get(transicoesEntrada.get(i))) && destino.equals(estados.get(transicoesDestino.get(i)))){
				return;
			}
		}
		transicoesEntrada.add(alfabeto.indexOf(entrada));
		transicoesOrigem.add(estados.indexOf(origem));
		transicoesDestino.add(estados.indexOf(destino));
	}
}
