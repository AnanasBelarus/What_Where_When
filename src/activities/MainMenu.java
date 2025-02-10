package activities;

import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Главное меню:");
            System.out.println("1. Импортировать вопросы из файла .txt");
            System.out.println("2. Вывести содержимое чемпионата");
            System.out.println("3. Сыграть простую игру");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> ImportQuestions.ImportQuestionsFromTXT();
                case "2" -> System.out.println(ImportQuestions.getChampionshipFromFile());

                case "3" -> {
                    ImportQuestions.runSimpleGame();
                }
                default -> System.out.println("Некорректный выбор. Поkа.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
