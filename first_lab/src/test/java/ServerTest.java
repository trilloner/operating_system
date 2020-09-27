import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import spos.lab1.demo.IntOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

class ServerTest {

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