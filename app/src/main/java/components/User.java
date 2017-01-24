package components;

import javax.inject.Singleton;

import dagger.Component;
import io.fusionbit.vcarrycustomer.ActivityBookTrip;
import io.fusionbit.vcarrycustomer.ActivityHome;
import io.fusionbit.vcarrycustomer.ActivityRegistrationForm;
import io.realm.RealmConfiguration;
import module.ApiModule;
import module.AppModule;
import module.RealmConfigModule;
import module.RealmModule;
import module.UserModule;

/**
 * Created by rutvik on 1/24/2017 at 9:13 AM.
 */

@Singleton
@Component(modules = {AppModule.class, RealmConfigModule.class, RealmModule.class, UserModule.class, ApiModule.class})
public interface User
{
    void inject(ActivityHome activityBookTrip);

    void inject(ActivityBookTrip activityBookTrip);

    void inject(ActivityRegistrationForm activityBookTrip);

    RealmConfiguration getRealmConfiguration();
}
