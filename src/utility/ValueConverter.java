package utility;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 05/12/2012
 * Time: 19:22
 */
public class ValueConverter {
    public static double[] getBipodal(boolean... input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = input[i] ? 1 : -1;
        }
        return output;
    }

    public static boolean[] getBoolean(double max_error, double... input) {
        boolean[] output = new boolean[input.length];
        for (int i = 0; i < input.length; ++i) {
            if (Math.abs(1.0 - input[i]) < max_error)
                output[i] = true;
            else if (Math.abs(-1.0 - input[i]) < max_error)
                output[i] = false;
            else
                throw new RuntimeException("Could not convert bipodal to boolean");
        }
        return output;
    }
}
