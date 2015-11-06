package moi2;

/**
 * Created by Alice on 04/11/2015.
 */
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private static Socket clientSocket = null;
    private static PrintStream sortie = null;
    private static DataInputStream entree = null;

    private static BufferedReader lecture = null;
    private static boolean closed = false;

    public static void main(String[] args) {

        int portNumber = 483;
        String host = "localhost";

    /* On ouvre la socket et on met en place l'�mission et la r�ception */
        try {
            clientSocket = new Socket(host, portNumber);
            lecture = new BufferedReader(new InputStreamReader(System.in));
            sortie = new PrintStream(clientSocket.getOutputStream());
            entree = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("host inconnu");
        } catch (IOException e) {
            System.err.println("Connexion impossible");
        }

        if (clientSocket != null && sortie != null && entree != null) {
            try {

                // Cr�ation du thread de lecture depuis le serveur
                new Thread(new Client()).start();
                // Tant que la connexion n'est pas ferm�e, on lit
                while (!closed) {
                    sortie.println(lecture.readLine().trim());
                }

                // Quand la connexion se ferme, on ferme tout
                sortie.close();
                entree.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    public void run() {
        String responseLine;
        try {
            // On continue � lire tant que "Quit" n'est pas re�u
            while ((responseLine = entree.readLine()) != null) {
                System.out.println(responseLine);
                if (responseLine.indexOf("Quit") != -1)
                    break;
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
