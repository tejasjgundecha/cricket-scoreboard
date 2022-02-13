package com.example.scoreboard.service;

import com.example.scoreboard.model.Ball;
import com.example.scoreboard.model.Over;
import com.example.scoreboard.model.Player;
import com.example.scoreboard.model.Team;
import java.util.List;
import java.util.Optional;

public class ScoreboardManagementService {

    Team team1, team2;
    Player player1, player2;

    //Init of service with given teams to be played
    public void init(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    //Sets which team would be batting and fetches first 2 players from that team
    public void setBattingTeam(int no) {
        if (no == 1) {
            team1.setIsBatting(true);
            team2.setIsBatting(false);
            player1 = team1.getNextPlayer();
            player2 = team1.getNextPlayer();
        } else {
            team2.setIsBatting(true);
            team1.setIsBatting(false);
            player1 = team2.getNextPlayer();
            player2 = team2.getNextPlayer();
        }
        player1.setPlaying(true);
        player2.setPlaying(true);

    }

    //    public void setBallingTeam(Team team){
    //        team.setIsBatting(false);
    //    }

    //Method to process ball based on ball type
    public void processBall(Ball ball) {
        if (getCurrentBattingTeam().isAllOut()) {
            throw new IllegalStateException(getCurrentBattingTeam().getTeamName() + " is all out");
        }

        switch (ball.getBallType()) {
            case NORMAL:
                processNormalBall(ball);
                break;
            case WICKET:
                processWicketBall(ball);
                break;
            case WIDE:
            case NO_BALL:
                processWideAndNoBall(ball);
                break;
        }
    }

    //Adds run to team and playing player. Changes strike for runs and over completion. Adds ball to current over
    void processNormalBall(Ball ball) {
        int runs = ball.getRun();
        Player onStrike = getPlayerOnStrike();
        onStrike.addRuns(runs);
        onStrike.incrementBallsPlayed();

        getCurrentBattingTeam().addRuns(runs);

        addBall(ball);

        if (runs % 2 == 1) {
            Player notOnStrike = getPlayerNotOnStrike();
            onStrike.setOnStrike(false);
            notOnStrike.setOnStrike(true);
        }
        checkAndProcessForOver();
    }

    //Fetches player on strike and marks player out. Adds wicket. Adds ball to over. Changes strike if over
    //Returns if current team is allOut, otherwise fetches next player from the batting team
    void processWicketBall(Ball ball) {

        Player player = getPlayerOnStrike();
        player.incrementBallsPlayed();
        player.setPlaying(false);
        getCurrentBattingTeam().addWicket();
        addBall(ball);
        checkAndProcessForOver();
        if (getCurrentBattingTeam().isAllOut()) {
            System.out.println(getCurrentBattingTeam() + " all out");
            return;
        }
        if (player1 == player) {
            player1 = getCurrentBattingTeam().getNextPlayer();
            player1.setOnStrike(true);
            player1.setPlaying(true);
        } else {
            player2 = getCurrentBattingTeam().getNextPlayer();
            player2.setOnStrike(true);
            player2.setPlaying(true);
        }

    }

    //Adds ball and run to batting team
    void processWideAndNoBall(Ball ball) {
        addBall(ball);
        getCurrentBattingTeam().addRuns(1);
    }

    //Adds ball to current over or creates new over if current over is not present or complete
    void addBall(Ball ball) {
        Optional<Over> currentOverOpt = getCurrentBallingTeam().getCurrentOver();
        if (currentOverOpt.isEmpty() || currentOverOpt.get().isOver()) {
            Over newOver = new Over();
            newOver.addBall(ball);
            getCurrentBallingTeam().addOver(newOver);
        } else {
            currentOverOpt.get().addBall(ball);
        }
    }

    //If over is complete, changes strike of players
    void checkAndProcessForOver() {
        Optional<Over> over = getCurrentBallingTeam().getCurrentOver();
        if (over.isPresent() && over.get().isOver()) {
            getPlayerOnStrike().setOnStrike(false);
            getPlayerNotOnStrike().setOnStrike(true);
        }
    }

    Team getCurrentBattingTeam() {
        if (team1.isBatting()) {
            return team1;
        }
        return team2;
    }

    Team getCurrentBallingTeam() {
        if (!team1.isBatting()) {
            return team1;
        }
        return team2;
    }

    Player getPlayerOnStrike() {
        if (getPlayer1().isOnStrike()) {
            return player1;
        } else {
            return getPlayer2();
        }
    }

    Player getPlayerNotOnStrike() {
        if (!getPlayer1().isOnStrike()) {
            return player1;
        } else {
            return getPlayer2();
        }
    }

    Player getPlayer1() {
        if (player1 != null) {
            return player1;
        }
        player1 = getCurrentBattingTeam().getNextPlayer();
        return player1;
    }

    Player getPlayer2() {
        if (player2 != null) {
            return player2;
        }
        player2 = getCurrentBattingTeam().getNextPlayer();
        return player2;
    }

    //method to decide winner
    public Optional<Team> getWinner() {
        if (team1.getFinalScore() > team2.getFinalScore()) {
            return Optional.of(team1);
        } else if (team2.getFinalScore() > team1.getFinalScore()) {
            return Optional.of(team2);
        } else {
            System.out.println("Tie");
            return Optional.empty();
        }
    }

    //Prints stats
    public void printStats(int teamNo) {
        if (teamNo == 1) {
            printTeamStats(team1, team2);
        } else {
            printTeamStats(team2, team1);
        }

    }

    //prints score, player stats, overs, wickets
    public void printTeamStats(Team teamX, Team teamY) {
        System.out.println("\n" + teamX.getTeamName() + " team Statistics");
        System.out.println("Score: " + teamX.getFinalScore() + "/" + teamX.getWickets());
        System.out.println("Player Name\tScore\t4s\t6s\tBalls");
        for (Player player : teamX.getPlayers()) {
            String isPlayingAstr = player.isPlaying() ? "*" : "";
            System.out.println(String.format("%s%s\t%s\t%s\t%s\t%s", player.getName(), isPlayingAstr, player.getRuns(),
                    player.getNoOf4s(), player.getNoOf6s(), player.getBallsPlayed()));
        }
        System.out.println("Overs: " + getOversInStringFormat(teamY));
    }

    //Returns current overs balled from team
    String getOversInStringFormat(Team team) {
        List<Over> overs = team.getOvers();
        if (overs.size() == 0) {
            return "0.0";
        }
        Over lastOver = overs.get(overs.size() - 1);
        if (lastOver.isOver()) {
            return overs.size() + ".0";
        } else {
            return (overs.size() - 1) + "." + lastOver.getValidBalls();
        }
    }
}
