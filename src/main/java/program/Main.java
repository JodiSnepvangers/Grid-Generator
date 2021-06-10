package program;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//Made by: Kitty Soldier.
//This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
//To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.


public class Main {
    static final double programVersion = 1.0;
    static final String windowTitle = "Grid Generator V" + programVersion + ". Made by Kitty Soldier";

    static final Color foreGround = new Color(168, 168, 168, 255);
    static final Color backGround = new Color(137, 137, 137, 255);
    static final Color borderColor = new Color(0, 0, 0, 255);

    static BufferedImage catIcon;
    static BufferedImage catConfuseIcon;
    static BufferedImage catAlertIcon;
    static BufferedImage helpPage;

    static final String inputName = "Input";
    static final String outputName = "Output";

    static DirectoryScanner scanner = new DirectoryScanner();
    static GridFrame gridFrame;

    //program variables:
    public static ImageContainer selectedImage = null;



    public static void main(String[] args){
        //load local images:
        try {
            catIcon = ImageIO.read(Main.class.getResourceAsStream("/CatIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
            catIcon = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
        }
        try {
            helpPage = ImageIO.read(Main.class.getResourceAsStream("/HelpPage.png"));
        } catch (IOException e) {
            e.printStackTrace();
            helpPage = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
        }
        try {
            catConfuseIcon = ImageIO.read(Main.class.getResourceAsStream("/CatQuestionIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
            catConfuseIcon = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
        }
        try {
            catAlertIcon = ImageIO.read(Main.class.getResourceAsStream("/CatAlertIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
            catAlertIcon = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
        }


        scanner.scanDirectory();
        //selectg first image loaded:
        if(scanner.getImageList().size() > 0){
            selectedImage = scanner.getImageList().get(0);
        }


        gridFrame = new GridFrame();

        gridFrame.updatePreview();
    }
}
