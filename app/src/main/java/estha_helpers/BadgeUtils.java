package estha_helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import miluca.skatetime.R;

/**
 * Created by Emil
 */
public class BadgeUtils {
    public static void setBadgeCountPrograms(Context context, LayerDrawable icon, int count) {

        BadgeDrawablePrograms badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge_activities);
        if (reuse != null && reuse instanceof BadgeDrawablePrograms) {
            badge = (BadgeDrawablePrograms) reuse;
        } else {
            badge = new BadgeDrawablePrograms(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge_activities, badge);
    }

    public static void setBadgeCountDate(Context context, LayerDrawable icon, String text) {

        BadgeDrawableDate badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge_date);
        if (reuse != null && reuse instanceof BadgeDrawableDate) {
            badge = (BadgeDrawableDate) reuse;
        } else {
            badge = new BadgeDrawableDate(context);
        }

        badge.setText(text);

        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge_date, badge);
    }

}
