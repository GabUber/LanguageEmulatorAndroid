package autogeneretadeanduglylanguagethingsdonottouch;

import edu.poc.languageemulatorandroid.R;
import lingua.Lingua;
import android.app.Activity;
import android.widget.Button;

public abstract class MainTextiveis {
    private static Button mainbutaogr;
    private static Button mainbutaommoore;
    private static Button mainbutaommealy;
    private static Button mainbutaoer;
    private static Button mainbutaoafnl;
    private static Button mainbutaoafn;
    private static Button mainbutaoafd;
	public static void setaLingua(Lingua l) {
		MainTextiveis.mainbutaogr.setText(l.gr);
	    MainTextiveis.mainbutaommoore.setText(l.mmoore);
	    MainTextiveis.mainbutaommealy.setText(l.mmealy);
	    MainTextiveis.mainbutaoer.setText(l.ER);
	    MainTextiveis.mainbutaoafnl.setText(l.afnl);
	    MainTextiveis.mainbutaoafn.setText(l.afn);
	    MainTextiveis.mainbutaoafd.setText(l.af);
	}
	public static void carregaTextiveis(Activity c) {
		MainTextiveis.mainbutaogr=(Button) c.findViewById(R.id.mainbutaogr);
	    MainTextiveis.mainbutaommoore=(Button) c.findViewById(R.id.mainbutaommoore);
	    MainTextiveis.mainbutaommealy=(Button) c.findViewById(R.id.mainbutaommealy);
	    MainTextiveis.mainbutaoer=(Button) c.findViewById(R.id.mainbutaoer);
	    MainTextiveis.mainbutaoafnl=(Button) c.findViewById(R.id.mainbutaoafnl);
	    MainTextiveis.mainbutaoafn=(Button) c.findViewById(R.id.mainbutaoafn);
	    MainTextiveis.mainbutaoafd=(Button) c.findViewById(R.id.mainbutaoafd);
	}
}
