/**
 * Operating system. Lab1. Variant 5
 *
 * @author Bogdan Volokhonenko
 */

import sun.rmi.runtime.Log;

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
    private ArrayList<Thread> clientThreads = new ArrayList<>();

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
    public void run() {
        try {
            socketServer = ServerSocketChannel.open();
            socketServer.configureBlocking(false);
            socketServer.socket().bind(address);

            System.out.println("Server started!");
            startClients(this.variant);

            while (calculateEnable) {
                //blocking wait for events
                SocketChannel channel = socketServer.accept(); // getting channel

                //server operations (writing and reading from socket)
                handle(channel);

                //checking connections
                if (this.maxConnections <= 0) {
                    this.calculateEnable = false;
                }

            }
            socketServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The function of working with clients
     *
     * @param variant
     */
    private void startClients(int variant) throws IOException {
        //connect and run our server-clients
        if (variant == 1 || variant == 3 || variant==5){
            ClientFx fx = new ClientFx();
            ClientGx gx = new ClientGx();
            fx.start();
            gx.start();
            clientThreads.add(fx);
            clientThreads.add(gx);

        }else{
            ClientGx gx = new ClientGx();
            ClientFx fx = new ClientFx();
            gx.start();
            fx.start();
            clientThreads.add(gx);
            clientThreads.add(fx);
        }
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
    private void handle(SocketChannel socket){
        try {
           sendMessage(socket);

            read(socket);

        } catch (IOException e) {
            e.printStackTrace();
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
    public void close() throws IOException {
        try{
            for (Thread thread : clientThreads) {
                thread.stop();
            }
            socketServer.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
