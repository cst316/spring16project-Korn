/*
 * file: TaskReportDialog.java
 * purpose: This dialog will be used to generate a detailed task report from a 
 * select project
 * @author Jordan Partridge
 */

package net.sf.memoranda.ui;

import net.sf.memoranda.util.Local;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;



public class TaskReportDialog extends javax.swing.JDialog {

  public boolean cancelled = true;
  private javax.swing.JPanel panel;
  
  public TaskReportDialog(java.awt.Frame parent, String title) {
    super(parent, title, true);
    initComponents1();
  }

  private void initComponents1() {
    panel = new javax.swing.JPanel();
    this.add(panel);
  } 
  
  private void initComponents() {
    taskReportPanel = new javax.swing.JPanel();
    btnOk = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();
    optionsPanel = new javax.swing.JPanel();
    encPanel = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    usetemplChB = new javax.swing.JCheckBox();
    xhtmlChB = new javax.swing.JCheckBox();
    templPanel = new javax.swing.JPanel();
    templF = new javax.swing.JTextField();
    templF.setEditable(false);
    templBrowseB = new javax.swing.JButton();
    numentChB = new javax.swing.JCheckBox();
    JPanel jPanel6 = new javax.swing.JPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setModal(true);
    
    btnOk.setText(Local.getString("Save"));
    btnOk.setPreferredSize(new java.awt.Dimension(90, 25));
    btnOk.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(ActionEvent event) {
          cancelled = false;
          dispose();
      }
        });
    btnOk.setEnabled(false);
    JPanel jpanel2 = new JPanel();
    jpanel2.add(btnOk);

    btnCancel.setText(Local.getString("Cancel"));
    btnCancel.setPreferredSize(new java.awt.Dimension(90, 25));
    btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent event) {                
        dispose();
      }
        });
    jpanel2.add(btnCancel);

    getContentPane().add(jpanel2, java.awt.BorderLayout.SOUTH);

    filePanel.setLayout(new java.awt.BorderLayout());

 
        
        

    filePanel.add(fileChooser, java.awt.BorderLayout.CENTER);

    optionsPanel.setLayout(new java.awt.GridLayout(3, 2, 5, 0));

    optionsPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
    encPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

    jLabel2.setText(Local.getString("Encoding") + ":");
    encPanel.add(jLabel2);

   
    usetemplChB.setText(Local.getString("Use template") + ":");
    usetemplChB.setMargin(new java.awt.Insets(0, 0, 0, 0));
    usetemplChB.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent event) {
        if (usetemplChB.isSelected()) {
          templF.setEnabled(true);
          templBrowseB.setEnabled(true);
                }
                else {
                    templF.setEnabled(false);
                    templBrowseB.setEnabled(false);                    
                }
            }
        });
        optionsPanel.add(usetemplChB);

        xhtmlChB.setText(Local.getString("Save as XHTML"));
        xhtmlChB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xhtmlChBActionPerformed(evt);
            }
        });

        optionsPanel.add(xhtmlChB);

        templPanel.setLayout(new java.awt.BorderLayout());
        templF.setEnabled(false);
        templPanel.add(templF, java.awt.BorderLayout.CENTER);

        templBrowseB.setText("Browse");
        templBrowseB.setEnabled(false);
        
        templPanel.add(templBrowseB, java.awt.BorderLayout.EAST);

        optionsPanel.add(templPanel);

        numentChB.setText("Use numeric entities");
        optionsPanel.add(numentChB);

      jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

      optionsPanel.add(jPanel6);

      filePanel.add(optionsPanel, java.awt.BorderLayout.SOUTH);

      getContentPane().add(filePanel, java.awt.BorderLayout.CENTER);
      getRootPane().setDefaultButton(btnOk);
      pack();
  }

  private void xhtmlChBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xhtmlChBActionPerformed
        // TODO add your handling code here:
   }

  private void chooserActionPerformed() {//GEN-FIRST:event_chooserActionPerformed
    btnOk.setEnabled(fileChooser.getSelectedFile() != null);            
  }
   
    
  private javax.swing.JButton btnCancel;
  private javax.swing.JFileChooser fileChooser;
    
  private javax.swing.JPanel encPanel;
  private javax.swing.JPanel filePanel;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel taskReportPanel;
  public javax.swing.JCheckBox numentChB;
  private javax.swing.JButton btnOk;
  private javax.swing.JPanel optionsPanel;
  private javax.swing.JButton templBrowseB;
  public javax.swing.JTextField templF;
  private javax.swing.JPanel templPanel;
  public javax.swing.JCheckBox usetemplChB;
  public javax.swing.JCheckBox xhtmlChB;
    
}

