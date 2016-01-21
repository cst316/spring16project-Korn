package net.sf.memoranda.ui;

import javax.swing.JOptionPane;

import net.sf.memoranda.util.Local;

public class ImportSticker {

String name;        
        
        public ImportSticker(String x) {
                name = x;
        }

        public boolean import_file(){
                /*
                 We are working on this =)
                  
                  
                  */
                
                JOptionPane.showMessageDialog(null,Local.getString("Aun no podemos importar su documento"));
                return true;
        }
        
        
}