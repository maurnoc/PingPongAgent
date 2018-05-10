import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Agent {
    byte myID;
    byte destinationID;
    byte typeVal;
    private DatagramSocket socket;
    boolean isRunning = true;
    private String host;
    private int port;

    Agent(DatagramSocket socket, String host, int port, byte myID) {
        this.socket = socket;
        this.host = host;
        this.port = port;
        this.myID = myID;
    }

    void send (byte[] m) {
        if (this.socket == null) {
            System.err.println("Null error");
            System.exit(1);
        }

        try {
            InetAddress hostAdress = InetAddress.getByName(this.host);
            DatagramPacket DatPacket = new DatagramPacket(m, m.length, hostAdress, this.port);
            this.socket.send(DatPacket);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    byte[] recieve () {
        try {
            byte[] buf = new byte[3];
            DatagramPacket DatPacket = new DatagramPacket(buf, buf.length);
            this.socket.receive(DatPacket);
            return DatPacket.getData();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }

    void kill () {
        this.isRunning = false;
    }

}
