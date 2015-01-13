package com.example.gonzalo.androidgcm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Descargar librería Jackson los 3 jar:
//http://wiki.fasterxml.com/JacksonDownload
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.gonzalo.androidgcm.vo.Content;


public class POST2GCM {

	public POST2GCM() {
		
	}
	
	public static void post(String apiKey, Content content) {
		try {
			//1. URL
			URL url = new URL("https://android.googleapis.com/gcm/send");
			
			//2. Abrir Conexión
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//3. Especificar el método post
			conn.setRequestMethod("POST");
			
			//4. Cabeceras
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + apiKey);
			conn.setDoOutput(true);
			
			//5. Añadir los datos JSON
			//5.1. Utilice Jackson objectmapper convertir objeto contenido en JSON
			ObjectMapper mapper = new ObjectMapper();
			
			//5.2 Obtener la conexión output stream
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			
			//5.3 Copiar el contenido del JSOn dentro
			mapper.writeValue(wr, content);
			
			//5.4 Mandar la petición
			wr.flush();
			
			//5.5 Cerrar
			wr.close();
			
			//6. Obtener la respuesta
			int responseCode = conn.getResponseCode();
			
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}//while
			in.close();
			
			//7. Imprimir el resultado
			System.out.println(response.toString());
			
		}//try
		catch(MalformedURLException e) {
			e.printStackTrace();
		}//catch
		catch(IOException e) {
			e.printStackTrace();
		}//catch
  }
}
