import processing.core.PApplet;
import processing.sound.*;

public class Pong extends PApplet {
    private SoundFile hitWallSound;
    private SoundFile hitPaddleSound;
    private SoundFile pointSound;

    private World world;
    private Player playerL;
    private Player playerR;
    private Ball ball;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private static final int background = 0;
    private static final int elementColor[] = {255, 255, 255};
    private static final int margin = 6;
    private static final int netLines = 30;
    private static final int fps = 40;

    private static final int xScore1 = WIDTH / 4 - 50;
    private static final int yScore1 = 150;
    private static final int xScore2 = 3 * WIDTH / 4 - 50;
    private static final int yScore2 = 150;

    private static final int playerWidth = 10;
    private static final int playerHeight = 50;
    private static final int playerMargin = 50;
    private static final int playerSpeed = 50;
    private static final int[] playerColor = {255, 255, 255};

    private static final int ballPos = 30;
    private static final int diameter = 20;
    private static final int ballSpeed = 5;
    private static final int[] ballColor = {255, 255, 255};

    private static final int scoreSize = 128;

    @Override
    public void settings() {
        super.settings();
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        super.setup();

        world = new World(this, background, elementColor, margin, netLines, fps, xScore1, yScore1, xScore2, yScore2, scoreSize);

        playerR = new Player(this, 0, 0, playerWidth, playerHeight, playerMargin, playerSpeed, playerColor);
        playerR.x1 = width - playerR.x2 - playerR.margin;
        playerR.y1 = height / 2 - playerR.x2 / 2;

        playerL = new Player(this, 0, 0, playerWidth, playerHeight, playerMargin, playerSpeed, playerColor);
        playerL.x1 = playerL.x2 + playerL.margin;
        playerL.y1 = height / 2 - playerL.x2 / 2;

        ball = new Ball(this, ballPos, diameter, ballSpeed, ballSpeed, 1, 1, ballColor);
        ball.spawn();

        hitWallSound = new SoundFile(this, "res/ping_pong_8bit_plop.wav");
        hitPaddleSound = new SoundFile(this, "res/ping_pong_8bit_beeep.wav");
        pointSound = new SoundFile(this, "res/ping_pong_8bit_peeeeeep.wav");
    }

    @Override
    public void draw() {
        world.refresh();
        playerL.refresh();
        playerR.refresh();
        boolean[] isPoint = xBallMotion();
        yBallMotion();
        ball.refresh();
        delimitBallMotion();
        for (int i = 0; i < 2; i++) {
            if (isPoint[i]) world.refreshScore(i);
        }
    }


    public static void main(String[] args) {
        PApplet.main("Pong");
    }

    @Override
    public void keyPressed() {
        super.keyPressed();
        if (key == CODED) {
            if (keyCode == UP && playerR.y1 > (0 + world.margin)) playerR.y1 -= playerR.speed;
            else if (keyCode == DOWN && playerR.y1 < (height - (world.margin + playerR.y2 + world.margin)))
                playerR.y1 += playerR.speed;
        } else {
            if (key == 'w' && playerL.y1 > (0 + world.margin)) playerL.y1 -= playerL.speed;
            else if (key == 's' && playerL.y1 < (height - (world.margin + playerL.y2 + world.margin)))
                playerL.y1 += playerL.speed;
        }
    }

    public void delimitBallMotion() {
        if (ball.y < (ball.DIAMETER / 2 + world.margin)) ball.y = ball.DIAMETER / 2 + world.margin;
        else if (ball.y > height - (ball.DIAMETER / 2 + world.margin))
            ball.y = height - (ball.DIAMETER / 2 + world.margin);
    }

    private boolean[] xBallMotion() {
        boolean[] result = {false, false};
        ball.x += ball.xSpeed * ball.xDir;
        int collisionRight = playerR.x1 - (ball.x + ball.DIAMETER);
        int collisionLeft = ball.x - (playerL.x1 + ball.DIAMETER + playerL.x2);

        boolean isCollisionRight = collisionRight < -playerR.x2 && ball.y >= playerR.y1 && ball.y <= (playerR.y1 + playerR.y2);
        boolean isCollisionLeft = collisionLeft < -playerR.x2 && ball.y >= playerL.y1 && ball.y <= (playerL.y1 + playerL.y2);
        if (isCollisionRight || isCollisionLeft) {
            hitPaddleSound.play();
            ball.xDir = -ball.xDir;
        } else if (collisionRight < -playerR.x2 * 10) {
            pointSound.play();
            ball.xDir = 1;
            ball.spawn();
            result[1] = true;
        } else if (collisionLeft < -playerL.x2 * 10) {
            pointSound.play();
            ball.xDir = -1;
            ball.spawn();
            result[0] = true;
        }
        return result;
    }

    private void yBallMotion() {
        ball.y += ball.ySpeed * ball.yDir;
        int collisionTop = ball.y - (ball.DIAMETER / 2 + world.margin);
        int collisionBottom = height - (ball.DIAMETER / 2 + ball.y + world.margin);
        boolean isCollisionTop = collisionTop <= 0;
        boolean isCollisionBottom = collisionBottom <= 0;
        if (isCollisionTop || isCollisionBottom) {
            hitWallSound.play();
            ball.yDir = -ball.yDir;
        }
    }


}