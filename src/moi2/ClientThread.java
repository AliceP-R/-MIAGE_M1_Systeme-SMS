package moi2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Alice on 04/11/2015.
 */
class ClientThread extends Thread {

    private BufferedReader entree = null;
    private PrintStream sortie = null;
    private Socket clientSocket = null;
    private ClientThread[] client_connecte;
    private int maxClientsCount;
    private ArrayList<ClientThread> l_client = new ArrayList<>();


    public String getNom() {
        return id;
    }

    private String id;

    public ClientThread(Socket clientSocket, ClientThread[] client_connecte) {
        this.clientSocket = clientSocket;
        this.client_connecte = client_connecte;
        maxClientsCount = client_connecte.length;
    }

    public ClientThread(Socket clientSocket, ArrayList<ClientThread> client)
    {
        this.l_client = client;
        this.clientSocket = clientSocket;

    }

    /* Pour savoir a qui remettre le message.
       le message remis par le client est :POUR_QUI:DE_QUI:MESSAGE
       name = POUR_QUI
       donc, on va parcourir la liste de connecté et si un id correspond, on envoie le message
     */
    private boolean remettreMessage(String name)
    {
        if(name.equals(id))
            return true;
        else
            return false;
    }
    public void run() {
        int maxClientsCount = this.maxClientsCount;
        ClientThread[] client_connecte = this.client_connecte;
        boolean enligne=true;

        try {
      /*
       * Create input and output streams for this client.
       */
            entree = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sortie = new PrintStream(clientSocket.getOutputStream());
            sortie.println("S:Entrez votre identifiant");
            id = entree.readLine();
            sortie.println("S:Bienvenue " + id + ".");
            sortie.println("S:Pour quitter la messagerie, S:QUIT.");
            sortie.println("S:Pour envoyer un message à une autre personne, taper POUR_QUI:MESSAGE.");

            // POur chaque personne déjà connecté, on affiche l'arrivée du nouveau
            for (int i = 0; i < maxClientsCount; i++) {
                if (client_connecte[i] != null && client_connecte[i] != this) {
                    client_connecte[i].sortie.println("S:" + id + " viens de se connecter:");
                }
            }
           while(enligne)
            {
                // On récupère le message que le client veut envoyer
                String line = entree.readLine();
                if (line.equals("S:QUIT")) {
                    enligne = false;
                }

                String[] lignesplit = line.split(":");

                if(enligne)
                {
                    // On l'affiche chez chaque client connecté
                    for (int i = 0; i < maxClientsCount; i++)
                    {
                        if (client_connecte[i] != null)
                        {
                            if (client_connecte[i].remettreMessage(lignesplit[0]))
                            {
                                client_connecte[i].sortie.println(id + " dit : " + lignesplit[1]);
                            }
                            else if(remettreMessage(lignesplit[0]) == false)
                            {
                                sortie.println("S:Ce destinataire n'existe pas.");
                            }
                        }
                    }
                }
            }

            // On annonce la deconnexion à chaque utilisateur
            for (int i = 0; i < maxClientsCount; i++) {
                if (client_connecte[i] != null && client_connecte[i] != this) {
                    client_connecte[i].sortie.println("S:" + id + " est deconnecté:");
                }
            }
            // le serveur dit au revoir !
            sortie.println(":S: Vous êtes déconnecté" + id + ":");

            // On libère le thread pour qu'il puisse être réutilisé.
            for (int i = 0; i < maxClientsCount; i++) {
                if (client_connecte[i] == this) {
                    client_connecte[i] = null;
                }
            }

            // On ferme tout
            entree.close();
            sortie.close();
            clientSocket.close();

        } catch (IOException e) {
        }
    }
}
