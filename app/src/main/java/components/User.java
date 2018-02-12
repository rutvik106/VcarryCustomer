package components;

import javax.inject.Singleton;

import api.API;
import dagger.Component;
import fragments.FragmentAccBalance;
import fragments.FragmentTrips;
import io.fusionbit.vcarrycustomer.ActivityBookTrip;
import io.fusionbit.vcarrycustomer.ActivityDriverLocation;
import io.fusionbit.vcarrycustomer.ActivityFareDetails;
import io.fusionbit.vcarrycustomer.ActivityHome;
import io.fusionbit.vcarrycustomer.ActivityOnGoingTrips;
import io.fusionbit.vcarrycustomer.ActivityRegistrationForm;
import io.fusionbit.vcarrycustomer.ActivityTripDetails;
import io.fusionbit.vcarrycustomer.ActivityTripSummary;
import io.fusionbit.vcarrycustomer.ActivityTrips;
import io.realm.RealmConfiguration;
import module.AppModule;
import module.RealmConfigModule;
import module.RealmModule;
import module.UserModule;

/**
 * Created by rutvik on 1/24/2017 at 9:13 AM.
 */

@Singleton
@Component(modules = {AppModule.class, RealmConfigModule.class, RealmModule.class, UserModule.class})
public interface User
{
    void inject(ActivityHome activityBookTrip);

    void inject(ActivityBookTrip activityBookTrip);

    void inject(ActivityRegistrationForm activityBookTrip);

    void inject(ActivityDriverLocation activityDriverLocation);

    void inject(FragmentAccBalance fragmentAccBalance);

    void inject(ActivityTrips activityTrips);

    void inject(FragmentTrips fragmentTrips);

    void inject(ActivityTripDetails activityTripDetails);

    void inject(ActivityOnGoingTrips activityOnGoingTrips);

    RealmConfiguration getRealmConfiguration();

    void inject(ActivityTripSummary activityTripSummary);

    void inject(ActivityFareDetails activityFareDetails);
}
