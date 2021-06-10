package program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class GridFrame extends JFrame {

    /**
     *
     * this is the display frame and interface to the user
     *
     */
    JScrollPane scroller = new JScrollPane();
    SettingsPage settingsPage = new SettingsPage();
    PreviewDisplay previewDisplay = new PreviewDisplay();

    public GridFrame(){
        //set up frame parameters:
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(700, 610));
        setBackground(Main.backGround);
        setTitle(Main.windowTitle);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //create components;
        add(settingsPage, BorderLayout.EAST);
        scroller.setViewportView(previewDisplay);
        scroller.getHorizontalScrollBar().setUnitIncrement(13);
        scroller.getVerticalScrollBar().setUnitIncrement(10);
        add(scroller, BorderLayout.CENTER);

        //make visible
        setVisible(true);
    }

    public void updatePreview(){
        //updates preview: load image from main
        if(Main.selectedImage != null) {
            previewDisplay.setDisplayImage(Main.selectedImage.getImage());
        }

        //update window
        repaint();
    }
}
