package com.clickshipp.shop.adapters;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clickshipp.shop.R;
import com.clickshipp.shop.activities.MainActivity;
import com.clickshipp.shop.app.App;
import com.clickshipp.shop.customs.CircularImageView;
import com.clickshipp.shop.fragments.Products;
import com.clickshipp.shop.models.category_model.CategoryDetails;

import java.util.ArrayList;
import java.util.List;

import static com.clickshipp.shop.app.App.getContext;


/**
 * CategoryListAdapter is the adapter class of RecyclerView holding List of Categories in MainCategories
 **/

public class CategoryListAdapter_5 extends RecyclerView.Adapter<CategoryListAdapter_5.MyViewHolder> {

    boolean isSubCategory;

    Context context;
    List<CategoryDetails> categoriesList;
    List<CategoryDetails> allCategoriesList;


    public CategoryListAdapter_5(Context context, List<CategoryDetails> categoriesList, boolean isSubCategory) {
        this.context = context;
        this.isSubCategory = isSubCategory;
        this.categoriesList = categoriesList;

        allCategoriesList = ((App) getContext().getApplicationContext()).getCategoriesList();
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories_5, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // Get the data model based on Position
        final CategoryDetails categoryDetails = categoriesList.get(position);

        holder.category_title.setText(categoryDetails.getName().replace("&amp;", "&"));
        holder.category_products.setText(categoryDetails.getCount() + " "+ context.getString(R.string.products));
    
    
        // Set Category Image on ImageView with Glide Library
        if (categoryDetails.getImage() != null  &&  categoryDetails.getImage().getSrc() != null) {
            Glide.with(context)
                    .load(categoryDetails.getImage().getSrc())
                    .error(R.drawable.placeholder)
                    .into(holder.category_image);
        }
        else {
            Glide.with(context)
                    .load(R.drawable.placeholder)
                    .into(holder.category_image);
        }
        

        List<CategoryDetails> subCategoriesList = new ArrayList<>();

        for (int i=0;  i<allCategoriesList.size();  i++) {
            // Get Subcategories from all Categories
            if (allCategoriesList.get(i).getParent() == categoryDetails.getId()) {
                subCategoriesList.add(allCategoriesList.get(i));
            }
        }

        // Initialize the SubCategoryListAdapter for RecyclerView
        SubCategoryListAdapter subCategoryListAdapter = new SubCategoryListAdapter(context, subCategoriesList);

        holder.sub_categories_list.setAdapter(subCategoryListAdapter);

    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    
        RelativeLayout category_card;
        ListView sub_categories_list;
        CircularImageView category_image;
        TextView category_title, category_products;


        public MyViewHolder(final View itemView) {
            super(itemView);
            
            category_card = (RelativeLayout) itemView.findViewById(R.id.category_card);
            
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            category_products = (TextView) itemView.findViewById(R.id.category_products);
            category_image = (CircularImageView) itemView.findViewById(R.id.category_image);
            sub_categories_list = (ListView) itemView.findViewById(R.id.sub_categories_list);
    
            
            category_card.setOnClickListener(this);
        }
    
    
        // Handle Click Listener on Category item
        @Override
        public void onClick(View view) {
            // Get Category Info
            Bundle categoryInfo = new Bundle();
            categoryInfo.putInt("CategoryID", categoriesList.get(getAdapterPosition()).getId());
            categoryInfo.putString("CategoryName", categoriesList.get(getAdapterPosition()).getName());
    
            Fragment fragment = new Products();
    
            fragment.setArguments(categoryInfo);
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(((MainActivity) context).currentFragment)
                    .add(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        
        }

    }

}

