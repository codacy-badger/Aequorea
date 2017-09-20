package nich.work.aequorea.ui.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import butterknife.ButterKnife;
import nich.work.aequorea.R;
import nich.work.aequorea.common.Constants;
import nich.work.aequorea.common.utils.DisplayUtils;
import nich.work.aequorea.common.utils.ThemeHelper;
import nich.work.aequorea.model.SimpleArticleListModel;
import nich.work.aequorea.model.entity.Data;
import nich.work.aequorea.presenter.TagPresenter;
import nich.work.aequorea.ui.adapters.SimpleArticleListAdapter;

public class TagActivity extends SimpleArticleListActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_tag;
    }
    
    @Override
    protected void initModel() {
        mModel = new SimpleArticleListModel();
        mModel.setId((int) getIntent().getLongExtra(Constants.TAG_ID, 0));
    }
    
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        
        mToolbar.setNavigationIcon(R.drawable.icon_ab_back_material);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mCoordinatorLayout.setPadding(0, DisplayUtils.getStatusBarHeight(getResources()), 0, 0);
        
        mAdapter = new SimpleArticleListAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(mScrollListener);
        
        setStatusBarStyle();
    }
    
    @Override
    protected void initPresenter() {
        mPresenter = new TagPresenter();
        mPresenter.attach(this);
        mPresenter.load();
    }
    
    @Override
    public void onDataLoaded(Data a) {
        super.onDataLoaded(a);
        updateToolbarTitle(a);
    }
    
    protected void updateToolbarTitle(Data a) {
        if (mIsFirstPage) {
            mToolbar.setTitle(a.getData().get(0).getTopics().get(0).getName());
        }
    }
    
    @Override
    public void onThemeSwitch() {
        super.onThemeSwitch();
        
        int primaryColor = ThemeHelper.getResourceColor(this, R.attr.colorPrimary);
        int titleColor = ThemeHelper.getResourceColor(this, R.attr.title_color);
        
        mToolbar.setTitleTextColor(titleColor);
        mToolbar.setBackgroundColor(primaryColor);
    }
}