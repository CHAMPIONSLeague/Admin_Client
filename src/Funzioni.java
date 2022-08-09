import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Funzioni {
    public BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
    public JSONObject json = new JSONObject();
    JSONParser p = new JSONParser();
    private String ris = "";
    private String msg = "";
    private final String address_admin = "http://clowncinema.altervista.org/src/admin/";
    private final String address = "http://clowncinema.altervista.org/src/";

    //funziona 25/05
    public String login(){
        try{
            System.out.println("Inserire l'username:");
            String msg = tastiera.readLine();
            json.put("username", msg);

            System.out.println("Inserire password:");
            msg = tastiera.readLine();
            json.put("password", msg);

            //fa il login richiamando il metodo postRequest
            ris = reciveParser(postRequest("http://clowncinema.altervista.org/src/login.php", json.toJSONString()));

            //se non trova l'account lo fa registrare
            switch (ris) {
                case "N" -> {
                    ris = "Credenziali errate";
                }
                case "YA" -> {
                    System.out.println("Benvenuto " + json.get("username"));
                    return "logged";
                }
                case "YU" -> {
                    System.out.println("Sig. " + json.get("username") + " questo programma e' dedicato agli amministratori del servizo");
                    System.out.println("La invitiamo a scaricare il programma rivolto agli utenti");
                    return "user logged";
                }
            }
        }catch (Exception e){
            System.out.println("Formato credenziali inserite non valido");
        }
        return ris;
    }

    //todo: 18/05 funziona
    public void changeUser(){
        try {
            System.out.println("Inserire la password dell'account");
            msg = tastiera.readLine();
            //username già caricato
            json.put("password", msg);
            json.put("cmd", "checkPass");

            ris = reciveParser(postRequest(address +"change_username.php",json.toJSONString()));

            if (ris.equals("Y")){ //credenziali corrette
                // permette al server di fare il cambio username
                json.put("cmd", "ch_user");

                System.out.println("Inserire il nuovo Username");
                msg = tastiera.readLine();
                json.put("new_user", msg);

                ris = reciveParser(postRequest(address +"change_username.php",json.toJSONString()));
                if (ris.equals("Y")){
                    System.out.println("Username aggiornato");
                    json.put("username", json.get("new_user"));
                }else if (ris.equals("N")){
                    System.out.println("Errore nel cambio di username");
                }else{
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Password errata");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e){
            System.out.println("Errore nel cambio di username");
        }
    }

    //todo: 18/05 funziona
    public void changeEmail(){
        try{
            System.out.println("Inserire la password");
            msg = tastiera.readLine();
            json.put("password", msg);
            json.put("cmd", "checkPass");

            ris = reciveParser(postRequest(address +"change_email.php", json.toJSONString()));

            if (ris.equals("Y")){ //credenziali corrette
                json.put("cmd", "ch_email");

                System.out.println("Inserire la nuova email");
                msg = tastiera.readLine();
                json.put("new_email", msg);

                ris = reciveParser(postRequest(address +"change_email.php", json.toJSONString()));
                if (ris.equals("Y")){
                    System.out.println("Email aggiornata con successo");
                    json.put("email", json.get("new_user"));
                }else if (ris.equals("N")){
                    System.out.println("Email occupata da qualcun'altro");
                }else{
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Password errata");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e){
            System.out.println(ris);
        }

    }

    //todo: 18/05 funziona
    public void stmpPalinsesto(){
        String response = postRequest(address + "stampaPalinsesto.php", "");
        response = response.replace("{","").replace("/", "").replace('}','\n').replace('"',' ').replace(","," ");
        System.out.println(response);
        postiDisp();
    }

    //todo: 23/05 funziona
    public void postiDisp(){
        JSONObject json_receive;
        try {
            String response = postRequest(address + "stampa_posti_liberi_spettacolo.php", "");
            ris = reciveParser(response);
            if (ris.equals("Y")){
                json_receive = (JSONObject) p.parse(response);
                System.out.println("Spettacolo: "+json_receive.get("id")+" | Posti liberi: "+json_receive.get("posti_liberi")+"\n");
            }else{
                System.out.println(ris);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //todo: 24/05 funziona
    public void changePassword(){
        json.put("cmd","ch_token");
        try{
            System.out.println("Inserire il proprio username: ");
            msg = tastiera.readLine();
            json.put("username",msg);

            System.out.println("Inserire il token ricevuto via email");
            msg = tastiera.readLine();
            json.put("token",msg);

            ris = reciveParser(postRequest(address +"mod_password.php", json.toJSONString()));

            if(ris.equals("Y")){
                json.put("cmd","ch_pass");
                System.out.println("Inserire la nuova password: ");
                msg = tastiera.readLine();
                json.put("new_pass", msg);

                ris = reciveParser(postRequest(address +"mod_password.php", json.toJSONString()));
                if(ris.equals("Y")){
                    System.out.println("Aggiornamento password effettuato!");
                }else if(ris.equals("N")){
                    System.out.println("Errore aggiornamento password!");
                }
            }else if(ris.equals("N")){
                System.out.println("Credenziali errate!");
            }else{
                System.out.println(ris);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //todo: 24/05 funziona
    public void recuperaPasword(){
        try {
            String username = "", email = "";
            //menu dati richiesti per la modifica della password
            System.out.println("Per recuperare la tua password inserisci:" +
                    "\nUsername > ");
            username = tastiera.readLine();
            //inserimento username nel json
            json.put("username", username);
            System.out.println("\nEmail > ");
            email = tastiera.readLine();
            //inserimento email nel json
            json.put("email", email);
            ris = reciveParser(postRequest(address + "change_password.php", json.toJSONString()));
            if (ris.equals("Y")) {
                System.out.println("Email inviata!");
            } else if (ris.equals("N")) {
                System.out.println("Utente inesistente!");
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }

    //todo: 25/05 funziona
    public void deleteAccount(){
        try{
            System.out.println("Vuoi veramente eliminare l'account?");
            System.out.println("Scelta> Y/N");
            msg = tastiera.readLine();

            if (msg.equals("Y")){
                System.out.println("Inserire l'email dell'account: ");
                msg = tastiera.readLine();
                json.put("email", msg);
                ris = reciveParser(postRequest(address+"delete_account.php", json.toJSONString()));

                if(ris.equals("Y")){
                    System.out.println("Email inviata!");
                }else if(ris.equals("N")){
                    System.out.println("Errore invio email!");
                }
            }else{
                System.out.println("Procedura annullata...");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // -------------------- DA TESTARE ----------------------------

    //funziona 25/05
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
            ris = reciveParser(postRequest(address_admin +"nuovo_film.php", json.toJSONString()));

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

    // da aggiungere sul server
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

    //funziona 25/05
    public void eliminazioneFilm(){
        ricercaFilm();
        try{
            System.out.println("\n Inserire il codice del film da eliminare: ");
            String msg = tastiera.readLine();
            json.put("cod_film", msg);
            json.put("cmd", "checkFilm");

            ris = reciveParser(postRequest(address_admin +"delete_film.php", json.toJSONString()));
            if (ris.equals("Y")){
                json.put("cmd", "del_film");
                ris = reciveParser(postRequest(address_admin +"delete_film.php", json.toJSONString()));
                if (ris.equals("N")){
                    System.out.println("Il film è presente in uno spettacolo attivo");
                }else if (ris.equals("Y")){
                    System.out.println("Film eliminato");
                }else{
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Film inesistente");
            }else{
                System.out.println(ris);
            }

        }catch (IOException e) {
            System.out.println("Errore eliminazione del film");
        }
    }

    //Funziona 24-05
    public void ricercaFilm(){
        JSONObject json_receive;
        try{
            System.out.println("Inserire il nome del film");
            String msg = tastiera.readLine();
            json.put("nome_film", msg);

            String response = postRequest(address+"stampa_film.php", json.toJSONString());
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

    //funziona 24/05
    public void inserimentoSala(){
        try {
            System.out.println("Inserire il nome della sala: ");
            msg = tastiera.readLine();
            json.put("nome", msg);

            System.out.println("Inserire la dimensione della sala (massimo 3 cifre): ");
            msg = tastiera.readLine();
            json.put("dim_sala", msg);

            //nome file destinazione da rivedere (nuova_sala.php)
            ris = reciveParser(postRequest(address_admin +"nuovo_sala.php", json.toJSONString()));

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

    //
    public void modificaSala(){
        try{
            System.out.println("Inserire il nuovo nome della sala: ");
            msg = tastiera.readLine();
            json.put("nome", msg);
            json.put("cmd", "checkSala");

            System.out.println("Inserire la nuova dimensione della sala (massimo 3 cifre): ");
            msg = tastiera.readLine();
            json.put("dim_sala", msg);

            //nome file destinazione da rivedere (modifica_sala.php)
            ris = reciveParser(postRequest(address_admin +"change_sala.php", json.toJSONString()));

            if (ris.equals("Y")){
                json.put("cmd", "ch_sala");
                ris = reciveParser(postRequest(address_admin+"change_sala.php", json.toJSONString()));
                if (ris.equals("Y")){

                }else {

                }
            }else if (ris.equals("N")){
                System.out.println("Sala inesistente");
            }else {
                System.out.println(ris);
            }
        } catch (IOException e) {
            System.out.println("Errore nella modifica della sala");
        }
    }

    //campi mancanti
    public void eliminazioneSala(){
        try{
            System.out.println("Inserire il nome della sala da eliminare: ");
            String msg = tastiera.readLine();
            json.put("nome", msg);

            //nome file destinazione da rivedere (eliminazione_sala.php)
            ris = reciveParser(postRequest(address_admin +"delete_sala.php", json.toJSONString()));

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

    //Errore di conversione
    public void inserimentoSpettacolo(){
        try {
            System.out.println("Inserire la data e l'orario yyyy-mm-dd hh:mm:ss : ");
            String msg = tastiera.readLine();
            json.put("data ora", msg);

            System.out.println("Inserire i posti occupati dallo spettacolo: ");
            msg = tastiera.readLine();
            json.put("p occupati", msg);

            //nome file destinazione da rivedere (nuovo_spettacolo.php)
            ris = reciveParser(postRequest(address_admin+"nuovo_spettacolo.php", json.toJSONString()));

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

    //da controllare
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

    // da controllare
    public void eliminaPalinsesto(){
        try {
            //nome file destinazione da rivedere (delete_palinsesto.php)
            ris = reciveParser(postRequest(address_admin +"delete_palinsesto.php", ""));

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
