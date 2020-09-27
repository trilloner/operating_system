import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
           Server mainServer =  new Server("localhost", 2020, 6,2);
           mainServer.run();
           System.out.println( mainServer.getResults());
           System.out.println( mainServer.getMultiplication());
           mainServer.close();

    }
}
