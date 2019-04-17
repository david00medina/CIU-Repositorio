import processing.core.PApplet;

public class World {
    private PApplet parent;

    public int margin;
    private int background;
    private int[] elementColor;
    private int netLines;
    private int score1;
    private int xScore1;
    private int yScore1;
    private int score2;
    private int xScore2;
    private int yScore2;
    private int scoreSize;
    private int fps;

    public World(PApplet parent, int background, int[] elementColor, int margin, int netLines, int fps, int xScore1,
                 int yScore1, int xScore2, int yScore2, int scoreSize) {
        this.parent = parent;

        this.background = background;
        this.elementColor = elementColor;
        this.margin = margin;
        this.netLines = netLines;
        this.fps = fps;

        score1 = 0;
        this.xScore1 = xScore1;
        this.yScore1 = yScore1;

        score2 = 0;
        this.xScore2 = xScore2;
        this.yScore2 = yScore2;

        this.scoreSize = scoreSize;
    }

    public void refresh() {
        parent.stroke(elementColor[0], elementColor[1], elementColor[2]);
        parent.fill(elementColor[0], elementColor[1], elementColor[2]);
        parent.frameRate(fps);
        buildWorld();
        buildNet();
        buildScore();
    }

    private void buildWorld() {
        parent.background(background);
        parent.strokeWeight(margin);
        parent.line(0,0,parent.width,0);
        parent.line(0,parent.height,parent.width,parent.height);
    }

    private void buildNet() {
        for(int i = 0; i < netLines; i++) {
            int startY = i*(parent.height/(netLines*2) + netLines);
            int endY = startY + parent.height/(netLines*2);
            parent.line(parent.width/2, startY, parent.width/2, endY);
        }
    }

    private void buildScore() {
        parent.textSize(scoreSize);
        parent.text(score1, xScore1, yScore1);
        parent.text(score2, xScore2, yScore2);
    }

    public void refreshScore(int i) {
        switch(i) {
            case 1:
                score1++;
                break;
            case 0:
                score2++;
                break;
        }
    }
}
