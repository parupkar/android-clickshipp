package com.clickshipp.shop.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.clickshipp.shop.R;
import com.clickshipp.shop.activities.MainActivity;
import com.clickshipp.shop.adapters.LanguagesAdapter;
import com.clickshipp.shop.app.App;
import com.clickshipp.shop.app.MyAppPrefsManager;
import com.clickshipp.shop.constant.ConstantValues;
import com.clickshipp.shop.customs.DialogLoader;
import com.clickshipp.shop.models.banner_model.BannerDetails;
import com.clickshipp.shop.models.category_model.CategoryDetails;
import com.clickshipp.shop.models.language_model.LanguageData;
import com.clickshipp.shop.models.language_model.LanguageDetails;
import com.clickshipp.shop.network.StartAppRequests;


import java.util.ArrayList;
import java.util.List;


public class Languages extends Fragment {

    View rootView;

    MyAppPrefsManager appPrefs;

    String selectedLanguageID;
    String selectedLanguageCode;

    AdView mAdView;
    Button saveLanguageBtn;
    ListView languageListView;
    FrameLayout banner_adView;

    LanguagesAdapter languagesAdapter;
    List<LanguageData> languagesList;
    DialogLoader dialogLoader;
    private CheckBox lastChecked_CB = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.languages, container, false);

        ((MainActivity) getActivity()).toggleNavigaiton(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.actionLanguage));


        appPrefs = new MyAppPrefsManager(getContext());

        selectedLanguageCode = appPrefs.getUserLanguageCode();
        selectedLanguageID = String.valueOf(appPrefs.getUserLanguageCode());
        dialogLoader = new DialogLoader(getContext());

        // Binding Layout Views
        banner_adView = (FrameLayout) rootView.findViewById(R.id.banner_adView);
        saveLanguageBtn = (Button) rootView.findViewById(R.id.btn_save_language);
        languageListView = (ListView) rootView.findViewById(R.id.languages_list);


        if (ConstantValues.IS_ADMOBE_ENABLED) {
            // Initialize Admobe
            mAdView = new AdView(getContext());
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(ConstantValues.AD_UNIT_ID_BANNER);
            AdRequest adRequest = new AdRequest.Builder().build();
            banner_adView.addView(mAdView);
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    banner_adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    banner_adView.setVisibility(View.GONE);
                }
            });
        }


        languagesList = new ArrayList<>();

        // Initialize the LanguagesAdapter for RecyclerView
        languagesAdapter = new LanguagesAdapter(getContext(), languagesList, this);

        languageListView.setAdapter(languagesAdapter);


        saveLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!selectedLanguageID.equalsIgnoreCase(String.valueOf(appPrefs.getUserLanguageId()))) {
                    // Change Language

                    appPrefs.setUserLanguageCode(selectedLanguageCode);
                    appPrefs.setUserLanguageId(Integer.parseInt(selectedLanguageID));

                    ConstantValues.LANGUAGE_ID = "" + appPrefs.getUserLanguageId();
                    ConstantValues.LANGUAGE_CODE = appPrefs.getUserLanguageCode();


                    ChangeLocaleTask changeLocaleTask = new ChangeLocaleTask();
                    changeLocaleTask.execute();

                }
            }
        });

        RequestLanguages();

        return rootView;
    }

    //*********** Recreates Activity ********//

    private void recreateActivity() {
        My_Cart.ClearCart();
        ((App) getContext().getApplicationContext()).setBannersList(new ArrayList<BannerDetails>());
        ((App) getContext().getApplicationContext()).setCategoriesList(new ArrayList<CategoryDetails>());
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    public void setLastCheckedCB(CheckBox lastChecked_CB) {
        this.lastChecked_CB = lastChecked_CB;
    }

    public String getSelectedLanguageID() {
        return selectedLanguageID;
    }

    //*********** Adds Orders returned from the Server to the OrdersList ********//

    private void addLanguages(LanguageDetails languageData) {

        languagesList.addAll(languageData.getData());


        if (selectedLanguageID.equalsIgnoreCase("") && languagesList.size() != 0) {

            selectedLanguageID = languagesList.get(0).getLanguagesId();
            selectedLanguageCode = languagesList.get(0).getCode();

            for (int i = 0; i < languagesList.size(); i++) {
                if (languagesList.get(i).getIsDefault().equalsIgnoreCase("1")) {
                    selectedLanguageCode = languagesList.get(i).getCode();
                    selectedLanguageID = languagesList.get(i).getLanguagesId();
                }
            }

        } else {
            for (int i = 0; i < languagesList.size(); i++) {
                if (languagesList.get(i).getLanguagesId().equalsIgnoreCase(String.valueOf(appPrefs.getUserLanguageId()))) {
                    selectedLanguageCode = languagesList.get(i).getCode();
                    selectedLanguageID = languagesList.get(i).getLanguagesId();
                }
            }
        }


        languagesAdapter.notifyDataSetChanged();


        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox currentChecked_CB = (CheckBox) view.findViewById(R.id.cb_language);
                LanguageData language = (LanguageData) parent.getAdapter().getItem(position);


                // UnCheck last Checked CheckBox
                if (lastChecked_CB != null) {
                    lastChecked_CB.setChecked(false);
                }

                currentChecked_CB.setChecked(true);
                lastChecked_CB = currentChecked_CB;


                selectedLanguageID = language.getLanguagesId();
                selectedLanguageCode = language.getCode();
            }
        });

    }

    //*********** Request App Languages from the Server ********//

    public void RequestLanguages() {
/*        dialogLoader.showProgressDialog();
        Call<LanguageDetails> call = APIClient.getInstance()
                .getLanguages( );
        
        call.enqueue(new Callback<LanguageDetails>() {
            @Override
            public void onResponse(Call<LanguageDetails> call, retrofit2.Response<LanguageDetails> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        
                        // Languages have been returned. Add Languages to the languageList
                        addLanguages(response.body());
                        dialogLoader.hideProgressDialog();
                        
                    }
                   
                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                        dialogLoader.hideProgressDialog();
                    }
                }
                else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    dialogLoader.hideProgressDialog();
                }
            }
            
            @Override
            public void onFailure(Call<LanguageDetails> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                dialogLoader.hideProgressDialog();
            }
        });*/

        LanguageDetails languageDetails = new LanguageDetails();
        List<LanguageData> list = new ArrayList<>();
        LanguageData en = new LanguageData();
        en.setLanguagesId("0");
        en.setName("English");
        en.setCode("en");
        en.setIsDefault("1");
        LanguageData ar = new LanguageData();
        ar.setLanguagesId("1");
        ar.setName("Arabic");
        ar.setCode("ar");
        ar.setIsDefault("0");
        list.add(en);
        list.add(ar);
        languageDetails.setStatus("1");
        languageDetails.setData(list);
        addLanguages(languageDetails);
    }

    private class ChangeLocaleTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoader.showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

            // Recall some Network Requests
            StartAppRequests startAppRequests = new StartAppRequests(getContext());
            startAppRequests.StartRequests();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialogLoader.hideProgressDialog();
            recreateActivity();
        }
    }
}

