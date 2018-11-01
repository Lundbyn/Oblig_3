import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        int[] verdier = {5,5,8,8,6,7,4,3,2};
        for (int i : verdier) {
            tre.leggInn(i);
        }
        System.out.println(tre.toString());
        System.out.println(tre.lengstGren());

    }
}
