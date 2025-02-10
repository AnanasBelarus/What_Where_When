package activities;

import models.MyThread;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {
    private static class ParallelInput implements Runnable {
        private Scanner scanner = new Scanner(System.in);
        private String input;
        @Override
        public void run(){


        }

        public void input(){
            input = scanner.nextLine();
            latch.countDown();
        }

        public String getInput() {
            return input;
        }
    }
    private static CountDownLatch latch;

    private synchronized static void waitForInput(){

    }
    private synchronized static void waiting() throws InterruptedException {
        latch.await(10, TimeUnit.SECONDS);
        System.out.println(latch.getCount());
    }
    public static void main(String[] args) throws InterruptedException {
        latch = new CountDownLatch(1);
        ParallelInput parallelInput = new ParallelInput();
        MyThread thread = new MyThread();
        thread.start();
        thread.setAnsw();
        latch.await(10, TimeUnit.SECONDS);
        System.out.println(latch.getCount());
        System.out.println(parallelInput.getInput());
    }
}
