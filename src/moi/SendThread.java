package moi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Alice on 02/11/2015.
 */
class SendThread implements Runnable
{
    Socket sock=null;
    PrintWriter sortie=null;
    BufferedReader brinput=null;

    public SendThread(Socket sock)
    {
        this.sock = sock;
    }//end constructor
    public void run(){
        try{
            if(sock.isConnected())
            {
                System.out.println("Client connect\u00e9 sur la socket " +sock.getInetAddress() + " sur le port "+sock.getPort());
                // la sortie est sur l'output Stream de la socket
                this.sortie = new PrintWriter(sock.getOutputStream(), true);
                while(true){
                    System.out.println("Taper votre message sous la forme de  :POUR_QUI:DE_QUI:MESSAGE: ou EXIT pour quitter");
                    // Lecture du message
                    brinput = new BufferedReader(new InputStreamReader(System.in));
                    String msgtoServerString=null;
                    msgtoServerString = brinput.readLine();
                    // Envoie du message au serveur
                    this.sortie.println(msgtoServerString);
                    this.sortie.flush();

                    if(msgtoServerString.equals("EXIT"))
                        break;
                }//end while
                sock.close();}}catch(Exception e){System.out.println(e.getMessage());}
    }//end run method
}//end class
