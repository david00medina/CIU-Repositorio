package gui;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.HashMap;

public class PreludeScreen extends Screen {
    private final String MASKS_BASE_DIR;

    private PApplet parent;
    private String playerName;

    private boolean boolScreenSelect = true;
    private boolean boolScreenRanking = false;
    private ArrayList<PImage> mask = new ArrayList<PImage>();
    private int maskSelected = 0;

    private Table ranking;
    private PImage crown, gold, silver, bronze, titulo;

    PreludeScreen(PApplet parent, HashMap<UISelector, PImage> uiResources, String rankingPath, String maskPath) {
        super(parent, uiResources);
        this.parent = parent;

        MASKS_BASE_DIR = maskPath;

        playerName = "Jugador1";
        loadMask();
        ranking = this.parent.loadTable(rankingPath,"header");

        loadImgRanking();
    }

    void loadImgRanking(){
        crown = UIResources.get(UISelector.CROWN);
        crown.resize(0,50);
        gold = UIResources.get(UISelector.GOLD_MEDAL);
        gold.resize(0,20);
        silver = UIResources.get(UISelector.SILVER_MEDAL);
        silver.resize(0,20);
        bronze = UIResources.get(UISelector.BRONZE_MEDAL);
        bronze.resize(0,20);
        titulo = UIResources.get(UISelector.TITLE);
        titulo.resize(0, 120);
    }

    private void loadMask() {
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask8.png"));
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask7.png"));
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask1.png"));
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask2.png"));
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask3.png"));
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask4.png"));
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask5.png"));
        mask.add(parent.loadImage(MASKS_BASE_DIR + "mask6.png"));
    }

    public void show() {
        parent.background(0);
        parent.rectMode(parent.CORNER);
        parent.textMode(parent.CORNER);
        parent.textAlign(parent.CORNER, parent.CORNER);
        parent.imageMode(parent.CORNER);
        if (boolScreenSelect == true) screenSelectName();
        if (boolScreenRanking == true) screenRanking();
    }

    void loadScore(String nombre, int points){
        TableRow newRow = ranking.addRow();
        newRow.setString("Name",nombre);
        newRow.setInt("Score",points);
        parent.saveTable(ranking, MASKS_BASE_DIR + "ranking.csv");
    }

    void screenRanking(){
        ranking.setColumnType(1, "int");
        ranking.sortReverse("Score");
        parent.fill(255);
        parent.textSize(26);
        parent.text("Ranking: ", 100, parent.height/5-30 );
        parent.textSize(18);
        int Yvalue = parent.height/4;
        parent.text("Name ", 100,Yvalue-10 );
        parent.text("Score ", 300, Yvalue-10 );
        showRankingImg(Yvalue);
        int max10 = 0; //Solo mostramos un max de 10
        for (TableRow row : ranking.rows()) {
            Yvalue +=25;
            parent.text(row.getString("Name"), 100, Yvalue );
            parent.text(row.getString("Score"), 300, Yvalue );
            if(++max10 >= 10) break;
        }

        //Boton de Continuar
        parent.fill(85, 154, 232);
        parent.rect(parent.width/2-100/2, parent.height-70, 100, 50,7); //rectangulo para el texto
        parent.fill(255);
        parent.text("Continue", parent.width/2 - 40, parent.height-38 );
    }

    void showRankingImg(int Yvalue){
        parent.image(crown, 240, parent.height/5-66);
        parent.image(gold, 360, Yvalue+10);
        parent.image(silver, 360, Yvalue+35);
        parent.image(bronze, 360, Yvalue+60);
        parent.image(titulo, 420, 30);
    }

    void screenSelectName(){
        parent.background(0);

        PImage back = UIResources.get(UISelector.BACK);
        parent.image(back, 10, 10);

        parent.textSize(26);
        parent.fill(255);
        parent.text("Nombre del jugador: ", 100, parent.height/5 );
        parent.stroke(100);
        parent.fill(100);

        parent.rect(100, parent.height/4, 310, 50); //rectangulo para el texto
        parent.fill(255);
        parent.text(playerName, 120, parent.height/3-5 );
        screenSelectMask();

        //Boton de Play
        parent.fill(85, 154, 232);
        parent.rect(parent.width/2-100/2, parent.height-70, 100, 50,7); //rectangulo para el texto
        parent.fill(255);
        parent.text("Play", parent.width/2 - 50/2, parent.height-38 );
    }

    void screenSelectMask() {
        parent.fill(255);
        parent.text("Mascara del jugador: ", 100, parent.height/2 );

        PImage m = mask.get(maskSelected);
        m.resize(0,100);
        parent.image(m, 280, parent.height / 2 + 50);

        if(maskSelected == 0) parent.fill(100); //si no se puede pulsar mas se pone en gris
        parent.triangle(200,300,180,330,200,360);
        parent.fill(255);
        if (mask.size() -1 == maskSelected) parent.fill(100);
        parent.triangle(440,300,460,330,440,360);
    }

    boolean mouseOverBack() {
            //Boton de ir al menú principal
        return (parent.mouseX >= 10 && parent.mouseX <= 60) && (parent.mouseY >= 10 && parent.mouseY <= 60);
    }

    boolean mouseOverPlay() {
        //Boton de jugar
        return (parent.mouseX >= parent.width / 2 - 100 / 2 && parent.mouseX <= parent.width / 2 - 100 / 2 + 100)
                && (parent.mouseY >= parent.height - 70 && parent.mouseY <= parent.height - 70 + 50);
    }

    boolean mouseOverMainScreen() {
        return mouseOverPlay();
    }

    void mouseOverLeft() {
        if ((parent.mouseX >= 180 && parent.mouseX <= 200)&&(parent.mouseY >= 300 && parent.mouseY <= 360) && maskSelected > 0)
            maskSelected--;
    }

    void mouseOverRight() {
        if ((parent.mouseX >= 440 && parent.mouseX<=460)&&(parent.mouseY >= 300 && parent.mouseY <= 360) && maskSelected<mask.size()-1)
            maskSelected++;
    }

    void keyboardTextArea() {
        if (parent.key == 8 && playerName.length()>0) //borrar
            playerName = playerName.substring(0,playerName.length()-1);
        if (playerName.length() >= 20) return; //límite de caracteres
        if (parent.key >= 'A' && parent.key <= 'z') playerName+= parent.key;
        if (parent.key == 32) playerName+= " ";
    }
}
