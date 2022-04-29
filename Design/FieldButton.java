package Design;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

public class FieldButton extends JButton {
    public FieldButton(){
        super();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                System.out.println(getX());
                System.out.println(getY());
            }
        });
    }
}
