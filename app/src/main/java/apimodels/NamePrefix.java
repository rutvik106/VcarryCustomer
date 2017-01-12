package apimodels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rutvik on 1/9/2017 at 4:58 PM.
 */

public class NamePrefix implements SpinnerModel
{

    public String id;

    public String label;

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
