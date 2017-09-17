package com.ik.readthenews.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ik.readthenews.R;
import com.ik.readthenews.common.SortOption;
import com.ik.readthenews.common.Utils;
import com.ik.readthenews.repository.Repository;
import com.ik.readthenews.repository.database.entity.Article;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.ik.readthenews.common.SortOption.CATEGORY;
import static com.ik.readthenews.common.SortOption.OLD_TO_NEW;
import static com.ik.readthenews.common.SortOption.PUBLISHER;

public class ListActivityViewModel extends AndroidViewModel {

    private Repository mRepository;

    private MutableLiveData<List<Article>> mArticleList = new MutableLiveData<>();
    private MutableLiveData<String> mMsgs = new MutableLiveData<>();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private ListActivityViewModel(Application application, Repository repository) {
        super(application);
        mRepository = repository;
    }

    /**
     * This method fetches the Article List from Repository based on the SortOption selected
     *
     * @return LiveData which the client can observer on
     */
    public LiveData<List<Article>> getArticleList() {
        fetchArticleList(new ListArticleSubscriber(mArticleList, mMsgs),
                Utils.getSortOption(getApplication()));
        return mArticleList;
    }

    /**
     * This method is used to observe msgs to be shown on the UI
     *
     * @return LiveData of the type String
     */
    public LiveData<String> observeMsgs() {
        return mMsgs;
    }

    /**
     * When the user selects a different SortOption
     * this method is called to fetch the list from the Repository is the
     * selected SortOption
     */
    public void refreshArticleList() {
        fetchArticleList(new ListArticleSubscriber(mArticleList, mMsgs),
                Utils.getSortOption(getApplication()));
    }

    /**
     * This method is called by the client when it is being destroyed
     */
    public void unbind() {
        mCompositeDisposable.clear();
    }

    /**
     * Returns a Flowable of the type List<Article> based on the SortOption
     *
     * @param sortOption The order in which list should be fetched
     * @return Flowable
     */
    private Flowable<List<Article>> getFlowableBySortOption(SortOption sortOption) {
        if (sortOption == PUBLISHER) {
            return mRepository.getAllArticlesByPublisher();
        } else if (sortOption == CATEGORY) {
            return mRepository.getAllArticlesByCategory();
        } else if (sortOption == OLD_TO_NEW) {
            return mRepository.getAllArticlesByOldToNew();
        } else {
            return mRepository.getAllArticlesByNewToOld();
        }
    }

    /**
     * This method is called by the client when the device is online.
     * This method first checks if the list is null or empty, if it is,
     * fetches the list from the repository based on the SortOption
     */
    public void fetchListIfNotFetched() {
        List<Article> list = mArticleList.getValue();
        if (list == null || list.size() == 0) {
            mMsgs.setValue(getApplication().getString(R.string.fetching_list));
            fetchArticleList(new ListArticleSubscriber(mArticleList, mMsgs),
                    Utils.getSortOption(getApplication()));
        }
    }

    /**
     * This method subscribes to the Flowable to listen to the items emitted.
     *
     * @param subscriber To listen to items emitted
     * @param sortOption Order in which the list should be fetched
     */
    private void fetchArticleList(DisposableSubscriber<List<Article>> subscriber,
                                  SortOption sortOption) {

        getFlowableBySortOption(sortOption)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        mCompositeDisposable.add(subscriber);
    }

    /**
     * This class listens to items emitted by the Flowable.
     * If success updates the ArticleList LiveData - mArticleList
     * If fails updates the String LiveData - mMsgs
     */
    private class ListArticleSubscriber extends DisposableSubscriber<List<Article>> {

        private final MutableLiveData<List<Article>> articleList;
        private final MutableLiveData<String> errorMsgs;

        ListArticleSubscriber(MutableLiveData<List<Article>> articleList,
                              MutableLiveData<String> errorMsgs) {
            this.articleList = articleList;
            this.errorMsgs = errorMsgs;
        }

        @Override
        public void onNext(List<Article> articles) {
            articleList.setValue(articles);
        }

        @Override
        public void onError(Throwable t) {
            errorMsgs.setValue("Error: "+t.getLocalizedMessage());
        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * This class is used to create the instance of the ListActivityViewModel
     * and inject the dependencies required by it.
     */
    public static class Factory implements ViewModelProvider.Factory {

        private Application application;
        private Repository repository;

        public Factory(Application application, Repository repository) {
            this.application = application;
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ListActivityViewModel(application, repository);
        }
    }

}
