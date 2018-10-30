import java.util.Comparator;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) {
        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.toString()); // [1, 2, 4, 4, 4, 6, 7, 7, 8, 9, 10]


        StringJoiner s = new StringJoiner(",", "[", "]");
        System.out.println(s);

    }
}
