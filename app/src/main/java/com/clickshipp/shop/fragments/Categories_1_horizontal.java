package com.clickshipp.shop.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clickshipp.shop.R;
import com.clickshipp.shop.adapters.CategoryListAdapter_1_horizontal;
import com.clickshipp.shop.app.App;
import com.clickshipp.shop.models.category_model.CategoryDetails;

import java.util.ArrayList;
import java.util.List;


public class Categories_1_horizontal extends Fragment {

    Boolean isMenuItem = true;
    Boolean isHeaderVisible = false;

    TextView emptyText, headerText;
    RecyclerView category_recycler;
    LinearLayout titleLayout;

    CategoryListAdapter_1_horizontal categoryListAdapter;

    List<CategoryDetails> allCategoriesList;
    List<CategoryDetails> mainCategoriesList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories, container, false);
    
        if (getArguments() != null) {
            if (getArguments().containsKey("isHeaderVisible")) {
                isHeaderVisible = getArguments().getBoolean("isHeaderVisible", false);
            }
        
            if (getArguments().containsKey("isMenuItem")) {
                isMenuItem = getArguments().getBoolean("isMenuItem", true);
            }
        }
        
        
        if (isMenuItem) {
            // Enable Drawer Indicator with static variable actionBarDrawerToggle of MainActivity
//            ((MainActivity)getActivity()).toggleNavigaiton(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.categories));
        }
        

        allCategoriesList = new ArrayList<>();

        // Get CategoriesList from ApplicationContext
        allCategoriesList = ((App) getContext().getApplicationContext()).getCategoriesList();
        
        
        // Binding Layout Views
        emptyText = (TextView) rootView.findViewById(R.id.empty_record_text);
        headerText = (TextView) rootView.findViewById(R.id.categories_header);
        category_recycler = (RecyclerView) rootView.findViewById(R.id.categories_recycler);
        NestedScrollView scroll_container = (NestedScrollView) rootView.findViewById(R.id.scroll_container);
        titleLayout = rootView.findViewById(R.id.titleLayout);

        scroll_container.setNestedScrollingEnabled(true);
        category_recycler.setNestedScrollingEnabled(false);
        

        // Hide some of the Views
        emptyText.setVisibility(View.GONE);

        // Check if Header must be Invisible or not
        if (!isHeaderVisible) {
            // Hide the Header of CategoriesList
            titleLayout.setVisibility(View.GONE);
        } else {
            headerText.setText(getString(R.string.categories));
        }



        mainCategoriesList= new ArrayList<>();

        for (int i=0;  i<allCategoriesList.size();  i++) {
            
            int count_sub_categories = 0;
    
            for (int x=0;  x<allCategoriesList.size();  x++) {
                if (allCategoriesList.get(x).getParent() == allCategoriesList.get(i).getId())
                    count_sub_categories += 1;
            }
    
            allCategoriesList.get(i).setSub_categories(count_sub_categories);
            
            if (allCategoriesList.get(i).getParent() == 0) {
                mainCategoriesList.add(allCategoriesList.get(i));
            }
        }


        // Initialize the CategoryListAdapter for RecyclerView
        categoryListAdapter = new CategoryListAdapter_1_horizontal(getContext(), mainCategoriesList, false, false);

        // Set the Adapter and LayoutManager to the RecyclerView
        category_recycler.setAdapter(categoryListAdapter);
        category_recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        categoryListAdapter.notifyDataSetChanged();


        return rootView;
    }

}

