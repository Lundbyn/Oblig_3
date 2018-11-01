import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        int[] a = {7,1,11,3,8,12,2,5,10,4,6,9};
        for (int verdi : a) tre.leggInn(verdi);

        System.out.println(tre.toString());
        tre.fjernHvis(x -> x % 2 == 0);
        System.out.println(tre.toString());
        tre.fjernHvis(x -> x % 2 != 0);
        System.out.println(tre.toString());
    }
}
