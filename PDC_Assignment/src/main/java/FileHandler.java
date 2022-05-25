import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/*  Controller process for read/write
    Don't handle read/write functions here.
*/

public class FileHandler
{
    private File file;
    private FileReadHandler readHandler;
    private FileWriteHandler writeHandler;
    protected String DATA_LOC = KnowledgebaseController.DATA_LOCATION;
    protected final String ACCESS_TXT = KnowledgebaseController.ACCESS_TXT;
    protected boolean accessFound = false;
    protected boolean writeFound;
    protected BaseFile writeBFile;
    protected final String ACCESS_LOC;
    
    //  --------------------------- MAIN FUNCTIONS ---------------------------  //
    
    /*  Initialize the File Read Handlers and File Write handlers early because
        we're going to need to read the folders and files early
    */
    public FileHandler()
    {
        this.file = new File(DATA_LOC);
        this.readHandler = new FileReadHandler(this, file);
        this.writeHandler = new FileWriteHandler(this, file);
        this.ACCESS_LOC =  DATA_LOC + "/" + ACCESS_TXT;
    }
    
    public ArrayList<Folder> getFolders()
    {
        return readHandler.getFolders();
    }
    
    /*  See File Write Handler for create access text logic
        Used when the program quits
    */
    public void createAccessText()
    {
        this.writeHandler.createAccessText(KnowledgebaseController.folders);
    }
    
    public boolean exists(File file)
    {
        return readHandler.exists(file);
    }
    
    //  --------------------------- SEARCH IMPLEMENTATION ---------------------------  //
    
    public LinkedList<String> search(String s)
    {
        s = s.toLowerCase();
        LinkedList<String> matches = readHandler.search(s);
        
        return matches;
    }
    
    public LinkedList<String> readFile(String fileName)
    {
        return readHandler.readFile(fileName);
    }
    
    //  --------------------------- ADD IMPLEMENTATION ---------------------------  //
    
    public boolean add(LinkedList<String> content, String fileName, String folderName)
    {
        return writeHandler.addFile(content, fileName, folderName);
    }
    
    //  --------------------------- REMOVE IMPLEMENTATION ---------------------------  //
    
    public void removeLoaded(Folder f, String s)
    {
        writeHandler.removeFile(f.LOC, s);
    }
    
    //  --------------------------- WRITE IMPLEMENTATION ---------------------------  //
    
    public void write(LinkedList<String> contents, String s)
    {
        writeHandler.writeFile(contents, writeBFile, s);
    }
}