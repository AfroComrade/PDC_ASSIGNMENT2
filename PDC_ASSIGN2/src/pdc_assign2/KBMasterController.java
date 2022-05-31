package pdc_assign2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.sql.ResultSet;

/*  Control access to each folder (with it's individual files).
    Call the Command-line interface and the filehandling classes from here.
*/

/*  All classes have separation for search, add and remove functionality
    The results of all outputs are passed from FileReadHandler as linked lists
*/

public class KBMasterController
{
    public static final FileHandler fileHandler = new FileHandler();
    public static boolean running = true; // Modified in the CLIHandler if the user ever puts '0' or 'x' in an input
    public static ArrayList<Folder> folders;
    public static final String DATA_LOCATION = "./data";
    public static final String ACCESS_TXT = "accessed.txt";
    public static final DatabaseHandler dbhandle = new DatabaseHandler();
    
    public static ResultSet temp;

    public KBGUI GUI;
    
    
    //  --------------------------- MAIN MENU LOOP CONTROLLER ---------------------------  //
    
    /*  Start the program.
        At program close, create an 'accessed.txt' file that records how often a file is searched. Load it from fileHandler at program start.
    */
    public static void main(String[] args) 
    {
        new KBMasterController().start();
        //fileHandler.createAccessText();
    }
    
    /*  Load folder name and csv names into memory
        Show intro text and get the first option from the user
    */
    public void start()
    {
        folders = fileHandler.getFolders();
        dbhandle.dbsetup();
        
        KBGUIController GUIHandler = new KBGUIController();
    }
    
    //  Handle adding file to the folder structure in memory
    public static boolean addFileToFolders(String newFileName, String folderName)
    {
        folderName = folderName.toLowerCase();
        FileType fType = checkType(newFileName);
        boolean added = false;
        boolean folderAlreadyExists = false;
        
        for (Folder folder : folders) {
            if (folder.name.toLowerCase().equals(folderName))
            {
                folderAlreadyExists = true;
                if (fType == FileType.CSV)
                    folder.bFiles.add(new CSV(newFileName, ".\\data\\" + folder.name + "/" + newFileName, 0));
                else
                    folder.bFiles.add(new TextFile(newFileName, ".\\data\\" + folder.name + "/" + newFileName, 0));
                added = true;
            }
        }
        if (!folderAlreadyExists) // Create a new folder if we need it
        {
            LinkedList<BaseFile> bFiles = new LinkedList<BaseFile>();
            
            if (fType == FileType.CSV)
                bFiles.add(new CSV(newFileName, ".\\data\\" + folderName + "/" + newFileName, 0));
            else
                bFiles.add(new TextFile(newFileName, ".\\data\\" + folderName + "/" + newFileName, 0));
            
            folders.add(new Folder(folderName, fileHandler.DATA_LOC + "/" + folderName, bFiles));
            added = true;
        }
        return added;
    }
    
    // Handle determining if the file added is .txt or .csv
    public static FileType checkType(String s)
    {
        FileType fType;
        if (s.substring(s.length()-3, s.length()).equals("txt"))
            fType = FileType.TEXT;
        else
            fType = FileType.CSV;
        return fType;
    }
    
    // Check the file exists in the folder structure loaded in memory, then call fileHandler to handle file deletion
    public static boolean checkFileExists(String s)
    {
        for (Folder folder : folders)
            {
                for (BaseFile bFile : folder.bFiles)
                {
                    System.out.println("Checking " + bFile + " against " + s);
                    if (bFile.name.equals(s))
                    {
                        System.out.println("Found");
                        fileHandler.removeLoaded(folder, s);
                        folder.remove(bFile);
                        return true;
                    }
                }
            }
        return false;
    }
}