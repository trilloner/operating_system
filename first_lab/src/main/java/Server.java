/**
 * Operating system. Lab1. Variant 5
 *
 * @author Bogdan Volokhonenko
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Non blocking Server class
 */
public class Server {
    private ServerSocketChannel socketServer;
    private InetSocketAddress address;//address
    private final int variant;
    private boolean calculateEnable = true;
    private int maxConnections;
    private final HashMap<Integer, String> results = new HashMap<>();
    private ArrayList<Process> clientProcesses = new ArrayList<>();

    /**
     * Constructor
     *
     * @param host
     * @param port
     * @param variant
     * @param maxConnections
     */
    public Server(String host, int port, int variant, int maxConnections) {
        this.address = new InetSocketAddress(host, port);
        this.variant = variant;
        this.maxConnections = maxConnections;
    }

    /**
     * Function of running the server
     */
    public void run() throws IOException {
        try {
            socketServer = ServerSocketChannel.open();
            socketServer.configureBlocking(false);
            socketServer.socket().bind(address);

            System.out.println("Server started!");

            //starting clients
            startClients(this.variant);


            while (calculateEnable) {

                //blocking wait for events
                SocketChannel channel = socketServer.accept(); // getting channel
                if (channel != null) {

                    //server operations (writing and reading from socket)

                    handle(channel);

                    //checking connections
                    if (this.maxConnections == 0) {
                        this.calculateEnable = false;
                    }
                }




            }

        } catch (IOException | InterruptedException e) {
          close();
        }
    }
    
    /**
     * The function of starting processes
     *
     */
    private void startProcess(String s) throws InterruptedException{
        try{
            ProcessBuilder builder = new ProcessBuilder("java", "-jar", "C:\\Users\\mamko\\Documents\\GitHub\\operating_system\\first_lab\\out\\artifacts\\client_" + s +"_jar\\client-" + s +".jar", String.valueOf(this.variant));

            Process process = builder.start();
            clientProcesses.add(process);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * The function of working with clients
     *
     *
     */
    private void startClients(int variant) throws IOException, InterruptedException {
        //connect and run our server-clients
        startProcess("f");
        startProcess("g");
    }

    /**
     * Function of sending message to the client
     *
     * @param socket
     */
    private void sendMessage(SocketChannel socket){
        try{
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            String s = String.valueOf(this.variant);
            byteBuffer.put(s.getBytes());
            byteBuffer.flip();
            socket.write(byteBuffer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Function of working with the socket
     *
     * @param socket
     */
    private void handle(SocketChannel socket) throws IOException {
        try {
            sendMessage(socket);

            read(socket);

        } catch (IOException e) {
          this.close();
        }
    }

    /**
     * Function that reading message from client
     *
     * @throws IOException
     */
    private void read(SocketChannel socket) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = socket.read(byteBuffer);
        if (numRead == -1) {
            socket.close();
            return;
        }
        //creating byte array for message
        byte[] data = new byte[numRead];
        System.arraycopy(byteBuffer.array(), 0, data, 0, numRead);
        String gotData = new String(data);
        System.out.println("Got:" + gotData);
        analyzeResult(gotData);
    }

    /**
     * Function that analyzing result from client
     *
     * @param result
     */
    private void analyzeResult(String result) {
        String[] args = result.split(" ");
        if (args.length < 2)
            return;
        int value = Integer.parseInt(args[0]);

        if (value == 0) {
            this.calculateEnable = false;
            System.out.println("Another client's calculations were canceled");
        }

        String name = args[1];

        this.results.put(value, name);
        maxConnections--;

    }

    /**
     * Getter for hashmap (my results)
     *
     * @return
     */
    public HashMap<Integer, String> getResults() {
        return results;
    }

    /**
     * Function that calculate result
     *
     * @return
     */
    public int getMultiplication() {
        int result = 1;
        Set<Integer> keys = this.results.keySet();
        for (int s : keys) {
            if (s == 0) return 0;
            else result = s * result;
        }
        return result;

    }

    /**
     * Function that closing server
     */
    public void close()  {
        try{
            for (Process proc : clientProcesses) {
                proc.destroy();
            }
            socketServer.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
