package autogeneretadeanduglylanguagethingsdonottouch;

import lingua.Lingua;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import edu.poc.languageemulatorandroid.R;


public abstract class GRTextiveis{
	private static TextView grtextviewmensagemconfirma;
    private static Button grmenucancela;
    private static Button grbutaoconfirma;
    private static Button grbutaoesquerda;
    private static Button grbutaodireita;
    private static Button grbutaoconfirmanovaregra;
    private static Button grbutaoconfirmaremoveregra;
    private static Button grbutaoconfirmamoveregra;
    private static Button grbutaoconfirmatestapadrao;
    private static Button grgeraafnl;
    private static Button grgeracancela;
    public static Button pegaButaoDireita(){
    	return grbutaodireita;
    }
	public static Button pegaButaoEsquerda() {
		return grbutaoesquerda;
	}
	public static void setaLingua(Lingua l) {
		GRTextiveis.grtextviewmensagemconfirma.setText(l.avisoGR);
		GRTextiveis.grmenucancela.setText(l.cancel);
		GRTextiveis.grbutaoconfirma.setText(l.ok);
		GRTextiveis.grbutaoesquerda.setText(l.esquerda);
		GRTextiveis.grbutaodireita.setText(l.direita);
		GRTextiveis.grbutaoconfirmanovaregra.setText(l.ok);
		GRTextiveis.grbutaoconfirmaremoveregra.setText(l.ok);
		GRTextiveis.grbutaoconfirmamoveregra.setText(l.ok);
		GRTextiveis.grbutaoconfirmatestapadrao.setText(l.ok);
		GRTextiveis.grgeraafnl.setText(l.AFNL);
		GRTextiveis.grgeracancela.setText(l.cancel);
	}
	public static void carregaTextiveis(Activity c) {
		GRTextiveis.grtextviewmensagemconfirma=(TextView) c.findViewById(R.id.grtextviewmensagemconfirma);
		GRTextiveis.grmenucancela=(Button) c.findViewById(R.id.grmenucancela);
		GRTextiveis.grbutaoconfirma=(Button) c.findViewById(R.id.grbutaoconfirma);
		GRTextiveis.grbutaoesquerda=(Button) c.findViewById(R.id.grbutaoesquerda);
		GRTextiveis.grbutaodireita=(Button) c.findViewById(R.id.grbutaodireita);
		GRTextiveis.grbutaoconfirmanovaregra=(Button) c.findViewById(R.id.grbutaoconfirmanovaregra);
		GRTextiveis.grbutaoconfirmaremoveregra=(Button) c.findViewById(R.id.grbutaoconfirmaremoveregra);
		GRTextiveis.grbutaoconfirmamoveregra=(Button) c.findViewById(R.id.grbutaoconfirmamoveregra);
		GRTextiveis.grbutaoconfirmatestapadrao=(Button) c.findViewById(R.id.grbutaoconfirmatestapadrao);
		GRTextiveis.grgeraafnl=(Button) c.findViewById(R.id.grgeraafnl);
		GRTextiveis.grgeracancela=(Button) c.findViewById(R.id.grgeracancela);
	}
}
