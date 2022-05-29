package pdc_assign2;


//  Main difference for CSV vs TEXT files is print style

public class TextFile extends BaseFile
{
    public TextFile(String name, String LOC, int accessed)
    {
        super(name, LOC, accessed);
        this.type = FileType.TEXT;
    }
}
