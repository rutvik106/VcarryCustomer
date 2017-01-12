package io.fusionbit.vcarrycustomer;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapters.CustomListAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.Area;
import apimodels.City;
import apimodels.NamePrefix;
import apimodels.SpinnerModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ActivityRegistrationForm extends AppCompatActivity implements Validator.ValidationListener
{

    @BindView(R.id.spin_prefix)
    AppCompatSpinner spinPrefix;

    @NotEmpty
    @BindView(R.id.et_fullName)
    EditText etFullName;

    @BindView(R.id.spin_city)
    AppCompatSpinner spinCity;

    @BindView(R.id.spin_area)
    AppCompatSpinner spinArea;

    @NotEmpty
    @BindView(R.id.et_addressLineOne)
    EditText etAddressLineOne;

    @BindView(R.id.et_addressLineTwo)
    EditText etAddressLineTwo;

    @NotEmpty
    @BindView(R.id.et_contact)
    EditText etContact;

    @BindView(R.id.btn_registerNewCustomer)
    Button btnRegisterNewCustomer;

    String selectedPrefixId = null;
    String selectedCityId = null;
    String selectedAreaId = null;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Customer Registration");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        validator = new Validator(this);

        validator.setValidationListener(this);

        final List<NamePrefix> namePrefixList = new ArrayList<>();
        namePrefixList.add(new NamePrefix("1", "Mr."));
        namePrefixList.add(new NamePrefix("3", "Ms."));
        namePrefixList.add(new NamePrefix("4", "M/s"));

        spinPrefix.setAdapter(new CustomListAdapter<>(ActivityRegistrationForm.this,
                android.R.layout.simple_spinner_item, namePrefixList));

        spinPrefix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectedPrefixId = ((SpinnerModel) adapterView.getSelectedItem()).getId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectedCityId = ((SpinnerModel) adapterView.getSelectedItem()).getId() + "";
                getAreaList(selectedCityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        spinArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectedAreaId = ((SpinnerModel) adapterView.getSelectedItem()).getId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        getCityList();

    }

    private void getAreaList(String cityId)
    {
        API.getInstance().getAreas(cityId, new RetrofitCallbacks<List<Area>>()
        {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response)
            {
                super.onResponse(call, response);
                spinArea.setAdapter(new CustomListAdapter<>(ActivityRegistrationForm.this,
                        android.R.layout.simple_list_item_1, response.body()));
            }
        });
    }

    private void getCityList()
    {

        API.getInstance().getCities(new RetrofitCallbacks<List<City>>()
        {

            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response)
            {
                super.onResponse(call, response);
                if (response.isSuccessful())
                {
                    spinCity.setAdapter(new CustomListAdapter<>(ActivityRegistrationForm.this,
                            android.R.layout.simple_spinner_item, response.body()));
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_registerNewCustomer)
    public void tryRegisteringNewCustomer()
    {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded()
    {
        btnRegisterNewCustomer.setVisibility(View.GONE);

        if (selectedCityId == null || selectedAreaId == null)
        {
            Toast.makeText(this, "please select City and Area", Toast.LENGTH_SHORT).show();
            return;
        }

        final RetrofitCallbacks<ResponseBody> onRegistrationCallback =
                new RetrofitCallbacks<ResponseBody>()
                {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        super.onResponse(call, response);
                        if (response.isSuccessful())
                        {
                            try
                            {
                                if (!response.body().string().isEmpty() && !response.body().string().contains("error"))
                                {
                                    if (TextUtils.isDigitsOnly(response.body().string()))
                                    {
                                        PreferenceManager.getDefaultSharedPreferences(ActivityRegistrationForm.this)
                                                .edit()
                                                .putString(Constants.CUSTOMER_ID, response.body().string())
                                                .apply();
                                        startActivity(new Intent(ActivityRegistrationForm.this,
                                                ActivityHome.class));
                                    } else
                                    {
                                        Toast.makeText(ActivityRegistrationForm.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }

                        }
                        btnRegisterNewCustomer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {
                        super.onFailure(call, t);
                        btnRegisterNewCustomer.setVisibility(View.VISIBLE);
                    }
                };

        API.getInstance().insertCustomer(selectedPrefixId,
                etFullName.getText().toString(),
                etAddressLineOne.getText().toString(),
                etAddressLineTwo.getText().toString(),
                selectedAreaId,
                etContact.getText().toString(),
                selectedCityId,
                onRegistrationCallback);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText)
            {
                ((EditText) view).setError(message);
            } else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
