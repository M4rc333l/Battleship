package Design;

import javax.swing.*;
import java.awt.*;

public class FieldButton extends JButton {

    private final Color color;
    public FieldButton(){
        super();
        this.color = new Color(80,150,255);
    }
    public Color getColor(){
        return color;
    }
}
