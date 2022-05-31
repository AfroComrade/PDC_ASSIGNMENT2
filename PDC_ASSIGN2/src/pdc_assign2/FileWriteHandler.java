package pdc_assign2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/*  Handle writing to files
    Should never read files or handle CLI input/output
*/

public class FileWriteHandler
{
    private FileHandler fileHandler;
    private File file;
    
    //  --------------------------- MAIN FUNCTIONS ---------------------------  //
    
    public FileWriteHandler(FileHandler fileHandler, File file)
    {
        this.fileHandler = fileHandler;
        this.file = file;
    }
    
    /*  Create access text file when the program is exited normally
    
    FORMAT: 
        name_of_csv_file.csv, 5
        name_of_text_file.txt, 6
    
        Where the number saves and represents how often a file is directly accessed
    */
    public void createAccessText(ArrayList<Folder> folders)
    {
        File file = new File(fileHandler.DATA_LOC + "/" + fileHandler.ACCESS_TXT);
        FileWriter fWriter;
        BufferedWriter bfWriter;
        try 
        {
            fWriter = new FileWriter(file);
            bfWriter = new BufferedWriter(fWriter);
            for (Folder folder : folders)
                for (BaseFile bFile : folder.bFiles)
                {
                    bfWriter.write(bFile.name + ", " + bFile.accessed);
                    bfWriter.newLine();
                }
            bfWriter.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    //  --------------------------- ADD IMPLEMENTATION ---------------------------  //
    /*  Add a file with the relevant content, file name, and a folder name for
        the relevant folder        
    */
    public boolean addFile(LinkedList<String> content, String fileName, String folderName)
    {
        File f = new File(fileHandler.DATA_LOC + "/" + folderName);
        if (!fileHandler.exists(f))
            f.mkdir();
        
        File file = new File(fileHandler.DATA_LOC + "/" + folderName + "/" + fileName);
        boolean addSuccessful = false;
        
        if (fileHandler.exists(file))
            return addSuccessful;
        
        FileWriter fWriter;
        BufferedWriter bfWriter;
        
        try {
            fWriter = new FileWriter(file);
            bfWriter = new BufferedWriter(fWriter);
            for (String line : content)
            {
                bfWriter.write(line);
            }
            addSuccessful = true;
            bfWriter.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return addSuccessful;
    }
    
    //  --------------------------- REMOVE IMPLEMENTATION ---------------------------  //
    /*  Remove a file with a given name
    */
    public void removeFile(String folderLoc, String fileName)
    {
        File file = new File(folderLoc + "/" + fileName);
        if (fileHandler.exists(file))
        {
            if (file.delete())
            {
                System.out.println("Deleted?");
            }
            else
            {
                System.out.println("Not deleted");
            }
            
        }
            
    }
    
    //  --------------------------- WRITE IMPLEMENTATION ---------------------------  //
    /*  Write to a file with a given 
    */
    
    public boolean writeFile(LinkedList<String> content, BaseFile bFile, String s)
    {
        File file = new File(bFile.LOC);
        
        FileWriter fWriter;
        BufferedWriter bfWriter;
        boolean writeSuccessful = false;
        
        if (!fileHandler.exists(file))
        {
            return writeSuccessful;
        }

        try {
            fWriter = new FileWriter(file);
            bfWriter = new BufferedWriter(fWriter);
            for (String line : content)
            {
                bfWriter.write(line);
                bfWriter.newLine();
            }
            bfWriter.write(s);
            bfWriter.newLine();
            writeSuccessful = true;
            bfWriter.close();
        } catch (Exception e)
        {
            System.out.println("Exception found");
            e.printStackTrace();
        }
        return writeSuccessful;
    }
}
