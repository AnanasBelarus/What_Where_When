package models;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Host {
    private List<TeamListener> teams;
    private AbstractQuestion currentQuestion;
    private final int speed = 850 / 60;

    public Host(List<TeamListener> teams) {
        this.teams = teams;
    }


    public List<TeamListener> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamListener> teams) {
        this.teams = teams;
    }

    public AbstractQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(AbstractQuestion currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public void registerTeam(TeamListener team) {
        teams.add(team);
        System.out.println("Team " + team.getName() + " added to the game.");
    }

    public void removeTeam(TeamListener team) {
        teams.remove(team);
    }



    public void tellAnswer() throws InterruptedException {
        speak(currentQuestion.getAnswer());
    }

    public void speak(String message) throws InterruptedException {
        for (TeamListener team : teams) {
            team.setMessage(message);
        }
    }
}
