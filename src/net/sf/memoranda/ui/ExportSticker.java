package net.sf.memoranda.ui;

import net.sf.memoranda.EventsManager;
import net.sf.memoranda.util.Local;
import nu.xom.Element;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ExportSticker {

    private String name;
        
    public ExportSticker(String x) {
        this.name = remove1(x);
    }

    /**
     * Function to eliminate special chars from a string
     */
    public static String remove1(String input) {

        String original = "Ã¡Ã Ã¤Ã©Ã¨Ã«Ã­Ã¬Ã¯Ã³Ã²Ã¶ÃºÃ¹uÃ±Ã�Ã€Ã„Ã‰ÃˆÃ‹Ã�ÃŒÃ�Ã“Ã’Ã–ÃšÃ™ÃœÃ‘Ã§Ã‡";

        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {

            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }

    public boolean export(String src) {
        boolean result = true;
        String fs = System.getProperty("file.separator");

        String contents = getSticker();
        try {
            File file = new File(this.name + "." + src);


            FileWriter fwrite = new FileWriter(file, true);

            fwrite.write(contents);

            fwrite.close();
            JOptionPane.showMessageDialog(null, Local.getString("Successfully Created Document"));


        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, Local.getString("unable to create document"));
        }


        return result;
    }

    public String getSticker() {
        Map stickers = EventsManager.getStickers();
        String result = "";
        String nl = System.getProperty("line.separator");
        for (Iterator i = stickers.keySet().iterator(); i.hasNext(); ) {
            String id = (String) i.next();
            result += (String) (((Element) stickers.get(id)).getValue()) + nl;
        }

        return result;
    }
        
        /*public static String getStickers() {
                String result ="";
                Elements els = _root.getChildElements("sticker");
                for (int i = 0; i < els.size(); i++) {
                        Element se = els.get(i);
                        m.put(se.getAttribute("id").getValue(), se.getValue());
                }
                return m;
        }*/


}