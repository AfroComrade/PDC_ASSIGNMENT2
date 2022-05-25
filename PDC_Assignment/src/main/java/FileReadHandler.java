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
    
    //  --------------------------- SEARCH IMPLEMENTATION ---------------------------  //
    /*  Search through the files, recording any that contain the searched string
        If the string is ever identical to the file name, read from and return
        that file's contents
    */
    public LinkedList<String> search(String s)
    {
        LinkedList<String> matches = new LinkedList<>();
        String fileFound = fileHandler.DATA_LOC + "/";
        fileHandler.writeFound = false;
        
        for (Folder folder : KnowledgebaseController.folders)
        {
            if (folder.name.toLowerCase().contains(s))
            {
                matches.add("Folder: " + folder.name);
                matches.add("Contains...");
                for (BaseFile bFile : folder.bFiles)
                {
                    String fName = bFile.getName();
                    if (fName.toLowerCase().equals(s))
                    {
                        fileHandler.writeFound = true;
                        fileHandler.writeBFile = bFile;
                        bFile.incrementAccessed();
                        folder.sort();
                        if (bFile.type == FileType.CSV)
                            return csvPrint(fileFound + folder.name + "/" + fName);
                        else if (bFile.type == FileType.TEXT)
                            return textPrint(fileFound + folder.name + "/" + fName);
                    }
                    else
                        matches.add("    " + fName);
                }
            }
            else
            {
                for (BaseFile bFile : folder.bFiles)
                {
                    String fName = bFile.getName();
                    if (fName.toLowerCase().equals(s))
                    {
                        fileHandler.writeFound = true;
                        fileHandler.writeBFile = bFile;
                        bFile.incrementAccessed();
                        folder.sort();
                        if (bFile.type == FileType.CSV)
                            return csvPrint(fileFound + folder.name + "/" + fName);
                        else if (bFile.type == FileType.TEXT)
                            return textPrint(fileFound + folder.name + "/" + fName);
                    }
                    else if (fName.toLowerCase().contains(s))
                        matches.add(fName);
                }
            }
        }
        
        return matches;
    }
    
    //  Class for printing if it's a text file
    public LinkedList<String> textPrint(String csvName)
    {
        LinkedList<String> csvString = new LinkedList<>();
        
        FileReader fReader;
        BufferedReader bfReader;
        try {
            fReader = new FileReader(csvName);
            bfReader = new BufferedReader(fReader);
            char input = (char)bfReader.read(); 
            String line = "";
            while (input != 65535) // Exact value for end of file
            {
                if (input == '\n')
                {
                    csvString.add(line.substring(0, line.length()-1));
                    line = "";
                }
                else
                    line += input;
                
                input = (char)bfReader.read();
            }
            
            csvString.add(line);
            bfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return csvString;
    }
    
    //  Class for printing if it's a CSV file
    public LinkedList<String> csvPrint(String csvName)
    {
        LinkedList<String> csvString = new LinkedList<>();
        
        FileReader fReader;
        BufferedReader bfReader;
        try {
            fReader = new FileReader(csvName);
            bfReader = new BufferedReader(fReader);
            // we read per-character so we can replace commas with lines
            // Make the output look marginally cleaner
            char input = (char)bfReader.read();
            String line = "";
            while (input != 65535)
            {
                if(input == ',')
                {
                    line += "  |  ";
                }
                else if (input == '\n')
                {
                    csvString.add(line.substring(0, line.length()-1));
                    line = "";
                }
                else
                    line += input;
                
                input = (char)bfReader.read();
            }
            
            csvString.add(line);
            bfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return csvString;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
