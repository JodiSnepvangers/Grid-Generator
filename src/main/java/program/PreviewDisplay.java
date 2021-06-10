package program;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PreviewDisplay extends JLabel {

    public static boolean showCutRegion = true;
    public static boolean showHelpPage = false;

    /**
     *
     * previews the changes thats gonna be applied to the image without actually applying them
     *
     */
    //internal parameters:
    public BufferedImage displayImage = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);

    //functions:

    public void setDisplayImage(BufferedImage image){
        this.displayImage = image;
        setSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(displayImage.getWidth(), displayImage.getHeight());
    }

    //draw routine:

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.clipRect(0, 0, displayImage.getWidth(), displayImage.getHeight());

        if(displayImage.getHeight() != 1) {
            //first draw image:
            graphics2D.drawImage(displayImage, 0, 0, null);

            //draw grid preview:
            GridGenerator.drawGrid(graphics2D, displayImage.getWidth(), displayImage.getHeight());
        }




        if(showCutRegion){
            //show new image size
            graphics2D.setColor(new Color(255, 0, 0, 100));
            Dimension cutSize = GridGenerator.calculateImageCut(displayImage.getWidth(), displayImage.getHeight());

            if(cutSize != null) {

                //draw first box
                int posX = cutSize.width;
                int posY = 0;
                int width = displayImage.getWidth() - cutSize.width;
                int height = displayImage.getHeight();
                graphics2D.fillRect(posX, posY, width, height);

                //draw second box
                posX = 0;
                posY = cutSize.height;
                width = cutSize.width;
                height = displayImage.getHeight() - cutSize.height;
                graphics2D.fillRect(posX, posY, width, height);
            }
        }

        graphics2D.setClip(null);

        //draw help page:
        if(showHelpPage){

            //get viewport to offset drawing correctly
            JViewport viewport = Main.gridFrame.scroller.getViewport();
            graphics2D.setColor(new Color(50, 50, 50, 200));
            graphics2D.fillRect(viewport.getViewPosition().x,viewport.getViewPosition().y,Main.gridFrame.getWidth(),Main.gridFrame.getHeight());

            //draw help page to the top right of screen
            int posX = viewport.getViewPosition().x + (viewport.getViewRect().width - Main.helpPage.getWidth() - 1);
            graphics2D.drawImage(Main.helpPage, posX,viewport.getViewPosition().y, null);
        }

    }
}
