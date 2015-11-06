package Client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Alice on 04/11/2015.
 */
public class ThreadPourClient extends Thread {

    private DataInputStream entree = null;
    private PrintStream sortie = null;
    private Socket clientSocket = null;
    private ThreadPourClient[] client_connecte;
    private int maxClient;
    private String id;

    //region Contructeur et Accesseurs
    public ThreadPourClient(Socket clientSocket, ThreadPourClient[] client_connecte) {
        this.clientSocket = clientSocket;
        this.client_connecte = client_connecte;
        maxClient = client_connecte.length;
    }

    public String getNom()
    {
        return id;
    }
    //endregion

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
        int maxClientsCount = this.maxClient;
        ThreadPourClient[] client_connecte = this.client_connecte;
        boolean enligne=true;

        try {

            // création des entrées sorties
            entree = new DataInputStream(clientSocket.getInputStream());
            sortie = new PrintStream(clientSocket.getOutputStream());

            //region Processus d'inscription
            boolean inscriptionIncorrect;
            do {
                sortie.println("S:Entrez votre identifiant");
                id = entree.readLine();

                inscriptionIncorrect = false;
                for(int i=0; i<client_connecte.length; i++)
                {
                    if (client_connecte[i] != null && client_connecte[i] != this)
                    {
                        if (((client_connecte[i].getNom()).equals(id)))
                        {
                            inscriptionIncorrect = true;
                        }
                    }
                }
                if(inscriptionIncorrect)
                    sortie.println("Cette identifiant est d\u00e9j\u00e0 utilis\u00e9");

            }while(inscriptionIncorrect);
            //endregion

            //Début des conversations
            sortie.println("S:Bienvenue " + id + ".");
            sortie.println("S:Pour quitter la messagerie, S:QUIT");
            sortie.println("S:Pour voir qui est connect\u00e9, taper S:WHO");
            sortie.println("S:Pour envoyer un message à une autre personne, taper POUR_QUI:MESSAGE");

            // Pour chaque personne déjà connecté, on affiche l'arrivée du nouveau
            for (int i = 0; i < maxClientsCount; i++)
            {
                if (client_connecte[i] != null && client_connecte[i] != this) {
                    client_connecte[i].sortie.println("S:" + id + " viens de se connecter");
                }
            }

            while(enligne)
            {
                // On récupère le message que le client veut envoyer
                String ligne = entree.readLine();
                boolean pourServeur = false;

                //region message serveur
                // Si S:QUIT, on déconnecte
                if (ligne.equals("S:QUIT")) {
                    enligne = false;
                    pourServeur = true;
                    break;
                }
                // Si S:WHO, on affiche la liste des personnes connectées
                if(ligne.equals("S:WHO"))
                {
                    pourServeur = true;
                    sortie.println("Clients conntect\u00e9s : ");
                    for (int i = 0; i < maxClientsCount; i++)
                    {
                        if (client_connecte[i] != null && client_connecte[i]!=this)
                        {
                            sortie.println(client_connecte[i].getNom());
                        }
                    }
                    sortie.println("S:Pour quitter la messagerie, S:QUIT");
                    sortie.println("S:Pour voir qui est connect\u00e9, taper S:WHO");
                    sortie.println("S:Pour envoyer un message à une autre personne, taper POUR_QUI:MESSAGE");
                }
                //endregion

                //region envoie message
                /* Sinon, on découpe le message :
                   lignesplit[0] = le destinataire
                   lignesplit[1] = le message
                 */
                String[] lignesplit = ligne.split(":");
                boolean remiseMessage = false;

                if((lignesplit.length == 1) || ((lignesplit.length>1)&&lignesplit[0].equals("")))
                {
                    sortie.println("S:Mauvaise synthaxe !");
                    sortie.println("S:Pour envoyer un message à une autre personne, taper POUR_QUI:MESSAGE");
                }

                if(enligne && (pourServeur == false) && (remiseMessage == false))
                {

                    // On l'affiche chez chaque client connecté
                    for (int i = 0; i < maxClientsCount; i++)
                    {
                        if (client_connecte[i] != null)
                        {
                            if ((client_connecte[i].getNom()).equals(lignesplit[0]))
                            {
                                client_connecte[i].sortie.println(id + " dit : " + lignesplit[1]);
                                remiseMessage = true;

                            }
                        }
                    }
                    //Si le message n'a pas été remis
                    if(remiseMessage == false)
                    {
                        sortie.println("S:Ce destinataire n'existe pas ou n'est pas connecté.");
                    }
                }
                //endregion
            }

            // On annonce la deconnexion à chaque utilisateur
            for (int i = 0; i < maxClientsCount; i++) {
                if (client_connecte[i] != null && client_connecte[i] != this) {
                    client_connecte[i].sortie.println("S:" + id + " est deconnect\u00e9");
                }
            }
            // le serveur dit au revoir !
            sortie.println("S: Vous \u00eates d\u00e9connect\u00e9");
            sortie.println("Quit");

            // On lib&egrave;re le thread pour qu'il puisse &ecirc;tre r&eacute;utilis&eacute;.
            for (int i = 0; i < maxClientsCount; i++) {
                if (client_connecte[i] == this) {
                    client_connecte[i] = null;
                }
            }

            // On ferme tout
            entree.close();
            sortie.close();
            clientSocket.close();

        } catch (IOException e)
        {
            System.err.println(e); 
        }
    }
}
