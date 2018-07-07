package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.CustomListAdapter;
import api.API;
import api.RetrofitCallbacks;
import apimodels.Area;
import apimodels.City;
import apimodels.SpinnerModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fusionbit.vcarrycustomer.ActivityBookTrip;
import io.fusionbit.vcarrycustomer.R;
import retrofit2.Call;
import retrofit2.Response;

public class SelectCityAreaDialog extends Dialog {

    private final List<Area> fromAreaList = new ArrayList<Area>();
    private final List<Area> toAreaList = new ArrayList<Area>();

    private final List<City> cityList = new ArrayList<City>();

    @BindView(R.id.spin_city2)
    AppCompatSpinner fromSpinCity;
    @BindView(R.id.spin_area2)
    AppCompatSpinner fromSpinArea;

    @BindView(R.id.ll_loadingCityArea)
    LinearLayout llLoadingCityArea;
    @BindView(R.id.ll_cityAreaContent)
    LinearLayout llCityAreaContent;
    @BindView(R.id.pb_loadingArea)
    ProgressBar pbLoadingArea;
    @BindView(R.id.spin_ToCity)
    AppCompatSpinner spinToCity;
    @BindView(R.id.pb_loadingToArea)
    ProgressBar pbLoadingToArea;
    @BindView(R.id.spin_ToArea)
    AppCompatSpinner spinToArea;

    private Call<List<City>> gettingCityList;

    private Call<List<Area>> gettingFromAreaList;
    private Call<List<Area>> gettingToAreaList;

    String selectedFromCityId = null, selectedFromAreaId = null;
    String selectedToCityId = null, selectedToAreaId = null;

    public SelectCityAreaDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city_area_dialog);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        ButterKnife.bind(this);

        getCityList();

    }

    private void setupFromSpinnerListeners() {

        fromSpinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFromCityId = ((SpinnerModel) adapterView.getSelectedItem()).getId() + "";
                //Toast.makeText(ActivityRegistrationForm.this, "ID: " + selectedFromAreaId, Toast.LENGTH_SHORT).show();
                getFromAreaList(selectedFromCityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fromSpinArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFromAreaId = ((SpinnerModel) adapterView.getSelectedItem()).getId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCityList() {
        if (gettingCityList != null) {
            gettingCityList.cancel();
        }
        gettingCityList =
                API.getInstance().getCities(new RetrofitCallbacks<List<City>>() {

                    @Override
                    public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful()) {
                            for (City city : response.body()) {
                                if (city instanceof City) {
                                    cityList.add(city);
                                }
                            }
                            fromSpinCity.setAdapter(new CustomListAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_item, cityList));

                            spinToCity.setAdapter(new CustomListAdapter<>(getContext(),
                                    android.R.layout.simple_spinner_item, cityList));

                            fromSpinCity.setSelection(1);
                            spinToCity.setSelection(1);

                            setupFromSpinnerListeners();
                            setupToSpinnerListeners();

                        }
                    }
                });

    }

    private void setupToSpinnerListeners() {
        llLoadingCityArea.setVisibility(View.GONE);
        llCityAreaContent.setVisibility(View.VISIBLE);

        spinToCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedToCityId = ((SpinnerModel) adapterView.getSelectedItem()).getId() + "";
                //Toast.makeText(ActivityRegistrationForm.this, "ID: " + selectedFromAreaId, Toast.LENGTH_SHORT).show();
                getToAreaList(selectedToCityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinToArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedToAreaId = ((SpinnerModel) adapterView.getSelectedItem()).getId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getToAreaList(String selectedToCityId) {

        spinToArea.setAdapter(null);
        pbLoadingToArea.setVisibility(View.VISIBLE);
        selectedToAreaId = null;

        if (gettingToAreaList != null) {
            gettingToAreaList.cancel();
        }
        gettingToAreaList =
                API.getInstance().getAreas(selectedToCityId, new RetrofitCallbacks<List<Area>>() {
                    @Override
                    public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                        super.onResponse(call, response);
                        toAreaList.clear();
                        for (Area area : response.body()) {
                            if (area instanceof Area) {
                                toAreaList.add(area);
                            }
                        }
                        spinToArea.setAdapter(new CustomListAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, toAreaList));

                        pbLoadingToArea.setVisibility(View.GONE);

                    }
                });

    }

    private void getFromAreaList(String cityId) {

        fromSpinArea.setAdapter(null);
        pbLoadingArea.setVisibility(View.VISIBLE);
        selectedFromAreaId = null;

        if (gettingFromAreaList != null) {
            gettingFromAreaList.cancel();
        }
        gettingFromAreaList =
                API.getInstance().getAreas(cityId, new RetrofitCallbacks<List<Area>>() {
                    @Override
                    public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                        super.onResponse(call, response);
                        fromAreaList.clear();
                        for (Area area : response.body()) {
                            if (area instanceof Area) {
                                fromAreaList.add(area);
                            }
                        }
                        fromSpinArea.setAdapter(new CustomListAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, fromAreaList));

                        pbLoadingArea.setVisibility(View.GONE);

                    }
                });
    }


    @OnClick(R.id.fab_doneCityArea)
    public void onViewClicked() {

        if (selectedFromAreaId == null || selectedToAreaId == null) {
            Toast.makeText(getContext(), "Please select proper from to city and area", Toast.LENGTH_SHORT).show();
            return;
        }

        ActivityBookTrip.start(getContext(), selectedFromAreaId, selectedToAreaId);

        dismiss();

    }
}
