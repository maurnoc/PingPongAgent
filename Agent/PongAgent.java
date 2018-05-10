import java.net.DatagramSocket;

public class PongAgent extends Agent implements Runnable {

    PongAgent(DatagramSocket socket, String hostAdress, int portAdress, byte IDVal) {
        super(socket, hostAdress, portAdress, IDVal);
        this.typeVal = 0;
    }

    @Override
    public void run () {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run () {
                kill();
            }
        });

        System.out.println("PongAgent[id=" + this.myID + "]: Waiting for pings...");

        while (this.isRunning) {
            if (Thread.interrupted())
                kill();
            byte[] returnedInfo = this.recieve();
            if (returnedInfo[1] == this.myID && returnedInfo[2] != this.typeVal) {
                this.destinationID = returnedInfo[0];
                System.out.println(
                        "PongAgent[id=" + this.myID + "]: Received ping from PingAgent[id=" + this.destinationID + "]");
                System.out.println(
                        "PongAgent[id=" + this.myID + "]: Sending pong to PingAgent[id=" + this.destinationID + "]");
                this.send(new byte[] { this.myID, this.destinationID, this.typeVal });

            }
        }

    }
}