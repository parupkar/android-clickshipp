package com.clickshipp.shop.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.clickshipp.shop.R;

import com.clickshipp.shop.activities.MainActivity;
import com.clickshipp.shop.constant.ConstantValues;
import com.clickshipp.shop.customs.DialogLoader;
import com.clickshipp.shop.models.post_model.PostDetails;
import com.clickshipp.shop.models.api_response_model.ErrorResponse;
import com.clickshipp.shop.models.post_model.PostMedia;
import com.clickshipp.shop.network.APIClient;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;


public class NewsDescription extends Fragment {

    View rootView;
    
    int postID;
    
    ImageView news_cover;
    TextView news_title, news_date;
    WebView news_description_webView;

    DialogLoader dialogLoader;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.news_description, container, false);
        

        // Get NewsDetails from bundle arguments
        postID = getArguments().getInt("postID");
    
    
        // Enable Drawer Indicator with static variable actionBarDrawerToggle of MainActivity
        ((MainActivity)getActivity()).toggleNavigaiton(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.news_description));


        // Binding Layout Views
        news_cover = (ImageView) rootView.findViewById(R.id.news_cover);
        news_title = (TextView) rootView.findViewById(R.id.news_title);
        news_date = (TextView) rootView.findViewById(R.id.news_date);
        news_description_webView = (WebView) rootView.findViewById(R.id.news_description_webView);

        
        dialogLoader = new DialogLoader(getContext());
    
        
        Glide
            .with(getContext())
            .load(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(news_cover);
        
        
        RequestPostDetails(postID);
        

        return rootView;
    }
    
    
    
    //*********** Set Post Details to Views ********//
    
    private void setPostDetails(PostDetails postDetails) {
    
        // Set News Details
        news_title.setText(postDetails.getTitle().getRendered());
        news_date.setText(String.valueOf(postDetails.getDate()));
    
    
        String description = postDetails.getContent().getRendered();
        String styleSheet = "<style> " +
                                "body{background:#eeeeee; margin:0; padding:0} " +
                                "p{color:#666666;} " +
                                "img{display:inline; height:auto; max-width:100%;}" +
                            "</style>";
    
        news_description_webView.setHorizontalScrollBarEnabled(false);
        news_description_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
        
    }
    
    
    
    //*********** Request Post Details from the Server based on postID ********//
    
    public void RequestPostDetails(final int postID) {
        
        dialogLoader.showProgressDialog();
        
        Call<PostDetails> call = APIClient.getInstance()
                .getSinglePost
                        (
                                String.valueOf(postID),
                                ConstantValues.LANGUAGE_CODE
                        );
        
        call.enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, retrofit2.Response<PostDetails> response) {
                
                dialogLoader.hideProgressDialog();
                
                if (response.isSuccessful()) {
                    
                    setPostDetails(response.body());
                    
                    RequestPostMedia(response.body().getFeaturedMedia());
                }
                else {
                    Converter<ResponseBody, ErrorResponse> converter = APIClient.retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    ErrorResponse error;
                    try {
                        error = converter.convert(response.errorBody());
                    } catch (IOException e) {
                        error = new ErrorResponse();
                    }
                    
                    Toast.makeText(getContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
        
    }
    
    
    
    //*********** Request Post Media from the Server based on MediaID ********//
    
    public void RequestPostMedia(final int media_id) {
        
        Call<PostMedia> call = APIClient.getInstance()
                .getPostMedia
                        (
                                String.valueOf(media_id),
                                ConstantValues.LANGUAGE_CODE
                        );
        
        call.enqueue(new Callback<PostMedia>() {
            @Override
            public void onResponse(Call<PostMedia> call, retrofit2.Response<PostMedia> response) {
                
                if (response.isSuccessful()) {
                    
                    PostMedia postMedia = response.body();
    
                    Glide
                        .with(getContext())
                        .load(postMedia.getSourceUrl()).asBitmap()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(news_cover);
                    
                }
                else {
                    Converter<ResponseBody, ErrorResponse> converter = APIClient.retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    ErrorResponse error;
                    try {
                        error = converter.convert(response.errorBody());
                    } catch (IOException e) {
                        error = new ErrorResponse();
                    }
                    
                    Toast.makeText(getContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<PostMedia> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

}

