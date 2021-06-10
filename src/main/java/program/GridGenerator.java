package program;

import java.awt.*;
import java.util.logging.Filter;

public class GridGenerator {

    /**
     *
     * takes in a graphics object and draws a grid using the parameters saved here
     *
     */

    public static int gridSpacing = 40;
    public static float gridThickness = 1.5f;
    public static Color gridColor = Color.BLACK;

    public static int textScale = 12;
    public static Color textColor = Color.BLACK;
    public static int labelOpacity = 80;
    public static Color labelColor = new Color(255, 255, 255);

    public static Color transColor = new Color(255,255,255, (int)(labelOpacity * 2.56f));
    public static void updateTransColor(){
        int tempNumber = (int)(labelOpacity* 2.56f);
        tempNumber = Math.min(tempNumber,255);
        transColor = new Color(labelColor.getRed(),labelColor.getGreen(),labelColor.getBlue(), tempNumber);
    }

    public static int textSkip = 5;

    public static FilterType filterType = FilterType.CROSSGRID;


    public enum FilterType{
        BATTLESHIP,
        CROSSGRID,
        LETTERLINE,
        NONE,
        OFF
    }


    public static void drawGrid(Graphics2D graphics2D, int imageSizeX, int imageSizeY){

        //set line thickness
        graphics2D.setStroke(new BasicStroke(gridThickness));
        graphics2D.setColor(gridColor);

        //draw vertical lines
        for(int index = 0; index < imageSizeX; index = index + gridSpacing){
            graphics2D.drawLine(index, 0, index, imageSizeY);
        }

        //draw horizontal lines
        for(int index = 0; index < imageSizeY; index = index + gridSpacing){
            graphics2D.drawLine(0, index, imageSizeX, index);
        }

        //set text parameters:
        graphics2D.setFont(new Font("Dialog.bold", 0,textScale));

        int indexX = 0;
        int indexY = 0;

        //draw text:
        //filter notes: filters return a state: 0 = draw nothing, 1 = draw all, 2 = draw X axis, 3 = draw Y axis
        for(int posX = 0; posX < imageSizeX; posX = posX + gridSpacing) {
            for(int posY = 0; posY < imageSizeY; posY = posY + gridSpacing) {
                int filterState = 0;
                //run selected filter:
                if(filterType == FilterType.CROSSGRID){
                    filterState = crossGridFilter(indexX, indexY);
                } else if(filterType == FilterType.BATTLESHIP) {
                    filterState = battleShipFilter(indexX, indexY);
                } else if(filterType == FilterType.LETTERLINE){
                    filterState = letterLineFilter(indexX, indexY);
                } else if(filterType == FilterType.NONE){
                    filterState = 1;
                }

                placeMarker(posX, posY, indexX, indexY, graphics2D, filterState);

                indexY++;
            }
            indexX++;
            indexY = 0;
        }
    }

    private static int letterLineFilter(int indexX, int indexY){
        if(indexY == 0 && indexX == 0)return 1;
        if(indexX == 0)return 2;
        if(indexX % textSkip == 0 && indexY == 0)return 1;
        if(indexY == 0)return 3;
        if(indexX % textSkip == 0)return 2;
        return 0;
    }

    private static int crossGridFilter(int indexX, int indexY){
        boolean xMark = false;
        boolean yMark = false;
        if(indexX % textSkip == 0)xMark = true;
        if(indexY % textSkip == 0)yMark = true;

        if(xMark && yMark)return 1;
        if(xMark)return 2;
        if(yMark)return 3;
        return 0;
    }

    private static int battleShipFilter(int indexX, int indexY){
        //creates a top and side row fully (when requested) then places spacely spaced grid spaces
        if(indexY == 0 && indexX == 0)return 1;
        if(indexX == 0)return 2;
        if(indexY == 0)return 3;
        if((indexX % textSkip == 0) && (indexY % textSkip == 0))return 1;
        return 0;
    }

    private static void placeMarker(int posX, int posY, int indexX, int indexY, Graphics2D graphics2D, int markerState){
        //if state == 0, dont draw
        if(markerState == 0)return;

        //places a marker with the position details on grid spot
        String stringX = "";
        String stringY = "";
        if(markerState == 1 || markerState == 3){
            stringX = "" + indexX;
        }
        if(markerState == 1 || markerState == 2){
            stringY = "" + intToText(indexY);
        }

        graphics2D.setColor(transColor);
        graphics2D.fillRect(posX + 2,posY + 2, graphics2D.getFontMetrics().stringWidth(stringY + "" + stringX) + 2, graphics2D.getFontMetrics().getHeight());

        graphics2D.setColor(textColor);
        graphics2D.drawString( stringY +  "" + stringX, posX + 4, posY + textScale + 2);
    }

    private static String intToText(int number){
        int charStart = 65;
        int charLimit = 26;

        if(number == 0){
            return "A";
        } else if(number >= 676) {
            return "**";
        }
        char firstChar = (char) ((char) (number / charLimit) + charStart);
        char secondChar = (char) ((char) (number % charLimit) + charStart);

        if(number < charLimit){
            return secondChar + "";
        }
        return firstChar + "" + secondChar;
    }

    public static Dimension calculateImageCut(int imageSizeX, int imageSizeY){
        //calculates what the image should be resized to to make the grid fit correctly
        //uses the current settings

        int sizeX = 0;
        int sizeY = 0;

        if(filterType == FilterType.BATTLESHIP || filterType == FilterType.CROSSGRID){
            //do sizeX
            int largestSize = 0;
            boolean running = true;
            while (running){
                if(sizeX + gridSpacing <= imageSizeX){
                    sizeX = sizeX + gridSpacing;
                    if((sizeX / gridSpacing) % textSkip == 0){
                        if(sizeX + (gridSpacing * 2) < imageSizeX){
                            largestSize = sizeX;
                        }
                    }
                } else {
                    sizeX = largestSize + gridSpacing;
                    running = false;
                }
            }

            //do sizeY
            largestSize = 0;
            running = true;
            while (running){
                if(sizeY + gridSpacing < imageSizeY){
                    sizeY = sizeY + gridSpacing;
                    if((sizeY / gridSpacing) % textSkip == 0){
                        if(sizeY + (gridSpacing * 2) < imageSizeY){
                            largestSize = sizeY;
                        }
                    }
                } else {
                    sizeY = largestSize + gridSpacing;
                    running = false;
                }
            }

        } else if(filterType == FilterType.LETTERLINE) {
            //do sizeX
            int largestSize = 0;
            boolean running = true;
            while (running){
                if(sizeX + gridSpacing <= imageSizeX){
                    sizeX = sizeX + gridSpacing;
                    if((sizeX / gridSpacing) % textSkip == 0){
                        if(sizeX + (gridSpacing * 2) < imageSizeX){
                            largestSize = sizeX;
                        }
                    }
                } else {
                    sizeX = largestSize + gridSpacing;
                    running = false;
                }
            }

            //do sizeY
            //int index = 0;
            running = true;
            while (running){
                if(sizeY + gridSpacing <= imageSizeY){
                    sizeY = sizeY + gridSpacing;
                } else {
                    running = false;
                }
            }
        } else {
            //do sizeX
            //int index = 0;
            boolean running = true;
            while (running){
                if(sizeX + gridSpacing <= imageSizeX){
                    sizeX = sizeX + gridSpacing;
                } else {
                    running = false;
                }
            }

            //do sizeY
            //int index = 0;
            running = true;
            while (running){
                if(sizeY + gridSpacing <= imageSizeY){
                    sizeY = sizeY + gridSpacing;
                } else {
                    running = false;
                }
            }
        }



        return new Dimension(sizeX, sizeY);
    }
}
