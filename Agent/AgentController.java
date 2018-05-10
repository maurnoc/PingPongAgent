import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;

public class AgentController implements Runnable {

    private LinkedList<Byte> pings = new LinkedList<>();
    private LinkedList<Byte> pongs = new LinkedList<>();

    boolean isRunning = true;
    private static int THREE = 3;
    String ip = getLAN();
    byte type;

    private HashMap<Byte, String> allIP = new HashMap<>();
    private HashMap<Byte, Integer> allPorts = new HashMap<>();

    AgentController(byte type) {
        this.type = type;
    }

    @Override
    public void run () {
        
        
        try {
            DatagramSocket DataSocket = null;
            DatagramPacket DataPacket;
            Thread thread = null;
            
            int Out_Ping_Port = 8081;
            int In_Pong_Port = 8080;
            int pingRerouted = 4443;
            int pongRerouted = 4444;
            
            byte ID_UNIQ_Pong = (byte) (1);
            byte ID_UNIQ_Ping = (byte) (2);
            byte ID_UNIQ = (byte) (10);
            pings.add(ID_UNIQ_Ping);
            pongs.add(ID_UNIQ_Pong);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run () {
                    kill();
                }
            });

            //hard coded pong value;; change when put in other vm
            allIP.put(ID_UNIQ_Pong, this.ip); //pong ports
            allPorts.put(ID_UNIQ_Pong, 8080);
           pongs.add(ID_UNIQ_Pong);
            ///hard coded ping values
            allIP.put(ID_UNIQ_Pong, this.ip); //ping prots
            allPorts.put(ID_UNIQ_Pong, 8081);
           pings.add(ID_UNIQ_Pong);


            if (type == 1) {
                ID_UNIQ = ID_UNIQ_Ping;
                DataSocket = new DatagramSocket(Out_Ping_Port); //mayeb 
                thread = new Thread(new PingAgent(DataSocket, this.ip, pingRerouted, ID_UNIQ_Ping));
                DataSocket = new DatagramSocket(pingRerouted);
            } else if (type == 0) {
                ID_UNIQ =ID_UNIQ_Pong;
                DataSocket = new DatagramSocket(In_Pong_Port); //switch
                thread = new Thread(new PongAgent(DataSocket, this.ip, pongRerouted, ID_UNIQ_Pong));
                DataSocket = new DatagramSocket(pongRerouted);
            }
            if (thread == null) {
                System.err.println("Null....");
                System.exit(3);
            }
            allIP.put(ID_UNIQ, this.ip);
            thread.start();

            while (this.isRunning) {
                if (Thread.interrupted())
                    kill();

                try {

                    DataPacket = new DatagramPacket(new byte[THREE], THREE);
                    DataSocket.receive(DataPacket);
                    byte[] data = DataPacket.getData();
                    InetAddress host = InetAddress.getByName(allIP.get(data[1]));
                    Integer port = allPorts.get(data[1]);
                    if (data[2] == (byte) 1) {
                        DataPacket = new DatagramPacket(data, data.length, host, port);
                    }
                    if (data[2] == (byte) 0) {
                        DataPacket = new DatagramPacket(data, data.length, host, port);
                    }

                    DataSocket.send(DataPacket);
                    DataPacket.setData(new byte[THREE]);
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
            thread.join();
            DataSocket.close();
        } catch (InterruptedException | IOException err) {
            err.printStackTrace();
        }
    }

    private String getLAN () {
        int inc = 0;
        String IP = "";
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface NetInterface = networkInterfaces.nextElement();

                if (!NetInterface.isUp() || NetInterface.isLoopback() || NetInterface.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> INETaddress = NetInterface.getInetAddresses();
                while (INETaddress.hasMoreElements()) {
                    InetAddress thisAdress = INETaddress.nextElement();
                    if (thisAdress.isLoopbackAddress()) {
                        continue;
                    }
                    if (thisAdress instanceof Inet4Address && inc == 0) {
                        IP = thisAdress.getHostAddress();
                        inc++;
                        break;
                    }
                }
            }
        } catch (SocketException err) {
            err.printStackTrace();
        }
        return IP;
    }

    void kill () {
        this.isRunning = false;
    }

}
