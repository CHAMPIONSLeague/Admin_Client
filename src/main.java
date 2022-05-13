import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        Funzioni funzione = new Funzioni();
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        JSONObject json = new JSONObject(); //json utilizzato per inviare dati all'api interessata
        String risp = "";

        int scelta = -1;
        while (scelta != 0){

            risp = funzione.login();

            System.out.println(risp);
            switch (risp){
                case "Credenziali errate":
                    scelta = -1; //fa rifare il login
                    break;
                case "YA":
                    //TODO: da qua creare il ciclo while e mettere il menu admin
                    break;
                case "YU":
                    return;
            }
        }
    }
}
