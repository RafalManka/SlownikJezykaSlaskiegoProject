package pl.rafalmanka.sjs.old;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Window;

/**
 * Created by rafal on 8/15/13.
 */
public class ActivityUtils {

    public static void setLayout(Activity context,int layout,int titleBar) {
    	context.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);		
    	context.setContentView(layout);
    	context.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, titleBar);
    }
    
	public static void sendEmail(Context context, String recipient, String subject, String body) {
		StringBuilder builder = new StringBuilder("mailto:"
				+ Uri.encode(recipient));
		if (subject != null) {
			builder.append("?subject=" + subject);
			if (body != null) {
				builder.append("&body=" + body);
			}
		}
		String uri = builder.toString();
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
		intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
