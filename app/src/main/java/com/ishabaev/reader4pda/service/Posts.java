package com.ishabaev.reader4pda.service;

import java.util.ArrayList;
import java.util.List;

public class Posts {
    List<Post> posts;

    public Posts(){
        posts = new ArrayList<>();
    }
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void add(Post post){
        posts.add(post);
    }

    public List<Post> containsPosts(List<Post> loadedPosts){
        for (int i = 0; i < loadedPosts.size(); i++){
            for(int j = 0; j < posts.size(); j++) {
                if(posts.get(j).getDetailsUrl().equals(
                        loadedPosts.get(i).getDetailsUrl())){
                    posts.remove(j);
                    break;
                }
            }
        }
        return posts;
    }
}
