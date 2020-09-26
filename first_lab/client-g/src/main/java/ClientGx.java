import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientGx {
    private static final int port = 2020;

    private static final int BUFFER_SIZE = 1024;

    private static SocketChannel socket;

    private static ClientGx instance;


    private static int fx(int variant) throws InterruptedException {
        Thread.sleep(5);
        switch (variant) {
            case 1:
                Thread.sleep(500);
                return (int) (2 + Math.random() * 10);
            case 2:
            case 6:
                return (int) (2 + Math.random() * 10);
            case 3:
            case 5:
                Thread.sleep(5);
            case 4:
                return 0;
            default:
                throw new IllegalArgumentException("Error!");
        }
    }

    public ClientGx() {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            socket = SocketChannel.open(address);
            System.out.println("Trying to connect to");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            System.out.println("Trying to connect ");
            String msg = "Hello its me";
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            int readLenth = socket.read(buffer);

            buffer.flip();
            byte[] bytes = new byte[readLenth];
            buffer.get(bytes);
            String result = new String(bytes, "UTF-8");
            System.out.println(result);
            buffer.clear();

            int number = Integer.parseInt(result);

            int message = fx(number);
            this.sendMessage(message);


        } catch (IOException | InterruptedException e) {
            System.out.println("No connect");
            e.printStackTrace();
        }
    }

    public static ClientGx start() {
        if (instance == null)
            instance = new ClientGx();

        return instance;
    }

    private void sendMessage(int answer) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            String str = String.valueOf(answer);
            buffer.put(str.getBytes());
            buffer.flip();
            socket.write(buffer);
            buffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ClientGx client = new ClientGx();
        client.run();

        System.out.println("Client started");


    }
}
