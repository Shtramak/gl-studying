import java.util.function.UnaryOperator;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FunctionalCalculation {
    public static void main(String[] args) {
        UnaryOperator<Double> function = x -> (sin(x) * sin(x) + cos(x) * cos(x));
        CalculateFunction calculate = new CalculateFunction(function);
        double result = calculate.integral(-1, 1, 0.01);
        System.out.println(result);
    }

}

class CalculateFunction {
    private UnaryOperator<Double> function;

    public CalculateFunction(UnaryOperator<Double> function) {
        this.function = function;
    }

    public double integral(double from, double to, double step) {
        double result = 0;
        double x = from;
        while (x < to) {
            result += function.apply(x) * step;
            x += step;
        }
        return result;
    }
}