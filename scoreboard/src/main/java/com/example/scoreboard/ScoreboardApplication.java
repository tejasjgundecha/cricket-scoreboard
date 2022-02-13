package com.example.scoreboard;

import com.example.scoreboard.model.Ball;
import com.example.scoreboard.model.BallType;
import com.example.scoreboard.model.Player;
import com.example.scoreboard.model.Team;
import com.example.scoreboard.service.ScoreboardManagementService;
import java.util.List;
import java.util.Optional;

public class ScoreboardApplication {

    private static ScoreboardManagementService managementService = new ScoreboardManagementService();

    public static void main(String[] args) {
        //		SpringApplication.run(ScoreboardApplication.class, args);

        Team team1 = new Team("team1", 5);
        Team team2 = new Team("team2", 5);

        Player p1 = new Player("P1");
        p1.setOnStrike(true);
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        Player p5 = new Player("P5");

        Player p6 = new Player("P6");
        p6.setOnStrike(true);
        Player p7 = new Player("P7");
        Player p8 = new Player("P8");
        Player p9 = new Player("P9");
        Player p10 = new Player("P10");

        team1.addPlayers(List.of(p1, p2, p3, p4, p5));
        team2.addPlayers(List.of(p6, p7, p8, p9, p10));

        managementService.init(team1, team2);
        managementService.setBattingTeam(1);
        //        managementService.setBallingTeam(team2);

        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(2).ballType(BallType.NORMAL).build());

        managementService.printStats(1);

        managementService.processBall(Ball.builder().ballType(BallType.WICKET).build());
        managementService.processBall(Ball.builder().run(4).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(4).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().ballType(BallType.WIDE).build());
        managementService.processBall(Ball.builder().ballType(BallType.WICKET).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(6).ballType(BallType.NORMAL).build());

        managementService.printStats(1);

        managementService.setBattingTeam(2);

        managementService.processBall(Ball.builder().run(4).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(6).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().ballType(BallType.WICKET).build());
        managementService.processBall(Ball.builder().ballType(BallType.WICKET).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());

        managementService.printStats(2);

        managementService.processBall(Ball.builder().run(6).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().run(1).ballType(BallType.NORMAL).build());
        managementService.processBall(Ball.builder().ballType(BallType.WICKET).build());
        managementService.processBall(Ball.builder().ballType(BallType.WICKET).build());

        managementService.printStats(2);
        Optional<Team> winnerOpt = managementService.getWinner();

        if (winnerOpt.isPresent()) {
            System.out.println(winnerOpt.get().getTeamName() + " won the match");
        } else {
            System.out.println("TIE");
        }
    }

}
