package pdc_assign2;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Yeran
 */

/*  
    Handle button and JList listeners here    
*/  

public class KBGUIController
{
    public KBGUI GUI;
    
    public KBGUIController ()
    {
        GUI = new KBGUI(this);
        folderListSelectionCheck();
        fileListSelectionCheck();
        addButtonListener();
        removeButtonListener();
    }
    
    public void addButtonListener()
    {
        GUI.addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if (GUI.folderList.getSelectedIndex() < 0)
                {
                    GUI.errorPopup("Please select a folder first!");
                    return;
                }
                String filePath = GUI.chooseNewFile();
                String newPath = "";
                for (int i = 0; i < filePath.length(); i++)
                {
                    if (filePath.charAt(i) == '\\')
                    {
                        newPath = "";
                    }
                    else 
                    {
                        newPath += filePath.charAt(i);
                    }
                }
                
                if (!(newPath.substring(newPath.length() - 3, newPath.length()).equals("csv")))
                {
                    GUI.errorPopup("This program only supports CSVs!");
                }
                
                String folder = GUI.folderStrings[GUI.folderList.getSelectedIndex()];
                if (filePath != null)
                {
                    LinkedList<String> list = KBMasterController.fileHandler.readFile(filePath);
                    if (!list.isEmpty() && KBMasterController.fileHandler.add(list, newPath, folder))
                    {
                        KBMasterController.addFileToFolders(newPath, folder);
                        KBMasterController.dbhandle.dbsetup();
                    }
                    GUI.update();
                }
            }
        });
    }
    
    public void removeButtonListener()
    {
        GUI.removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                if (GUI.csvList.getSelectedIndex() < 0)
                {
                    GUI.errorPopup("Please select a file to remove");
                    return;
                }
                String toRemove = GUI.fileStrings[GUI.csvList.getSelectedIndex()];
                KBMasterController.checkFileExists(toRemove);
                KBMasterController.dbhandle.removeTable(toRemove);
                GUI.update();
            }
        });
    }

    public void folderListSelectionCheck()
    {
        GUI.folderList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    GUI.fileStrings = KBMasterController.dbhandle.getFiles(GUI.folderStrings[GUI.folderList.getSelectedIndex()]);
                    GUI.csvList.setListData(GUI.fileStrings);
                }
            }
        });
    }
    
    public void fileListSelectionCheck()
    {
        GUI.csvList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    String selectedFile = GUI.fileStrings[GUI.csvList.getSelectedIndex()];
                    
                    ResultSet rs = KBMasterController.dbhandle.getTable(selectedFile);
                    try {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        String[] titles = new String[rsmd.getColumnCount()];
                        for (int i = 0; i < titles.length; i++)
                            titles[i] = rsmd.getColumnName(i + 1);
                        rs.last();
                        int rows = rs.getRow();
                        for (String s : titles)
                            System.out.print(s + " : ");
                        
                        rs.first();
                        String[][] data = new String[rows][titles.length];
                        for (int i = 0; i < rows; i++)
                        {
                            for (int j = 0; j < titles.length; j++)
                            {
                                data[i][j] = rs.getString(j + 1);
                            }
                            rs.next();
                        }
                        GUI.tableModel.setDataVector(data, titles);
                        GUI.tableModel.fireTableDataChanged();
                        
                        GUI.tableLabel.setText("Table: " + selectedFile);
                        rs.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
    }
}
