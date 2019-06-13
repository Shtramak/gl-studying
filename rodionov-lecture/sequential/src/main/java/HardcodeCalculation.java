import static java.lang.Math.*;

public class HardcodeCalculation {
    public static void main(String[] args) {
        double hardcodedCalculation = integral(-1, 1, 0.01);
        System.out.println(hardcodedCalculation);
    }

    public static double integral(double from, double to, double step) {
        double result = 0;
        double x = from;
        while (x < to) {
            result += (cos(x) * cos(x) + sin(x) * sin(x)) * step;
            x += step;
        }
        return result;
    }
}
