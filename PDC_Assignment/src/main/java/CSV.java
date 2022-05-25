
//  Main difference for CSV vs TEXT files is print style

public class CSV extends BaseFile
{
    public CSV(String name, String LOC, int accessed)
    {
        super(name, LOC, accessed);
        this.type = FileType.CSV;
    }
}
