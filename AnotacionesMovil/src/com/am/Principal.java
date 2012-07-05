package com.am;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Principal extends MainActivity {
	
	int progreso = 0;
	AnotacionDownloader anotacion;
	ProgressDialog pleaseWaitDialog;
	
	/**
	 * Metodo que se ejecuta cuando se crea la vista principal de la app
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        
        anotacion = new AnotacionDownloader();
        anotacion.execute(SERVER_ANOTACIONES, progreso);
        
    }

    /** 
     * Metodo para mostrar un peque�o menu en la parte inferior
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * Metodo para traer las notas del servidor
     * @param v
     */
    public void traerNotas(View v){
    	progreso++;
    	anotacion = new AnotacionDownloader();
        anotacion.execute(SERVER_ANOTACIONES, progreso);
    }
    
    
    /**
     * Calse para traer la nota de internet
     * @author Juan
     *
     */
    private class AnotacionDownloader extends AsyncTask<Object, String, Boolean> {

    	int startingNumber;
    	String nota = "";
    	
    	protected void onPreExecute() {
			pleaseWaitDialog = ProgressDialog.show(Principal.this,"Anotaciones", "Descargando la nota mas reciente", true, true);
			
			pleaseWaitDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					AnotacionDownloader.this.cancel(true);
				}
			});
		}
    	
    	@Override
		protected void onPostExecute(Boolean result) {
			if (!isCancelled()) {
				if (result) {
					TextView anotacion = (TextView) findViewById(R.id.vista_nota);
			    	anotacion.setText(nota);
				} else {
					TextView anotacion = (TextView) findViewById(R.id.vista_nota);
			    	anotacion.setText("fdihduigfsgf");
				}
				pleaseWaitDialog.dismiss();
			} else {
			}
		}
    	
		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			try {
				// must put parameters in correct order and correct type,
				// otherwise a ClassCastException will be thrown
				startingNumber = (Integer) params[1];
				String pathToNote = params[0] + "";

				result = loadQuestionBatch(startingNumber, pathToNote);
				if (result){
					nota = nota + "\n" + startingNumber;
				}

			} catch (Exception e) {
				result = false;
			}
			return result;
		}
		
		private boolean loadQuestionBatch(int startQuestionNumber, String xmlSource) {
			boolean result = false;
			try {
			    // Create a URL for the desired page
			    URL url = new URL(SERVER_ANOTACIONES);

			    // Read all the text returned by the server
			    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			    String str;
			    while ((str = in.readLine()) != null) {
			        // str is one line of text; readLine() strips the newline character(s)
			    	nota = nota + "\n" + str;
			    }
			    in.close();
			    result = true;
			} catch (Exception e) {
				result = false;
			}
			return result;
		}
		
		
    	
    }
}