/**
 * Operating system. Lab1. Variant 5
 *
 * @author Bogdan Volokhonenko
 */
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import spos.lab1.demo.IntOps;

import java.util.HashMap;


class ServerTest {


    @Test
    public void case1() throws InterruptedException {
        Server mainServer =  new Server("localhost", 2020, 1,2);
        mainServer.run();
        HashMap<Integer,String> result = mainServer.getResults();
        assertEquals(result.size(), 2);

        assertEquals(result.keySet().toArray()[0],100);
        assertEquals(result.keySet().toArray()[1],10);
        assertEquals(5,IntOps.funcG(1));
        assertEquals(3,IntOps.funcF(1));
        assertEquals(mainServer.getMultiplication(), 1000);

        mainServer.close();
    }
    @Test
    public void case2() throws InterruptedException {
        Server mainServer =  new Server("localhost", 2020, 2,2);
        mainServer.run();
        HashMap<Integer,String> result = mainServer.getResults();
        assertEquals(result.size(), 2);


        assertEquals(result.keySet().toArray()[0],20);
        assertEquals(result.keySet().toArray()[1],10);

        assertEquals(mainServer.getMultiplication(), 200);

        mainServer.close();
    }

    @Test
    public void case3() throws InterruptedException {
        Server mainServer =  new Server("localhost", 2020, 3,2);
        mainServer.run();
        HashMap<Integer,String> result = mainServer.getResults();
        assertEquals(result.size(), 1);


        assertEquals(result.keySet().toArray()[0],IntOps.funcG(3));
        assertEquals(mainServer.getMultiplication(),0);
        mainServer.close();
    }
    @Test
    public void case4() throws InterruptedException {
        Server mainServer =  new Server("localhost", 2020, 4,2);
        mainServer.run();
        HashMap<Integer,String> result = mainServer.getResults();
        assertEquals(result.size(), 1);


        assertEquals(result.keySet().toArray()[0],IntOps.funcG(3));
        assertEquals(mainServer.getMultiplication(),0);
        mainServer.close();
    }

}