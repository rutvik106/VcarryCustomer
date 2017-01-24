package module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

/**
 * Created by rutvik on 1/24/2017 at 12:01 PM.
 */

@Module
public class RealmConfigModule
{
    private String realmDatabaseName;

    public RealmConfigModule(String realmDatabaseName)
    {
        this.realmDatabaseName = realmDatabaseName;
    }

    @Provides
    @Singleton
    public RealmConfiguration provideRealmConfiguration()
    {
        return new RealmConfiguration
                .Builder()
                .name(realmDatabaseName)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
