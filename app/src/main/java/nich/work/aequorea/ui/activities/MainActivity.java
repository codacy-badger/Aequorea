package nich.work.aequorea.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nich.work.aequorea.Aequorea;
import nich.work.aequorea.R;
import nich.work.aequorea.common.Constants;
import nich.work.aequorea.common.ui.activities.BaseActivity;
import nich.work.aequorea.common.ui.view.ParallaxImageView;
import nich.work.aequorea.common.ui.widget.NestedScrollAppBarLayout;
import nich.work.aequorea.common.ui.widget.StatusBarView;
import nich.work.aequorea.common.utils.NetworkUtils;
import nich.work.aequorea.common.utils.SnackbarUtils;
import nich.work.aequorea.common.utils.ThemeHelper;
import nich.work.aequorea.model.MainPageModel;
import nich.work.aequorea.model.entity.Datum;
import nich.work.aequorea.presenter.MainPresenter;
import nich.work.aequorea.ui.adapters.MainArticleAdapter;
import nich.work.aequorea.ui.view.HomeView;

public class MainActivity extends BaseActivity implements HomeView, NestedScrollAppBarLayout.OnNestedScrollListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter mPresenter;
    private MainArticleAdapter mAdapter;
    private MainPageModel mModel;
    private LinearLayoutManager mLinearLayoutManager;
    private MenuItem mThemeMenuItem;

    private long mClickTime;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
            super.onScrolled(mRecyclerView, dx, dy);
            if (dy > Constants.AUTO_LOAD_TRIGGER) {
                autoLoad();
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mModel.setRefreshing(true);
            mPresenter.refresh();
        }
    };

    @BindView(R.id.rec) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.main_content) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.appbar) NestedScrollAppBarLayout mAppBarLayout;
    @BindView(R.id.status_bar) StatusBarView mStatusBar;
    @BindView(R.id.layout_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.loading_progressbar) ProgressBar mProgressBar;
    @BindView(R.id.container_refresh) View mRefreshView;
    
    @OnClick(R.id.container_refresh) void refresh() {
        hideRefreshLayout();
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initModel();
        initView();
        initPresenter();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    private void initModel() {
        mModel = new MainPageModel();
    }

    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = new MainPresenter();
        }
        mPresenter.attach(this);
    }

    private void initView() {
        ButterKnife.bind(this);
        
        if (isInLightTheme()) {
            setStatusBarStyle(true);
            mStatusBar.setLightMask();
        } else {
            setStatusBarStyle(false);
            mStatusBar.setDarkMask();
        }

        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setOnClickListener(this);
        setSupportActionBar(mToolbar);

        mAdapter = new MainArticleAdapter(this, null);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setTag(ParallaxImageView.RECYCLER_VIEW_TAG);
        mRecyclerView.addOnScrollListener(mScrollListener);

        mAppBarLayout.setOnNestedListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
    }
    
    @Override
    public void onDataLoaded(List<Datum> data, boolean isRefresh) {
        hideRefreshLayout();
    
        mAdapter.setArticleList(data, isRefresh);
        mAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onError(Throwable error) {
        if (mAdapter.getItemCount() == 0){
            showRefreshLayout();
        } else {
            hideRefreshLayout();
        }
    
        if (error != null) {
            Log.d(TAG, error.getMessage());
            SnackbarUtils.show(mRecyclerView, error.getMessage());
        }
    }

    @Override
    public void setRefreshing(boolean isRefreshing){
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void onNestedScrolling() {
        changeStatusBarStyle();
    }

    @Override
    public void onStopNestedScrolling() {
        changeStatusBarStyle();
    }

    private void changeStatusBarStyle() {
        // light status bar only show in light theme
        if (isInLightTheme()) {
            if (mAppBarLayout.getY() <= -mAppBarLayout.getMeasuredHeight()) {
                if (mStatusBar.isInitState()) {
                    setStatusBarStyle(false);
                    mStatusBar.setDarkMask();
                }
            } else {
                if (!mStatusBar.isInitState()) {
                    setStatusBarStyle(true);
                    mStatusBar.setLightMask();
                }
            }
        }
    }

    @Override
    public MainPageModel getModel(){
        return mModel;
    }
    
    private void autoLoad() {
        int lastVisibleItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
        
        int totalCount = mAdapter.getItemCount();
        if (!mModel.isLoading() && !mModel.isRefreshing() && totalCount > 0
            && lastVisibleItem >= totalCount - 3 && NetworkUtils.isNetworkAvailable()) {
            mPresenter.loadData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar:
                if (System.currentTimeMillis() - mClickTime < 200) {
                    int position = mLinearLayoutManager.findFirstVisibleItemPosition();
                    scrollToTop(position);
                }
                mClickTime = System.currentTimeMillis();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int position = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (position > 0) {
                scrollToTop(position);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onThemeSwitch() {
        setTheme(ThemeHelper.getThemeStyle(Aequorea.getCurrentTheme()));
        currentTheme = Aequorea.getCurrentTheme();
        
        setStatusBarStyle();
        setStatusBarMask();
        
        int primaryColor = ThemeHelper.getResourceColor(this, R.attr.colorPrimary);
        int titleColor = ThemeHelper.getResourceColor(this, R.attr.title_color);
        int rootColor = ThemeHelper.getResourceColor(this, R.attr.root_color);
        int themeDrawable = ThemeHelper.getResourceId(this, R.attr.icon_theme);
        
        mAppBarLayout.setBackgroundColor(primaryColor);
        mToolbar.setTitleTextColor(titleColor);
        mThemeMenuItem.setIcon(themeDrawable);
        mRecyclerView.setBackgroundColor(rootColor);
    }
    
    private void scrollToTop(int currentPosition) {
        if (currentPosition >= 10) {
            mRecyclerView.scrollToPosition(6);
        }
        mRecyclerView.smoothScrollToPosition(0);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mThemeMenuItem = menu.findItem(R.id.action_switch_theme);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch_theme:
                String themeToSwitch = isInLightTheme() ? Constants.THEME_DARK : Constants.THEME_LIGHT;
                setTheme(ThemeHelper.getThemeStyle(themeToSwitch));
                ThemeHelper.setTheme(themeToSwitch);
    
                onThemeSwitch();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void setStatusBarStyle() {
        if (isInLightTheme()) {
            setStatusBarStyle(true);
        } else {
            setStatusBarStyle(false);
        }
    }
    
    private void setStatusBarMask() {
        if (isInLightTheme()) {
            mStatusBar.setLightMask();
        } else {
            mStatusBar.setDarkMask();
        }
    }
    
    private void hideRefreshLayout() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRefreshView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }
    
    private void showRefreshLayout() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRefreshView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}