package comun;

import java.io.*;
import java.net.Socket;

public class ConexionUtil {
    public static final String HOST   = "192.168.1.10";
    public static final int    PUERTO = 1234;

    /** Pide el siguiente ticket del tipo indicado; devuelve null si no hay. */
    public static Ticket pedirSiguiente(Ticket.Tipo tipo)
            throws IOException, ClassNotFoundException {
        try (Socket s = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(s.getInputStream())) {

            out.writeUTF("NEXT");
            out.writeObject(tipo);
            out.flush();

            String resp = in.readUTF();
            if ("NEXT_OK".equals(resp)) {
                return (Ticket) in.readObject();
            } else {
                return null;
            }
        }
    }

    /** Marca como completado el ticket; devuelve true si el servidor responde COMPLETE_ACK. */
    public static boolean completarTicket(int numeroTicket)
            throws IOException {
        try (Socket s = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(s.getInputStream())) {

            out.writeUTF("COMPLETE");
            out.writeInt(numeroTicket);
            out.flush();

            String ack = in.readUTF();
            return "COMPLETE_ACK".equals(ack);
        }
    }

    /** Crea un ticket nuevo en el servidor y devuelve el objeto con número asignado. */
    public static Ticket crearTicket(Ticket tSinNumero)
            throws IOException, ClassNotFoundException {
        try (Socket s = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(s.getInputStream())) {

            out.writeUTF("CREATE");
            out.writeObject(tSinNumero);
            out.flush();

            String resp = in.readUTF();
            if ("CREATED".equals(resp)) {
                return (Ticket) in.readObject();
            } else {
                throw new IOException("CREATE rechazado: " + resp);
            }
        }
    }
    
    public static int contarEnEspera(Ticket.Tipo tipo)
            throws IOException, ClassNotFoundException {
        try (Socket s = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(s.getInputStream())) {

            out.writeUTF("COUNT");
            out.writeObject(tipo);
            out.flush();

            String resp = in.readUTF();
            if (! "COUNT_OK".equals(resp)) {
                throw new IOException("COUNT rechazado: " + resp);
            }
            return in.readInt();
        }
    }
    
}
