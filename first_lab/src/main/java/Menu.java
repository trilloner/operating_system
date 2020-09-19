import java.util.Scanner;

public class Menu {
    public void menu(){
        int select;
        Scanner selector = new Scanner(System.in);

        System.out.println("Testing: ");
        System.out.println("1. f finishes before g with non value");
        select = selector.nextInt();
        System.out.println(select);
    }
}
