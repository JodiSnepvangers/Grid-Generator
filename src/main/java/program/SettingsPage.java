package program;

import javax.swing.*;
import javax.swing.plaf.OptionPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsPage extends JPanel {

    /**
     *
     * holds all the settings for the program
     *
     */

    private int panelSizeX = 200;
    private int panelSizeY = 500;
    private boolean simpleInput = true;

    //global access to save label:
    private JLabel saveText = new JLabel("");

    FilePanel filePanel = new FilePanel();
    GridPanel gridPanel = new GridPanel();
    LabelPanel labelPanel = new LabelPanel();
    SavePanel savePanel = new SavePanel();
    HiddenPanel hiddenPanel = new HiddenPanel();

    public SettingsPage(){

        //set up frame parameters:
        setLayout(null);
        setBackground(Main.foreGround);
        setPreferredSize(new Dimension(panelSizeX, panelSizeY));

        //add components:
        filePanel.setBounds(0, 0, panelSizeX, 100);
        add(filePanel);
        gridPanel.setBounds(0, 99, panelSizeX, 105);
        add(gridPanel);
        labelPanel.setBounds(0,203, panelSizeX, 270);
        add(labelPanel);
        savePanel.setBounds(0,472, panelSizeX, 100);
        add(savePanel);
        hiddenPanel.setBounds(0,571, panelSizeX, 60);
        add(hiddenPanel);
    }

    //file panel: lets you select a file and refresh

    private class FilePanel extends JPanel{
        private JComboBox fileSelector = new JComboBox(Main.scanner.getImageNames().toArray());
        private JButton refreshButton = new JButton("Refresh");
        private JButton loadButton = new JButton("Load");
        private String selectedString = "";

        public FilePanel(){
            //set component parameter:
            if(Main.selectedImage != null){
                selectedString = fileSelector.getSelectedItem().toString();
            }

            //set panel parameters
            setLayout(null);
            setBackground(Main.foreGround);
            setPreferredSize(new Dimension(panelSizeX, 300));

            //deal with border
            setBorder(BorderFactory.createLineBorder(Main.borderColor));

            //add components
            JLabel label = new JLabel("File Select:");
            label.setBounds(16, 9, 100, 20);
            add(label);
            fileSelector.setBounds(15, 30, 170, 25);
            add(fileSelector);
            refreshButton.setBounds(15, 60, 80, 25);
            add(refreshButton);
            loadButton.setBounds(105, 60, 80, 25);
            add(loadButton);

            //update program
            //loadNewImage(fileSelector.getSelectedItem().toString());

            //add mouse listeners:
            refreshButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    //scan for new items
                    Main.scanner.scanDirectory();

                    //save old selection:
                    if(fileSelector.getItemCount() > 0){
                        selectedString = fileSelector.getSelectedItem().toString();
                    }

                    //update combo box:
                    List<String> fileList = Main.scanner.getImageNames();
                    fileSelector.removeAllItems();
                    for(String name : fileList){
                        fileSelector.addItem(name);

                        //if item still exists, select it
                        if(selectedString.equals(name)){
                            fileSelector.setSelectedItem(name);
                        }
                    }

                    //update program:
                    if(Main.selectedImage != null){
                        loadNewImage(fileSelector.getSelectedItem().toString());
                    }


                }
            });

            loadButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    //update program:
                    if(Main.selectedImage != null){
                        loadNewImage(fileSelector.getSelectedItem().toString());
                        Main.selectedImage.refreshImage();
                    }


                }
            });

            fileSelector.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(fileSelector.getSelectedItem() != null)loadNewImage(fileSelector.getSelectedItem().toString());
                }
            });


        }


        private void loadNewImage(String imageName){
            //if(Main.selectedImage == null)return;
            ImageContainer imageContainer = Main.scanner.findImage(imageName);
            if(imageContainer == null)return;

            //image was found. update main!
            Main.selectedImage = imageContainer;
            Main.gridFrame.updatePreview();
            saveText.setText("");
        }
    }

    //holds basic grid settings
    private class GridPanel extends JPanel{
        //set up components:
        private JSlider gridSpaceSlider = new JSlider(JSlider.HORIZONTAL, 20, 200, GridGenerator.gridSpacing);
        private JLabel gridSpaceLabel = new JLabel("Grid Spacing: " + gridSpaceSlider.getValue());

        private JSlider gridThickSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, (int) (GridGenerator.gridThickness * 10.0f));
        private JLabel gridThickLabel = new JLabel("Grid Thickness: " + gridThickSlider.getValue() / 10.0f);

        private JNumberTextField gridSpaceField = new JNumberTextField(GridGenerator.gridSpacing);
        private JDoubleTextField gridThicknessField = new JDoubleTextField(GridGenerator.gridThickness);

        public GridPanel(){
            //set component parameters
            sliderPrepare(gridSpaceSlider);
            sliderPrepare(gridThickSlider);

            fieldPrepare(gridSpaceField, 1, 999999999);

            gridThicknessField.setEnabled(!simpleInput);
            gridThicknessField.setVisible(!simpleInput);
            gridThicknessField.setLowerLimit(0.001);
            gridThicknessField.setUpperLimit(999999999);

            //set panel parameters
            setLayout(null);
            setBackground(Main.foreGround);
            setPreferredSize(new Dimension(panelSizeX, 300));

            //deal with border
            setBorder(BorderFactory.createLineBorder(Main.borderColor));

            //add components:
            gridSpaceSlider.setBounds(10,30,  panelSizeX - 20, 20);
            add(gridSpaceSlider);
            gridSpaceLabel.setBounds(10, 10,100, 20);
            add(gridSpaceLabel);

            gridThickSlider.setBounds(10,70,  panelSizeX - 20, 20);
            add(gridThickSlider);
            gridThickLabel.setBounds(10, 50,200, 20);
            add(gridThickLabel);

            gridSpaceField.setBounds(10,30,  panelSizeX - 20, 20);
            add(gridSpaceField);
            gridThicknessField.setBounds(10,70,  panelSizeX - 20, 20);
            add(gridThicknessField);

            //add listeners:
            gridSpaceSlider.addChangeListener(e -> {
                gridSpaceLabel.setText("Grid Spacing: " + gridSpaceSlider.getValue());
                GridGenerator.gridSpacing = gridSpaceSlider.getValue();
                Main.gridFrame.updatePreview();
            });
            gridThickSlider.addChangeListener(e -> {
                gridThickLabel.setText("Grid Thickness: " + gridThickSlider.getValue() / 10.0f);
                GridGenerator.gridThickness = gridThickSlider.getValue() / 10.0f;
                Main.gridFrame.updatePreview();
            });
            gridSpaceField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    gridSpaceLabel.setText("Grid Spacing: " + gridSpaceField.getNumberValue());
                    GridGenerator.gridSpacing = gridSpaceField.getNumberValue();
                    Main.gridFrame.updatePreview();
                }
            });
            gridThicknessField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    gridThickLabel.setText("Grid Thickness: " + gridThicknessField.getNumberValue());
                    GridGenerator.gridThickness = (float) gridThicknessField.getNumberValue();
                    Main.gridFrame.updatePreview();
                }
            });
        }

        public void updateSimpleInput(){
            gridSpaceField.setEnabled(!simpleInput);
            gridSpaceField.setVisible(!simpleInput);
            gridThicknessField.setEnabled(!simpleInput);
            gridThicknessField.setVisible(!simpleInput);
            gridSpaceSlider.setEnabled(simpleInput);
            gridSpaceSlider.setVisible(simpleInput);
            gridThickSlider.setEnabled(simpleInput);
            gridThickSlider.setVisible(simpleInput);

            int tempValue = Math.min(GridGenerator.gridSpacing, gridSpaceSlider.getMaximum());
            tempValue = Math.max(tempValue, gridSpaceSlider.getMinimum());
            gridSpaceLabel.setText("Grid Spacing: " + tempValue);
            gridSpaceSlider.setValue(tempValue);
            gridSpaceField.setNumberValue(tempValue);
            GridGenerator.gridSpacing = tempValue;


            double tempDouble = Math.min((GridGenerator.gridThickness), gridThickSlider.getMaximum() / 10);
            tempDouble = Math.max(tempDouble, gridThickSlider.getMinimum() / 10);
            GridGenerator.gridThickness = (float) tempDouble;
            gridThickLabel.setText("Grid Thickness: " + round(GridGenerator.gridThickness, 1));
            gridThickSlider.setValue((int) (tempDouble * 10));
            gridThicknessField.setNumberValue(round(GridGenerator.gridThickness, 1));
        }
    }

    //holds label and filter settings:
    private class LabelPanel extends JPanel{
        //set up components:
        private JRadioButton filterCrossGrid = new JRadioButton("Cross Grid");
        private JRadioButton filterBattleship = new JRadioButton("Battleship");
        private JRadioButton filterLetterLine = new JRadioButton("Letter Line");
        private JRadioButton filterNone = new JRadioButton("No Filter");
        private JRadioButton filterOff = new JRadioButton("Off");
        private ButtonGroup buttonGroup = new ButtonGroup();
        private JPanel buttonCollection = new JPanel();
        private JLabel filterLabel = new JLabel("Filter Type:");

        private JSlider opacitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, GridGenerator.labelOpacity);
        private JLabel opacityLabel = new JLabel("Label Opacity: " + opacitySlider.getValue() + "%");
        private JSlider textScaleSlider = new JSlider(JSlider.HORIZONTAL, 8, 32, GridGenerator.textScale);
        private JLabel textScaleLabel = new JLabel("Label Scale: " + textScaleSlider.getValue());
        private JSlider textSkipSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, GridGenerator.textSkip);
        private JLabel textSkipLabel = new JLabel("Label Skip: " + textSkipSlider.getValue());

        private JButton gridColorButton = new JButton("Grid");
        private JButton textColorButton = new JButton("Text");
        private JButton labelColorButton = new JButton("Label");
        private JLabel colorText = new JLabel("Color Picker:");

        private JNumberTextField labelOpacityField = new JNumberTextField((int) (GridGenerator.labelOpacity* 2.56f));
        private JNumberTextField labelScaleField = new JNumberTextField(GridGenerator.textScale);
        private JNumberTextField labelSkipField = new JNumberTextField(GridGenerator.textSkip);

        public LabelPanel(){
            //set up components:
            buttonGroup.add(filterCrossGrid);
            buttonGroup.add(filterBattleship);
            buttonGroup.add(filterLetterLine);
            buttonGroup.add(filterNone);
            buttonGroup.add(filterOff);
            filterCrossGrid.setSelected(true);

            //set up inner panel:
            buttonCollection.setLayout(new GridLayout(5, 1));
            buttonCollection.setBackground(Main.foreGround);
            buttonCollection.setOpaque(false);
            buttonCollection.add(filterCrossGrid);
            buttonCollection.add(filterBattleship);
            buttonCollection.add(filterLetterLine);
            buttonCollection.add(filterNone);
            buttonCollection.add(filterOff);

            filterCrossGrid.setBackground(Main.foreGround);
            filterBattleship.setBackground(Main.foreGround);
            filterLetterLine.setBackground(Main.foreGround);
            filterNone.setBackground(Main.foreGround);
            filterOff.setBackground(Main.foreGround);

            sliderPrepare(opacitySlider);
            sliderPrepare(textScaleSlider);
            sliderPrepare(textSkipSlider);

            fieldPrepare(labelOpacityField, 0, 256);
            fieldPrepare(labelScaleField, 1, 999999999);
            fieldPrepare(labelSkipField, 1, 999999999);

            //set panel parameters
            setLayout(null);
            setBackground(Main.foreGround);
            setPreferredSize(new Dimension(panelSizeX, 300));

            //deal with border
            setBorder(BorderFactory.createLineBorder(Main.borderColor));

            //add components:
            filterLabel.setBounds(10, 10, 200, 20);
            add(filterLabel);
            buttonCollection.setBounds(10, 30, 90, 100);
            add(buttonCollection);

            opacityLabel.setBounds(10, 130, 180, 20);
            add(opacityLabel);
            opacitySlider.setBounds(10, 150, 180, 20);
            add(opacitySlider);

            textScaleLabel.setBounds(10, 170, 180, 20);
            add(textScaleLabel);
            textScaleSlider.setBounds(10, 190, 180, 20);
            add(textScaleSlider);

            textSkipLabel.setBounds(10, 210, 180, 20);
            add(textSkipLabel);
            textSkipSlider.setBounds(10, 230, 180, 20);
            add(textSkipSlider);

            colorText.setBounds(110, 7, 80, 25);
            add(colorText);
            gridColorButton.setBounds(110, 35, 80, 25);
            add(gridColorButton);
            textColorButton.setBounds(110, 65, 80, 25);
            add(textColorButton);
            labelColorButton.setBounds(110, 95, 80, 25);
            add(labelColorButton);

            labelOpacityField.setBounds(opacitySlider.getBounds());
            add(labelOpacityField);
            labelScaleField.setBounds(textScaleSlider.getBounds());
            add(labelScaleField);
            labelSkipField.setBounds(textSkipSlider.getBounds());
            add(labelSkipField);

            //add listeners:
            addButtonListener(filterCrossGrid);
            addButtonListener(filterBattleship);
            addButtonListener(filterLetterLine);
            addButtonListener(filterNone);
            addButtonListener(filterOff);

            opacitySlider.addChangeListener(e -> {
                opacityLabel.setText("Label Opacity: " + opacitySlider.getValue() + "%");
                GridGenerator.labelOpacity = opacitySlider.getValue();
                GridGenerator.updateTransColor();
                Main.gridFrame.updatePreview();
            });
            textScaleSlider.addChangeListener(e -> {
                textScaleLabel.setText("Label Scale: " + textScaleSlider.getValue());
                GridGenerator.textScale = textScaleSlider.getValue();
                Main.gridFrame.updatePreview();
            });
            textSkipSlider.addChangeListener(e -> {
                textSkipLabel.setText("Label Skip: " + textSkipSlider.getValue());
                GridGenerator.textSkip = textSkipSlider.getValue();
                Main.gridFrame.updatePreview();
            });
            labelOpacityField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    GridGenerator.labelOpacity = (int) (labelOpacityField.getNumberValue() / 2.56);
                    opacityLabel.setText("Label Opacity: " + GridGenerator.labelOpacity + "%");
                    GridGenerator.updateTransColor();
                    Main.gridFrame.updatePreview();
                }
            });
            labelScaleField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    textScaleLabel.setText("Label Scale: " + labelScaleField.getNumberValue());
                    GridGenerator.textScale = labelScaleField.getNumberValue();
                    Main.gridFrame.updatePreview();
                }
            });
            labelSkipField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    textSkipLabel.setText("Label Skip: " + labelSkipField.getNumberValue());
                    GridGenerator.textSkip = labelSkipField.getNumberValue();
                    Main.gridFrame.updatePreview();
                }
            });

            gridColorButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color color = JColorChooser.showDialog(new JFrame(), "Color Select: Grid", GridGenerator.gridColor);
                    if(color != null)GridGenerator.gridColor = color;
                    Main.gridFrame.updatePreview();
                }
            });
            textColorButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color color = JColorChooser.showDialog(new JFrame(), "Color Select: Text", GridGenerator.textColor);
                    if(color != null)GridGenerator.textColor = color;
                    Main.gridFrame.updatePreview();
                }
            });
            labelColorButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color color = JColorChooser.showDialog(new JFrame(), "Color Select: Label", GridGenerator.labelColor);
                    if(color != null)GridGenerator.labelColor = color;
                    GridGenerator.updateTransColor();
                    Main.gridFrame.updatePreview();
                }
            });

            //start timer:
            new Timer().scheduleAtFixedRate(autoCheck, 0, 100);
        }

        private TimerTask autoCheck = new TimerTask() {
            @Override
            public void run() {
                updateGridState();
            }
        };

        private void updateGridState(){
            GridGenerator.FilterType selectedFilter;
            if(filterCrossGrid.isSelected()){
                selectedFilter = GridGenerator.FilterType.CROSSGRID;
            } else if(filterBattleship.isSelected()){
                selectedFilter = GridGenerator.FilterType.BATTLESHIP;
            } else if(filterLetterLine.isSelected()){
                selectedFilter = GridGenerator.FilterType.LETTERLINE;
            } else if(filterNone.isSelected()){
                selectedFilter = GridGenerator.FilterType.NONE;
            } else {
                selectedFilter = GridGenerator.FilterType.OFF;
            }

            if(GridGenerator.filterType != selectedFilter){
                GridGenerator.filterType = selectedFilter;
                Main.gridFrame.updatePreview();
            }
        }

        public void updateSimpleInput(){
            labelOpacityField.setEnabled(!simpleInput);
            labelOpacityField.setVisible(!simpleInput);
            labelScaleField.setEnabled(!simpleInput);
            labelScaleField.setVisible(!simpleInput);
            labelSkipField.setEnabled(!simpleInput);
            labelSkipField.setVisible(!simpleInput);
            opacitySlider.setEnabled(simpleInput);
            opacitySlider.setVisible(simpleInput);
            textScaleSlider.setEnabled(simpleInput);
            textScaleSlider.setVisible(simpleInput);
            textSkipSlider.setEnabled(simpleInput);
            textSkipSlider.setVisible(simpleInput);

            //ensure numbers are within range:
            opacitySlider.setValue(GridGenerator.labelOpacity);
            opacityLabel.setText("Label Opacity: " + GridGenerator.labelOpacity + "%");
            labelOpacityField.setNumberValue((int) (GridGenerator.labelOpacity * 2.56));

            int tempValue = Math.min(GridGenerator.textScale, textScaleSlider.getMaximum());
            tempValue = Math.max(tempValue, textScaleSlider.getMinimum());
            textScaleLabel.setText("Label Scale: " + tempValue);
            textScaleSlider.setValue(tempValue);
            labelScaleField.setNumberValue(tempValue);

            tempValue = Math.min(GridGenerator.textSkip, textSkipSlider.getMaximum());
            tempValue = Math.max(tempValue, textSkipSlider.getMinimum());
            textSkipLabel.setText("Label Skip: " + tempValue);
            textSkipSlider.setValue(tempValue);
            labelSkipField.setNumberValue(tempValue);
        }

        private void addButtonListener(JRadioButton button){
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    updateGridState();
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.drawLine(100, 0, 100, 130);
            graphics2D.drawLine(100, 130, 200, 130);
        }
    }

    //holds the save button, save label and auto cut checkbox
    private class SavePanel extends JPanel{
        //add components:
        private JButton saveButton = new JButton("Save");
        private JCheckBox autoCutCheck = new JCheckBox("Auto Cut Image", true);
        private JLabel iconLabel = new JLabel(new ImageIcon(Main.catIcon));
        private JLabel helpLabel = new JLabel("<--- Click me for help!");

        public SavePanel(){
            //set up component parameters
            autoCutCheck.setOpaque(false);
            helpLabel.setFont(new Font("Default", 0, 10));

            //set panel parameters
            setLayout(null);
            setBackground(Main.foreGround);
            setPreferredSize(new Dimension(panelSizeX, 300));

            //deal with border
            setBorder(BorderFactory.createLineBorder(Main.borderColor));

            //add components:
            saveButton.setBounds(10, 10, 80, 25);
            add(saveButton);
            saveText.setBounds(105, 10, 80, 25);
            add(saveText);
            autoCutCheck.setBounds(10, 40, 180, 25);
            add(autoCutCheck);
            iconLabel.setBounds(20, 60, Main.catIcon.getWidth(), Main.catIcon.getHeight());
            add(iconLabel);
            helpLabel.setBounds(80, 65, 150, 50);
            add(helpLabel);

            //add listeners:
            saveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(Main.selectedImage != null) {
                        saveText.setText("Generating...");
                        repaint();
                        Main.selectedImage.saveImage(autoCutCheck.isSelected());
                        saveText.setText("Saving...");
                        repaint();
                    }
                }
            });
            autoCutCheck.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PreviewDisplay.showCutRegion = autoCutCheck.isSelected();
                    Main.gridFrame.updatePreview();
                }
            });
            iconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    PreviewDisplay.showHelpPage = !PreviewDisplay.showHelpPage;
                    if(PreviewDisplay.showHelpPage){
                        iconLabel.setIcon(new ImageIcon(Main.catAlertIcon));
                    } else {
                        iconLabel.setIcon(new ImageIcon(Main.catConfuseIcon));
                    }

                    Main.gridFrame.updatePreview();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if(PreviewDisplay.showHelpPage)return;
                    iconLabel.setIcon(new ImageIcon(Main.catConfuseIcon));
                    helpLabel.setVisible(false);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if(PreviewDisplay.showHelpPage)return;
                    iconLabel.setIcon(new ImageIcon(Main.catIcon));
                    repaint();
                }
            });
        }
    }

    private class HiddenPanel extends JPanel{
        //adds a advance option that replaces the sliders with input fields
        private JCheckBox autoCutCheck = new JCheckBox("Enable Direct Input", false);
        private JButton aboutButton = new JButton("About");

        public HiddenPanel() {
            //set up component parameters
            autoCutCheck.setOpaque(false);

            //set panel parameters
            setLayout(null);
            setBackground(Main.foreGround);
            setPreferredSize(new Dimension(panelSizeX, 300));

            //deal with border
            setBorder(BorderFactory.createLineBorder(Main.borderColor));

            //add components:
            autoCutCheck.setBounds(10, 5, 200, 25);
            add(autoCutCheck);
            aboutButton.setBounds(10, 30, 80, 25);
            add(aboutButton);

            //listeners:
            autoCutCheck.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    simpleInput = !autoCutCheck.isSelected();
                    updateSimpleImput();
                }
            });
            Component parent = this;
            aboutButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(parent, "Made by: Kitty Soldier.\nThis work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. \nTo view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.");
                }
            });
        }
    }


    private void sliderPrepare(JSlider slider){
        slider.setBackground(Main.backGround);
        slider.setForeground(Color.white);
        slider.setBorder(BorderFactory.createLineBorder(Main.borderColor));
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintTicks(true);
        slider.setEnabled(simpleInput);
        slider.setVisible(simpleInput);
    }

    private void fieldPrepare(JNumberTextField textField, int minLimit, int maxLimit){
        textField.setEnabled(!simpleInput);
        textField.setVisible(!simpleInput);
        textField.setLowerLimit(minLimit);
        textField.setUpperLimit(maxLimit);
    }

    public static void saveTextUpdate(Boolean status){
        if(status){
            Main.gridFrame.settingsPage.saveText.setText("Image Saved!");
        } else {
            Main.gridFrame.settingsPage.saveText.setText("Error!");
        }
    }

    public void updateSimpleImput(){
        labelPanel.updateSimpleInput();
        gridPanel.updateSimpleInput();
        Main.gridFrame.updatePreview();
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
