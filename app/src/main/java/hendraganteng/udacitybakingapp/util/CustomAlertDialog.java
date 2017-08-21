package hendraganteng.udacitybakingapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Helper to show alert dialog
 *
 * @author hendrawd on 6/6/16
 */
public class CustomAlertDialog {
    private static AlertDialog alert;

    /**
     * @param context activity context
     * @param text    string to show
     */
    public static void show(Context context, String text) {
        if (context != null && context == context.getApplicationContext())
            throw new IllegalStateException("application context can't be used here");
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    alert.dismiss();
                }
            };
            builder.setMessage(text)
                    .setCancelable(true)
                    .setPositiveButton("OK", listener);
            alert = builder.create();
            alert.show();
        } catch (Exception e) {
            //in case context is not present, etc
        }
    }
}
