package com.ik.readthenews.repository.network;

import com.ik.readthenews.repository.database.entity.Article;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    @GET("newsjson")
    Flowable<List<Article>> getArticles();

    @GET
    Flowable<String>  getUrlData(@Url String url);
}
