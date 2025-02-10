package models;

public class TeamSwallow {
    private Organizer organizer;
    private TeamListener team;

    public TeamSwallow(Organizer organizer, TeamListener team) {
        this.organizer = organizer;
        this.team = team;
    }

    // Метод для отправки апелляции
    public void submitAppeal(String appealContent) {
        organizer.receiveAppeal(team, appealContent);  // Передаём апелляцию с командой
    }

    public void submitAnswer() {
        organizer.checkTeams(team);
    }
}
