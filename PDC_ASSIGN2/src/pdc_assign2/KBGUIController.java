package pdc_assign2;


import java.util.LinkedList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Yeran
 */
public class KBGUIController
{
    public KBGUI GUI;
    
    public KBGUIController ()
    {
        GUI = new KBGUI(this);
        folderListSelectionCheck();
        fileListSelectionCheck();
    }
    
    public void folderListSelectionCheck()
    {
        GUI.folderList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    GUI.csvList.setListData(
                        KBMasterController.folders.get(GUI.folderList.getSelectedIndex()).toList());
                    //view.getEntryListScroll().setEnabled(true);
                    //view.getNewEntryButton().setEnabled(true);
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
                    BaseFile bFile = KBMasterController.folders.get(GUI.folderList.getSelectedIndex()).bFiles.get(GUI.csvList.getSelectedIndex());
                    LinkedList<String> ll = KBMasterController.fileHandler.readFile(bFile.getPath());
                    String[] titles = ll.remove().split(",");
                    String[][] data = new String[ll.size()][titles.length];
                    for (int i = 0; i < ll.size(); i++)
                        data[i] = ll.get(i).split(",");
                    GUI.tableModel.setDataVector(data, titles);
                    GUI.tableModel.fireTableDataChanged();
                    /*
                            
                    GUI.csvList.getSelectedIndex();
                    KBMasterController.fileHandler.readFile()
                    GUI.table = new JTable
                    GUI.ta.setListData(
                        KBMasterController.folders.get(GUI.folderList.getSelectedIndex()).toList());*/
                }

            }
        });
    }
}
