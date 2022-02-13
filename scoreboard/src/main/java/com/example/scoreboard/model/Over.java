package com.example.scoreboard.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Over {

    private static final EnumSet<BallType> validTypes = EnumSet.of(BallType.NORMAL, BallType.WICKET);

    private List<Ball> balls;
    private int validBalls;

    public Over() {
        balls = new ArrayList<>();
        validBalls = 0;
    }

    //Returns true if valid balls count is 6
    public boolean isOver() {
        return validBalls == 6;
    }

    /**
     * Adds ball to over. Throws exception if over is complete
     * Only increments count of valid balls if ball type is NORMAL / WICKET
     * @param ball
     * @throws IllegalStateException
     */
    public void addBall(Ball ball) throws IllegalStateException {
        if (validBalls == 6) {
            throw new IllegalStateException("An over can only have 6 valid balls");
        }

        balls.add(ball);
        if (validTypes.contains(ball.getBallType())) {
            validBalls++;
        }
    }

    public int getValidBalls() {
        return this.validBalls;
    }
}
