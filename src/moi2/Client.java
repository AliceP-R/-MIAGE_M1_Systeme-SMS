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

        if (args.length < 2) {
            System.out
                    .println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
                            + "Now using host=" + host + ", portNumber=" + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

    /*
     * Open a socket on a given host and port. Open input and output streams.
     */
        try {
            clientSocket = new Socket(host, portNumber);
            lecture = new BufferedReader(new InputStreamReader(System.in));
            sortie = new PrintStream(clientSocket.getOutputStream());
            entree = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host "
                    + host);
        }

    /*
     * If everything has been initialized then we want to write some data to the
     * socket we have opened a connection to on the port portNumber.
     */
        if (clientSocket != null && sortie != null && entree != null) {
            try {

        /* Create a thread to read from the server. */
                new Thread(new Client()).start();
                while (!closed) {
                    sortie.println(lecture.readLine().trim());
                }
        /*
         * Close the output stream, close the input stream, close the socket.
         */
                sortie.close();
                entree.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    /*
     * Create a thread to read from the server. (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
    /*
     * Keep on reading from the socket till we receive "Bye" from the
     * server. Once we received that then we want to break.
     */
        String responseLine;
        try {
            while ((responseLine = entree.readLine()) != null) {
                System.out.println(responseLine);
                if (responseLine.indexOf("*** Bye") != -1)
                    break;
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
