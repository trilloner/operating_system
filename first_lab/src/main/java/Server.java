import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    private Selector selector; //multiplexer
    private InetSocketAddress address;//address

    public Server(String host, int port) {
        this.address = new InetSocketAddress(host, port);
    }

    public void start() throws IOException {
        this.selector = Selector.open(); // init selector
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(address);
        serverSocketChannel.configureBlocking(false); // non blocking
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT); // register selector and type of event

        System.out.println("Server started!");

        while (true) {
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

        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept(); // getting channel
        channel.configureBlocking(false);
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
        System.out.println("Got: " + gotData);
    }

    private void broadcast(String data, SelectionKey key) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(data.getBytes());
        byteBuffer.flip();
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(byteBuffer);
            byteBuffer.flip();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

