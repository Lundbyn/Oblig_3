import java.util.Comparator;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) {
        int[] a = {3,2,1,0,-1,-2};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.omvendtString()); // [10, 9, 8, 7, 7, 6, 4, 4, 4, 2, 1]

    }
}
