package synchronization;

import java.util.Scanner;

public class VolatileSample {
    public static void main(String[] args) {
        HelloPrinter helloPrinter = new HelloPrinter();
        Thread thread = new Thread(helloPrinter);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        helloPrinter.shutdown();
        System.out.println("Main method is finished");
    }
}

class HelloPrinter implements Runnable {
    /*
    Set modifier volatile to avoid cache coherency
    The coherency problem arises from the fact that the same block of the shared main
    memory may be resident in two or more of the independent cashes
    */
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            System.out.println("Hello");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Run finished...");
    }

    public void shutdown() {
        System.out.println("Running shutting down");
        this.running = false;
    }
}