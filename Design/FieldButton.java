package Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

public class FieldButton extends JButton {

    private Color color;
    public FieldButton(){
        super();
        this.color = new Color(80,150,255);
    }
    public Color getColor(){
        return color;
    }
    @Override
    public boolean equals(Object button) {
        if(this==button) return true;
        if(!(button instanceof FieldButton)) return false;
        FieldButton c = (FieldButton) button;
        return this.color==c.getColor();
    }
}
