package com.ishabaev.reader4pda.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.ishabaev.reader4pda.service.Post;
import com.ishabaev.reader4pda.service.Posts;
import com.ishabaev.reader4pda.R;
import com.ishabaev.reader4pda.service.PdaClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListActivity extends AppCompatActivity {

    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;
    private List<Post> postList;
    private int currentPage;
    private boolean loading;
    private LinearLayoutManager mLayoutManager;
    private Gson gson;
    private static final String POSTS_STATE = "POSTS_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        postList = new ArrayList<>();
        currentPage = 1;
        gson = new Gson();
        restoreState(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
        setupRecyclerView(recyclerView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        if (findViewById(R.id.item_detail_container) != null) {
            postsRecyclerViewAdapter.setmTwoPane(true);
        }
    }

    private void restoreState(Bundle savedInstanceState){
        if(savedInstanceState != null){
            String sPostsState = savedInstanceState.getString(POSTS_STATE);
            if(sPostsState != null && sPostsState.length() > 0){
                Posts posts = gson.fromJson(sPostsState, Posts.class);
                postList = posts.getPosts();
                return;
            }
        }
        fetchTimelineAsync(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Posts postsOut = new Posts();
        postsOut.setPosts(postList);
        outState.putString(POSTS_STATE,gson.toJson(postsOut));
    }

    private void fetchTimelineAsync(final int page) {
        loading = true;
        Call<Posts> call = PdaClient.getPdaService().getNews(page);
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                loading = false;
                if(page == 0){
                    List<Post> newPosts = response.body().containsPosts(postList);
                    if(postList.size() == 0){
                        postList.addAll(newPosts);
                    }else if(newPosts.size()>0){
                        postList.addAll(0,newPosts);
                    }
                }else {
                    currentPage++;
                    postList.addAll(response.body().getPosts());
                }
                postsRecyclerViewAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                loading = false;
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(postList, this);
        recyclerView.setAdapter(postsRecyclerViewAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0){
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        fetchTimelineAsync(currentPage+1);
                    }
                }
            }
        }
    };
}
