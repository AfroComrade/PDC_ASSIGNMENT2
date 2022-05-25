
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class KnowledgebaseGUI extends JPanel
{
    JTable view;
    JPanel left;
    JPanel right;
    JList csvJList;
    JScrollPane sp;

    public KnowledgebaseGUI(String[] titles, String[][] data)
    {
        super();
        setLayout(new BorderLayout());
        add(left = new JPanel(), BorderLayout.WEST);
        left.setPreferredSize(new Dimension(300, 300));
        add(right = new JPanel(), BorderLayout.CENTER);
        right.setPreferredSize(new Dimension(300, 300));

        setPreferredSize(new Dimension(900, 900));
        right.add(view = new JTable(data, titles));
        view.getTableHeader().setReorderingAllowed(false);
        JScrollBar sb = new JScrollBar();
        String[] list = {"Hey", "Ho", "Hi", "No"};
        csvJList = new JList(list);
        csvJList.add(sb);
        //csvJList.setPreferredSize(new Dimension(100, 100));
        
        left.add(csvJList);
        left.add(sb);
        sp = new JScrollPane(view);
        right.add(sp);
        setVisible(true);
        
    }
    
    public void setTable(String[] titles, String[][] data)
    {
        view = new JTable(data, titles);
        right.add(view);
        sp = new JScrollPane(view);
        right.add(sp);
        setVisible(true);
    }
    
    public static void main(String[] args)
    {
        String testFile1 = "./data/Census/access-to-basic-amenities-total-responses-2018-census-csv.csv";
        String testFile2 = "./data/Census/occupation-2018-census-csv.csv";
        
        
        FileHandler fh = new FileHandler();
        LinkedList<String> list = fh.readFile(testFile1);
        String[] titles = list.remove().split(",");
        
        String[][] data = new String[list.size()][titles.length];
        for (int i = 0; i < list.size(); i++)
        {
            data[i] = list.remove().split(",");
        }
        
        JFrame frame = new JFrame("CSV Repository");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Test data change after table is already instantiated
        LinkedList<String> list2 = fh.readFile(testFile2);
        String[] titles2 = list2.remove().split("\",\"");
        String[][] data2 = new String[list2.size()][titles.length];
        System.out.println("Titles:");
        for (int i = 0; i < titles2.length; i++)
        {
            System.out.println(titles2[i]);
        }
        System.out.println("Titles ended");
        
        for(int i = 0; i < titles2.length; i++)
        {
            data2[i] = list2.remove().split("\",\"");
            System.out.println(data2[0][i]);
        }
        /*
        for (int i = 0; i < list2.size(); i++) {
            data2[i] = list2.remove().split("\",\"");
        }
        */
            
        
        //kbgui.setTable(titles, data);
        KnowledgebaseGUI kbgui = new KnowledgebaseGUI(titles2, data2);
        frame.getContentPane().add(kbgui);
        frame.pack();
        frame.setVisible(true);

        
    }
}