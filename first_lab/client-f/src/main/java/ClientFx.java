import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientFx {
    public static int fx (int variant) throws InterruptedException {
        Thread.sleep(5);
        switch (variant){
            case 1:
                return (int) (2+ Math.random()*10);
            case 2:
                Thread.sleep(5);
                return (int) (2+ Math.random()*10);
            case 3:
                return 0;
            case 4:
            case 6:
                Thread.sleep(1000);
            case 5:
                return (int) (2+ Math.random()*10);
            default:
                throw new IllegalArgumentException("Error!");
        }
    }
    public static void main(String[] args) throws InterruptedException {


    }
}
