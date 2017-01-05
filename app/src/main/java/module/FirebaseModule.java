package module;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by rutvik on 1/5/2017 at 8:44 AM.
 */

@Module
public class FirebaseModule
{

    @Provides
    static FirebaseAuth provideFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }

    @Provides
    static FirebaseRemoteConfig provideFirebaseRemoteConfig(FirebaseRemoteConfigSettings settings)
    {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setConfigSettings(settings);
        return remoteConfig;
    }

    @Provides
    static FirebaseDatabase provideFirebaseDatabase()
    {
        return FirebaseDatabase.getInstance();
    }

}
