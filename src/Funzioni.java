import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.text.html.parser.Parser;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Funzioni {
    public BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
    private JSONObject json = new JSONObject();
    String risp = "";

    public String login(){
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
            switch (risp) {
                case "N" -> {
                    risp = "Credenziali errate";
                }
                case "YA" -> {
                    System.out.println("Benvenuto " + json.get("username"));
                }
                case "YU" -> {
                    System.out.println("Sig. " + json.get("username") + " questo programma e' dedicato agli amministratori del servizo");
                    System.out.println("La invitiamo a scaricare il programma rivolto agli utenti");
                }
            }
        }catch (Exception e){
            System.out.println("Formato credenziali inserite non valido");
        }
        return risp;
    }

    public void inserimentoFilm(){
        try{
            System.out.println("Inserire il nome del film: ");
            String msg = tastiera.readLine();
            json.put("nome", msg);

            System.out.println("Inserire la descrizione: ");
            msg = tastiera.readLine();
            json.put("descrizione", msg);

            System.out.println("Inserire la durata del film hh:mm");
            msg = tastiera.readLine();
            json.put("durata", msg);

            risp = reciveParser(postRequest("http://localhost/Server_Cinema/src/nuovo_film.php", json.toJSONString()));

            if (risp.equals("Y")){
                System.out.println("Nuovo film inserito");
            }else if (risp.equals("N")){
                System.out.println("Errore nell'inserimento del film");
            }else {
                System.out.println(risp);
            }
        } catch (Exception e) {
            System.out.println("Errore nell'inserimento del film");
        }
    }

    public void inserimentoSala(){
        try {
            System.out.println("Inserire il nome della sala: ");
            String msg = tastiera.readLine();
            json.put("nome", msg);

            System.out.println("Inserire la dimensione della sala (massimo 3 cifre): ");
            msg = tastiera.readLine();
            json.put("dim sala", msg);

            //nome file destinazione da rivedere (nuova_sala.php)
            risp = reciveParser(postRequest("http://localhost/Server_Cinema/src/nuova_sala.php", json.toJSONString()));

            if (risp.equals("Y")){
                System.out.println("Nuova sala inserita");
            }else if (risp.equals("N")){
                System.out.println("Errore nell'inserimento della sala");
            }else {
                System.out.println(risp);
            }
        }catch (Exception e) {
            System.out.println("Errore nell'inserimento della sala");
        }
    }

    public void inserimentoSpettacolo(){
        try {
            System.out.println("Inserire la data e l'orario yyyy-mm-dd hh:mm: ");
            String msg = tastiera.readLine();
            json.put("data ora", msg);

            System.out.println("Inserire i posti occupati dallo spettacolo: ");
            msg = tastiera.readLine();
            json.put("p occupati", msg);

            //nome file destinazione da rivedere (nuovo_spettacolo.php)
            risp = reciveParser(postRequest("http://localhost/Server_Cinema/src/nuovo_spettacolo.php", json.toJSONString()));

            if (risp.equals("Y")){
                System.out.println("Nuovo spettacolo inserito");
            }else if (risp.equals("N")){
                System.out.println("Errore nell'inserimento dello spettacolo");
            }else {
                System.out.println(risp);
            }
        }catch (Exception e) {
            System.out.println("Errore nell'inserimento dello spettacolo");
        }
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
