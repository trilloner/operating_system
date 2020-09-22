import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Menu menu = new Menu();
//        menu.menu();
            new Server("localhost", 2020).start();
    }
}
