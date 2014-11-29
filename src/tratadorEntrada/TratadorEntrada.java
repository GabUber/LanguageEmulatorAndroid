package tratadorEntrada;

public abstract class TratadorEntrada {
	public abstract String transicao(CharSequence cs);
	public abstract String estado(CharSequence cs);
	public String removeSimbolos(CharSequence cs){
		StringBuilder sb = new StringBuilder("");
		int max=cs.length();
		char c;
		for(int i = 0; i<max;i++){
			c=cs.charAt(i);
			if( !protegido(c) ) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public String removeEspaco(CharSequence cs){
		StringBuilder sb = new StringBuilder("");
		int max=cs.length();
		char c;
		for(int i=0;i<max;i++){
			c=cs.charAt(i);
			if(c!=' ') sb.append(c);
		}
		return sb.toString();
	}
	public String removeVirgula(CharSequence cs){
		StringBuilder sb = new StringBuilder("");
		int max=cs.length();
		char c;
		for(int i=0;i<max;i++){
			c=cs.charAt(i);
			if(c!=',') sb.append(c);
		}
		return sb.toString();
	}
	protected abstract boolean protegido(char c);
}
