package net.sf.memoranda.ui;

import java.awt.Component;
import java.awt.Font;
import java.util.Collections;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import net.sf.memoranda.CurrentNote;
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.Note;
import net.sf.memoranda.NoteList;
import net.sf.memoranda.NoteListener;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectListener;
import net.sf.memoranda.ResourcesList;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.date.DateListener;
import net.sf.memoranda.util.Configuration;
//import net.sf.memoranda.util.NotesVectorSorter;

/*$Id: NotesList.java,v 1.9 2005/05/05 16:19:16 ivanrise Exp $*/
public class NotesList extends JList {

    public static final int EMPTY = 0;    
    public static final int ALL = 1;
    public static final int BOOKMARKS = 2;

    private Vector notes = null;
    boolean sortOrderDesc = false;

    int _type = ALL;

    public NotesList(int type) {
        super();
		if(Configuration.get("NOTES_SORT_ORDER").toString().equalsIgnoreCase("true")) {
			sortOrderDesc = true;
		}
        _type = type;
        this.setFont(new java.awt.Font("Dialog", 0, 11));
        this.setModel(new NotesListModel());
        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {
                updateUI();
            }
        });
		
        CurrentNote.addNoteListener(new NoteListener() {
            public void noteChange(Note n, boolean toSaveCurrentNote) {
                updateUI();
            }
        });

        CurrentProject.addProjectListener(new ProjectListener() {
            public void projectChange(Project p, NoteList nl, TaskList tl, ResourcesList rl) {
            }
            public void projectWasChanged() {
                update();
            }
        });
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public NotesList() {
        this(ALL);
    }

    public void update() {
        if (_type != EMPTY) {
            update(CurrentProject.getNoteList());
		}
        else {
			update(new Vector());
		}
    }

    public void update(NoteList nl) {
        if (_type == ALL)
            notes = (Vector) nl.getAllNotes();
        else
            notes = (Vector) nl.getMarkedNotes();
        
//        Util.debug("No. of notes in noteList " + notes.size());
        //NotesVectorSorter.sort(notes);
		Collections.sort(notes);
		if (sortOrderDesc) {
			Collections.reverse(notes);		    
		}
        updateUI();
    }

    public void update(Vector ns) {
        notes = ns;
        // NotesVectorSorter.sort(notes);
		Collections.sort(notes);
		if (sortOrderDesc) {
			Collections.reverse(notes);		    
		}		
        updateUI();
    }

    public Note getNote(int index){
        return (Note) notes.get(index);
    }
    
    void invertSortOrder() {
        sortOrderDesc = !sortOrderDesc;
    }


    /*$Id: NotesList.java,v 1.9 2005/05/05 16:19:16 ivanrise Exp $*/
public class NotesListModel extends AbstractListModel<Object> {

        public NotesListModel() {
            update();
        }

        public Object getElementAt(int i) {
            Note note = (Note)notes.get(i);
            return note.getDate().getShortDateString() + " " + note.getTitle();
        }

        public int getSize() {
            return notes.size();
        }

    }

    ImageIcon bookmarkIcon = new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/star8.png"));

    public ListCellRenderer getCellRenderer() {
        return new DefaultListCellRenderer()  {

     public Component getListCellRendererComponent(
       JList list,
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // the list and the cell have the focus
     {
         JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         String s = value.toString();
         label.setText(s);
         //Note currentNote = CurrentProject.getNoteList().getActiveNote();
		 Note currentNote = CurrentNote.get();
         if (currentNote != null) {
            if (getNote(index).getId().equals(currentNote.getId()))
                label.setFont(label.getFont().deriveFont(Font.BOLD));
         }
         if (getNote(index).isMarked())
            label.setIcon(bookmarkIcon);
         //setIcon();
       /*if (isSelected) {
             setBackground(list.getSelectionBackground());
           setForeground(list.getSelectionForeground());
       }
         else {
           setBackground(list.getBackground());
           setForeground(list.getForeground());
       }
       setEnabled(list.isEnabled());
       setFont(list.getFont());
         setOpaque(true);*/
         label.setToolTipText(s);
         return label;
     }
    };

 }


}