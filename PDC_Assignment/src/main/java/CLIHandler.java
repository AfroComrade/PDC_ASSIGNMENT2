import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

/*  THIS CLASS PRIMARILY HANDLES INTERFACE LOGIC
    INPUT/OUTPUT SHOULD GO THROUGH CLInterface.JAVA
    
Note:   The main distinction is that checking if the input is an exit variable
        is checked here.
*/  

public class CLIHandler
{
    
    //  --------------------------- MAIN MENU LOOP CONTROLLER ---------------------------  //
    /*  Handle both the main menu input and the first screen the user sees.
        Also contains implementation to make sure any text or int input is valid, and that
            the user wants to continue running the program.
    */
    public static int intro()
    {
        CLInterface.printIntro();
        return mainMenu();
    }
    
    public static int mainMenu()
    {
        int option = -1;
        while(KBMasterController.running && (option < 0 || option > 4))
        {
            String input = CLInterface.printMenu();
            checkRunning(input);
            if (KBMasterController.running)
            {
                option = parseInputToInt(input);
                if (option < 0 || option > 4)
                    CLInterface.failedInput();
            }
        }
        return option;
    }
    
    //  parse input to int
    public static int parseInputToInt(String input)
    {
        int toreturn = -1;
        
        try {
            toreturn = Integer.parseInt(input);             
            
            if ((toreturn > 4) || (toreturn < 0))
                throw new InputMismatchException();
            return toreturn;
        } catch (Exception e) {
        }
        return toreturn;
    }
    
    //  Called at the end of every input to make sure the user still wants to
    //      run the program
    public static boolean checkRunning(String input)
    {
        boolean running = true;
        if (input.toLowerCase().equals("x"))
        {
            running = false;
        }
        
        try {
            if (Integer.parseInt(input) == 0)
                running = false;
        } catch (Exception e)
        {
        }
        
        if (!running)
        {
            KBMasterController.running = false;
            System.out.println("Goodbye!");
        }
        return (running);
    }

    //  --------------------------- SEARCH IMPLEMENTATION ---------------------------  //
    /*  Contains: 
            - Receaive a search string
            - Print a file (if found)
    */
    public static String search()
    {
        String input = CLInterface.printSearch();
        checkRunning(input);
        return input;
    }
    
    public static void searchConfirm(LinkedList<String> output)
    {
        CLInterface.printSearchResult(output);
    }
    
    
    //  --------------------------- ADD IMPLEMENTATION ---------------------------  //
    /*  Contains: 
            - Input and validate the absolute path of a file
            - Input the new name of the file
            - Input the name of the folder this file resides in
            - Check that the file being added conforms with naming conventions (.txt or .csv)
        Also handles feedback to the user if the file is added or not
    */
    public static String add()
    {
        String absolutePath = "";
        boolean correctAbsolute = false;
        
        while (KBMasterController.running && !correctAbsolute)
        {
            absolutePath = CLInterface.addAbsolute();
            checkRunning(absolutePath);
            if (checkAdd(absolutePath) > 0)
                correctAbsolute = true;
            else if (KBMasterController.running)
                CLInterface.printInvalidFile();
        }
        
        return absolutePath;
    }
    
    public static String newFile()
    {
        String newName = "";
        boolean correctNewName = false;
        
        while (KBMasterController.running && !correctNewName)
        {
            newName = CLInterface.addNewName();
            checkRunning(newName);
            if (checkAdd(newName) > 0)
                correctNewName = true;
            else if (KBMasterController.running)
                CLInterface.printInvalidFile();
                
        }
        return newName;
    }
    
    public static String getFolder()
    {
        String folderName = "";
        boolean correctFolderSize = false;
        
        while (KBMasterController.running && !correctFolderSize)
        {
            folderName = CLInterface.addFolderName();
            checkRunning(folderName);
            if (folderName.length() >= 4)
                correctFolderSize = true;
            else if (KBMasterController.running)
                CLInterface.printInvalidFolder();
        }
        return folderName;
    }
    
    public static int checkAdd(String s)
    {
        int correctFileType = 0;
        if (s.length() <= 5)
            correctFileType = 0;
        else if (s.substring(s.length()-4, s.length()).equals(".csv"))
            correctFileType = 1;
        else if (s.substring(s.length()-4, s.length()).equals(".txt"))
            correctFileType = 2;
        return correctFileType;
    }
    
    public static void addConfirm(boolean b)
    {
        CLInterface.addConfirmPrint(b);
    }
    
    //  --------------------------- REMOVE IMPLEMENTATION ---------------------------  //
    /*  Handle receiving the file name to remove and confirming if the file is removed
    */
    public static String remove()
    {
        String toReturn = CLInterface.printRemove();
        checkRunning(toReturn);
        return toReturn;
    }
    
    public static void removeConfirm(boolean b)
    {
        CLInterface.removeConfirmPrint(b);
    }
    
    //  --------------------------- WRITE IMPLEMENTATION ---------------------------  //
    public static String getWriteFileName()
    {
        String writeFile = "";
        boolean correctWritePath = false;
        
        while (KBMasterController.running && !correctWritePath)
        {
            writeFile = CLInterface.writeFile();
            checkRunning(writeFile);
            if (checkAdd(writeFile) > 0)
                correctWritePath = true;
            else if (KBMasterController.running)
                CLInterface.printInvalidFile();
        }

        return writeFile;
    }
    
    public static String getWriteString()
    {
        String writeString = CLInterface.writeString();
        checkRunning(writeString);
        return writeString;
    }
    
    public static void writeConfirm(boolean b)
    {
        CLInterface.writeConfirm(b);
    }
}