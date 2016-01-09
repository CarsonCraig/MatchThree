/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package match3;

import java.util.Random;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/**
 *
 * @author carson
 */
public class Match3 {

    public static int pixel = 25;
    public static int borderSize = 1;
    public static int jewelNumber = 3;
    public static int tileMatchNumber = 2;
    public static int[][] jewel;
    public static boolean[][] jewelBreak;
    public static int[][] goldenSquare;
    public static boolean[][] closedSquare;
    public static Random newRandom = new Random();
    public static boolean leftClick;
    public static boolean startGame;

    public static int selectedX = -1;
    public static int selectedY = -1;

    public static void main(String[] args) {
        renderGL();
        closedSquare = new boolean[800 / pixel][600 / pixel];
        createBoard();
        gameLoop();
    }

    public static void gameLoop() {
        while (!Display.isCloseRequested()) {
            mouseInput();
            keyboardInput();
            drawBoard();
            updateMatch();
            jewelFall();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();

    }

    public static void mouseInput() {
        try {
            Mouse.setGrabbed(false);
            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        int jewelValue = 0;

        int squareX = (int) Math.ceil(Mouse.getX() / pixel);
        int squareY = (int) Math.ceil(Mouse.getY() / pixel);

        if (Mouse.isButtonDown(0) && !leftClick && selectedX == -1 && selectedY == -1) {

            selectedX = squareX;
            selectedY = squareY;

            leftClick = true;
        }

        if (!Mouse.isButtonDown(0) && selectedX != -1 && selectedY != -1) {

            if (squareX > selectedX && squareY == selectedY) {
                jewelValue = jewel[selectedX][selectedY];
                jewel[selectedX][selectedY] = jewel[selectedX + 1][selectedY];
                jewel[selectedX + 1][selectedY] = jewelValue;
                updateMatch();
            } else if (squareX < selectedX && squareY == selectedY) {
                jewelValue = jewel[selectedX][selectedY];
                jewel[selectedX][selectedY] = jewel[selectedX - 1][selectedY];
                jewel[selectedX - 1][selectedY] = jewelValue;
                updateMatch();
            } else if (squareX == selectedX && squareY > selectedY) {
                jewelValue = jewel[selectedX][selectedY];
                jewel[selectedX][selectedY] = jewel[selectedX][selectedY + 1];
                jewel[selectedX][selectedY + 1] = jewelValue;
                updateMatch();
            } else if (squareX == selectedX && squareY < selectedY) {
                jewelValue = jewel[selectedX][selectedY];
                jewel[selectedX][selectedY] = jewel[selectedX][selectedY - 1];
                jewel[selectedX][selectedY - 1] = jewelValue;
                updateMatch();
            }

            if(jewel[selectedX][selectedY]!=0){
            if (squareX > selectedX && squareY == selectedY) {

                if (!jewelBreak[selectedX][selectedY] && !jewelBreak[selectedX + 1][selectedY]) {
                    jewelValue = jewel[selectedX][selectedY];
                    jewel[selectedX][selectedY] = jewel[selectedX + 1][selectedY];
                    jewel[selectedX + 1][selectedY] = jewelValue;
                    System.out.println("Stop");
                }
            } else if (squareX < selectedX && squareY == selectedY) {
                if (!jewelBreak[selectedX][selectedY] && !jewelBreak[selectedX - 1][selectedY]) {
                    jewelValue = jewel[selectedX][selectedY];
                    jewel[selectedX][selectedY] = jewel[selectedX - 1][selectedY];
                    jewel[selectedX - 1][selectedY] = jewelValue;
                    System.out.println("Stop");
                }
            } else if (squareX == selectedX && squareY > selectedY) {
                if (!jewelBreak[selectedX][selectedY] && !jewelBreak[selectedX][selectedY + 1]) {
                    jewelValue = jewel[selectedX][selectedY];
                    jewel[selectedX][selectedY] = jewel[selectedX][selectedY + 1];
                    jewel[selectedX][selectedY + 1] = jewelValue;
                    System.out.println("Stop");
                }
            } else if (squareX == selectedX && squareY < selectedY) {
                if (!jewelBreak[selectedX][selectedY] && !jewelBreak[selectedX][selectedY - 1]) {
                    jewelValue = jewel[selectedX][selectedY];
                    jewel[selectedX][selectedY] = jewel[selectedX][selectedY - 1];
                    jewel[selectedX][selectedY - 1] = jewelValue;
                    System.out.println("Stop");
                }
            }
            }
            
            if (!startGame) {
                goldenSquare = new int[800 / pixel][600 / pixel];
                startGame = true;
            }

            selectedX = -1;
            selectedY = -1;
            leftClick = false;
        }
        
        
        if (Mouse.isButtonDown(0) && Keyboard.isKeyDown(Keyboard.KEY_S) && closedSquare[squareX][squareY]==false) {
            closedSquare[squareX][squareY]=true;
            jewel[squareX][squareY]=0;
        }
        if (Mouse.isButtonDown(1) && Keyboard.isKeyDown(Keyboard.KEY_S) && closedSquare[squareX][squareY]==true) {
            closedSquare[squareX][squareY]=false;
            jewel[squareX][squareY]=0;
        }
    }

    public static void keyboardInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                    setDisplayMode(800, 600, !Display.isFullscreen());
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_UP && jewelNumber<12) {
                    closedSquare = new boolean[800 / pixel][600 / pixel];
                    jewelNumber++;
                    createBoard();
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && jewelNumber>3) {
                    jewelNumber--;
                    createBoard();
                }
            }
        }
    }

    public static void createBoard() {

        
        
        jewel = new int[800 / pixel][600 / pixel];
        jewelBreak = new boolean[800 / pixel][600 / pixel];
        goldenSquare = new int[800 / pixel][600 / pixel];
        startGame = false;

        boolean endLoop=false;
        
        while(!endLoop){
        
        endLoop=true;
        for (int xi = 0; xi < 800 / pixel; xi++) {

            for (int yi = 0; yi < 600 / pixel; yi++) {

                if(jewel[xi][yi]==0 && !closedSquare[xi][yi]){

                jewel[xi][yi] = (int) (newRandom.nextDouble() * jewelNumber) + 1;
                endLoop=false;
                }
            }
        }
        updateMatch();
        jewelFall();
        
        }
    }

    public static void drawBoard() {
        int squareX = (int) Math.ceil(Mouse.getX() / pixel);
        int squareY = (int) Math.ceil(Mouse.getY() / pixel);

        GL11.glColor3ub((byte) 51, (byte) 25, (byte) 0);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(800, 0);
        GL11.glVertex2f(800, 600);
        GL11.glVertex2f(0, 600);
        GL11.glEnd();

        if (!Keyboard.isKeyDown(Keyboard.KEY_S) && selectedX != -1 && selectedY != -1) {
            updateColour(jewel[selectedX][selectedY]);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(selectedX * pixel - borderSize, 600 - (selectedY * pixel - borderSize));
            GL11.glVertex2f(selectedX * pixel + pixel + borderSize, 600 - (selectedY * pixel - borderSize));
            GL11.glVertex2f(selectedX * pixel + pixel + borderSize, 600 - (selectedY * pixel + pixel + borderSize));
            GL11.glVertex2f(selectedX * pixel - borderSize, 600 - (selectedY * pixel + pixel + borderSize));
            GL11.glEnd();
        }

        for (int xi = 0; xi < 800 / pixel; xi++) {

            for (int yi = 0; yi < 600 / pixel; yi++) {

                if(closedSquare[xi][yi]){
                    GL11.glColor3ub((byte) 51, (byte) 25, (byte) 0);
                }
                else if (startGame) {
                    if(goldenSquare[xi][yi]==tileMatchNumber)GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
                    else GL11.glColor3ub((byte) (200 * goldenSquare[xi][yi]/tileMatchNumber), (byte) (200 * goldenSquare[xi][yi]/tileMatchNumber), (byte) (200 * goldenSquare[xi][yi]/tileMatchNumber));
                } 
                else {
                    GL11.glColor3ub((byte) 0, (byte) 0, (byte) 0);
                } 

                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2f(xi * pixel + borderSize, 600 - (yi * pixel + borderSize));
                GL11.glVertex2f(xi * pixel + pixel - borderSize, 600 - (yi * pixel + borderSize));
                GL11.glVertex2f(xi * pixel + pixel - borderSize, 600 - (yi * pixel + pixel - borderSize));
                GL11.glVertex2f(xi * pixel + borderSize, 600 - (yi * pixel + pixel - borderSize));
                GL11.glEnd();

            }
        }

        int sides = 1;
        int rotate = 0;

        float DEG2RAD = (float) (3.14159 / 180);

        if (!Keyboard.isKeyDown(Keyboard.KEY_D)) {

            for (int xi = 0; xi < 800 / pixel; xi++) {

                for (int yi = 0; yi < 600 / pixel; yi++) {

                    if (!closedSquare[xi][yi] && jewel[xi][yi] != 0){
                    
                    double sizeMultiplier = 1;
                    if ((squareX == xi && squareY == yi)) {
                        sizeMultiplier = 1.25;
                    }

                    switch(jewel[xi][yi]){
                    
                        case 1:
                            sides = 20;
                            rotate = 90;
                            break;
                            
                        case 2:
                            sides = 3;
                            rotate = 90;
                            break;
                            
                        case 3:
                            sides = 4;
                            rotate = 90;
                            break;
                            
                        case 4:
                            sides = 5;
                            rotate = 90;
                            break;
                            
                        case 5:
                            sides = 6;
                            rotate = 90;
                            break;
                            
                        case 6:
                            sides = 3;
                            rotate = 270;
                            break;
                            
                        case 7:
                            sides = 4;
                            rotate = 45;
                            break;
                            
                        case 8:
                            sides = 5;
                            rotate = 270;
                            break;

                            
                        case 9:
                            sides = 6;
                            rotate = 0;
                            break;
 
                        case 10:
                            sides = 3;
                            rotate = 0;
                            break;
                            
                        case 11:
                            sides = 3;
                            rotate = 180;
                            break;
                            
                        case 12:
                            sides = 8;
                            rotate = 90;
                            break;
                            
                        default:
                            sides = 10;
                            rotate = 90;
                            break;
                                                
                    }        
                            
                            
                    updateColour(-1);

                    GL11.glBegin(GL11.GL_POLYGON);
                    for (float i = rotate; i < 360f+rotate; i = i + 360 / sides) {
                        float degInRad = (i) * DEG2RAD;
                        GL11.glVertex2f((float) (Math.cos(degInRad) * pixel * sizeMultiplier * 6 / 15 + (xi + 0.5) * pixel), 600 - (float) (Math.sin(degInRad) * pixel * sizeMultiplier * 6 / 15 + (yi + 0.5) * pixel));
                    }
                    GL11.glEnd();

                    updateColour(jewel[xi][yi]);

                    GL11.glBegin(GL11.GL_POLYGON);

                    for (float i = rotate; i < 360f+rotate; i = i + 360 / sides) {
                        float degInRad = (i) * DEG2RAD;
                        GL11.glVertex2f((float) (Math.cos(degInRad) * pixel * sizeMultiplier * 5 / 15 + (xi + 0.5) * pixel), 600 - (float) (Math.sin(degInRad) * pixel * sizeMultiplier * 5 / 15 + (yi + 0.5) * pixel));
                    }
                    GL11.glEnd();
                    }
                }
            }
        }

    }

    public static void updateMatch() {
        int[][] flag=new int[800/pixel][600/pixel];
        

        for (int xi = 0; xi < 800 / pixel; xi++) {

            for (int yi = 0; yi < 600 / pixel; yi++) {

                if (jewel[xi][yi]!=0){
                
                int rightCount = -1;
                int leftCount = -1;
                int upCount = -1;
                int downCount = -1;

                for (int i = 0; jewel[xi][yi] == jewel[xi + i][yi]; i++) {
                    rightCount++;
                    if (i + xi + 1 >= 800 / pixel) {
                        break;
                    }
                }
                for (int i = 0; jewel[xi][yi] == jewel[xi - i][yi]; i++) {
                    leftCount++;
                    if (xi - i - 1 < 0) {
                        break;
                    }
                }
                for (int i = 0; jewel[xi][yi] == jewel[xi][yi + i]; i++) {
                    upCount++;
                    if (i + yi + 1 >= 600 / pixel) {
                        break;
                    }
                }
                for (int i = 0; jewel[xi][yi] == jewel[xi][yi - i]; i++) {
                    downCount++;
                    if (yi - i - 1 < 0) {
                        break;
                    }
                }

                if (rightCount + leftCount >= 2) {
                    for (int i = -leftCount; i <= rightCount; i++) {
                        jewelBreak[xi + i][yi] = true;
                    }
                }

                if (upCount + downCount >= 2) {
                    for (int i = -downCount; i <= upCount; i++) {
                        jewelBreak[xi][yi + i] = true;
                    }
                }
                
                }
            }

        }

    }

    public static void jewelFall() {
        for (int xi = 0; xi < 800 / pixel; xi++) {
            for (int yi = 0; yi < 600 / pixel; yi++) {
                if (jewelBreak[xi][yi]) {
                    jewel[xi][yi] = 0;
                    jewelBreak[xi][yi] = false;
                    if(goldenSquare[xi][yi]<tileMatchNumber)goldenSquare[xi][yi]++;
                }
            }

        }

        for (int xi = 0; xi < 800 / pixel; xi++) {
            int indexEmpty = -1;
            for (int yi = 0; yi < 600 / pixel; yi++) {
                if (jewel[xi][yi] == 0) {
                    indexEmpty = yi;
                }
                if(closedSquare[xi][yi]&&yi!=600 / pixel){
                    indexEmpty = -1;
                }
                
                if (indexEmpty != -1 && jewel[xi][yi] != 0) {
                    jewel[xi][indexEmpty] = jewel[xi][yi];
                    jewel[xi][yi] = 0;
                    indexEmpty = -1;
                    yi = 0;
                }

            }
        }

                for (int xi = 0; xi < 800 / pixel; xi++) {
            for (int yi = (600 / pixel) - 1; yi >= 0; yi--) {
                                if (jewel[xi][yi] == 0 && !closedSquare[xi][yi]) {
                    jewel[xi][yi] = (int) (newRandom.nextDouble() * jewelNumber) + 1;
                }
                                if (!closedSquare[xi][yi])break;
            }
                }
    
    }

    public static void updateColour(int index) {
         if (index == -1) {
            GL11.glColor3ub((byte) 0, (byte) 0, (byte) 0);
        }else if (index == 0) {
            GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
        } else if (index == 1) {
            GL11.glColor3ub((byte) 255, (byte) 0, (byte) 0);
        } else if (index == 2) {
            GL11.glColor3ub((byte) 0, (byte) 255, (byte) 0);
        } else if (index == 3) {
            GL11.glColor3ub((byte) 0, (byte) 0, (byte) 255);
        } else if (index == 4) {
            GL11.glColor3ub((byte) 255, (byte) 255, (byte) 0);
        } else if (index == 5) {
            GL11.glColor3ub((byte) 0, (byte) 255, (byte) 255);
        } else if (index == 6) {
            GL11.glColor3ub((byte)255,(byte)0,(byte)255);
        } else if (index == 7) {
            GL11.glColor3ub((byte)255,(byte)69,(byte)0);
        } else if (index == 8) {
            GL11.glColor3ub((byte)0,(byte)51,(byte)51);
        } else if (index == 9) {
            GL11.glColor3ub((byte)0,(byte)60,(byte)0);
        } else if (index == 10) {
            GL11.glColor3ub((byte)75,(byte)0,(byte)130);
        }
         else if (index == 11) {
            GL11.glColor3ub((byte)85,(byte)107,(byte)47);
        }
         else if (index == 12) {
            GL11.glColor3ub((byte)188,(byte)143,(byte)143);
        }
    }

    public static void renderGL() {
        try {
            Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glViewport(0, 0, 800, 600);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        Display.setVSyncEnabled(true);
    }

    public static void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i = 0; i < modes.length; i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }
}
