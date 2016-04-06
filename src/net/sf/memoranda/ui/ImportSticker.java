package net.sf.memoranda.ui;

import net.sf.memoranda.util.Local;

import javax.swing.*;

public class ImportSticker {

    String name;

    public ImportSticker(String x) {
        name = x;
    }

    public boolean import_file() {
        /* We are working on this =)  */
        JOptionPane.showMessageDialog(null, Local.getString("Unable to import document"));
        return true;
    }
}
