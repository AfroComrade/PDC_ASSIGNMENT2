package pdc_assign2;

import java.util.Collections;
import java.util.LinkedList;

/*  Folders record their list of txt and csv files
*/

public class Folder
{
    public LinkedList<BaseFile> bFiles;
    public String name;
    public final String LOC;
    
    // MAIN IMPLEMENTATION
    public Folder(String name, String LOC, LinkedList<BaseFile> csvs)
    {
        this.bFiles = csvs;
        this.name = name;
        this.LOC = LOC;
    }
    
    /*  Called:
             when all initial files are added to the folder 
             when a direct file is accessed (once accessed is incremented)
             when adding a new file
    */
    public void sort()
    {
        Collections.sort(this.bFiles);
    }
    
    //  ADD IMPLEMENTATION
    //  Called to add csv and text files to the folder in memory
    public void add(BaseFile x)
    {
        bFiles.add(x);
        Collections.sort(this.bFiles);
    }
    
    //  REMOVE IMPLEMENTATION
    //  Called to remove csv and text files to the folder in memory
    public void remove(BaseFile x)
    {
        bFiles.remove(x);
    }
    
    public String[] toList()
    {
        String[] toReturn = new String[bFiles.size()];
        for (int i = 0; i < bFiles.size(); i++)
        {
            toReturn[i] = bFiles.get(i).name;
        }
        return toReturn;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
