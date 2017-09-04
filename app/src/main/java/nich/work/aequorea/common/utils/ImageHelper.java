package nich.work.aequorea.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import nich.work.aequorea.common.ui.widget.glide.CircleTransformation;

public class ImageHelper {
    public static Target setImage(Context context, String url, ImageView imageView) {
        return setImage(context, url, imageView, false, -1);
    }
    
    public static Target setImage(Context context, String url, ImageView imageView, boolean isRound) {
        return setImage(context, url, imageView, isRound, -1);
    }
    
    public static Target setImage(Context context, String url, ImageView imageView, int placeHolder) {
        return setImage(context, url, imageView, false, placeHolder);
    }
    
    public static Target setImage(Context context, String url, ImageView imageView, boolean isRound, int placeHolder) {
        if (isRound) {
            return Glide.with(context)
                .load(url)
                .transform(new CircleTransformation(context))
                .placeholder(placeHolder)
                .into(imageView);
        } else {
            return Glide.with(context)
                .load(url)
                .placeholder(placeHolder)
                .into(imageView);
        }
    }
}