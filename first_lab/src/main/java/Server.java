/**
 * Operating system. Lab1. Variant 6
 *
 * @author Bogdan Volokhonenko
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Non blocking Server class
 */
public class Server {
    private Selector selector; //multiplexer
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
     * Function that running server
     */
    public void run() {
        try {
            this.selector = Selector.open(); // init selector
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false); // non blocking
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT); // register selector and type of event

            ClientGx gx = new ClientGx();
            ClientFx fx = new ClientFx();
            gx.start();
            fx.start();
            clientThreads.add(gx);
            clientThreads.add(fx);
            System.out.println("Server started!");

            while (calculateEnable) {
                //blocking wait for events
                this.selector.select();
                Iterator keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = (SelectionKey) keys.next();
                    keys.remove();
                    if (!key.isValid()) continue;
                    if (key.isAcceptable()) accept(key);
                    else if (key.isReadable()) read(key);
                }

                if (this.maxConnections <= 0) {
                    this.calculateEnable = false;
                }

            }
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that accepting connections
     *
     * @param key
     * @throws IOException
     */
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept(); // getting channel
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_WRITE);

        String s = String.valueOf(this.variant);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(s.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    /**
     * Function that reading message from client
     *
     * @param key
     * @throws IOException
     */
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = channel.read(byteBuffer);
        if (numRead == -1) {
            channel.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(byteBuffer.array(), 0, data, 0, numRead);
        String gotData = new String(data);
        System.out.println("Got:" + gotData);
        analyzeResult(gotData);
        channel.register(this.selector, SelectionKey.OP_WRITE);
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
    public void close() {
        try {
            this.selector.close();
            for (Thread thread : clientThreads) {
                thread.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
