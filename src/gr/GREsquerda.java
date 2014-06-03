package gr;


public class GREsquerda extends GR {
	@Override
	protected String trataString(CharSequence entrada) {
		return new StringBuilder(entrada).reverse().toString();
	}

}
