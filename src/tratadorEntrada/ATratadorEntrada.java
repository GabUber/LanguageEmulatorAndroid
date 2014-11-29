package tratadorEntrada;

public class ATratadorEntrada extends TratadorEntrada{
	@Override
	public String transicao(CharSequence cs){
		return removeSimbolos(removeEspaco(cs)).toLowerCase();
	}
	@Override
	public String estado(CharSequence cs){
		String s = removeVirgula(removeSimbolos(cs));
		if( s.length()>0 && Character.isLetter(s.charAt(0))) return s.toUpperCase();
		return "";
	}
	@Override
	public boolean protegido(char c){
		return (c == '(' ) || ( c == ')' ) || ( c == '*' ) || ( c == '+' ) || ( c == '?' ) || ( c == '|' ) || ( c == 'λ' );
	}

}
