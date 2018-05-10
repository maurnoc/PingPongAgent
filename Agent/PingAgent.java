import java.net.DatagramSocket;
import java.util.HashMap;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class PingAgent extends Agent implements Runnable {

    private HashMap<Byte, String> listForPongs = new HashMap<>();

    PingAgent(DatagramSocket socket, String hostAdress, int portAdress, byte IDVal) {
        super(socket, hostAdress, portAdress, IDVal);
        this.typeVal = 1;
    }

    @Override
    public void run () {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run () {
                kill();
            }
        });

        System.out.println("PingAgent[id=" + this.myID + "]: Looking for PongAgents...");
        if (SearchAgent()) {
            for (Byte ID : listForPongs.keySet()) {
                this.destinationID = ID;
                System.out.println("PingAgent[id=" + this.myID + "]: Found PongAgent[id=" + this.destinationID + "]");
                System.out.println(
                        "PingAgent[id=" + this.myID + "]: Sending ping to PongAgent[id=" + this.destinationID + "]");
                this.send(new byte[] { this.myID, this.destinationID, this.typeVal });
            }
            for (int i = 0; i < listForPongs.size(); i++) {
                byte[] data = this.recieve();
                if (data[1] == this.myID && data[2] != this.typeVal)
                    System.out.println(
                            "PingAgent[id=" + this.myID + "]: Received pong from PongAgent[id=" + data[0] + "]");
            }

        } else
            System.out.println("Something is wrong you are alone");
    }

    private boolean SearchAgent () {
        // Still not done
          for(int ii=1;ii<255;ii++){
               try{
                    String newIP="10.0.0."+ii;
                    DatagramSocket DataSocket = new DatagramSocket(8080);
                        new Thread(new PongAgent(DataSocket, newIP, 4444, (byte)ii));
                        
                     
                }catch(Exception err){
                err.printStackTrace();
                }
          }
         //hard coded for example
        this.listForPongs.put((byte) 1, "10.0.0.125");
        return true;
    }

}