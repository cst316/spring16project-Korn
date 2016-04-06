package net.sf.memoranda.ui.htmleditor;

import javax.swing.*;
import java.awt.*;

public class HTMLEditorPane extends JEditorPane {

    boolean antiAlias = true;

    public HTMLEditorPane(String text) {
        super("text/html", text);
    }

    public boolean isAntialiasOn() {
        return antiAlias;
    }

    public void setAntiAlias(boolean on) {
        antiAlias = on;
    }

    public void paint(Graphics g) {
        if (antiAlias) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            /*g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);*/
            super.paint(g2);
        } else {
            super.paint(g);
        }
    }
}
