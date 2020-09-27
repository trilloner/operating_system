import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server {
    private Selector selector; //multiplexer
    private InetSocketAddress address;//address
    private final int variant;
    private boolean calculateEnable = true;
    private int maxConnections;
    private final HashMap<Integer,String> results = new HashMap<>();
    private ArrayList<Thread> clientThreads = new ArrayList<>();

    public Server(String host, int port, int variant, int maxConnections) {
        this.address = new InetSocketAddress(host, port);
        this.variant = variant;
        this.maxConnections = maxConnections;
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



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



    }


    private void broadcast(String data, SelectionKey key) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(data.getBytes());
        byteBuffer.flip();
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(byteBuffer);

    }

            e.printStackTrace();
        }
    }

}

