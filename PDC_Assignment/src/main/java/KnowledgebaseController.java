import java.util.ArrayList;
import java.util.LinkedList;

/*  Control access to each folder (with it's individual files).
    Call the Command-line interface and the filehandling classes from here.
*/

/*  All classes have separation for search, add and remove functionality
    The results of all outputs are passed from FileReadHandler as linked lists
*/

public class KnowledgebaseController
{
    private static final FileHandler fileHandler = new FileHandler();
    public static boolean running = true; // Modified in the CLIHandler if the user ever puts '0' or 'x' in an input
    public static ArrayList<Folder> folders;
    public static final String DATA_LOCATION = "./data";
    public static final String ACCESS_TXT = "accessed.txt";

    
    
    //  --------------------------- MAIN MENU LOOP CONTROLLER ---------------------------  //
    
    /*  Start the program.
        At program close, create an 'accessed.txt' file that records how often a file is searched. Load it from fileHandler at program start.
    */
    public static void main(String[] args) 
    {
        start();
        fileHandler.createAccessText();
    }
    
    /*  Load folder name and csv names into memory
        Show intro text and get the first option from the user
    */
    public static void start()
    {
        folders = fileHandler.getFolders(); 
        int input = CLIHandler.intro();     
        if (running)
            functionPerform(input);
    }
    
    //  Called at the end of every loop if the program is still running to search/add/remove
    public static void functionPerform(int input) 
    {
        switch(input) {
            case 1:
                searchCSV();
                break;
            case 2:
                addFile();
                break;
            case 3:
                removeCSV();
                break;
            case 4:
                writeToFile();
                break;
        }
    }
    
    //  --------------------------- SEARCH IMPLEMENTATION ---------------------------  //
    /*  Search through the folder and file names that are loaded into memory.
        If a file name is a direct match for the input, print it's contents.
        Otherwise, print the name of every file that contains the searched string, 
            or all subfiles if a folder contains the searched string.    
    */
    public static void searchCSV()
    {
        String fileSearched = CLIHandler.search();
        
        if (running)
        {
            CLIHandler.searchConfirm(fileHandler.search(fileSearched));
            
            int input = CLIHandler.mainMenu();
            functionPerform(input);
        }
    }
    
    //  --------------------------- ADD FILE IMPLEMENTATION ---------------------------  //
    /*  Retrieve the following:
             - Existing absolute file path
             - New file name
             - Folder name 
        Then copy from existing file path to new file path in the folder name.
        If the file name is invalid or the absolute path doesn't exist, tell the user
    */
    public static void addFile() //
    {
        String existingFilePath = CLIHandler.add();
        if (!running)
            return;
        String newFileName = CLIHandler.newFile();
        if (!running)
            return;
        String folderName = CLIHandler.getFolder();
        
        if (running)
        {
            LinkedList<String> list = fileHandler.readFile(existingFilePath); // Save the list of strings read to then add later
            boolean added = false;
            if (!list.isEmpty() && fileHandler.add(list, newFileName, folderName))
            {
                addFileToFolders(newFileName, folderName);
                added = true;
            }                
            CLIHandler.addConfirm(added);
            functionPerform(CLIHandler.mainMenu());
        }
    }
    
    //  Handle adding file to the folder structure in memory
    public static void addFileToFolders(String newFileName, String folderName)
    {
        folderName = folderName.toLowerCase();
        FileType fType = checkType(newFileName);
        boolean folderAlreadyExists = false;
        
        for (Folder folder : folders)
            if (folder.name.toLowerCase().equals(folderName))
            {
                folderAlreadyExists = true;
                if (fType == FileType.CSV)
                    folder.bFiles.add(new CSV(newFileName, folder.name + "/" + newFileName, 0));
                else
                    folder.bFiles.add(new TextFile(newFileName, folder.name + "/" + newFileName, 0));
            }
        if (!folderAlreadyExists) // Create a new folder if we need it
        {
            LinkedList<BaseFile> bFiles = new LinkedList<BaseFile>();
            
            if (fType == FileType.CSV)
                bFiles.add(new CSV(newFileName, folderName + "/" + newFileName, 0));
            else
                bFiles.add(new TextFile(newFileName, folderName + "/" + newFileName, 0));
            
            folders.add(new Folder(folderName, fileHandler.DATA_LOC + "/" + folderName, bFiles));
        }
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
    
    //  --------------------------- REMOVE FILE IMPLEMENTATION ---------------------------  //
    /*  Simply take a string input, check the file exists, and if it does then delete it.
    */
    
    public static void removeCSV()
    {
        String fileToRemove = CLIHandler.remove();
        
        if (running)
        {
            CLIHandler.removeConfirm(checkFileExists(fileToRemove));
            functionPerform(CLIHandler.mainMenu());
        }
    }
    
    // Check the file exists in the folder structure loaded in memory, then call fileHandler to handle file deletion
    public static boolean checkFileExists(String s)
    {
        for (Folder folder : folders)
            {
                for (BaseFile bFile : folder.bFiles)
                {
                    if (bFile.name.equals(s))
                    {
                        fileHandler.removeLoaded(folder, s);
                        folder.remove(bFile);
                        return true;
                    }
                }
            }
        return false;
    }
    
    //  --------------------------- WRITE TO FILE IMPLEMENTATION ---------------------------  //
    
    public static void writeToFile()
    {
        String fileName = CLIHandler.getWriteFileName();
        if (!running)
            return;
        String string = CLIHandler.getWriteString();
        
        if (running)
        {
            LinkedList<String> list = fileHandler.search(fileName);
            boolean writeSuccessful = false;
            if (!list.isEmpty() && fileHandler.writeFound)
            {
                fileHandler.write(list, string);
                writeSuccessful = true;
            }
            CLIHandler.writeConfirm(writeSuccessful);
            functionPerform(CLIHandler.mainMenu());
        }
    }
}