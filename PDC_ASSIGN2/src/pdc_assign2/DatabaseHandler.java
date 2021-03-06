package pdc_assign2;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.sql.SQLSyntaxErrorException;

/*  Handle DB Commands
    
*/  

/**
 *
 * @author Yeran
 */
public class DatabaseHandler
{
    Connection conn;
    String url = "jdbc:derby:KnowledgeDB_Ebd; create=true";
    String dbusername = "pdc";
    String dbpassword = "pdc";
    
    String foldersName = "folders";
    

    public DatabaseHandler() {
        
    }
    
    //  Read in files from the project. Any files not loaded in the database, get loaded here
    public void dbsetup() {
        try {
            establishConnection();
            if(!checkTableExists("\"" + foldersName + "\""))
            {
                String createUserInfoTable = "CREATE TABLE \"" + foldersName + "\"  (id INT, folder VARCHAR(50), file VARCHAR(250))";
                updateDB(createUserInfoTable);
            }
            
            for (Folder folder : KBMasterController.folders)
            {
                for (BaseFile bFile : folder.bFiles)
                {
                    String name = bFile.name;
                    if (name.length() > 250)
                        name = name.substring(0, 250);
                    if (!(checkTableExists(name)) && !(checkTableInFolders(name)))
                    {
                        createTableFromBFile(bFile, folder);
                    }
                }
            }
            System.out.println("dbsetup complete");
            conn.close();
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    // Used by the Files lies in KBGUI
    public String[] getFiles(String folder)
    {
        establishConnection();
        String getFiles = "SELECT file FROM \"" + foldersName + "\" WHERE folder = \'" + folder + "\'";
        
        try {
            ResultSet rs = queryDB(getFiles);
            rs.last();
            int rows = rs.getRow();
            rs.first();
            
            String [] files = new String[rows];
            for (int i = 0; i < rows; i++)
            {
                files[i] = rs.getString("file");
                rs.next();
            }
            rs.close();
            conn.close();
            return files;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get the list of folders for folder JList
    public String[] getFolders()
    {
        establishConnection();
        String getFolders = "SELECT DISTINCT folder FROM \"" + foldersName + "\"";
        ResultSet rs = queryDB(getFolders);

        try {
            rs.last();
            int rows = rs.getRow();
            rs.first();
            
            String[] folders = new String[rows];
            for (int i = 0; i < rows; i++)
            {
                folders[i] = rs.getString("folder");
                rs.next();
            }
            rs.close();
            conn.close();
            return folders;
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return null;
    }
    
    // We need to read each character individually in order to create tables properly on the GUI
    private String[] processToStringArray(String s)
    {
        ArrayList<String> strings = new ArrayList<>();
        
        String newString = "";
        boolean quoting = false;
        for (int i = 0; i < s.length(); i++)
        {
            switch (s.charAt(i)) {
                case '\n':
                    strings.add(newString);
                    break;
                case '\'':
                    newString += "";
                    break;
                case '???':
                    newString += '-';
                    break;
                case ',':
                    if (quoting)
                    {
                        newString += s.charAt(i);
                        break;
                    }
                    if (newString.length() > 50)
                    {
                        newString = newString.substring(0, 50);
                    }
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
    
    //  Create a table for each file that doesn't exist in the db
    //  Also insert the data into that table as imported from the dsv
    private boolean createTableFromBFile(BaseFile bFile, Folder folder)
    {
        String name = bFile.name;
        if (name.length() > 250)
            name = name.substring(0, 250);
        if (checkTableExists(name) || checkTableInFolders(name))
            return false;
        
        LinkedList<String> ll = KBMasterController.fileHandler.readFile(bFile.getPath());
        String[] titles = processToStringArray(ll.remove());
        
        String createTableCommand = "CREATE TABLE \"" + name + "\"  (\"";
        for (int i = 0; i < titles.length - 1; i++)
        {
            createTableCommand += titles[i] + "\" VARCHAR(50), \"";
        }
        createTableCommand += titles[titles.length - 1] + "\" VARCHAR(50))";
        this.updateDB(createTableCommand);
        
        
        String[][] data = new String[ll.size()][titles.length];
        for (int i = 0; i < data.length; i++)
            data[i] = processToStringArray(ll.remove());
        
        for (int i = 0; i < data.length; i++)
        {
            String insertTableCommand = "INSERT INTO \"" + name + "\" VALUES (\'";
            for (int j = 0; j < titles.length - 1; j++)
            {
                if (data[i][j].length() > 50)
                {
                    data[i][j] = data[i][j].substring(0, 50);
                }
                insertTableCommand += data[i][j] + "\', \'";
            }
            if (data[i][titles.length - 1].length() > 50)
                data[i][titles.length - 1] = data[i][titles.length-1].substring(0, 50);
            insertTableCommand += data[i][titles.length - 1] + "\')";
            updateDB(insertTableCommand);
        }
        
        String insertFolderTable = "INSERT INTO \"" + foldersName + "\" VALUES (" 
                + Math.abs(bFile.name.hashCode()) + ", \'"
                + folder.name + "\', \'" 
                + name + "\')";
        updateDB(insertFolderTable);
        
        return true;
    }
    
    // Simple function for deleting tables
    public boolean removeTable(String tableName)
    {
        establishConnection();
        if (!checkTableExists(tableName))
            return false;
        
        String removeTable = "DROP TABLE \"" + tableName + "\"";
        updateDB(removeTable);
        String removeEntry = "DELETE FROM \"" + foldersName + "\" WHERE id = " + Math.abs(tableName.hashCode());
        updateDB(removeEntry);
        
        try {
            conn.close();
        } catch (Exception e) {}
        return true;
    }
    
    //  Simple function for checking a table exists in the folders table
    private boolean checkTableInFolders(String tableName)
    {
        String selectFolders = "SELECT * FROM \"" + foldersName + "\" WHERE id = " + Math.abs(tableName.hashCode()) + " AND file = \"" + tableName + "\"";
        boolean exists = true;
        try {
            establishConnection();
            this.conn.createStatement().executeQuery(selectFolders);
        } catch (SQLException ex) { 
            if (ex instanceof SQLSyntaxErrorException)
            {
                exists = false;
            }
            else
            {
                ex.printStackTrace();
            }
        }
        return exists;
    }

    //  Simple function for checking a table exists in the database
    private boolean checkTableExists(String newTableName) {
        boolean exists = false;
        try {
            establishConnection();

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
            System.out.println("Error checktablexists: " +newTableName);
            ex.printStackTrace();
        }
        return exists;
    }
    
    //  NOTE: This function doesn't close the result set.
    //  It is only called when we click on a csv in the GUI
    //  In here, we close it.
    public ResultSet getTable(String tableName)
    {
        establishConnection();
        String name = tableName;
        if (name.length() > 250)
            name = tableName.substring(0, 250);
        String getTable = "SELECT * FROM \"" + name + "\"";
        ResultSet rs = queryDB(getTable);
        return rs;
    }
    
    // Basic funtion for updating a DB
    public void updateDB(String sql) 
    {
        Connection connection = this.conn;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Command: " + sql);
            System.out.println(ex.getMessage());
        }
    }
    
    // Basic funtion for querying a DB
    public ResultSet queryDB(String sql) 
    {
        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultSet;
    }

    // Allow establish and drop outside of dbhandle
    public void establishConnection() {
        try {
            conn = DriverManager.getConnection(url, dbusername, dbpassword);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Allow establish and drop outside of dbhandle
    public void dropConnection() 
    {
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

