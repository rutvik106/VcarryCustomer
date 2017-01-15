package components;

import javax.inject.Singleton;

import api.API;
import dagger.Component;
import io.fusionbit.vcarrycustomer.VCarryActivity;
import module.ApiModule;
import module.AppModule;

/**
 * Created by rutvik on 1/15/2017 at 1:13 PM.
 */

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface VcarryApi
{

    API api();

    void inject(VCarryActivity activity);

}
