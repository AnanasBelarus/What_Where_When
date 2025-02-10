package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Organizer {

    private Map<TeamListener, TeamMap> teams;
    private Championship championship;
    private List<String> appeals;
    private Host host;
    private CountDownLatch latch; // Для ожидания ответов

    private Organizer(Builder builder) {
        this.teams = builder.teams;
        this.championship = builder.championship;
        this.appeals = new ArrayList<String>();
        this.host = builder.host;

    }


    public static class Builder {
        private Map<TeamListener, TeamMap> teams = new HashMap<>();
        private Championship championship;
        private Host host = null;


        // Метод для добавления команды
        public Builder addTeam(TeamListener listener) {
            this.teams.put(listener, new TeamMap());
            return this;
        }

        // Метод для добавления команды
        public Builder addTeam(String listenerName) {
            TeamListener listener = new TeamListener(listenerName);
            this.teams.put(listener, new TeamMap());
            return this;
        }

        // Метод для добавления списка вопросов
        public Builder setChampionship(Championship championship) {
            this.championship = championship;
            return this;
        }

        // Метод для добавления хоста
        public Builder setHost(Host host) {
            this.host = host;
            return this;
        }

        // Метод для создания экземпляра Organizer
        public Organizer build() {
            if (this.host == null) {
                host = new Host(new ArrayList<TeamListener>(teams.keySet()));
            }
            return new Organizer(this);
        }
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Championship getChampionship() {
        return championship;
    }

    public void setChampionship(Championship championship) {
        this.championship = championship;
    }

    public void addTeamListener(TeamListener teamListener) {
        if (teams.containsKey(teamListener)) {
            teams.get(teamListener).activate();
        } else {
            teamListener.setTeamSwallow(new TeamSwallow(this, teamListener));
            teams.put(teamListener, new TeamMap());
        }
        if (host != null) {
            host.registerTeam(teamListener);
        }
    }

    public TeamListener addTeamListener(String teamListenerName) throws Exception {
        for (TeamListener listener : teams.keySet()) {
            if (listener.getName().equals(teamListenerName)) {
                throw new Exception("Such team already exists");
            }
        }
        TeamListener team = new TeamListener(teamListenerName, this);
        teams.put(team, new TeamMap());
        if (host != null) {
            host.registerTeam(team);
        }
        return team;
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

            int isCorrect = question.checkAnswer(answer);
            String[] choice = {"не верный", "верный"};
            team.getValue().addAnswer(question.getId(), answer, isCorrect);
            System.out.println("Команда " + team.getKey().getName() + " ответила " + answer + " и организатор считает что это " + choice[isCorrect] + " ответ");
            team.getKey().eraseAnswer();
        }
    }

    public void checkTeams(TeamListener team) {
        //if (countFinished() < latch.getCount())
        latch.countDown();
        //else ban team
    }

    public void startGame() {
        try {
            for (Round round : championship) {
                startRound(round);
                for (Map.Entry<TeamListener, TeamMap> entry : teams.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue().getScore());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean allFinished() {
        for (TeamListener team : teams.keySet()) {
            if (!team.isFinalized()) return false;
        }
        return true;
    }

    private int countActive() {
        int count = 0;
        for (TeamMap team : teams.values()) {
            if (team.isActive()) count++;
        }
        return count;
    }

    private int countFinished() {
        int count = 0;
        for (TeamListener team : teams.keySet()) {
            if (team.isFinalized()) count++;
        }
        return count;
    }

    public void startRound(Round round) throws InterruptedException {
        host.speak(round.getTitle());
        Thread.sleep(round.getTitle().length() * 60000 / 850);
        class myThread extends Thread {
            private TeamListener teamListener;

            public myThread(TeamListener teamListener) {
                this.teamListener = teamListener;
            }

            @Override
            public void run() {
                teamListener.setCurrentAnswer();
            }
        }
        for (AbstractQuestion question : round) {
            boolean roundActive = true;
            String qs = question.getQuestion();
            host.speak(qs);
            Thread.sleep(qs.length() * 60000 / 850);
            for (TeamListener team : teams.keySet()) {
                new myThread(team).start();
            }
            //wait several second or input from real host
            host.speak("60 second for discussion");
            latch = new CountDownLatch(countActive());
            latch.await(50, TimeUnit.SECONDS);
            if (!allFinished()) {
                host.speak("10 second left");
                latch.await(10, TimeUnit.SECONDS);
            }
            String qa = question.getAnswer();
            host.speak(qa);
            Thread.sleep(qa.length() * 60000 / 850);
            collectAnswers(question);

            //wait several sec
            roundActive = false;
        }
    }
}
