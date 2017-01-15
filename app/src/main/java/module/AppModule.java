package module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by rutvik on 1/15/2017 at 1:07 PM.
 */

@Module
public class AppModule
{

    Application mApplication;

    public AppModule(Application application)
    {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication()
    {
        return mApplication;
    }

}
