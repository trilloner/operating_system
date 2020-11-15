import java.util.Comparator;

public class ImprovedProcessPriority implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        sProcess pr1 = (sProcess) o1;
        sProcess pr2 = (sProcess) o2;

        if (pr1.precendence == pr2.precendence && pr1.ioblocking == pr2.ioblocking && pr1.cputime == pr2.cputime)
            return 0;
        float priority1 = (float) pr1.precendence / pr1.ioblocking;
        float priority2 = (float) pr2.precendence / pr2.ioblocking;
        if (priority2 - priority1 > 0.0f)
            return 1;
        else return -1;

    }
}
