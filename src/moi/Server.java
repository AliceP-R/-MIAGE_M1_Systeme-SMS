package moi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Created by Alice on 02/11/2015.
 */

public class Server
{
    private Hashtable<String, Socket> l_client = new Hashtable<>();
    private int maxConnexion = 10;

    public static void main(String[] args) throws IOException {
        final int port = 483;
        System.out.println("Serveur en attente de connexion sur le port "+port);
        // Création de la socket d'écoute
        ServerSocket ss = new ServerSocket(port);
        Socket clientSocket = ss.accept();
        System.out.println("Recieved connection from "+clientSocket.getInetAddress()+" on port "+clientSocket.getPort());
        //create two threads to send and recieve from client
        RecieveFromClientThread recieve = new RecieveFromClientThread(clientSocket);
        Thread thread = new Thread(recieve);
        thread.start();
        SendToClientThread send = new SendToClientThread(clientSocket);
        Thread thread2 = new Thread(send);
        thread2.start();
    }
}

