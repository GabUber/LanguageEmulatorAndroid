package af;

public class AFD extends AF {
	@Override
	public void togglaInicial(String nome) {
		modificado=true;
		if(iniciais.contains(estados.indexOf(nome))) iniciais.clear();
		else{
			iniciais.clear();
			iniciais.add(estados.indexOf(nome));
		}
	}
	@Override
	public void adicionaTransicao(String origem, String destino, String entrada) {
		modificado=true;
		if (entrada==null) throw(new RuntimeException());
		int n = transicoesOrigem.size();
		if(!alfabeto.contains(entrada)) alfabeto.add(entrada);
		else for (int i=0;i<n;i++){
			if (origem.equals(estados.get(transicoesOrigem.get(i))) && entrada.equals(alfabeto.get(transicoesEntrada.get(i)))){
				transicoesEntrada.remove((int)i);
				transicoesOrigem.remove((int)i);
				transicoesDestino.remove((int)i);
				i=n;
			}
		}
		transicoesEntrada.add(alfabeto.indexOf(entrada));
		transicoesOrigem.add(estados.indexOf(origem));
		transicoesDestino.add(estados.indexOf(destino));
	}
}