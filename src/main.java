import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        Funzioni funzione = new Funzioni();
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        JSONObject json_send = new JSONObject(); //json utilizzato per inviare dati all'api interessata
        String risp = "";
        String msg = "";

        int scelta = 100;
        try{
            System.out.println("Inserire l'username:");
            String user = tastiera.readLine();
            json_send.put("username", user);

            System.out.println("Inserire password:");
            msg = tastiera.readLine();
            json_send.put("password", msg);

            //fa il login richiamando il metodo postRequest
            risp = funzione.login(json_send);

            //se non trova l'account lo fa registrare
            if(risp.equals("N")){
                System.out.println("Account admin inesistente...");
            }else{
                System.out.println("Benvenuto "+user);
                while(scelta != 0){
                    try{
                        System.out.println("Scegli la tua prossima operazione: ");
                        System.out.println("1. ");
                        System.out.println("2. ");
                        System.out.println("0. Exit");
                        scelta = Integer.parseInt(tastiera.readLine());

                        //le funzioni mandano i dati al server, ma sanno gia' dove devono andare
                        //le post request avranno link specifici in base alla destinazione dei dati
                        //es. mando user e pass, nel post request le mando al login DAL CLIENT
                        switch (scelta){
                            case 0: //Chiusura
                                System.out.println("Client in chiusura...");
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
