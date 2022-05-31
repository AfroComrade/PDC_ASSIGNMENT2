package pdc_assign2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

/*  Handle all read functions. Should not write or handle cli input/output
*/

public class FileReadHandler
{
    public FileHandler fileHandler;
    public File file;
    private ArrayList<String> accessedCSVS;
    
    //  --------------------------- MAIN FUNCTIONS ---------------------------  //
    public FileReadHandler(FileHandler fileHandler, File file)
    {
        this.fileHandler = fileHandler;
        this.file = file;
    }
    
    public boolean exists(File f)
    {
        return f.exists();
    }
    
    /*  Function called early to get all the folders and their contained files
        Called early and passed to Knowledgebase Controller
    */
    public ArrayList<Folder> getFolders()           
    {
        String[] folderStrings = file.list();
        ArrayList<Folder> folders = new ArrayList<>();
        
        /*  If an access text file exists, then load it
            Regardless if it exists, it'll be overwritten in File Write Handler
        */
        if (folderStrings[0].equals(fileHandler.ACCESS_TXT))
        {
            fileHandler.accessFound = true;
            readAccessFile();
        }
        
        
        /*  Handle creating folders, and call loadBaseFiles to load txt and 
            csv files for each folder
        */
        for (String folderName : folderStrings)     
        {
            if (folderName.equals(fileHandler.ACCESS_TXT))
                continue;
            String folderLOC = fileHandler.DATA_LOC + "/" + folderName;
            Folder f = new Folder(folderName, folderLOC, loadBaseFiles(folderLOC));
            f.sort();
            folders.add(f);
        }
        
        return folders;
    }
    
    /*  Called for each folder found
    */
    public LinkedList<BaseFile> loadBaseFiles(String folderLOC)
    {
        LinkedList<BaseFile> bFiles = new LinkedList<>();
        
        File FOLDER = new File(folderLOC);
        String[] strings = FOLDER.list();
        
        for (String csvName : strings)
        {
            int accessed = 0;

            //  Only update the accessed value for loaded csvs if 
            //  the access txt has been found
            if (fileHandler.accessFound)
            {
                for (String accessedCSV : accessedCSVS)
                {
                    if (accessedCSV.contains(csvName))
                    {
                        accessed = Integer.parseInt(accessedCSV.substring(accessedCSV.length() -1, accessedCSV.length()));
                    }
                }
            }
            String LOC = folderLOC + "/" + csvName;
            bFiles.add(new CSV(csvName, LOC, accessed));
        }
        
        return bFiles;
    }
    
    /*  Read the recorded amount of times files have been access from
        previous sessions
    */
    private void readAccessFile()
    {
        this.accessedCSVS = new ArrayList<>();
        if (fileHandler.accessFound)
        {
            try {
                File accessTXT = new File(fileHandler.ACCESS_LOC);
                FileReader fReader = new FileReader(accessTXT);
                BufferedReader bfReader = new BufferedReader(fReader);
                String accessedInput = bfReader.readLine();
                while (accessedInput != null)
                {
                    this.accessedCSVS.add(accessedInput);
                    accessedInput = bfReader.readLine();
                }
                bfReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    //  --------------------------- ADD IMPLEMENTATION ---------------------------  //
    public LinkedList<String> readFile(String fileName)
    {
        LinkedList<String> content = new LinkedList<>();
        
        File file = new File(fileName);
        if (!file.exists())
            return new LinkedList<>();
        
        FileReader fReader;
        BufferedReader bfReader;
        try {
            fReader = new FileReader(fileName);
            bfReader = new BufferedReader(fReader);
            String input = bfReader.readLine();
            while (input != null)
            {
                content.add(input + "\n");
                input = bfReader.readLine();
            }
            bfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
