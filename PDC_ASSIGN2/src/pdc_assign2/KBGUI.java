package pdc_assign2;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class KBGUI extends JPanel
{
    JFrame frame;
    
    JPanel left;
    JList folderList;
    JScrollPane fListSP;
    JList csvList;
    JScrollPane csvListSP;
    
    JPanel right;
    JTable table;
    DefaultTableModel tableModel;
    JScrollPane sp;
    
    KBGUIController GUIController;

    public KBGUI(KBGUIController controller)
    {
        super();
        /*
        setLayout(new BorderLayout());
        try
        {  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){}
        */
        
        setLayout(null);
        setPreferredSize(new Dimension(810, 800));
        setLocation(0, 0);
        
        
        /*
        left = new JPanel();
        folderList = new JList();
        fListSP = new JScrollPane(folderList);
        
        left.add(fListSP);
        add(left, BorderLayout.WEST);
        */
        leftPanelInitializer();
        rightPanelInitializer();
        
        GUIController = controller;
        
        setBorder(BorderFactory.createLineBorder(Color.BLUE));
        setVisible(true);

        frameInitializer();
    }
    
    private void leftPanelInitializer()
    {
        left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setLocation(10, 10);
        left.setSize(new Dimension(390, 600));
        
        JLabel folderLabel = new JLabel("Folders");
        folderLabel.setForeground(Color.BLUE);
        JLabel csvLabel = new JLabel("Files");
        csvLabel.setForeground(Color.RED);
        
        folderList = new JList();
        folderList.setForeground(Color.BLUE);
        fListSP = new JScrollPane(folderList);
        fListSP.setPreferredSize(new Dimension(150, 300));
        
        csvList = new JList();
        csvList.setForeground(Color.RED);
        csvListSP = new JScrollPane(csvList);
        csvListSP.setPreferredSize(new Dimension(150, 300));
        
        left.add(folderLabel);
        left.add(fListSP);
        left.add(csvLabel);
        left.add(csvListSP);
        add(left);
        left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        
        update();
    }
    
    private void rightPanelInitializer()
    {
        right = new JPanel();
        table = new JTable();
        tableModel = (DefaultTableModel)table.getModel();
        sp = new JScrollPane(table);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setLocation(410, 10);
        right.setSize(new Dimension(390, 600));
        sp.setPreferredSize(new Dimension(300, 500));
        
        JLabel tableLabel = new JLabel("Table");
                
        right.add(tableLabel);
        right.add(sp);
        add(right);
        right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    private void frameInitializer()
    {
        frame = new JFrame("Knowledge Base Respository");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
        frame.add(this);
    }
    
    private void fileListUpdater()
    {
        
    }
    
    public void update()
    {
        folderList.setListData(KBMasterController.folders.toArray());
        csvList.setListData(new String[0]);
        csvListSP.setEnabled((this.folderList.getSelectedIndex() > 0));
    }

}