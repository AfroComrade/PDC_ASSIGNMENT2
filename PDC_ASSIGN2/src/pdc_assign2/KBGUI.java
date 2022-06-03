package pdc_assign2;


import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/* Staticly handle the JPanel, initialize everything
    All listeners handled in KBGUICONTROLLER
*/

public class KBGUI extends JPanel
{
    Dimension screenSize;
    JFrame frame;
    
    JPanel left;
    String[] folderStrings;
    JList folderList;
    JScrollPane fListSP;
    String[] fileStrings;
    JList csvList;
    JScrollPane csvListSP;
    
    JButton addButton;
    JButton removeButton;
    
    JPanel right;
    JLabel tableLabel;
    JTable table;
    DefaultTableModel tableModel;
    JScrollPane sp;
    
    KBGUIController GUIController;

    public KBGUI(KBGUIController controller)
    {
        super();
        
        screenSize = Toolkit.getDefaultToolkit(). getScreenSize();
        screenSize = new Dimension(screenSize.width - 100, screenSize.height - 100);
        
        setLayout(null);
        setPreferredSize(screenSize);
        setLocation(0, 0);
        folderStrings = KBMasterController.dbhandle.getFolders();
        
        leftPanelInitializer();
        rightPanelInitializer();
        buttonInitializer();
        
        GUIController = controller;
        
        setBorder(BorderFactory.createLineBorder(Color.BLUE));
        setVisible(true);

        frameInitializer();
    }
    
    private void buttonInitializer()
    {
        JLabel addLabel = new JLabel("Adds csv to selected folder");
        JLabel removeLabel = new JLabel("Remove selected csv from repo");
        addLabel.setLocation(28, 605);
        addLabel.setSize(190, 50);
        removeLabel.setLocation(215, 605);
        removeLabel.setSize(190, 50);
        
        addButton = new JButton("Add file");
        addButton.setSize(190, 100);
        addButton.setLocation(10, 640);
        removeButton = new JButton("Remove File");
        removeButton.setSize(190, 100);
        removeButton.setLocation(210, 640);
        add(addLabel);
        add(removeLabel);
        add(addButton);
        add(removeButton);
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
        right.setSize(new Dimension((int)(screenSize.width * 0.6), 600));
        sp.setPreferredSize(new Dimension(300, 500));
        
        tableLabel = new JLabel("Table");
                
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
    
    public String chooseNewFile()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    public void update()
    {
        folderStrings = KBMasterController.dbhandle.getFolders();
        folderList.setListData(folderStrings);
        csvList.setListData(new String[0]);
        csvListSP.setEnabled((this.folderList.getSelectedIndex() > 0));
    }

    public void errorPopup(String errorString)
    {
        new errorPanel(errorString);
    }
    
    protected class errorPanel extends JPanel implements ActionListener
    {
        JFrame frame;
        
        public errorPanel(String warning)
        {
            super();
            frame = new JFrame("ERROR");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(this);
            frame.pack();
            frame.setVisible(true);
            frame.setSize(400, 100);
            
            setBorder(new TitledBorder(null, warning, TitledBorder.LEADING, TitledBorder.TOP, null, null));
            
            Button okButton = new Button("OK");
            add(okButton);
            okButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent event)
        {
            setVisible(false);
            frame.setVisible(false);
        }
    }
}