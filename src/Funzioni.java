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
    String ris = "";
    JSONParser p = new JSONParser();

    public String login(){
        try{
            System.out.println("Inserire l'username:");
            String msg = tastiera.readLine();
            json.put("username", msg);

            System.out.println("Inserire password:");
            msg = tastiera.readLine();
            json.put("password", msg);

            //fa il login richiamando il metodo postRequest
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/login.php", json.toJSONString()));

            //se non trova l'account lo fa registrare
            switch (ris) {
                case "N" -> {
                    ris = "Credenziali errate";
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
        return ris;
    }

    public void inserimentoFilm(){
        try{
            System.out.println("Inserire il nome del film: ");
            String msg = tastiera.readLine();
            json.put("nome_film", msg);

            System.out.println("Inserire la descrizione: ");
            msg = tastiera.readLine();
            json.put("descrizione", msg);

            System.out.println("Inserire la durata del film hh:mm:ss : ");
            msg = tastiera.readLine();
            json.put("durata", msg);

            //nome file destinazione da rivedere (nuovo_film.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/nuovo_film.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Nuovo film inserito!");
            }else if (ris.equals("N")){
                System.out.println("Errore nell'inserimento del film");
            }else {
                System.out.println(ris);
            }
        } catch (Exception e) {
            System.out.println("Errore nell'inserimento del film");
        }
    }

    public void modificaFilm(){
        try {
            System.out.println("Modifica nome del film: ");
            String msg = tastiera.readLine();
            json.put("nome_film", msg);

            System.out.println("Modifica la descrizione: ");
            msg = tastiera.readLine();
            json.put("descrizione", msg);

            System.out.println("Modifica la durata del film [hh:mm]: ");
            msg = tastiera.readLine();
            json.put("durata", msg);

            //nome file destinazione da rivedere (modifica_film.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/modifica_film.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Film modificato con successo!");
            }else if (ris.equals("N")){
                System.out.println("Errore nella modifica del film");
            }else {
                System.out.println(ris);
            }
        } catch (IOException e) {
            System.out.println("Errore nella modifica del film");
        }
    }

    public void eliminazioneFilm(){
        try{
            System.out.println("Inserire il nome del film da eliminare: ");
            String msg = tastiera.readLine();
            json.put("nome_film", msg);

            //nome file destinazione da rivedere (eliminazione_film.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/eliminazione_film.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Confermi l'eliminazione? ");
                if (ris.equals("Y")){
                    json.put("cmd", "del_film");
                    System.out.println("film eliminato");
                }else {
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Errore eliminazione del film");
            }else {
                System.out.println(ris);
            }
        }catch (IOException e) {
            System.out.println("Errore eliminazione del film");
        }
    }

    public void ricercaFilm(){
        JSONObject json_receive;
        try{
            System.out.println("Inserire il nome del film");
            String msg = tastiera.readLine();
            json.put("nome_film", msg);

            String response = postRequest("http://localhost/Server_Cinema/src/stampa_film.php", json.toJSONString());
            ris = reciveParser(response);
            if (ris.equals("Y")){
                json_receive = (JSONObject) p.parse(response);
                System.out.println("ID: "+(String) json_receive.get("codice_film"));
                System.out.println("Nome: "+(String) json_receive.get("nome_film"));
                System.out.println("Durata: "+(String) json_receive.get("durata"));
                System.out.println("Descrizione: "+(String) json_receive.get("descrizione"));
            }else{
                System.out.println(ris);
            }
        }catch (Exception e){
            e.printStackTrace();
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
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/nuova_sala.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Nuova sala inserita!");
            }else if (ris.equals("N")){
                System.out.println("Errore nell'inserimento della sala");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e) {
            System.out.println("Errore nell'inserimento della sala");
        }
    }

    public void modificaSala(){
        try{
            System.out.println("Inserire il nuovo nome della sala: ");
            String msg = tastiera.readLine();
            json.put("nome", msg);

            System.out.println("Inserire la nuova dimensione della sala (massimo 3 cifre): ");
            msg = tastiera.readLine();
            json.put("dim sala", msg);

            //nome file destinazione da rivedere (modifica_sala.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/modifica_sala.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Sala modificata con successo!");
            }else if (ris.equals("N")){
                System.out.println("Errore nella modifica della sala");
            }else {
                System.out.println(ris);
            }
        } catch (IOException e) {
            System.out.println("Errore nella modifica della sala");
        }
    }

    public void eliminazioneSala(){
        try{
            System.out.println("Inserire il nome della sala da eliminare: ");
            String msg = tastiera.readLine();
            json.put("nome", msg);

            //nome file destinazione da rivedere (eliminazione_sala.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/eliminazione_sala.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Confermi l'eliminazione? ");
                if (ris.equals("Y")){
                    json.put("cmd", "del_sala");
                    System.out.println("sala eliminata");
                }else {
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Errore eliminazione della sala");
            }else {
                System.out.println(ris);
            }
        }catch (IOException e) {
            System.out.println("Errore eliminazione della sala");
        }
    }

    public void inserimentoSpettacolo(){
        try {
            System.out.println("Inserire la data e l'orario yyyy-mm-dd hh:mm:ss : ");
            String msg = tastiera.readLine();
            json.put("data ora", msg);

            System.out.println("Inserire i posti occupati dallo spettacolo: ");
            msg = tastiera.readLine();
            json.put("p occupati", msg);

            //nome file destinazione da rivedere (nuovo_spettacolo.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/nuovo_spettacolo.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Nuovo spettacolo inserito");
            }else if (ris.equals("N")){
                System.out.println("Errore nell'inserimento dello spettacolo");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e) {
            System.out.println("Errore nell'inserimento dello spettacolo");
        }
    }

    public void modificaPalinsesto(){
        try {
            String scelta = "Si";

            while (scelta.equals("Si")) {
                System.out.println("Inserire il numero della sala: ");
                String msg = tastiera.readLine();
                json.put("codice_sala", msg);

                for (int i = 0; i > 3; i++) {
                    System.out.println("Inserire il codice del film: ");
                    msg = tastiera.readLine();
                    json.put("codice_film", msg);

                    System.out.println("Inserire l'orario (10-12-14 formato hh:mm:ss");
                    msg = tastiera.readLine();
                    json.put("data_orario", msg);
                }

                System.out.println("Vuoi modificare un'altra sala? (Si,No)");
                scelta=tastiera.readLine();
            }

            //nome file destinazione da rivedere (change_palinsesto.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/change_palinsesto.php", json.toJSONString()));

            if (ris.equals("Y")){
                System.out.println("Palinsesto modificato con successo!");
            }else if (ris.equals("N")){
                System.out.println("Errore nella modifica del palinsesto");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e) {
            System.out.println("Errore nella modifica del palinsesto");
        }
    }

    public void eliminaPalinsesto(){
        try {
            //nome file destinazione da rivedere (delete_palinsesto.php)
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/delete_palinsesto.php", ""));

            if (ris.equals("Y")){
                System.out.println("Palinsesto eliminato con successo");
            }else if (ris.equals("N")){
                System.out.println("Errore nell'eliminazione del palinsesto");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e) {
            System.out.println("Errore nell'eliminazione del palinsesto");
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

        try {
            json_receive = (JSONObject) p.parse(s_response);
            ris = (String) json_receive.get("ris");
        }catch (ParseException e) {
            System.out.println("Errore di conversione");
        }
        return ris;
    }

}
