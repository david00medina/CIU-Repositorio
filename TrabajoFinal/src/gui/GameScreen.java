package gui;

import control.algorithms.Statistics;
import control.algorithms.Transformation;
import control.kinect.Kinect;
import control.kinect.KinectAnathomy;
import control.kinect.KinectSelector;
import kinect4WinSDK.SkeletonData;
import model.postures.DancerData;
import processing.core.PApplet;
import processing.core.PShape;

import java.util.List;

public class GameScreen extends Screen
{
    private static final int SCALE = 60;
    private static final int COLS = 60;
    private static final int ROWS = 60;

    private Kinect kinect;
    private PShape floor;

    private boolean printHeader = false;

    private List<DancerData> ddl;

    GameScreen(PApplet parent, String csvPath) {
        super(parent, csvPath);

        kinect = new Kinect(this.parent, null, null, null);
        kinect.setHandRadius(0);

        createFloor();

        preprocessDancerData();
    }

    private void preprocessDancerData() {
        ddl = readDancerDataFromCSV();
        translateToOrigin();
    }

    private void translateToOrigin() {
        for (DancerData dd :
                ddl) {
            Transformation.translateToOrigin(parent, dd, KinectAnathomy.SPINE);
        }
    }

    public void show() {
        kinect.doSkeleton(true);
        kinect.refresh(KinectSelector.NONE, true);

        DancerData liveDancer = new DancerData(parent, kinect);
        Transformation.translateToOrigin(parent, liveDancer, KinectAnathomy.SPINE);

        DancerData ddCSV = getDancerDataByUUID("37f46031-9e19-4ac5-afe0-5d99974faf8e");

        System.out.println(Statistics.euclideanMSE(parent, liveDancer, ddCSV));
        makeFloor();
    }

    private DancerData getDancerDataByUUID(String uuid) {
        for (DancerData dd :
                ddl) {
            if (dd.getDancerUUID().equals(uuid)) return dd;
        }
        return null;
    }

    private void makeFloor() {
        parent.pushMatrix();
        parent.translate(-ROWS * SCALE / 2.f, 400, -COLS * SCALE /2.f);
        parent.shape(floor);
        parent.popMatrix();
    }

    private void createFloor() {
        parent.stroke(255);
        parent.noFill();

        floor = parent.createShape();
        for (int z = 0; z < COLS; z++) {
            floor.beginShape(parent.QUAD_STRIP);
            for (int x = 0; x < ROWS; x++) {
                floor.vertex(x * SCALE, 0, z * SCALE);
                floor.vertex(x * SCALE, 0, (z+1) * SCALE);
            }
            floor.endShape();
        }
    }

    void appearEvent(SkeletonData _s) {
        kinect.appearEvent(_s);
    }

    void disappearEvent(SkeletonData _s) {
        kinect.disappearEvent(_s);
    }

    void moveEvent(SkeletonData _b, SkeletonData _a) {
        kinect.moveEvent(_b, _a);
    }
}
