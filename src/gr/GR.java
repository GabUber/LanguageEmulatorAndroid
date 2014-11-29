package gr;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
			StringBuilder s1 = new StringBuilder(transicoesOrigem.get(0)).append(' ');
			s1.append("-> ");
			StringBuilder s2 = new StringBuilder("");
			for(int i = 0; i < transicoesEntrada.size(); i++){
				if(transicoesEntrada.get(i)!=null){
					s2.append(trataString(transicoesEntrada.get(i)));
					s2.append(" ");
				}
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
	protected void criaRegra(String origem, StringTokenizer st){
		modificado=true;
		Regra r = new Regra();
		String s=null;
		int i=0;
		if(!st.hasMoreTokens()){
			r.transicoesOrigem.add(origem);
			r.transicoesEntrada.add(null);
			r.transicoesDestino.add("final");
		}
		else{
			while (st.hasMoreTokens()){
				s=st.nextToken();
				if(i==0)r.transicoesOrigem.add(origem); 
				else r.transicoesOrigem.add("_"+((Integer)(i-1)).toString());
				if(!temMaiuscula(s)){
					r.transicoesDestino.add("_"+((Integer)i).toString());
					r.transicoesEntrada.add(trataString(s));
				}
				else {
					r.transicoesDestino.add(trataString(s.toUpperCase()));
					r.transicoesEntrada.add(null);
				}
				i++;		
			}
			if(!temMaiuscula(s)){
				r.transicoesDestino.remove(i-1);
				r.transicoesDestino.add("final");
			}
		}
		regras.add(r);
	}
	public void novaRegra(String parteEsquerda, String parteDireita){
		StringTokenizer parteDireitaTratada = new StringTokenizer(trataString(parteDireita));
		
		criaRegra(parteEsquerda, parteDireitaTratada);
	}
	public void removeRegra(int n){
		if(regras.size()>n)regras.remove(n);
		modificado=true;
	}
	protected boolean temMaiuscula(String s){
		for (int i=0;i<s.length();i++) if(Character.isUpperCase(s.charAt(i)))return true;
		return false;
	}
	public void moveRegra(int n){
		Regra r;
		if(regras.size()>n){
			r = regras.remove(n);
			regras.add(0, r);
		}
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
			ArrayList<String> nomes = new ArrayList<String>();
			afnl=new AFNL();
			if(regras.size()==0) return;
			int max = regras.size();
			for (int i = 0; i < max; i++){
				Regra r = regras.get(i);
				int max2 = r.transicoesOrigem.size();
				for(int j=0; j < max2; j++){
					String a = r.transicoesDestino.get(j);
					if(a.charAt(0)=='_'){
						a="A"+i+a;
						while(nomes.contains(a)) a+="'";
						if(j<max2-1) r.transicoesOrigem.set(j+1, a);
					}
					nomes.add(a);
					String origem = r.transicoesOrigem.get(j);
					String destino = a;
					String entrada = r.transicoesEntrada.get(j);
					try{
						afnl.adicionaEstado(origem);
					}
					catch(RuntimeException rte){}
					try{
						afnl.adicionaEstado(destino);
					}
					catch(RuntimeException rte){}
					afnl.adicionaTransicao(origem, destino, entrada);
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
		StringTokenizer sb = new StringTokenizer(entradaTratada);
		resposta=afnl.simula(sb);
		return resposta;
	}
	public AFNL retornaAF(){
		if(modificado){
			preparaSimulacao();
		}
		return afnl;
	}
}
