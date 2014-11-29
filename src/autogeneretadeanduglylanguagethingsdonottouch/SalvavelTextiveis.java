package autogeneretadeanduglylanguagethingsdonottouch;

import lingua.Lingua;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import edu.poc.languageemulatorandroid.R;
public abstract class SalvavelTextiveis{
		private static TextView explicacao;
		private static Button aflikestartbutton;
		private static Button butaoconfirma;
		private static Button butaodeleta;
		private static Button butaocancela;
		private static Button confirmaok;
		private static Button confirmacancela;
		private static MenuItem lingua;
		private static MenuItem arquivo;
		private static MenuItem salvar;
		private static MenuItem salvarcomo;
		private static MenuItem abrir;
		private static MenuItem salvaenvia;
		public static void carregaTextiveisMenuSalvavel(Menu m) {
			SalvavelTextiveis.lingua = m.findItem(R.id.lingua);
			SalvavelTextiveis.arquivo= m.findItem(R.id.arquivo);
			SalvavelTextiveis.salvar= m.findItem(R.id.salvar);
			SalvavelTextiveis.salvarcomo= m.findItem(R.id.salvarcomo);
			SalvavelTextiveis.abrir= m.findItem(R.id.abrir);
			SalvavelTextiveis.salvaenvia= m.findItem(R.id.salvaenvia);
		}
		public static void carregaTextiveisMenuMain(Menu m){
			SalvavelTextiveis.lingua = m.findItem(R.id.mainlingua);
			SalvavelTextiveis.arquivo= m.findItem(R.id.mainarquivo);
			SalvavelTextiveis.abrir= m.findItem(R.id.mainabrir);
		}
		public static void carregaTextiveis(Activity c) {
			SalvavelTextiveis.explicacao=(TextView) c.findViewById(R.id.explicacao);
			SalvavelTextiveis.aflikestartbutton=(Button) c.findViewById(R.id.aflikestartbutton);
			SalvavelTextiveis.butaoconfirma=(Button) c.findViewById(R.id.butaoconfirma);
			SalvavelTextiveis.butaodeleta=(Button) c.findViewById(R.id.butaodeleta);
			SalvavelTextiveis.butaocancela=(Button) c.findViewById(R.id.butaocancela);
			SalvavelTextiveis.confirmaok=(Button) c.findViewById(R.id.confirmaok);
			SalvavelTextiveis.confirmacancela=(Button) c.findViewById(R.id.confirmacancela);
		}
		public static void setaLingua(Lingua l) {
			SalvavelTextiveis.aflikestartbutton.setText(l.ok);
			SalvavelTextiveis.butaoconfirma.setText(l.Salvar);
			SalvavelTextiveis.butaodeleta.setText(l.deleta);
			SalvavelTextiveis.butaocancela.setText(l.cancel);
			SalvavelTextiveis.confirmaok.setText(l.ok);
			SalvavelTextiveis.confirmacancela.setText(l.cancel);
		}
		public static void setaLinguaMenusSalvavel(Lingua l) {
			SalvavelTextiveis.lingua.setTitle(l.Lingua);
			SalvavelTextiveis.arquivo.setTitle(l.Arquivo);
			SalvavelTextiveis.salvar.setTitle(l.Salvar);
			SalvavelTextiveis.salvarcomo.setTitle(l.Salvarcomo);
			SalvavelTextiveis.abrir.setTitle(l.Abrir);
			SalvavelTextiveis.salvaenvia.setTitle(l.SalvaEnvia);
		}
		public static void setaLinguaMenusMain(Lingua l) {
			SalvavelTextiveis.lingua.setTitle(l.Lingua);
			SalvavelTextiveis.arquivo.setTitle(l.Arquivo);
			SalvavelTextiveis.abrir.setTitle(l.Abrir);
		}
		public static void setaExplicacao(String s) {
			explicacao.setText(s);
		}
}

