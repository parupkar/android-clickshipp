package com.clickshipp.shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clickshipp.shop.R;
import com.clickshipp.shop.fragments.CurrencyFrag;
import com.clickshipp.shop.models.currency_model.CurrencyList;

import java.util.List;

/**
 * Created by Muhammad Nabeel on 08/03/2019.
 */
public class CurrencyAdapter extends BaseAdapter {
    
    Context context;
    LayoutInflater layoutInflater;
    
    private CurrencyFrag currencyListFrag;
    private List<CurrencyList> currencyLists;
    
    
    public CurrencyAdapter(Context context, List<CurrencyList> currencyLists, CurrencyFrag currencyListFrag) {
        this.context = context;
        this.currencyLists = currencyLists;
        this.currencyListFrag = currencyListFrag;
        
        layoutInflater = LayoutInflater.from(context);
    }
    
    //********** Returns the total number of items in the data set represented by this Adapter *********//
    
    @Override
    public int getCount() {
        return currencyLists.size();
    }
    
    
    //********** Returns the item associated with the specified position in the data set *********//
    
    @Override
    public Object getItem(int position) {
        return currencyLists.get(position);
    }
    
    
    //********** Returns the item id associated with the specified position in the list *********//
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    
    //********** Returns a View that displays the data at the specified position in the data set *********//
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        final CurrencyAdapter.ViewHolder holder;
        
        if (convertView == null) {
            
            convertView = layoutInflater.inflate(R.layout.row_item_currency, parent, false);
            
            holder = new CurrencyAdapter.ViewHolder();
            
            holder.currencyName = (TextView) convertView.findViewById(R.id.currency_name);
            holder.currencyIcon = (ImageView) convertView.findViewById(R.id.currency_icon);
            holder.currencySelector = (CheckBox) convertView.findViewById(R.id.cb_currency);
            
            convertView.setTag(holder);
            
        } else {
            holder = (CurrencyAdapter.ViewHolder) convertView.getTag();
        }
        
        
        
        holder.currencyName.setText(currencyLists.get(position).getDescription());
        
        Glide.with(context).load(currencyLists.get(position).getFlag()).into(holder.currencyIcon);
        
        
        if (currencyLists.get(position).getName().equalsIgnoreCase(currencyListFrag.getSelectedLanguageID())) {
            holder.currencySelector.setChecked(true);
            currencyListFrag.setLastCheckedCB(holder.currencySelector);
            
        } else {
            holder.currencySelector.setChecked(false);
        }
        
        return convertView;
    }
    
    
    
    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/
    
    static class ViewHolder {
        TextView currencyName;
        ImageView currencyIcon;
        CheckBox currencySelector;
        
    }
    
    
}

