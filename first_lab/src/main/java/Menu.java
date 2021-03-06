/**
 * Operating system. Lab1. Variant 5
 *
 * @author Bogdan Volokhonenko
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Menu {
    public void menu() throws IOException {
        while (true){
            int select;
            Scanner selector = new Scanner(System.in);

            System.out.println("Testing: ");
            System.out.println("1. f finishes before g with non value");
            System.out.println("2. g finishes before f with non value");
            System.out.println("3. f finishes zero  g hangs");
            System.out.println("4. g finishes zero f hangs");
            System.out.println("5. f finishes non zero value g hangs");
            System.out.println("6. g finishes non zero value f hangs");

            select = selector.nextInt();

            if (select == -1){
                System.exit(-1);
            }
            else if(select>=0 && select<7){
            Server mainServer =  new Server("localhost", 2020, select,2);
            mainServer.run();


            System.out.println("");
            System.out.println( "RESULT OF MULTIPLY: " +  mainServer.getMultiplication() + "\n\n");
            mainServer.close();
            }else {
                System.out.println("Try again");
            }
        }
    }
}
