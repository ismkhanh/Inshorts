package com.ik.readthenews.repository.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ik.readthenews.repository.database.dao.ArticleDao;
import com.ik.readthenews.repository.database.entity.Article;

@Database(entities = {Article.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "readthenews";
    public abstract ArticleDao articleDao();
}
