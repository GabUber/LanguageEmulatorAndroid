package autogeneretadeanduglylanguagethingsdonottouch;

import lingua.Lingua;
import android.app.Activity;
import android.widget.Button;
import edu.poc.languageemulatorandroid.R;

public abstract class ERTextiveis{
	private static Button erbutaoconfirmaer;
	private static Button ercomfirmapadrao;
	private static Button ergeraafnl;
	private static Button ergeracancela;
	public static void setaLingua(Lingua l) {
		ERTextiveis.ercomfirmapadrao.setText(l.ok);
		ERTextiveis.erbutaoconfirmaer.setText(l.ok);
		ERTextiveis.ergeraafnl.setText(l.AFNL);
		ERTextiveis.ergeracancela.setText(l.cancel);
	}
	public static void carregaTextiveis(Activity c) {
		ERTextiveis.erbutaoconfirmaer=(Button) c.findViewById(R.id.erbutaoconfirmaer);
		ERTextiveis.ercomfirmapadrao=(Button) c.findViewById(R.id.ercomfirmapadrao);
		ERTextiveis.ergeraafnl=(Button) c.findViewById(R.id.ergeraafnl);
		ERTextiveis.ergeracancela=(Button) c.findViewById(R.id.ergeracancela);
	}
}
