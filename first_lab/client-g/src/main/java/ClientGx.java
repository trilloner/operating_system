public class ClientGx {
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
    public static void main(String[] args) throws InterruptedException {


    }
}
