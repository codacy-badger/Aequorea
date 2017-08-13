package nich.work.aequorea.author.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nich.work.aequorea.R;
import nich.work.aequorea.author.model.entities.Datum;
import nich.work.aequorea.common.utils.IntentUtils;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.ViewHolder> {
    private List<Datum> mArticleDataList;
    private Context mContext;
    private LayoutInflater mInflater;
    
    public AuthorAdapter(Context context, List<Datum> dataList) {
        mContext = context;
        mArticleDataList = dataList;
        mInflater = LayoutInflater.from(context);
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_author, parent, false));
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Datum article = mArticleDataList.get(position);
        
        holder.bindData(article);
        holder.articleTitleTv.setText(article.getTitle());
        holder.articleSummaryTv.setText(article.getSummary());
    
        Glide.with(mContext)
            .load(article.getCoverUrl())
            .placeholder(R.color.colorPrimary_light)
            .into(holder.coverIv);
    }
    
    @Override
    public int getItemCount() {
        return mArticleDataList != null ? mArticleDataList.size() : 0;
    }
    
    public List<Datum> getArticleDataList() {
        return mArticleDataList;
    }
    
    public void setArticleDataList(List<Datum> mArticleDataList) {
        this.mArticleDataList = mArticleDataList;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        Datum article;
        
        @BindView(R.id.iv_article_cover) ImageView coverIv;
        @BindView(R.id.tv_article_title) TextView articleTitleTv;
        @BindView(R.id.tv_article_summary) TextView articleSummaryTv;
    
        @OnClick(R.id.container) void startArticleActivity() {
            IntentUtils.startArticleActivity(itemView.getContext(), article.getId());
        }
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        
        public void bindData(Datum data){
            article = data;
        }
    }
}