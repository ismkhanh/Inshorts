package com.ik.readthenews.ui;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ik.readthenews.R;
import com.ik.readthenews.adapters.ArticleListAdapter;
import com.ik.readthenews.common.NetworkInfo;
import com.ik.readthenews.common.SortOption;
import com.ik.readthenews.common.Utils;
import com.ik.readthenews.repository.RepositoryImpl;
import com.ik.readthenews.repository.database.AppDatabase;
import com.ik.readthenews.repository.database.entity.Article;
import com.ik.readthenews.repository.network.ApiService;
import com.ik.readthenews.repository.network.NetworkInstance;
import com.ik.readthenews.viewmodels.ListActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListActivity extends AppCompatActivity implements
        LifecycleRegistryOwner, ArticleListAdapter.ArticleClickListener {

    @BindView(R.id.root)
    View mRootView;
    @BindView(R.id.rv_article_list)
    RecyclerView rvArticleList;

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private ListActivityViewModel mViewModel;
    private ArticleListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        ButterKnife.bind(this);

        mRootView = findViewById(R.id.root);

        //Getting the ApiService
        ApiService apiService = NetworkInstance.getInstance(this).getApiService();

        //Getting the Database
        AppDatabase database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, AppDatabase.DB_NAME).build();

        //Getting the ViewModel
        ListActivityViewModel.Factory factory = new ListActivityViewModel.Factory(getApplication(),
                new RepositoryImpl(apiService, database));
        mViewModel = ViewModelProviders.of(this, factory).get(ListActivityViewModel.class);

        rvArticleList.setLayoutManager(new LinearLayoutManager(this));

        NetworkInfo.getInstance(this).observe(this, connectedToInternet -> {
            if (!connectedToInternet) {
                //offline
                showMsg(getString(R.string.no_internet));
            } else {
                //online fetch the list if not fetched already
                mViewModel.fetchListIfNotFetched();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mViewModel.getArticleList().observe(this,
                articles -> {
                    if (mAdapter == null) {
                        mAdapter = new ArticleListAdapter(articles);
                        rvArticleList.setAdapter(mAdapter);
                        mAdapter.registerClickListener(ArticleListActivity.this);
                    } else {
                        mAdapter.updateList(articles);
                    }
                });

        mViewModel.observeMsgs().observe(this, this::showMsg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.registerClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.unregisterClickListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.unbind();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    @Override
    public void onArticleClicked(Article clickedArticle) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ArticleDetailActivity.KEY_SELECTED_ARTICLE, clickedArticle);
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            showFilterDialogue();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialogue() {
        //get alert dialog view
        View alertDialogView = Utils.getAlertDialogView(this);

        //get alert dialog instance
        AlertDialog dialog = Utils.getAlertDialog(this, alertDialogView,
                this::refreshList);

        dialog.show();
    }

    private void refreshList(SortOption sortOption) {
        Utils.saveSortOption(this, sortOption);
        mViewModel.refreshArticleList();
        showMsg("Sort Option Selected: " + sortOption);
    }

    private void showMsg(String message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG).show();
    }


}
