package main;

import control.csv.CSVTools;
import control.kinect.Kinect;
import control.kinect.KinectAnathomy;
import control.kinect.KinectSelector;
import kinect4WinSDK.SkeletonData;
import model.DancerData;
import org.apache.commons.csv.CSVFormat;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KinectMe extends PApplet {
    private static final int SCALE = 60;
    private static final int COLS = 60;
    private static final int ROWS = 60;

    private Kinect kinect;
    private PShape floor;

    private boolean printHeader = true;

    @Override
    public void settings() {
        super.settings();
        size(640, 480, P3D);
    }

    @Override
    public void setup() {
        super.setup();
        smooth();
        stroke(255);

        kinect = new Kinect(this, null, null, null);
//        control.kinect.setHandRadius(0);

        loadDancerDataCSV();
        createFloor();
    }

    @Override
    public void draw() {
        background(0);

        setCamera();

        kinect.doSkeleton(true);
        kinect.refresh(KinectSelector.NONE, true);

        lights();

        makeFloor();
    }

    private void setCamera() {
        PVector spine = kinect.getSkelPos(KinectAnathomy.SPINE);

        if (spine != null) {
            PVector camPos = new PVector(width/2.f,
                    height/2.f,
                    (height/2.f) / tan(PI * 30.f / 180.f));

            camera(camPos.x, camPos.y, camPos.z,
                    spine.x, spine.y, spine.z,
                    0,1, 0);
        }
    }

    private void makeFloor() {
        pushMatrix();
        translate(-ROWS * SCALE / 2.f, 400, -COLS * SCALE /2.f);
        shape(floor);
        popMatrix();
    }

    private void createFloor() {
        stroke(255);
        noFill();

        floor = createShape();
        for (int z = 0; z < COLS; z++) {
            floor.beginShape(QUAD_STRIP);
            for (int x = 0; x < ROWS; x++) {
                floor.vertex(x * SCALE, 0, z * SCALE);
                floor.vertex(x * SCALE, 0, (z+1) * SCALE);
            }
            floor.endShape();
        }
    }

    private List<DancerData> loadDancerDataCSV() {
        HashMap<String, HashMap<String, List<String>>> allMoves = CSVTools.readCSV(
                Paths.get(".\\res\\dance_postures.csv"),
                CSVFormat.EXCEL, "POSTURE_ID", "LIMB_NAME", "X", "Y", "Z");

        List<DancerData> ddl = new ArrayList<>();

        for (String moveKey :
                allMoves.keySet()) {

            HashMap<KinectAnathomy, PVector> data = new HashMap<>();
            for (String limbKey :
                    allMoves.get(moveKey).keySet()) {

                float x = Float.parseFloat(allMoves.get(moveKey).get(limbKey).get(0));
                float y = Float.parseFloat(allMoves.get(moveKey).get(limbKey).get(1));
                float z = Float.parseFloat(allMoves.get(moveKey).get(limbKey).get(2));

                data.put(KinectAnathomy.getEnumById(limbKey), new PVector(x, y, z));
            }
            ddl.add(new DancerData(moveKey, data));
        }
        return ddl;
    }

    @Override
    public void mouseClicked() {
        DancerData dd = new DancerData(kinect);

        CSVFormat format;
        if (printHeader) {
            format = CSVFormat.EXCEL.withHeader("POSTURE_ID", "LIMB_NAME", "X", "Y", "Z");
            printHeader = false;
        } else
            format = CSVFormat.EXCEL;

        CSVTools.writeCSV(Paths.get(".\\res\\dance_postures.csv"), format, dd.getDancerUUID(), dd.getAnathomyData());
        System.out.println("DANCE POSTURE SAVED (" + dd.getDancerUUID() + ")");
    }

    public void appearEvent(SkeletonData _s) {
        kinect.appearEvent(_s);
    }

    public void disappearEvent(SkeletonData skel) {
        kinect.disappearEvent(skel);
    }

    public void moveEvent(SkeletonData _b, SkeletonData _a) {
        kinect.moveEvent(_b, _a);
    }

    public static void main(String[] args) {
        PApplet.main("main.KinectMe");
    }
}
