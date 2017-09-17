package com.ik.readthenews.repository;


import com.ik.readthenews.repository.database.entity.Article;

import java.util.List;

import io.reactivex.Flowable;

public interface Repository {

    Flowable<List<Article>> getAllArticlesByPublisher();
    Flowable<List<Article>> getAllArticlesByCategory();
    Flowable<List<Article>> getAllArticlesByOldToNew();
    Flowable<List<Article>> getAllArticlesByNewToOld();

}
