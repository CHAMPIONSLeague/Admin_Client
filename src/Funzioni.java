import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.text.html.parser.Parser;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Funzioni {
    public BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
    private JSONObject json = new JSONObject();

    public String login(){
        String risp = "";
        try{
            System.out.println("Inserire l'username:");
            String msg = tastiera.readLine();
            json.put("username", msg);

            System.out.println("Inserire password:");
            msg = tastiera.readLine();
            json.put("password", msg);

            //fa il login richiamando il metodo postRequest
            risp = reciveParser(postRequest("http://localhost/Server_Cinema/src/login.php", json.toJSONString()));

            //se non trova l'account lo fa registrare
            if(risp.equals("N")){
                risp="Credenziali errate";
            }else if (risp.equals("YA")){
                System.out.println("Benvenuto "+json.get("username"));
            }else if(risp.equals("YU")){
                System.out.println("Sig. "+json.get("username")+" questo programma e' dedicato agli amministratori del servizo");
                System.out.println("La invitiamo a scaricare il programma rivolto agli utenti");
            }

        }catch (Exception e){
            System.out.println("Formato credenziali inserite non valido");
        }
        return risp;
    }

    public static String postRequest(String indirizzo, String messaggio){
        String response = "";

        if(messaggio != null){
            //messaggio pieno
            try {
                URL uri = new URL(indirizzo);
                HttpURLConnection con = (HttpURLConnection) uri.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);

                OutputStream output = con.getOutputStream();
                output.write(messaggio.getBytes());
                output.flush(); // Dichiara la fine del body
                output.close();
                int rCode = con.getResponseCode();
                if(rCode == 200){
                    DataInputStream input = new DataInputStream(con.getInputStream());
                    String in = "";
                    while((in = input.readLine()) != null){
                        response = in;
                    }
                    input.close();
                }else{
                    System.out.println("Richiesta non andata a buon fine");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //messaggio vuoto
            System.out.println("Messaggio vuoto");
            //TODO: spiegare meglio cosa si intende per messaggio vuoto
        }
        //returna una stringa
        return response; //restituisce il dato richiesto
    }

    public String reciveParser(String s_response){
        JSONObject json_receive; //json utilizzato per ricevere dati specifici
        JSONParser p = new JSONParser();
        String ris = "";

        try {
            json_receive = (JSONObject) p.parse(s_response);
            ris = (String) json_receive.get("ris");
        }catch (ParseException e) {
            System.out.println("Errore di conversione");
        }
        return ris;
    }

}
