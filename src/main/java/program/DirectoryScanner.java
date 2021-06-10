package program;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner {

    /**
     *
     * creates a input folder and scans it for possible files!
      */

    private File inputFolder = null;
    private File outputFolder = null;

    private List<ImageContainer> imageList = new ArrayList<>();


    public List<ImageContainer> scanDirectory() {
        //empty image list:
        imageList = new ArrayList<>();

        //create folders if none exist:
        generateFolders();

        //now scan those folders for any compatible files
        imageList = scanFiles();

        //return imaghe list
        return imageList;
    }

    public List<ImageContainer> getImageList() {
        //returns image list without actually refreshing it
        return imageList;
    }

    //gets all image names
    public List<String> getImageNames(){
        List<String> output = new ArrayList<>();
        for(ImageContainer imageContainer : imageList){
            output.add(imageContainer.getFileName());
        }
        return output;
    }

    //finds image based on image name, or returns null if none is found
    public ImageContainer findImage(String imageName){
        for(ImageContainer imageContainer : imageList){
            if(imageContainer.getFileName().equals(imageName)){
                return imageContainer;
            }
        }
        JOptionPane.showMessageDialog(null, "error: image not found by name");
        return null;
    }



    private void generateFolders(){
        //get base path
        File currentDir = new File("");
        String basePath = currentDir.getAbsolutePath();
        //generating input folder
        inputFolder = new File(basePath + "/" + Main.inputName);
        if(inputFolder.exists() == false){
            inputFolder.mkdir();
            JOptionPane.showMessageDialog(null, "Input folder created!\nPlease copy your images there");
        }

        //generating output folder
        outputFolder = new File(basePath + "/" + Main.outputName);
        if(outputFolder.exists() == false){
            outputFolder.mkdir();
        }
    }

    private List<ImageContainer> scanFiles(){
        //create empty output list:
        List<ImageContainer> output = new ArrayList<>();
        for(File file : inputFolder.listFiles()){
            ImageContainer imageContainer = new ImageContainer(file);
            if(imageContainer.getFileExtension() == fileExt.OTHER){
                continue; //file was not a supported file type
            }

            //found image file! add it to list!
            output.add(imageContainer);
        }

        //all files have been scanned and filtered!
        return output;
    }

    public void saveFile(BufferedImage image, String name, fileExt extension){
        if(extension == fileExt.OTHER)return;
        File outputfile = new File(outputFolder + "/" + name + ".png");
        SavingThread savingThread = new SavingThread(outputfile, image);
        savingThread.start();
    }

    private class SavingThread extends Thread{
        //offload saving to a different thread

        File output;
        BufferedImage image;

        public SavingThread(File output, BufferedImage image){
            this.output = output;
            this.image = image;
        }

        @Override
        public void run() {
            try {
                ImageIO.write(image, "png", output);
            } catch (IOException e) {
                e.printStackTrace();
                SettingsPage.saveTextUpdate(false);
            }
            SettingsPage.saveTextUpdate(true);
        }
    }
}
