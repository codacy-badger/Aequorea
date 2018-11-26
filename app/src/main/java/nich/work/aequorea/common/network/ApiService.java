package nich.work.aequorea.common.network;

import io.reactivex.Single;
import nich.work.aequorea.data.entity.Data;
import nich.work.aequorea.data.entity.DataWrapper;
import nich.work.aequorea.data.entity.search.SearchData;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // main page
    @GET("/v2/first_page_infos")
    Single<Data> getMainPageInfo(@Query("page") int page, @Query("per") int per);

    // article details
    @GET("/v2/articles/{article_id}/")
    Single<DataWrapper> getArticleDetailInfo(@Path("article_id") long articleId);

    // article that belonging to specific author
    @GET("/v2/authors/{author_id}/articles")
    Single<Data> getArticleList(@Path("author_id") long authorId, @Query("page") int page, @Query("per") int per);
    
    // recommended article
    @GET("/v2/articles/{article_id}/recommendations")
    Single<Data> getRecommendedArticle(@Path("article_id") long articleId);
    
    // article with tags
    @GET("v2/topics/{tag_id}/articles")
    Single<Data> getTagsList(@Path("tag_id") long id, @Query("page") int page, @Query("per") int per);
    
    // article with keyword
    @GET("v2/pg_search_documents")
    Single<SearchData> getArticleListWithKeyword(@Query("page") int page, @Query("per") int per, @Query("query") String keyword, @Query("group") boolean group);
    
}