package tratadorEntrada;

public class MTratadorEntrada extends TratadorEntrada {
	@Override
	public String transicao(CharSequence cs){
		return removeEspaco(cs);
	}
	@Override
	public String estado(CharSequence cs){
		return removeVirgula(removeEspaco(cs));
	}
	@Override
	protected boolean protegido(char c){
		return false;
	}
}
