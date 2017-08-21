package nich.work.aequorea.author.presenter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nich.work.aequorea.author.model.entities.Author;
import nich.work.aequorea.author.model.entities.Datum;
import nich.work.aequorea.author.ui.AuthorView;
import nich.work.aequorea.common.network.NetworkService;
import nich.work.aequorea.common.network.RequestManager;
import nich.work.aequorea.common.presenter.BasePresenter;
import nich.work.aequorea.common.utils.NetworkUtils;

public class AuthorPresenter extends BasePresenter<AuthorView> {
    private NetworkService mService;
    private Author mAuthor;
    private Throwable mThrowable;
    private int mPage;
    private int mPer;
    private long mTotalPage;
    
    @Override
    protected void onAttach() {
        mService = RequestManager.getInstance().getRetrofit().create(NetworkService.class);
        
        mPage = 1;
        mPer = 15;
        mTotalPage = 1;
    }
    
    public void load(int id) {
        if (!NetworkUtils.isNetworkAvailable()) {
            mBaseView.onError(null);
            return;
        }
        
        if (mPage > mTotalPage || mBaseView.getModel().isLoading()) {
            return;
        }
        mBaseView.getModel().setLoading(true);
        
        mComposite.add(mService.getArticleList(id, mPage, mPer)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Author>() {
                @Override
                public void accept(Author author) throws Exception {
                    mAuthor = author;
                    publish();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    mThrowable = throwable;
                    publish();
                }
            }));
    }
    
    public void findAuthorInfo(final Author a) {
        final List<Datum> data = a.getData();
        
        mComposite.add(Single.create(new SingleOnSubscribe<Author>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Author> e) throws Exception {
                if (data.size() != 0) {
                    for (Author author : data.get(0).getAuthors()) {
                        // sometimes the leader may mark as the first author, so we need to make sure it's the right author
                        if (author.getId() == mBaseView.getModel().getAuthorId()) {
                            author.setMeta(a.getMeta());
                            e.onSuccess(author);
                            return;
                        }
                    }
                }
            }
        })
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Author>() {
                @Override
                public void accept(Author author) throws Exception {
                    mBaseView.onUpdateAuthorInfo(author);
                }
            }));
    }
    
    private void publish() {
        mBaseView.getModel().setLoading(false);
        if (mAuthor != null) {
            mPage++;
            mTotalPage = mAuthor.getMeta().getTotalPages();
            mBaseView.onDataLoaded(mAuthor);
        } else if (mThrowable != null) {
            mBaseView.onError(mThrowable);
        }
        mAuthor = null;
        mThrowable = null;
    }
}
