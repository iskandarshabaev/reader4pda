package com.ishabaev.reader4pda.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ishabaev.reader4pda.service.Description;
import com.ishabaev.reader4pda.service.Paragraph;
import com.ishabaev.reader4pda.R;
import com.ishabaev.reader4pda.service.PdaClient;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private static final String DESCRIPTION_STATE = "DESCRIPTION_STATE";
    private String detailsUrl;
    private LinearLayout content;
    private int headerTextSize;
    private int normalTextSize;
    private int tileTextSize;
    private int textMargin;
    private int headerLRMargin;
    private int headerBTMargin;
    private int imageSizeHeight;
    private int imageSizeWidth;
    private CollapsingToolbarLayout appBarLayout;
    private Description description;
    private Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMargins();
        if (getArguments().containsKey(ARG_ITEM_ID) ||
                (savedInstanceState != null && savedInstanceState.getString(ARG_ITEM_ID) != null)) {
            detailsUrl = getArguments().getString(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        }
    }

    private void initMargins() {
        int density = (int) (getResources().getDisplayMetrics().density);
        headerTextSize = (int) (getResources().getDimension(R.dimen.header_text_size) / density);
        normalTextSize = (int) (getResources().getDimension(R.dimen.normal_text_size) / density);
        tileTextSize = (int) (getResources().getDimension(R.dimen.title_text_size) / density);
        textMargin = (int) (getResources().getDimension(R.dimen.text_margin) / density);
        headerLRMargin = (int) (getResources().getDimension(R.dimen.header_lr_margin) / density);
        headerBTMargin = (int) (getResources().getDimension(R.dimen.header_bt_margin) / density);
        imageSizeHeight = (int) (getResources().getDimension(R.dimen.image_size_height) / density);
        imageSizeWidth = (int) (getResources().getDimension(R.dimen.image_size_width) / density);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        content = (LinearLayout) rootView.findViewById(R.id.container);
        gson = new Gson();
        restoreState(savedInstanceState);
        return rootView;
    }

    private void restoreState(Bundle savedInstanceState){
        if(savedInstanceState != null){
            String sDesriptionState = savedInstanceState.getString(DESCRIPTION_STATE);
            if(sDesriptionState != null && sDesriptionState.length() > 0) {
                description = gson.fromJson(sDesriptionState, Description.class);
                parseDescription(description);
                return;
            }
        }
        loadContent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DESCRIPTION_STATE, gson.toJson(description));
        if(detailsUrl != null){
            outState.putString(ItemDetailFragment.ARG_ITEM_ID, detailsUrl);
        }
    }

    private void loadContent() {
        Call<Description> call = PdaClient.getPdaService().getDescription(detailsUrl);
        call.enqueue(new Callback<Description>() {
            @Override
            public void onResponse(Call<Description> call, Response<Description> response) {
                try {
                    description = response.body();
                    parseDescription(description);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Description> call, Throwable t) {

            }
        });
    }

    private void parseDescription(Description pDescription) {
        String title = pDescription.getTitle();
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
        TextView titleTextView = createTextView(title, Paragraph.STYLE_TITLE);
        content.addView(titleTextView);
        for (int i = 0; i < pDescription.getParagraphs().size(); i++) {
            Paragraph paragraph = pDescription.getParagraphs().get(i);
            String text = paragraph.getText();
            String style = paragraph.getStyle();
            if (style.equals(Paragraph.STYLE_IMAGE)) {
                ImageView imageView = createImageView(paragraph.getImageUrl());
                content.addView(imageView);
            } else {
                TextView textView = createTextView(text, style);
                content.addView(textView);
            }
        }
    }

    private TextView createTextView(String text, String style) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        TextView textView = new TextView(getContext());
        textView.setText(text);
        float textSize = normalTextSize;
        int leftMargin = textMargin;
        int rightMargin = textMargin;
        int bottomMargin = textMargin;
        int topMargin = textMargin;
        int textColor = getResources().getColor(R.color.secondaryText);
        if (style.equals(Paragraph.STYLE_TEXT)) {
            textSize = normalTextSize;
        } else if (style.equals(Paragraph.STYLE_HEADER)) {
            textSize = headerTextSize;
            leftMargin = headerLRMargin;
            rightMargin = headerLRMargin;
            bottomMargin = headerBTMargin;
            topMargin = headerBTMargin;
            textColor = getResources().getColor(R.color.primaryText);
        } else if (style.equals(Paragraph.STYLE_TITLE)) {
            textSize = tileTextSize;
            leftMargin = headerLRMargin;
            rightMargin = headerLRMargin;
            bottomMargin = headerBTMargin;
            topMargin = headerBTMargin;
            textColor = getResources().getColor(R.color.primaryText);
        }
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        textView.setLayoutParams(params);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        return textView;
    }

    private ImageView createImageView(String url) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        ImageView imageView = new ImageView(getContext());
        params.setMargins(textMargin, textMargin, textMargin, textMargin);
        imageView.setLayoutParams(params);
        Picasso.with(getContext())
                .load(url)
                .resize(imageSizeWidth,imageSizeHeight)
                .centerCrop()
                .into(imageView);
        return imageView;
    }
}
