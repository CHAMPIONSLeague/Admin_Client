import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        Funzioni funzione = new Funzioni();
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        JSONObject json_send = new JSONObject(); //json utilizzato per inviare dati all'api interessata
        String risp = "";

        int scelta = -1, m_user = -1, m_film = -1, m_palin = -1, m_sala = -1;

        while(scelta != 0){
            try{
                System.out.println("1. Login");
                System.out.println("2. Password dimenticata?");
                System.out.println("0. Exit");
                scelta = Integer.parseInt(tastiera.readLine());

                switch (scelta) {
                    case 0 -> System.out.println("Client in chiusura...");
                    case 1 -> {
                        risp = funzione.login();
                        if (risp.equals("logged")) {

                            //TODO: Menu utente
                            while (m_user != 0) {
                                System.out.println("1. Menu Palinsesto");
                                System.out.println("2. Menu film");
                                System.out.println("3. Menu sala");
//                                System.out.println("4. Cancella prenotazione");
//                                System.out.println("5. Stampa prenotazioni");
                                System.out.println("6. Cambio username");
                                System.out.println("7. Cambio email");
                                System.out.println("8. Eliminazione Account");
                                System.out.println("0. Exit");

                                m_user = Integer.parseInt(tastiera.readLine());
                                switch (m_user) {
                                    case 0 -> System.out.println("LOGOUT");
                                    case 1 -> {
                                        while(m_palin != 0){
                                            System.out.println("1. Modifica palinsesto");
                                            System.out.println("2. Delete palinsesto");
                                            System.out.println("3. Stampa palinsesto");
                                            System.out.println("0. EXIT");
                                            m_palin = Integer.parseInt(tastiera.readLine());
                                            switch (m_palin){
                                                default -> System.out.println("Comando inesistente");
                                                case 1 -> funzione.modificaPalinsesto();
                                                case 2 -> funzione.eliminaPalinsesto();
                                                case 3 -> funzione.stmpPalinsesto();
                                            }
                                        }
                                    }//menu palinsesto
                                    case 2 -> {
                                        while(m_film != 0){
                                            System.out.println("1. Inserimento Film");
                                            System.out.println("2. Modifica Film");
                                            System.out.println("3. Delete Film");
                                            System.out.println("4. Ricerca Film");
                                            System.out.println("0. EXIT");
                                            m_film = Integer.parseInt(tastiera.readLine());
                                            switch (m_film){
                                                default -> System.out.println("Comando inesistente");
                                                case 1 -> funzione.inserimentoFilm();
                                                case 2 -> funzione.modificaFilm();
                                                case 3 -> funzione.eliminazioneFilm();
                                                case 4 -> funzione.ricercaFilm();
                                            }
                                        }
                                    }//menu film
                                    case 3 -> {
                                        while(m_sala != 0){
                                            System.out.println("1. Nuova Sala");
                                            System.out.println("2. Modifica Sala");
                                            System.out.println("3. Delete Sala");
                                            System.out.println("4. Stampa posti disponibili nelle sale");
                                            System.out.println("0. EXIT");
                                            m_sala = Integer.parseInt(tastiera.readLine());
                                            switch (m_sala){
                                                default -> System.out.println("Comando inesistente");
                                                case 1 -> funzione.inserimentoSala();
                                                case 2 -> funzione.modificaSala();
                                                case 3 -> funzione.eliminazioneSala();
                                                case 4 -> funzione.postiDisp();
                                            }
                                        }
                                    }//menu sala
//                                    case 4 ->
//                                    case 5 ->
                                    case 6 -> funzione.changeUser();
                                    case 7 -> funzione.changeEmail();
                                    case 8 -> {
                                        funzione.deleteAccount();
                                        m_user = 0;
                                    }
                                    default -> System.out.println("Comando non valido");
                                }
                            }
                        } else if (risp.equals("admin logged")) {
                            return;
                        }
                    }
                    case 2 -> {
                        System.out.println("""
                                Gestione/Recupero password
                                1.Modifica password
                                2.Richiedi la modifica della password
                                """);
                        int func_pass = Integer.parseInt(tastiera.readLine());
                        switch (func_pass){
                            case 1 -> funzione.changePassword();
                            case 2 -> funzione.recuperaPasword();
                        }
                    }
                    default -> System.out.println("Comando non valido");
                }
            }catch (Exception e){
                System.out.println("Comando non valido");
            }
        }
    }
}