package apimodels;

/**
 * Created by rutvik on 1/9/2017 at 4:58 PM.
 */

public class NamePrefix extends SpinnerModel
{

    private String id, label;

    public NamePrefix(String id, String label)
    {
        this.id = id;
        this.label = label;
    }

    @Override
    public int getId()
    {
        return Integer.valueOf(id);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
