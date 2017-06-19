package module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rutvik on 1/24/2017 at 11:58 AM.
 */

@Module
public class RealmModule
{

    @Provides
    @Singleton
    public Realm provideRealm(Application application, RealmConfiguration realmConfig)
    {
        Realm.setDefaultConfiguration(realmConfig);
        return Realm.getInstance(realmConfig);
    }

}
