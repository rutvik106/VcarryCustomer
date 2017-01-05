package module;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import dagger.Module;
import dagger.Provides;

/**
 * Created by rutvik on 1/5/2017 at 9:28 AM.
 */

@Module
public class FirebaseRemoteConfigSettingsModule
{

    FirebaseRemoteConfigSettings settings;

    public FirebaseRemoteConfigSettingsModule(FirebaseRemoteConfigSettings settings)
    {
        this.settings = settings;
    }

    @Provides
    FirebaseRemoteConfigSettings provideFirebaseRemoteConfigSettings()
    {
        return settings;
    }

}
