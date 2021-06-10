package program;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JDoubleTextField extends JTextField {

    /**
     *
     * a text field variant that accepts only numbers, resets itself if a wrong value is left, and automatically converts text to number
     *
     */

    double numberValue = 0;
    double minimumValue = 0;
    double maximumValue = 0;

    boolean minimumLimit = false;
    boolean maximumLimit = false;

    Color wrongColor = Color.RED;
    Color correctColor = Color.black;

    boolean isCorrect = true;


    public JDoubleTextField(){
        initialise();
    }

    public JDoubleTextField(double startingValue){
        numberValue = startingValue;
        initialise();
    }

    public JDoubleTextField(double startingValue, Color wrongColor){
        numberValue = startingValue;
        this.wrongColor = wrongColor;
        initialise();
    }

    public JDoubleTextField(double startingValue, Color wrongColor, Color correctColor){
        numberValue = startingValue;
        this.wrongColor = wrongColor;
        this.correctColor = correctColor;
        initialise();
    }


    private void initialise(){
        setText(numberValue + "");
        //add listeners:
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                illegalSymbolChecker();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                illegalSymbolChecker();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                illegalSymbolChecker();
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateNumber();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    setFocusable(false);
                    setFocusable(true);
                }
            }
        });
    }

    private void updateNumber(){
        if(isCorrect){
            try{
                numberValue = Double.parseDouble(getText());
                setText(numberValue + "");
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
                setText(numberValue + "");
                isCorrect = true;
            }
        } else {
            //entered string was invalid! get last valid value!
            setText(numberValue + "");
            isCorrect = true;
        }
    }

    private boolean checkLimit(double input){
        boolean output = true;
        if(input < minimumValue && minimumLimit){
            output = false;
        }
        if(input > maximumValue && maximumLimit){
            output = false;
        }
        return output;
    }

    private void illegalSymbolChecker(){
        String string = getText();
        try{
            double var = Double.parseDouble(getText());
            isCorrect = checkLimit(var);
        } catch (NumberFormatException numberFormatException) {
            isCorrect = false;
        }

        if(isCorrect){
            setForeground(correctColor);
        } else {
            setForeground(wrongColor);
        }

        /**
        if(string.startsWith("-")){
            //removes minus symbol, but only from start
            string = string.substring(1);
        }
        if(string.matches("[0-9-.]+") == false){
            //checks if string is only numbers
            setForeground(wrongColor);
            isCorrect = false;
        } else {
            setForeground(correctColor);
            isCorrect = true;
        }*/
    }

    public void checkValue(){
        /**
         * forces a value check (normally this is done automatically on focus loss
         *
         */
        updateNumber();
    }

    public double getNumberValue(){
        return numberValue;
    }

    public void setNumberValue(double numberValue) {
        this.numberValue = numberValue;
        setText(numberValue + "");
    }

    public Color getCorrectColor() {
        return correctColor;
    }

    public Color getWrongColor() {
        return wrongColor;
    }

    public void setCorrectColor(Color correctColor) {
        this.correctColor = correctColor;
        illegalSymbolChecker();
    }

    public void setWrongColor(Color wrongColor) {
        this.wrongColor = wrongColor;
        illegalSymbolChecker();
    }

    public void setLowerLimit(double limit){
        this.minimumLimit = true;
        this.minimumValue = limit;
    }

    public void setUpperLimit(double limit){
        this.maximumLimit = true;
        this.maximumValue = limit;
    }
}
