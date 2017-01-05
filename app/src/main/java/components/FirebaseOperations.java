package components;

import dagger.Component;
import firebase.TripOperations;
import module.FirebaseModule;
import module.FirebaseRemoteConfigSettingsModule;

/**
 * Created by rutvik on 1/5/2017 at 8:59 AM.
 */

@Component(modules = {FirebaseRemoteConfigSettingsModule.class, FirebaseModule.class})
public interface FirebaseOperations
{

    TripOperations tripOperations();

}
