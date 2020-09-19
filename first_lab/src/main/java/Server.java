import java.net.ServerSocket;

public class Server {
    private final int port;
    private ServerSocket ss;

    public Server( int port) throws Exception {
        this.port = port;
        try {
            ss = new ServerSocket(port);
            System.out.println("Server created!");

        }catch (Exception e){
            System.out.println("Eror! Server not created");
            throw e;

        }
    }

    public void start(){

    }
}
