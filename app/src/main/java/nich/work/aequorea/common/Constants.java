package nich.work.aequorea.common;

public class Constants {
    public static final String AUTHOR = "author";
    public static final String TAG = "tag";
    public static final String ARTICLE_ID = "article_id";
    public static final String AUTHOR_ID = "author_id";
    public static final String TAG_ID = "tag_id";
    public static final String SEARCH_KEY = "search_key";
    public static final String PHOTO = "photo";
    public static final String ARG_URL = "arg_url";
    
    // Article Type
    public static final String ARTICLE_TYPE_MAGAZINE = "magazine_article"; // 封面故事
    public static final String ARTICLE_TYPE_MAGAZINE_V2 = "magazine"; // 封面故事
    public static final String ARTICLE_TYPE_NORMAL = "normal_article"; // 一般文章
    public static final String ARTICLE_TYPE_NORMAL_V2 = "normal"; // 一般文章
    public static final String ARTICLE_TYPE_THEME = "theme"; // 专题
    public static final String ARTICLE_TYPE_TOP_ARTICLE = "top_article"; // 头条故事
    public static final String ARTICLE_TYPE_SUBJECT = "subject"; // 单行本


    public static final int TYPE_MAGAZINE = 1;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_THEME = 3;
    public static final int TYPE_TOP_ARTICLE = 4;
    public static final int TYPE_SUBJECT = 5;
    
    // Theme
    public static final String THEME = "theme";
    public static final String THEME_LIGHT = "theme_light";
    public static final String THEME_DARK = "theme_dark";
    
    // Trigger
    public static final int AUTO_LOAD_TRIGGER = 30;
    
    // Key of SharePreference
    public static final String SP_LATEST_MAIN_PAGE = "sp_latest_main_page";
    public static final String SP_HD_SCREENSHOT = "sp_hd_screenshot";
    public static final String SP_ENABLE_SELECTION = "sp_enable_selection";
    public static final String SP_OFFLINE_CACHE = "sp_offline_cache";
    public static final String SP_DISABLE_RECOMMEND_ARTICLE = "sp_disable_recommend_article";
    
    // Cache dir
    public static final String ARTICLE_CACHE = "article_cache";
    public static final String ARTICLE_PIC_CACHE = "article_pic_cache";
}
