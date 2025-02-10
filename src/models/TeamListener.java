package models;

import java.awt.event.ActionListener;
import java.util.Scanner;

public class TeamListener {
    private final String name;
    private final int index;
    private TeamSwallow teamSwallow;
    private String currentAnswer;
    private boolean isFinalized = false;
    private Scanner scanner;

    //Можно заменить строителем
    public TeamListener(String name) {
        index = -1;
        this.name = name;
        this.teamSwallow = null;
    }

    public TeamListener(String name, int index) {
        this.index = index;
        this.name = name;
        this.teamSwallow = null;
    }

    public TeamListener(String name, Organizer teamSwallow) {
        index = -1;
        this.name = name;
        this.teamSwallow = new TeamSwallow(teamSwallow, this);
    }

    public TeamListener(int index, String name, TeamSwallow teamSwallow) {
        this.index = index;
        this.name = name;
        this.teamSwallow = teamSwallow;
    }

    public String getName() {
        return name;
    }

    public void setTeamSwallow(TeamSwallow teamSwallow) {
        this.teamSwallow = teamSwallow;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer() {
//        if (isFinalized) {
//            System.out.println("Ответ уже принят, изменения невозможны.");
//            return;
//        }
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        for (isFinalized = false; !isFinalized; isFinalized = !scanner.nextLine().equals("н")) {
            System.out.println("Введите свой ответ:");
            this.currentAnswer = scanner.nextLine();
            System.out.println("Ответ окончательный? (д/н)");
        }
        teamSwallow.submitAnswer();
    }

    public boolean isFinalized() {
        return isFinalized;
    }

    public int getIndex() {
        return index;
    }

    public void eraseAnswer() {
        currentAnswer = "";
    }

    public void submitAppeal(String appealContent) {
        if (teamSwallow != null) {
            teamSwallow.submitAppeal(appealContent);
        } else {
            System.out.println("Not allowed to submit appeal");
        }
    }

    public void setMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String toString() {
        if (index != -1) {
            return "Команда " + index +
                    ") '" + name + "\' ";
        } else {
            return "Команда '" + name + "\' ";
        }
    }
}
