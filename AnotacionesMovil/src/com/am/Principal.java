package com.am;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.am.DAO.NotasDataSource;
import com.am.entidades.Nota;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Clase de la pantalla principal
 * @author Juan Sebastian Velez Posada
 *
 */
public class Principal extends MainActivity {
	
	int progreso = 0;
	AnotacionDownloader anotacion;
	ProgressDialog pleaseWaitDialog;
	NotasDataSource dataSource;
	
	/**
	 * Metodo que se ejecuta cuando se crea la vista principal de la app
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        
        //Abrir conexion con la base de datos
        dataSource = new NotasDataSource(this);
        dataSource.open();
        
        //Traer las notas de la db
        List<Nota> values = dataSource.getAllNotes();
        
        //Poner la ultima nota en la pantalla cuando esta cargue
        EditText anotacion = (EditText) findViewById(R.id.vista_nota);
    	anotacion.setText(values.get(values.size()-1).getNota());
        
        //anotacion = new AnotacionDownloader();
        //anotacion.execute(SERVER_ANOTACIONES, progreso);
        
    }
    
    @Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

    /** 
     * Metodo para mostrar un pequeño menu en la parte inferior
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        menu.findItem(R.id.acercade).setIntent(new Intent(this, AcercaDe.class));
        return true;
    }
    
    /**
     * Metodo para saber cual boton fue presionado en el menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

    	int id = item.getItemId();
    	
    	switch (id) {
		case R.id.menu_pull:
			onPull();
			break;
		case R.id.menu_push:
			EditText anotacion = (EditText) findViewById(R.id.vista_nota);
	    	anotacion.setText("on push");
			break;
		case R.id.acercade:
			startActivity(item.getIntent());
			break;
		default:
			anotacion = (EditText) findViewById(R.id.vista_nota);
	    	anotacion.setText(R.string.error);
			break;
		} 
    	
		return true;
	}
    
    /**
     * Metodo para traer las notas del servidor
     * @param v
     */
    public void onPull(){
    	progreso++;
    	anotacion = new AnotacionDownloader();
        anotacion.execute(SERVER_ANOTACIONES, progreso);
    }
    
    
    /**
     * Calse para traer la nota de internet
     * @author Juan Sebastian Velez Posada
     *
     */
    private class AnotacionDownloader extends AsyncTask<Object, String, Boolean> {

    	int startingNumber;
    	String nota = "";
    	
    	/**
    	 * Se ejecuta antes de ir al servidor y muestra la barra de carga
    	 */
    	protected void onPreExecute() {
			pleaseWaitDialog = ProgressDialog.show(Principal.this,"Anotaciones", "Descargando la nota mas reciente", true, true);
			
			pleaseWaitDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					AnotacionDownloader.this.cancel(true);
				}
			});
		}
    	
    	/**
    	 * Se ejecuta despues de ir al servidor e imprime los valores traidos
    	 */
		protected void onPostExecute(Boolean result) {
			if (!isCancelled()) {
				if (result) {
					EditText anotacion = (EditText) findViewById(R.id.vista_nota);
			    	anotacion.setText(nota);
			    	dataSource.updateNote(nota);
				} else {
					EditText anotacion = (EditText) findViewById(R.id.vista_nota);
			    	anotacion.setText(R.string.error);
				}
				pleaseWaitDialog.dismiss();
			} else {
			}
		}
    	
		/**
		 * Se conecta con el servidor para obtener las notas
		 */
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			try {
				// must put parameters in correct order and correct type,
				// otherwise a ClassCastException will be thrown
				startingNumber = (Integer) params[1];
				String pathToNote = params[0] + "?user=jvelezpo";

				result = loadNote(startingNumber, pathToNote);
				if (result)
					nota += "\n" + startingNumber;
				
			} catch (Exception e) {
				result = false;
			}
			return result;
		}
		
		/**
		 * Encargado de leer el archivo txt que es traido del servidor
		 * @param startQuestionNumber
		 * @param txtSource
		 * @return
		 */
		private boolean loadNote(int startQuestionNumber, String txtSource) {
			boolean result = false;
			try {
			    // Create a URL for the desired page
			    URL url = new URL(txtSource);

			    // Read all the text returned by the server
			    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			    String str;
			    while ((str = in.readLine()) != null) {
			    	nota += str + "\n";
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
