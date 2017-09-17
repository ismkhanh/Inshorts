package com.ik.readthenews.repository.shared_pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.ik.readthenews.common.SortOption;

public class MySharedPref {

    private static final String MY_PREF = "MyPref";
    private static final String KEY_SORT_OPTION = "sort_option";

    private SharedPreferences mSharedPref;
    private Context context;

    public MySharedPref(Context context){
        mSharedPref = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
    }

    public void saveSortOption(SortOption sortOption){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(KEY_SORT_OPTION, sortOption.toString());
        editor.apply();
    }

    public SortOption getSortOption(){
        return SortOption.toSortOption(mSharedPref.getString(KEY_SORT_OPTION, SortOption.OLD_TO_NEW.toString()));
    }

}
