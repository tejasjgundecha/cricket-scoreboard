package com.example.scoreboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {

    private String name;
    private Team team;
    private Integer runs;
    private Integer ballsPlayed;
    private boolean isOnStrike;
    private int noOf4s;
    private int noOf6s;
    private boolean isPlaying;

    public Player(String name) {
        this.name = name;
        this.runs = 0;
        this.ballsPlayed = 0;
        this.noOf4s = 0;
        this.noOf6s = 0;

    }

    public void addRuns(int runs) {
        if (runs == 4) {
            noOf4s++;
        } else if (runs == 6) {
            noOf6s++;
        }
        this.runs += runs;
    }

    public void incrementBallsPlayed() {
        this.ballsPlayed++;
    }

    public void toggleIsOnStrike() {
        this.isOnStrike = !this.isOnStrike;
    }
}
