package models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rutvik on 1/24/2017 at 12:21 AM.
 */

public class UserActivities
{

    Realm realm;
    private List<BookedTrip> bookedTripRealmList;

    @Inject
    public UserActivities(Realm realm)
    {
        this.realm = realm;
        RealmResults<BookedTrip> result = realm.where(BookedTrip.class).findAll();
        if (result.size() > 0)
        {
            bookedTripRealmList = realm.copyFromRealm(result);
        } else
        {
            bookedTripRealmList = new ArrayList<>();
        }
    }

    public void addBookedTrip(BookedTrip trip)
    {
        bookedTripRealmList.add(trip);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(trip);
        realm.commitTransaction();
    }
}
