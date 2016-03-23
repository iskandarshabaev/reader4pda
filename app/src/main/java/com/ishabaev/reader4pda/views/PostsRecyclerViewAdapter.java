package com.ishabaev.reader4pda.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.reader4pda.service.Post;
import com.ishabaev.reader4pda.R;
import com.ishabaev.reader4pda.service.CropSquareTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsRecyclerViewAdapter
        extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {

    private boolean mTwoPane;
    private final List<Post> posts;
    private AppCompatActivity activity;

    public void setmTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }

    public PostsRecyclerViewAdapter(List<Post> items, AppCompatActivity activity) {
        posts = items;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = posts.get(position);
        holder.mContentView.setText(posts.get(position).getTitle());
        Picasso.with(holder.mView.getContext())
                .load(posts.get(position).getImageUri())
                .transform(new CropSquareTransformation())
                .into(holder.mImageView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(true);
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getDetailsUrl());
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getDetailsUrl());
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mContentView;
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mView.setClickable(true);
            mImageView = (ImageView) view.findViewById(R.id.image);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
