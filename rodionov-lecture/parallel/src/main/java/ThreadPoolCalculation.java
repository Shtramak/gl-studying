import java.util.concurrent.*;
import java.util.function.UnaryOperator;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ThreadPoolCalculation {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        UnaryOperator<Double> function = x -> (sin(x) * sin(x) + cos(x) * cos(x));
        MainThreadPoolCalculator calculator = new MainThreadPoolCalculator(function, 10);
        double result = calculator.integral(-1, 1, 0.01);
        System.out.println(result);
    }
}

class MainThreadPoolCalculator {
    private final UnaryOperator<Double> function;
    private final int chunks;

    public MainThreadPoolCalculator(UnaryOperator<Double> function, int chunks) {
        this.function = function;
        this.chunks = chunks;
    }

    public double integral(double from, double to, double step) throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(chunks);
        FutureTask<Double>[] tasks = new FutureTask[chunks];
        double boundStep = (to - from) / chunks;
        double x = from;
        for (int i = 0; i < chunks; i++) {
            tasks[i] = new FutureTask<>(new CallableThread(function, x, x + boundStep, step));
            service.submit(tasks[i]);
            x += boundStep;
        }
        service.shutdown();
        double result = 0;
        for (FutureTask<Double> task : tasks) {
            result += task.get();
        }
        return result;
    }

}

class CallableThread implements Callable<Double> {
    private final UnaryOperator<Double> function;
    private final double from;
    private final double to;
    private final double step;

    public CallableThread(UnaryOperator<Double> function, double from, double to, double step) {
        this.function = function;
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public Double call() throws Exception {
        double result = 0;
        double x = from;
        while (x < to) {
            result += function.apply(x) * step;
            x += step;
        }
        return result;
    }
}
