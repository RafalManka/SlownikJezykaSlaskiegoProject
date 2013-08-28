package pl.rafalmanka.slownikjezykaslaskiego;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appflood.AFBannerView;
import com.appflood.AppFlood;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rafal on 8/27/13.
 */
public class FlavourUtils {

    public static void getAds(final Context context) {
        AppFlood.initialize(context, "1H4eGPIQqpLeO6bp", "sLMJUwp81645L520fedbe", AppFlood.AD_ALL);
        AFBannerView afBannerView = new AFBannerView(context, AppFlood.BANNER_SMALL, true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RelativeLayout iv = (RelativeLayout) ((Activity) context).findViewById(R.id.advertisment_imageview);
        iv.addView(afBannerView, params);
    }


}
