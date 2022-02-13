package com.example.scoreboard.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class Team {

    private final String teamName;
    private final int noOfPlayers;
    private final List<Player> players;
    private int currPlayer = -1;
    private int finalScore;
    private boolean isBatting;
    private final List<Over> overs;
    private int wickets;

    public Team(String teamName, int noOfPlayers) {
        this.teamName = teamName;
        this.noOfPlayers = noOfPlayers;
        players = new ArrayList<>(this.noOfPlayers);
        finalScore = 0;
        wickets = 0;
        overs = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if (players.size() == noOfPlayers) {
            throw new IllegalStateException("Max player size reached");
        }
        players.add(player);
    }

    public void addPlayers(List<Player> players) {
        if (players.size() != noOfPlayers) {
            throw new IllegalStateException("list should have all players");
        }
        if (this.players.size() != 0) {
            throw new IllegalStateException("Few players already added. Please add player one by one");
        }
        this.players.addAll(players);
    }

    //To set batting team
    public void setIsBatting(boolean isBatting) {
        this.isBatting = isBatting;
    }

    //Returns next player which will bat
    public Player getNextPlayer() {
        if (currPlayer == noOfPlayers - 1) {
            throw new IllegalStateException("All Out");
        }
        currPlayer++;
        return players.get(currPlayer);
    }

    //Adds runs to the totalSCore
    public void addRuns(int runs) {
        finalScore += runs;
    }

    //Fall of wickets
    public void addWicket() {
        wickets++;
    }

    //Returns whether team is all out or not
    public boolean isAllOut() {
        if (wickets == noOfPlayers - 1) {
            return true;
        }
        return false;
    }

    //Adds balling overs
    public void addOver(Over over) {
        Optional<Over> cOver = this.getCurrentOver();
        if (cOver.isPresent() && !cOver.get().isOver()) {
            throw new IllegalStateException("Current over is not complete");
        }
        overs.add(over);
    }

    //Returns current balled by the team
    public Optional<Over> getCurrentOver() {
        if (overs.size() > 0) {
            return Optional.of(overs.get(overs.size() - 1));
        }
        return Optional.empty();
    }
}
