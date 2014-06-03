package gr;

import java.util.ArrayList;

import af.AFNL;

public abstract class GR {
	protected class Regra{
		public ArrayList<String> transicoesOrigem;
		public ArrayList<String> transicoesDestino;
		public ArrayList<String> transicoesEntrada;
		public Regra(){
			transicoesOrigem = new ArrayList<String>();
			transicoesDestino = new ArrayList<String>();
			transicoesEntrada = new ArrayList<String>();
		}
		@Override
		public String toString(){
			StringBuilder s1 = new StringBuilder(transicoesOrigem.get(0));
			s1.append("->");
			StringBuilder s2 = new StringBuilder("");
			for(int i = 0; i < transicoesEntrada.size(); i++){
				s2.append((transicoesEntrada.get(i)==null) ? "" : transicoesEntrada.get(i));
			}
			StringBuilder s3;
			if(s2.length()==0&&transicoesDestino.get(transicoesDestino.size()-1).equals("final")) s3=new StringBuilder("λ");
			else if(transicoesDestino.get(transicoesDestino.size()-1).equals("final")) s3=new StringBuilder("");
			else s3=new StringBuilder(transicoesDestino.get(transicoesDestino.size()-1));
			return s1.append(trataString(s2.append(s3))).append(System.getProperty("line.separator")).toString();
		}
	}
	protected boolean aceitaVazio;
	protected boolean modificado;
	protected ArrayList<Regra> regras;
	protected AFNL afnl;
	public GR(){
		regras=new ArrayList<Regra>();
		modificado=true;
	}
	protected void criaRegra(String origem, String destino, String entrada){
		modificado=true;
		Regra r = new Regra();
		if(entrada == null) {
			r.transicoesOrigem.add(origem);
			r.transicoesEntrada.add(null);
			if(destino==null){
				r.transicoesDestino.add("final");
			}
			else {
				r.transicoesDestino.add(destino);
			}
		}
		else {
			r.transicoesOrigem.add(origem);
			int n = entrada.length()-1;
			if(n==0){
				r.transicoesEntrada.add(entrada);
			}
			else{
				for(int i = 0; i < n; i++){
					if(i!=0) r.transicoesOrigem.add("_"+((Integer)(i-1)).toString());
					r.transicoesDestino.add("_"+((Integer)i).toString());
					r.transicoesEntrada.add( ( ( Character ) ( entrada.charAt ( i ) ) ).toString ( ) );
				}
				r.transicoesOrigem.add("_"+((Integer)(n-1)).toString());
				r.transicoesEntrada.add( ( ( Character ) ( entrada.charAt ( n ) ) ).toString ( ) );
			}
			if(destino==null){
				r.transicoesDestino.add("final");
			}
			else{
				r.transicoesDestino.add(destino);
			}
		}
		regras.add(r);
	}
	public void novaRegra(String parteEsquerda, String parteDireita){
		String parteDireitaTratada = trataString(parteDireita);
		int transicao=parteDireitaTratada.length();
		for(int i = 0; i<transicao; i++){
			if(Character.isUpperCase(parteDireitaTratada.charAt(i))){
				transicao=i;
			}
		}
		String entrada, destino;
		if(transicao==parteDireitaTratada.length()){
			destino = null;
			if(parteDireitaTratada.length()==0){
				entrada = null;
			}
			else entrada = parteDireitaTratada;
		}
		else if (transicao==0){
			entrada=null;
			destino=parteDireitaTratada.toUpperCase();
		}
		else{
			entrada = parteDireitaTratada.substring(0, transicao);
			destino = (parteDireitaTratada.substring(transicao)).toUpperCase();
		}
		criaRegra(parteEsquerda, destino, entrada);
	}
	public void removeRegra(int n){
		if(regras.size()>n)regras.remove(n);
		modificado=true;
	}
	@Override
	public String toString(){
		StringBuilder s=new StringBuilder("");
		for(int i = 0; i < regras.size(); i++){
			s.append(i);
			s.append(" - ");
			s.append(regras.get(i).toString());
		}
		return s.toString();
	}
	public void preparaSimulacao(){
		if (modificado){
			afnl=new AFNL();
			for (int i = 0; i < regras.size(); i++){
				for(int j=0; j < regras.get(i).transicoesOrigem.size(); j++){
					String origem = (regras.get(i).transicoesOrigem.get(j).charAt(0)=='_') ? "_" + i + regras.get(i).transicoesOrigem.get(j) : regras.get(i).transicoesOrigem.get(j);
					String destino = (regras.get(i).transicoesDestino.get(j).charAt(0)=='_') ? "_"+i + regras.get(i).transicoesDestino.get(j): regras.get(i).transicoesDestino.get(j);
					try{
						afnl.adicionaEstado(origem);
					}
					catch(RuntimeException r){}
					try{
						afnl.adicionaEstado(destino);
					}
					catch(RuntimeException r){}
					afnl.adicionaTransicao(origem, destino, regras.get(i).transicoesEntrada.get(j));
				}
			}
			afnl.togglaInicial(regras.get(0).transicoesOrigem.get(0));
			afnl.togglaFinal("final");
		}
		aceitaVazio=afnl.preparaSimulacao();
	}
	public void paraSimulacao(){
		afnl.paraSimulacao();
		modificado=false;
	}
	protected abstract String trataString(CharSequence entrada);
	public boolean simula(String entrada){
		boolean resposta=aceitaVazio;
		String entradaTratada = trataString(entrada);
		for(int i = 0; i < entrada.length(); i++){
			resposta=afnl.simula(((Character)(entradaTratada.charAt(i))).toString());
		}
		return resposta;
	}
}
