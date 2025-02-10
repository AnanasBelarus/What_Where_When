package models;

import java.util.Scanner;

public class MyThread extends Thread{
    private Scanner scanner;
    private boolean isFinalized = false;
    private String currentAnswer;
    public void setAnsw(){
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        for (isFinalized = false; !isFinalized; isFinalized = !scanner.nextLine().equals("н")) {
            System.out.println("Введите свой ответ:");
            this.currentAnswer = scanner.nextLine();
            System.out.println("Ответ окончательный? (д/н)");
        }
    }

    public String getAnswer(){
        return currentAnswer;
    }
    @Override
    public void run() {
        System.out.println("Thread started");
    }
}
