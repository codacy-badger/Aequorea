package nich.work.aequorea.presenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nich.work.aequorea.common.utils.FilterUtils;
import nich.work.aequorea.model.entity.Data;
import nich.work.aequorea.model.entity.Datum;
import nich.work.aequorea.model.entity.search.Content;
import nich.work.aequorea.model.entity.search.SearchData;
import nich.work.aequorea.model.entity.search.SearchDatum;

public class SearchPresenter extends SimpleArticleListPresenter {
    @Override
    public void load() {
        
        if (mPage > mTotalPage || mBaseView.getModel().isLoading()) {
            return;
        }
        mBaseView.getModel().setLoading(true);
        
        mComposite.add(mService.getArticleListWithKeyword(mPage, 20, mBaseView.getModel()
            .getKey(), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSearchDataLoaded, this::onError));
    }
    
    private void onSearchDataLoaded(SearchData searchData) {
        
        Data data = transferSearchDataIntoNormalData(searchData);
        
        super.onDataLoaded(data);
    }
    
    // fuck this api designer
    private Data transferSearchDataIntoNormalData(SearchData searchData) {
        Data data = new Data();
        List<SearchDatum> searchDataList = searchData.getData();
        int size = searchDataList.size();
        if (size != 0) {
            List<Datum> dataList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Content content = searchDataList.get(i).getContent();
                if (FilterUtils.underSupport(content.getArticleType())) {
                    Datum datum = new Datum();
                    datum.setId(content.getId());
                    datum.setAuthors(content.getAuthors());
                    datum.setCoverUrl(content.getCoverUrl());
                    datum.setSummary(content.getSummary());
                    datum.setTitle(content.getTitle());
                    datum.setArticleType(content.getArticleType());
                    dataList.add(datum);
                }
            }
            data.setMeta(searchData.getMeta());
            data.setData(dataList);
        }
        return data;
    }
}
