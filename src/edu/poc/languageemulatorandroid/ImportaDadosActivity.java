package edu.poc.languageemulatorandroid;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import mensageiro.Mensageiro;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ImportaDadosActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Uri dados = getIntent().getData();
		if(dados!=null) {
			getIntent().setData(null);
			importaDados(dados);
		}
		Intent intent = new Intent(ImportaDadosActivity.this, MainActivity.class);
		startActivity(intent);
	}
				// launch home Activity (with FLAG_ACTIVITY_CLEAR_TOP) here…

	private void importaDados(Uri data) {
		try {
			FileReader a = new FileReader(data.getPath());
			String s = leArquivo(a);
			a.close();
			if(s!=null){
				Mensageiro.arquivo=s;
				Mensageiro.carrega=true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String leArquivo(FileReader a) throws IOException {
		StringBuilder sb = new StringBuilder(512);
		int c = a.read();
		while(c!=-1){
			sb.append((char)c);
			c=a.read();
		}
		return sb.toString();
	}
}