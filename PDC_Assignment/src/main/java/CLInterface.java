import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

/*  THIS CLASS HANDLES INPUT AND OUTPUT
    CLI LOGIC SHOULD BE HANDLED IN CLI HANDLER
*/

public class CLInterface
{
    private static Scanner scanner = new Scanner(System.in);
    
    //  --------------------------- MAIN MENU LOOP IMPLEMENTATION ---------------------------  //
    
    public static void printIntro()
    {
        System.out.println("Welcome to Yeran's Knowledge Repository!");
        System.out.print("Current Folders to be searched: ");
        for (Folder f : KnowledgebaseController.folders)
            System.out.print(f.name+" ");
        System.out.println("\nPress x at any time to quit   ");
        System.out.println("------------------------------");
    }
    
    public static String printMenu()
    {
        System.out.println("What would you like to do?    ");
        System.out.println("(Type just the number)        ");
        System.out.println("1. Search                     ");
        System.out.println("2. Add                        ");
        System.out.println("3. Remove                     ");
        System.out.println("4. Write                      ");
        System.out.println("0. Exit                       ");
        
        return scanner.nextLine();
    }
    
    public static String printSearch()
    {
        System.out.println("What would you like to search?");
        return scanner.nextLine();
    }
    
    public static void printSearchResult(LinkedList<String> output)
    {
        System.out.println("\n------------------------------");
        if (output.isEmpty())
            System.out.println("No matches found");
        else
        {
            for (String s : output)
                System.out.println(s);
        }
        System.out.println("------------------------------\n");
    }
    
    public static void failedInput()
    {
        System.out.println("Incorrect input. Please check the options and try again");
    }
    
    
        //  --------------------------- ADD IMPLEMENTATION ---------------------------  //
    /*  Contains: 
            - Input and validate the absolute path of a file
            - Input the new name of the file
            - Input the name of the folder this file resides in
            - Check that the file being added conforms with naming conventions (.txt or .csv)
        Also handles feedback to the user if the file is added or not
    */

    public static String addAbsolute()
    {
        String toReturn = "";
        System.out.println("What is the absolute path of the file you would like to add?");
        System.out.println("(csv or txt only)");

        toReturn = scanner.nextLine();
        return toReturn;
    }
    
    public static String addNewName()
    {
        System.out.println("What is the name of the new file?");
        String toReturn = scanner.nextLine();
        
        return toReturn;
    }
    
    public static String addFolderName()
    {
        System.out.println("And what folder?");
        String toReturn = scanner.nextLine();
        return toReturn;
    }
    
    public static void addConfirmPrint(boolean b)
    {
        if (b)
            System.out.println("File Added");
        else
            System.out.println("Error occurred. Either your filepath was wrong, your new filename wasn't a .txt or .csv, or the file already exists");
    }
    
    public static void printInvalidFile()
    {
        System.out.println("Invalid file name. Please make sure it is long enough and a .txt or .csv file");
    }
    
    public static void printInvalidFolder()
    {
        System.out.println("Invalid Folder name. Please make sure it is long enough");
    }
    
    
    //  --------------------------- REMOVE IMPLEMENTATION ---------------------------  //
    /*  Handle receiving the file name to remove and confirming if the file is removed
    */

    public static String printRemove()
    {
        System.out.println("What file would you like to remove?");
        return scanner.nextLine();
    }
    
    public static void removeConfirmPrint(boolean b)
    {
        if (b)
            System.out.println("--- File found. Removed");
        else
            System.out.println("File not found. Please search for your file to check your spelling");
    }
    
    //  --------------------------- REMOVE IMPLEMENTATION ---------------------------  //
    public static String writeFile()
    {
        System.out.println("What file would you like to write to?");
        return scanner.nextLine();
    }
    
    public static String writeString()
    {
        System.out.println("And what would you like to add to the file?");
        return scanner.nextLine();
    }
    
    public static void writeConfirm(boolean b)
    {
        if (b)
            System.out.println("--- File found, line added");
        else
            System.out.println("File not found. Please search for your file to check your spelling");

    }
}

