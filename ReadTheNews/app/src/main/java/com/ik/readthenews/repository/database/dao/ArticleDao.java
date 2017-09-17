package com.ik.readthenews.repository.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ik.readthenews.repository.database.entity.Article;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertArticle(List<Article> articles);

    @Query("select * from Article")
    public Flowable<List<Article>> getAllArticles();

    @Query("select * from Article ORDER BY PUBLISHER ASC")
    public Flowable<List<Article>> getAllArticlesByPublisher();

    @Query("select * from Article ORDER BY CATEGORY ASC")
    public Flowable<List<Article>> getAllArticlesByCategory();

    @Query("select * from Article ORDER BY TIMESTAMP ASC")
    public Flowable<List<Article>> getAllArticlesByOldToNew();

    @Query("select * from Article ORDER BY TIMESTAMP DESC")
    public Flowable<List<Article>> getAllArticlesByNewToOld();

    @Query("select * from Article where ID = :id")
    public Flowable<Article> getArticleById(int id);

}
