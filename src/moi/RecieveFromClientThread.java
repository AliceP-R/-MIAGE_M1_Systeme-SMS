package moi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Alice on 02/11/2015.
 */
class RecieveFromClientThread implements Runnable
{
    Socket clientSocket=null;
    BufferedReader brBufferedReader = null;

    public RecieveFromClientThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }//end constructor
    public void run() {
        try{
            // liaison sur la sortie du client (InputStream)
            brBufferedReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            String messageString;

            // Tant que le client n'a pas EXIT, on continue à lire
            while(true){
                System.out.println("while RecieveFromClientThread");
                while((messageString = brBufferedReader.readLine())!= null){//assign message from client to messageString
                    if(messageString.equals("EXIT"))
                    {
                        break;//break to close socket if EXIT
                    }
                    System.out.println("From Client: " + messageString);//print the message from client
                    System.out.println("Please enter something to send back to client..");
                }
                this.clientSocket.close();
                System.exit(0);
            }

        }
        catch(Exception ex){System.out.println(ex.getMessage());}
    }
}//end class RecieveFromClientThread
