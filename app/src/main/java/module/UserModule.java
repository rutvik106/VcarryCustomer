package module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import models.UserActivities;

/**
 * Created by rutvik on 1/24/2017 at 12:20 AM.
 */

@Module
public class UserModule
{

    @Provides
    @Singleton
    public UserActivities provideUserActivities(Realm realm)
    {
        return new UserActivities(realm);
    }

}
