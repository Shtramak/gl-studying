import java.util.function.UnaryOperator;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SimpleThreadCalculation {
    public static void main(String[] args) throws InterruptedException {
        UnaryOperator<Double> function = x -> (cos(x) * cos(x) + sin(x) * sin(x));
        MainCalculator calculator = new MainCalculator(function, 10);
        double result = calculator.integral(-1, 1, 0.01);
        System.out.println(result);
    }
}

class MainCalculator {
    private final UnaryOperator<Double> function;
    private final int chunks;

    public MainCalculator(UnaryOperator<Double> function, int chunks) {
        this.function = function;
        this.chunks = chunks;
    }

    public double integral(double from, double to, double step) throws InterruptedException {
        CalcThread[] calcThreads = new CalcThread[chunks];
        double boundStep = (to - from) / chunks;
        double x = from;
        for (int i = 0; i < chunks; i++) {
            calcThreads[i] = new CalcThread(function, x, x + boundStep, step);
            calcThreads[i].start();
            x += boundStep;
        }

        double result = 0;
        for (CalcThread calcThread : calcThreads) {
            calcThread.join();
            result += calcThread.getResult();
        }
        return result;
    }

}

class CalcThread extends Thread {
    private final UnaryOperator<Double> function;
    private final double from;
    private final double to;
    private final double step;
    private double result;

    public CalcThread(UnaryOperator<Double> function, double from, double to, double step) {
        this.function = function;
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public void run() {
        double x = from;
        while (x < to) {
            result += function.apply(x) * step;
            x += step;
        }
    }

    public double getResult() {
        return result;
    }
}
