package com.ik.readthenews.repository;

import com.ik.readthenews.repository.database.AppDatabase;
import com.ik.readthenews.repository.database.dao.ArticleDao;
import com.ik.readthenews.repository.database.entity.Article;
import com.ik.readthenews.repository.network.ApiService;

import java.util.List;

import io.reactivex.Flowable;


public class RepositoryImpl implements Repository {

    private ApiService mApiService;
    private AppDatabase mDatabase;

    public RepositoryImpl(ApiService apiService, AppDatabase database){
        mApiService = apiService;
        mDatabase = database;
    }

    @Override
    public Flowable<List<Article>> getAllArticlesByPublisher() {
        ArticleDao dao = mDatabase.articleDao();
        return dao.getAllArticlesByPublisher()//query the local db for article list
                .map(articles -> {
                    if(articles.size() > 0) return articles; //if the list size is > 0, return it
                    else return fetchFromNetwork(dao); //if list is empty fetch from network
                });
    }

    @Override
    public Flowable<List<Article>> getAllArticlesByCategory() {
        ArticleDao dao = mDatabase.articleDao();
        return dao.getAllArticlesByCategory()//query the local db for article list
                .map(articles -> {
                    if(articles.size() > 0) return articles; //if the list size is > 0, return it
                    else return fetchFromNetwork(dao); //if list is empty fetch from network
                });
    }

    @Override
    public Flowable<List<Article>> getAllArticlesByOldToNew() {
        ArticleDao dao = mDatabase.articleDao();
        return dao.getAllArticlesByOldToNew()//query the local db for article list
                .map(articles -> {
                    if(articles.size() > 0) return articles; //if the list size is > 0, return it
                    else return fetchFromNetwork(dao); //if list is empty fetch from network
                });
    }

    @Override
    public Flowable<List<Article>> getAllArticlesByNewToOld() {
        ArticleDao dao = mDatabase.articleDao();
        return dao.getAllArticlesByNewToOld()//query the local db for article list
                .map(articles -> {
                    if(articles.size() > 0) return articles; //if the list size is > 0, return it
                    else return fetchFromNetwork(dao); //if list is empty fetch from network
                });
    }

    private List<Article> fetchFromNetwork(final ArticleDao dao){
        return mApiService.getArticles() // else if the local db list size is empty, fetch the list from network
                .map(fetchedArticles -> {
                    dao.insertArticle(fetchedArticles); //after fetching the list over network store in local db
                    return fetchedArticles; //return the fetched list
                }).blockingFirst();
    }
}
