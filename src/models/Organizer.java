package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Organizer {

    private Map<TeamListener, TeamMap> teams;
    private List<AbstractQuestion> questions;
    private List<String> appeals;
    private Host host;

    private Organizer(Builder builder) {
        this.teams = builder.teams;
        this.questions = builder.questions;
        this.appeals = builder.appeals;
        this.host = builder.host;
    }

    public static class Builder {
        private Map<TeamListener, TeamMap> teams = new HashMap<>();
        private List<AbstractQuestion> questions = new ArrayList<>();
        private List<String> appeals = new ArrayList<>();
        private Host host = null;

        // Метод для добавления команды
        public Builder addTeam(TeamListener listener) {
            this.teams.put(listener, new TeamMap());
            return this;
        }

        // Метод для добавления списка вопросов
        public Builder setQuestions(List<AbstractQuestion> questions) {
            this.questions = questions;
            return this;
        }

        // Метод для добавления хоста
        public Builder setHost(Host host) {
            this.host = host;
            return this;
        }


        // Метод для добавления апелляции
        public Builder addAppeal(String appeal) {
            this.appeals.add(appeal);
            return this;
        }

        // Метод для создания экземпляра Organizer
        public Organizer build() {
            return new Organizer(this);
        }
    }

    public void setQuestions(List<AbstractQuestion> questions) {
        this.questions = questions;
    }

    public void addTeamListener(TeamListener teamListener) {
        if (teams.containsKey(teamListener)) {
            teams.get(teamListener).activate();
        } else {
            teams.put(teamListener, new TeamMap());
        }
        if (host != null) {
            host.registerTeam(teamListener);
        }
    }

    public void removeTeamListener(TeamListener teamListener) {
        if (host != null) {
            host.removeTeam(teamListener);
        }
        teams.get(teamListener).deactivate();
    }

    public void deleteTeam(TeamListener teamListener) {
        teams.remove(teamListener);
        host.removeTeam(teamListener);
    }

    public void receiveAppeal(TeamListener team, String appealContent) {
        String appeal = "Appeal from " + team.getName() + ": " + appealContent;
        appeals.add(appeal);
        System.out.println(appeal);
    }

    public void collectAnswers(AbstractQuestion question) {
        for (Map.Entry<TeamListener, TeamMap> team : teams.entrySet()) {
            String answer = team.getKey().getCurrentAnswer();
            team.getValue().addAnswer(question.getId(), answer, question.checkAnswer(answer));
        }
    }

    public void startGame() {
        for (AbstractQuestion question : questions) {
            startRound(question);
            //ready?
        }
    }

    public void startRound(AbstractQuestion question) {
        boolean roundActive = true;
        host.setCurrentQuestion(question);
        host.tellQuestion();
        //wait several second or input from real host
        host.speak("60 second for discussion");
        //wait 50 sec
        host.speak("10 second left");
        //wait 10 sec
        collectAnswers(question);
        host.tellAnswer();
        //wait several sec
        roundActive = false;
    }
}
