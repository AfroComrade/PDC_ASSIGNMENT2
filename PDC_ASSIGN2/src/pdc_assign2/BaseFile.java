package pdc_assign2;


/*  Format for files needing to be added.
    BaseFile itself is not supported in FileReadHandler, and is meant to 
    represent an abstract class.

NOTE:   ANY NEW FILE TYPES NEED TO BE IMPLEMENTED IN THE FileType ENUM
*/

public abstract class BaseFile implements Comparable
{
    protected String name;
    protected String LOC;
    protected int accessed; // Handle how often the basefile is accessed
    protected FileType type;
    
    public String getName()
    {
        return this.name;
    }
    
    public BaseFile(String name, String loc, int accessed)
    {
        this.name = name;
        this.LOC = loc;
        this.accessed = accessed;
    }
    
    public void incrementAccessed()
    {
        this.accessed++;
    }
    
    public String getPath()
    {
        return LOC;
    }
    
    //  First compare based on how often a file is accessed.
    //  If they are accessed an equal number of times, 
    //      compare strings alphabetically.
    @Override
    public int compareTo(Object o)
    {        
        try {
            BaseFile basefile = (BaseFile)o;
            if ((this.accessed - basefile.accessed) == 0)
                return name.compareTo(basefile.name);
            return basefile.accessed - accessed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
