import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientFx extends Thread {

    private static final int port = 2020;

    private static final int BUFFER_SIZE = 1024;

    private static SocketChannel socket;

    public static int fx(int variant) throws InterruptedException {
        Thread.sleep(5);
        switch (variant) {
            case 1:
                return (int) (2 + Math.random() * 10);
            case 2:
                Thread.sleep(5);
                return (int) (2 + Math.random() * 10);
            case 3:
                return 0;
            case 4:
            case 6:
                Thread.sleep(1000);
                System.out.println("Something went wrong...");
                Thread.sleep(1000);
                System.out.println("Error!");

                while(true){
                }
            case 5:
                return (int) (2 + Math.random() * 10);
            default:
                throw new IllegalArgumentException("Error!");
        }
    }


    public ClientFx() {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            socket = SocketChannel.open(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg = "Hello its me";
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            int readLenth = socket.read(buffer);

            buffer.flip();
            byte[] bytes = new byte[readLenth];
            buffer.get(bytes);
            String result = new String(bytes, "UTF-8");
            buffer.clear();

            int number = Integer.parseInt(result);

            int message = fx(number);
            this.sendMessage(message);


        } catch (IOException | InterruptedException e) {
            System.out.println("No connect");
            e.printStackTrace();
        }
    }


    private void sendMessage(int answer) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            String str = String.valueOf(answer) +  " f(x)";
            buffer.put(str.getBytes());
            buffer.flip();
            socket.write(buffer);
            buffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
