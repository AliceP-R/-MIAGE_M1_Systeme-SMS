package moi2;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Alice on 04/11/2015.
 */
public class Server {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;

    private static final int maxClient = 10;
    private static final ClientThread[] threads = new ClientThread[maxClient];

    public static void main(String args[]) {


        int port = 483;
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cr�ation d'une nouvelle socket � chaque connexion et liaison � une nouvelle thread
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                // on v�rifie qu'il reste de la place
                int i;
                for (i = 0; i < maxClient; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new ClientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                if (i == maxClient) {
                    PrintStream sortie = new PrintStream(clientSocket.getOutputStream());
                    sortie.println("Capacit� max du serveur atteinte.");
                    sortie.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}