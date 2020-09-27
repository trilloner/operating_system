import java.util.Scanner;
public class Menu {
    public int menu(){
        int select;
        Scanner selector = new Scanner(System.in);

        System.out.println("Testing: ");
        System.out.println("1. f finishes before g with non value");
        System.out.println("2. g finishes before f with non value");
        System.out.println("3. f finishes before g with non value");
        System.out.println("4. g finishes before f with non value");

        select = selector.nextInt();
        new ClientGx().start();
        return select;
    }
}
