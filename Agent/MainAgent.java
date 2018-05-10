
public class MainAgent {

    public static void main (String[] args) {

        byte type = 8;

        if (args.length != 1) {
            System.exit(1);
        } else {
            if (args[0].equals("PingAgent")) {
                type = 1;
            }
            if (args[0].equals("PongAgent")) {
                type = 0;
            }

        }
        if (type == 8) {
            System.err.println("Only Ping and Pong work right now");
            System.exit(1);
        }

        final Thread thread = new Thread(new AgentController(type));
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException err) {
            err.printStackTrace();
        }
    }
}
