package com.ik.readthenews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ik.readthenews.R;
import com.ik.readthenews.common.Utils;
import com.ik.readthenews.repository.database.entity.Article;

import java.util.Date;
import java.util.List;


public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private List<Article> mList;
    private ArticleClickListener mClickListener;

    public ArticleListAdapter(List<Article> list){
        mList = list;
    }

    public void registerClickListener(ArticleClickListener listener){
        mClickListener = listener;
    }

    public void unregisterClickListener(){
        mClickListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_article, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mList.get(position);
        holder.setArticleTitle(article.getTITLE());
        holder.setTvArticlePublisher(article.getPUBLISHER());
        holder.setArticleDate(new Date(article.getTIMESTAMP()));
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void updateList(List<Article> list) {
        mList = list;
        notifyDataSetChanged();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvArticleTitle, tvArticlePublisher, tvArticleDate;

        ViewHolder(View itemView) {
            super(itemView);
            tvArticleTitle = (TextView) itemView.findViewById(R.id.tv_article_title);
            tvArticlePublisher = (TextView) itemView.findViewById(R.id.tv_article_publisher);
            tvArticleDate = (TextView) itemView.findViewById(R.id.tv_article_date);

            itemView.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onArticleClicked(mList.get(getAdapterPosition()));
                }
            });
        }

        void setArticleTitle(String title){
            tvArticleTitle.setText(title);
        }

        void setTvArticlePublisher(String publisher){
            tvArticlePublisher.setText(publisher);
        }

        void setArticleDate(Date date){

            tvArticleDate.setText(Utils.getFormattedDate(date));
        }
    }

    public interface ArticleClickListener{
        void onArticleClicked(Article clickedArticle);
    }
}
