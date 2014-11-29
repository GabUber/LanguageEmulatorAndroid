package gerenciaArquivo;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroLangEmul implements FilenameFilter {

	@Override
	public boolean accept(File dir, String nomeArquivo) {
		if(nomeArquivo.length()<9) return false;
		return nomeArquivo.substring(nomeArquivo.length()-9).equals(".langemul");
	}
}
