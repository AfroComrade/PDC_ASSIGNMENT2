package pdc_assign2;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Yeran
 */
public class DatabaseHandler
{
    Connection conn;
    String url = "jdbc:derby:KnowledgeDB; create=true";
    String dbusername = "pdc";
    String dbpassword = "pdc";
    
    String foldersName = "folders";
    

    public DatabaseHandler() {
        establishConnection();
    }

    
    public void dbsetup() {
        try {
            if(!checkTableExists(foldersName))
            {
                String createUserInfoTable = "CREATE TABLE " + foldersName + " (id INT, folder VARCHAR(50), file VARCHAR(50))";
                updateDB(createUserInfoTable);
            }
            
            // Create tables for each file, and entries in 'Folders' for each basefile
            
            for (Folder folder : KBMasterController.folders)
            {
                for (BaseFile bFile : folder.bFiles)
                {
                    if (!(checkTableExists(bFile.name) && checkTableInFolders(bFile.name)))
                    {
                        createTableFromBFile(bFile, folder);
                    }
                        
                }
                
                
                
                /*
                            String insertTableCommand = "INSERT INTO " + bFile.name + " VALUES (";
            for (int j = 0; j < titles.length - 1; j++)
            {
                insertTableCommand += data[i][j] + " VARCHAR[50], ";
            }
            insertTableCommand += data[i][titles.length - 1] + "VARCHAR(50))";

                */
            }
            
        } catch (Throwable e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args)
    {
        String name = "annual-enterprise-survey-2020-financial-year-provisional-csv.csv";
        CSV csv = new CSV(name, "Business/" + name, 0);
        LinkedList ll = new LinkedList();
        ll.add(csv);
        
        Folder folder = new Folder("Business", "./data" + "/Business", ll);
        
        DatabaseHandler dbhandle = new DatabaseHandler();
        dbhandle.createTableFromBFile(csv, folder);
    }
    
    private boolean createTableFromBFile(BaseFile bFile, Folder folder)
    {
        if (checkTableExists(bFile.name))
            return false;
        
        LinkedList<String> ll = KBMasterController.fileHandler.readFile(bFile.getPath());
        String[] titles = ll.remove().split(",");
        
        String createTableCommand = "CREATE TABLE " + bFile.name + " (";
        for (int i = 0; i < titles.length-1; i++)
            createTableCommand += titles[i] + " VARCHAR(50), ";
        createTableCommand += titles[titles.length-1] + " VARCHAR(50))";
        this.updateDB(createTableCommand);
        
        
        String[][] data = new String[ll.size()][titles.length];
        for (int i = 0; i < ll.size(); i++)
            data[i] = ll.get(i).split(",");
        
        
        for (int i = 0; i < data.length; i++)
        {
            String insertTableCommand = "INSERT INTO " + bFile.name + " VALUES (";
            for (int j = 0; j < titles.length - 1; j++)
            {
                insertTableCommand += data[i][j] + " VARCHAR[50], ";
            }
            insertTableCommand += data[i][titles.length - 1] + "VARCHAR(50))";
            
            updateDB(insertTableCommand);
        }
        
        String insertFolderTable = "INSERT INTO " + foldersName + " VALUES (" 
                + bFile.name.hashCode() + " INT, "
                + folder.name + " VARCHAR(50), " 
                + bFile.name + " VARCHAR(50)";
        updateDB(insertFolderTable);
        return true;
    }
    
    private boolean checkTableInFolders(String tableName)
    {
        String selectFolders = "SELECT * FROM " + foldersName + " WHERE id = " + tableName.hashCode() + " AND name = " + tableName;
        ResultSet rs = queryDB(selectFolders);
        System.out.println(rs);
        return true;
    }
    
    private boolean checkTableExists(String newTableName) {
        boolean exists = false;
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rsDBMeta = dbmd.getTables(null, null, null, null);//types);
            //Statement dropStatement=null;
            while (rsDBMeta.next()) {
                String tableName = rsDBMeta.getString("TABLE_NAME");
                if (tableName.compareToIgnoreCase(newTableName) == 0) {
                    exists = true;
                }
            }
            if (rsDBMeta != null) {
                rsDBMeta.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return exists;
    }
    
    public void updateDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public ResultSet queryDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultSet;
    }
    
    public void establishConnection() {
        //Establish a connection to Database
        try {
            conn = DriverManager.getConnection(url, dbusername, dbpassword);
            System.out.println(url+" Connected.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
}

