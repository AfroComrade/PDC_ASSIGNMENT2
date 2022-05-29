package pdc_assign2;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    //String url = "jdbc:derby:KnowledgeDB; create=true";
    String url = "jdbc:derby://localhost:1527/KnowledgeDB; create=true";
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
                String createUserInfoTable = "CREATE TABLE \"" + foldersName + "\"  (id INT, folder VARCHAR(50), file VARCHAR(50))";
                updateDB(createUserInfoTable);
            }
            
            for (Folder folder : KBMasterController.folders)
            {
                for (BaseFile bFile : folder.bFiles)
                {
                    if (!(checkTableExists(bFile.name) && checkTableInFolders(bFile.name)))
                    {
                        createTableFromBFile(bFile, folder);
                    }
                        
                }
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args)
    {

        /*
        String name = "annual-enterprise-survey-2020-financial-year-provisional-csv.csv";
        CSV csv = new CSV(name, "./data/Business/" + name, 0);
        LinkedList ll = new LinkedList();
        ll.add(csv);
        
        Folder folder = new Folder("Business", "./data" + "/Business", ll);
        
        DatabaseHandler dbhandle = new DatabaseHandler();
        dbhandle.updateDB("DROP TABLE \"" + name + "\"");
        dbhandle.createTableFromBFile(csv, folder);
        
        System.out.println("Check");
        dbhandle.printTable("\"" + csv.toString() + "\"");
        */
    }
    
    private String[] processToStringArray(String s)
    {
        ArrayList<String> strings = new ArrayList<>();
        
        String newString = "";
        boolean quoting = false;
        for (int i = 0; i < s.length(); i++)
        {
            switch (s.charAt(i)) {
                case '\'':
                    newString += "";
                case '�':
                    newString += '-';
                    break;
                case ',':
                    if (quoting)
                    {
                        newString += s.charAt(i);
                        break;
                    }
                    if (newString.length() > 50)
                        newString = newString.substring(0, 50);
                    strings.add(newString);
                    newString = "";
                    break;
                case '\"':
                    if (!quoting)
                        quoting = true;
                    else
                        quoting = false;
                    break;
                default:
                    newString += s.charAt(i);
            }
        }
        
        
        String[] processedStrings = new String[strings.size()];
        for (int i = 0; i < processedStrings.length; i++)
            processedStrings[i] = strings.get(i);

        return processedStrings;
    }
    
    private boolean createTableFromBFile(BaseFile bFile, Folder folder)
    {
        if (checkTableExists(bFile.name))
            return false;
        
        LinkedList<String> ll = KBMasterController.fileHandler.readFile(bFile.getPath());
        String[] titles = processToStringArray(ll.remove());
        
        String createTableCommand = "CREATE TABLE \"" + bFile.name + "\"  (\"";
        
        for (int i = 0; i < titles.length-1; i++)
        {
            createTableCommand += titles[i] + "\" VARCHAR(50), \"";
        }
        createTableCommand += titles[titles.length-1] + "\" VARCHAR(50))";
        System.out.println(createTableCommand);
        this.updateDB(createTableCommand);
        
        
        String[][] data = new String[ll.size()][titles.length];
        for (int i = 0; i < ll.size(); i++)
            data[i] = processToStringArray(ll.remove());
        
        for (int i = 0; i < data.length; i++)
        {
            String insertTableCommand = "INSERT INTO \"" + bFile.name + "\" VALUES (\'";
            for (int j = 0; j < titles.length - 1; j++)
            {
                insertTableCommand += data[i][j] + "\', \'";
            }
            insertTableCommand += data[i][titles.length - 1] + "\')";
            //System.out.println(insertTableCommand);
            
            updateDB(insertTableCommand);
        }
        
        /*
        String insertFolderTable = "INSERT INTO " + foldersName + " VALUES (" 
                + bFile.name.hashCode() + " INT, "
                + folder.name + " VARCHAR(50), " 
                + bFile.name + " VARCHAR(50))";
        updateDB(insertFolderTable);
        */
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
        String name = "\"" + newTableName + "\"";
        boolean exists = false;
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rsDBMeta = dbmd.getTables(null, null, null, null);//types);
            //Statement dropStatement=null;
            while (rsDBMeta.next()) {
                String tableName = rsDBMeta.getString("TABLE_NAME");
                if (tableName.compareToIgnoreCase(name) == 0) {
                    exists = true;
                }
            }
            if (rsDBMeta != null) {
                rsDBMeta.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error checktablexists: " +name);
            ex.printStackTrace();
        }
        return exists;
    }
    
    public void printTable(String table)
    {
        ResultSet rs = queryDB("SELECT * FROM " + table);
        List<String> sids = new ArrayList<String>();
        try {
            while (rs.next())
            {
                sids.add(rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String s : sids)
            System.out.println(s);
    }
    
    public void updateDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            System.out.println("Command: " + sql);
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

