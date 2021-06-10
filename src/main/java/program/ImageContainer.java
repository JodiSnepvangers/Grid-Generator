package program;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageContainer {
    /**
     *
     * container for image. contains the file, name, and extention.
     *
     */

    private File imageFile; //actual file
    private String fileName; //file name (without extension)
    private fileExt fileExtension; //file extension ( without file name)
    private BufferedImage fileImage = null; //actual image (load once, read many)
    private LoadingState imageLoaded = LoadingState.NOTLOADED; //determines if image was loaded correctly already. if so, dont reload!
    private LoadingThread loadingThread = new LoadingThread();

    public ImageContainer(File imageFile){
        this.imageFile = imageFile;



        //retrieve file name:
        fileName = findFilename();

        //set extension
        String tempExt = findFileExtension();
        if(tempExt.toLowerCase().equals(".png")){
            fileExtension = fileExt.PNG;
        } else if(tempExt.toLowerCase().equals(".jpg") || tempExt.toLowerCase().equals(".jpeg")){
            fileExtension = fileExt.JPG;
        } else {
            fileExtension = fileExt.OTHER;
        }
    }

    //get functions

    public File getImageFile() {
        return imageFile;
    }

    public String getFileName() {
        return fileName;
    }

    public fileExt getFileExtension(){
        return fileExtension;
    }

    public void refreshImage(){
        //forces the image to get reloaded from disk, even if it wasnt loaded before
        imageLoaded = LoadingState.NOTLOADED;
        fileImage = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
        startLoadingThread();
        Main.gridFrame.updatePreview();
    }

    public BufferedImage getImage() {
        if(fileExtension == fileExt.OTHER){
            return new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
        }
        if(imageLoaded == LoadingState.NOTLOADED) {
            //image has not yet been loaded! load it now
            fileImage = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
            startLoadingThread();
        }
        //BufferedImage newImage = new BufferedImage(fileImage.getWidth(), fileImage.getHeight(), fileImage.getType());
        return fileImage;
    }

    //saves image after applying grid and cut. returns false if the image hasnt been saved
    public void saveImage(boolean cut){
        BufferedImage finalImage = getImage();
        if(finalImage.getHeight() == 1)return;
        if(cut){
            Dimension newSize = GridGenerator.calculateImageCut(finalImage.getWidth(), finalImage.getHeight());
            BufferedImage replacement = new BufferedImage(newSize.width, newSize.height, finalImage.getType());
            Graphics2D replacementGraphics = replacement.createGraphics();
            replacementGraphics.drawImage(finalImage, 0, 0, null);
            replacementGraphics.dispose();
            finalImage = replacement;
        }
        Graphics2D graphics2D = finalImage.createGraphics();
        GridGenerator.drawGrid(graphics2D, finalImage.getWidth(), finalImage.getHeight());
        graphics2D.dispose();
        Main.scanner.saveFile(finalImage, fileName, fileExtension);

    }

    //private functions:

    private String findFilename() {
        String fileName = imageFile.getName();
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }

    private String findFileExtension() {
        String name = imageFile.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    private class LoadingThread extends Thread{
        //handles loading the image from file!


        @Override
        public void run() {
            try {
                fileImage = ImageIO.read(new File(imageFile.getPath()));
                imageLoaded = LoadingState.LOADED;
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "error: unable to find image");
                fileImage = new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR);
                imageLoaded = LoadingState.NOTLOADED;
            }
            Main.gridFrame.updatePreview();
            loadingThread = new LoadingThread();
        }
    }

    private enum LoadingState{
        NOTLOADED, //image has not been requested yet
        LOADING, //image has been requested and is getting loaded
        LOADED, //image is correctly loaded!
    }

    private void startLoadingThread(){
        if(loadingThread.isAlive())return;
        loadingThread.start();
    }
}

enum fileExt{
    JPG,
    PNG,
    OTHER
}
