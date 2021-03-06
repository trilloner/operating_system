/**
 * Operating system. Lab1. Variant 5
 *
 * @author Bogdan Volokhonenko
 */

import spos.lab1.demo.DoubleOps;
import spos.lab1.demo.IntOps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Client class
 */
public class ClientFx {

    private static final int port = 2020;

    private static final int BUFFER_SIZE = 1024;

    private static SocketChannel socket;

    private int result;
    /**
     * Function f(x) that does complex calculations
     *
     * @param variant
     * @return
     * @throws InterruptedException
     */
    public static int fx(int variant) throws InterruptedException {
        Thread.sleep(500);
        switch (variant) {
            case 1:
                return (int) (10);
            case 2:
                Thread.sleep(500);
                return (int) (variant * 5);
            case 3:
                return 0;
            case 4:
            case 6:
                Thread.sleep(5000);

                JFrame jf = new JFrame();
                jf.setSize(500,200);
                jf.addKeyListener(new ClientGx.KeyHandler());
                jf.add(new JLabel("Client stopped responding \n Press to exit: CTRL+C", SwingUtilities.CENTER));
                jf.setVisible(true);
                jf.setLocationRelativeTo(null);
                while (true) {
                    Thread.sleep(1000);
                    System.out.println("Something went wrong...");
                    Thread.sleep(1000);
                    System.out.println("Error!");
                }
            case 5:
                return (int) (2 + Math.random() * 10);
            default:
                throw new IllegalArgumentException("Error!");
        }
    }

    /**
     * Constructor
     */
    public ClientFx(int variant) {
        try {
            this.result = variant;
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            socket = SocketChannel.open(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * An override method that starts and connects the client to the server
     */

    public void run() {
        try {

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            int readLenth = socket.read(buffer);

            buffer.flip();
            byte[] bytes = new byte[readLenth];
            buffer.get(bytes);
            String result = new String(bytes, "UTF-8");
            buffer.clear();

            this.sendMessage(this.result);


        } catch (IOException  e) {
            System.out.println("No connect");
            e.printStackTrace();
        }
    }

    /**
     * Function that send message to the server
     * @param answer
     */
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
    /**
     * Class cancellation
     */
    public static class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) { }

        @Override
        public void keyPressed(KeyEvent e) {
            AWTKeyStroke ak = AWTKeyStroke.getAWTKeyStrokeForEvent(e);
            if(ak.equals(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)))
            {
                System.exit(-1);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) { }
    }

    public static void main(String[] args) throws InterruptedException {
        int number =  Integer.parseInt(args[0]);
        int message = IntOps.funcF(number);
        //int message = fx(number);
        new ClientFx(message).run();

    }
}
