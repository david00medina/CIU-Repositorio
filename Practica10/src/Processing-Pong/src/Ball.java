import java.lang.Math;
import java.util.Random;

import processing.core.PApplet;
import processing.sound.*;

public class Ball {
    private PApplet parent;

    public int x;
    public int y;
    public final int DIAMETER;
    public int xSpeed;
    public int ySpeed;
    public int xDir;
    public int yDir;
    private int[] ballColor;


    public Ball(PApplet parent, int ballPos, int DIAMETER, int xSpeed, int ySpeed, int xDir, int yDir, int[] ballColor) {
        this.parent = parent;

        this.x = ballPos;
        this.y = ballPos;
        this.DIAMETER = DIAMETER;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.xDir = xDir;
        this.yDir = yDir;
        this.ballColor = ballColor;
    }

    public void spawn() {
        x = parent.width/2;
        y = (int)parent.random(parent.height);

        Random rand = new Random();
        yDir = (int)Math.pow(-1, rand.nextInt(3) + 1);
    }

    public void refresh() {
        parent.stroke(ballColor[0], ballColor[1], ballColor[2]);
        parent.fill(ballColor[0], ballColor[1], ballColor[2]);
        parent.ellipse(x, y, DIAMETER, DIAMETER);
    }
}
