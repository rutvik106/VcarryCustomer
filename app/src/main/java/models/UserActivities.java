package models;

import java.util.ArrayList;
import java.util.List;


import apimodels.TripByCustomerId;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rutvik on 1/24/2017 at 12:21 AM.
 */

public class UserActivities
{

    Realm realm;
    private List<TripByCustomerId> bookedTripRealmList;

    public UserActivities(Realm realm)
    {
        this.realm = realm;
        RealmResults<TripByCustomerId> result = realm.where(TripByCustomerId.class).findAll();
        if (result.size() > 0)
        {
            bookedTripRealmList = realm.copyFromRealm(result);
        } else
        {
            bookedTripRealmList = new ArrayList<>();
        }
    }

    public void addBookedTrip(TripByCustomerId trip)
    {
        bookedTripRealmList.add(trip);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(trip);
        realm.commitTransaction();
    }
}
