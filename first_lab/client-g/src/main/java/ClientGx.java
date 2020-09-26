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

    public static int fx (int variant) throws InterruptedException {
        Thread.sleep(5);
        switch (variant){
            case 1:
                Thread.sleep(5);
                return (int) (2+ Math.random()*10);
            case 2:
            case 6:
                return (int) (2+ Math.random()*10);
            case 3:
            case 5:
                Thread.sleep(5);
            case 4:
                return 0;
            default:
                throw new IllegalArgumentException("Error!");
        }
    }
    private ClientGx (){
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", port);
             socket = SocketChannel.open(address);
            System.out.println("Trying to connect to");

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void start(){
        try{
            System.out.println("Trying to connect ");
            String msg = "Hello its me";

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
//            buffer.put(msg.getBytes());
//            buffer.flip();
//
//            int bytesWritten = socket.write(buffer);
//            buffer.clear();
//            System.out.println("Bytes in your message: " + bytesWritten);

            int readLenth = socket.read(buffer);

            buffer.flip();
            byte[] bytes = new byte[readLenth];
            buffer.get(bytes);
            System.out.println(new String(bytes, "UTF-8"));
            buffer.clear();



        }catch (IOException e){
            System.out.println("No connect");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new ClientGx("localhost");

        System.out.println("Client started");




    }
}
