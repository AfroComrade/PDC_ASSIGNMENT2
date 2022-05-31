/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assign2_test;


import java.io.File;
import java.util.LinkedList;
import java.sql.ResultSet;
import org.junit.Test;
import static org.junit.Assert.*;
import pdc_assign2.DatabaseHandler;
import pdc_assign2.KBMasterController;
import pdc_assign2.KBGUIController;
import pdc_assign2.FileHandler;

/**
 *
 * @author Yeran
 */
public class pdcTest
{
    @Test
    public void testMain()
    {
        System.out.println("main");
        String[] args = null;
        KBMasterController.main(args);
    }
    
    // Check can create, insert an drop tables
    @Test
    public void testDB()
    {
        String testFile = "test.csv";
        
        String sql = "CREATE TABLE \"" + testFile + "\" (id INT, name VARCHAR(50))";
        
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.establishConnection();
        dbHandler.updateDB(sql);
        
        String sql2 = "INSERT INTO \"" + testFile + "\" VALUES (5, 'word')";
        dbHandler.updateDB(sql2);
        // Should assert if we can't update the db
        
        String sql3 = "DROP TABLE \"" + testFile + "\"";
        dbHandler.updateDB(sql3);
        dbHandler.dropConnection();
    }
    
    // Check the gui loads properly and can display an error message
    // Will not be viewable
    @Test
    public void errorPanel()
    {
        KBGUIController GUIcontrol = new KBGUIController();
        GUIcontrol.GUI.errorPopup("Test Error");
    }
    
    // Check can read and write files
    @Test
    public void testFile()
    {
        FileHandler fHandler = new FileHandler();
        LinkedList<String> ll = fHandler.readFile("test.csv");
        for (String s : ll)
            System.out.println("test: " + s);
        fHandler.add(ll, "test.csv", "test");
        
        File file = new File("data/test/test.csv");
        file.delete();
        file = new File("data/test");
        file.delete();
    }
    
    @Test
    public void testdbRead()
    {
        String testFile = "test.csv";
        
        String sql = "CREATE TABLE \"" + testFile + "\" (id INT, name VARCHAR(50))";
        
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.establishConnection();
        dbHandler.updateDB(sql);
        
        String sql2 = "INSERT INTO \"" + testFile + "\" VALUES (5, 'word')";
        dbHandler.updateDB(sql2);
        
        String sql3 = "SELECT * FROM \"" + testFile + "\"";
        ResultSet rs = dbHandler.queryDB(sql3);
        
        try {
            int i = rs.getInt("id");
            assertEquals(i, "5");
        } catch (Exception e) {
            e.printStackTrace();
            assert(true);
        }
        
        String sql4 = "DROP TABLE \"" + testFile + "\"";
        dbHandler.updateDB(sql4);
        dbHandler.dropConnection();
    }
}
