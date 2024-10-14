package models;

public abstract class TeamListener {
    private final String name;
    private final int index;
    private final TeamSwallow teamSwallow;
    private String currentAnswer;

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

    public TeamListener(String name, TeamSwallow teamSwallow) {
        index = -1;
        this.name = name;
        this.teamSwallow = teamSwallow;
    }

    public TeamListener(int index, String name, TeamSwallow teamSwallow) {
        this.index = index;
        this.name = name;
        this.teamSwallow = teamSwallow;
    }

    public String getName() {
        return name;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public int getIndex() {
        return index;
    }

    public void submitAppeal(String appealContent) {
        if (teamSwallow != null) {
            teamSwallow.submitAppeal(appealContent);
        } else {
            System.out.println("Not allowed to submit appeal");
        }
    }

    public void setMessage(String message) {
        System.out.printf(message);
    }

    public void setQuestion(String question) {
        setMessage(question);

        setCurrentAnswer( ""/*read from keyboard*/ );
    }
}
