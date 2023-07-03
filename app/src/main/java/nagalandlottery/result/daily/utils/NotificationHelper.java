package nagalandlottery.result.daily.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import nagalandlottery.result.daily.Activity.LatestResults;
import nagalandlottery.result.daily.R;

class NotificationHelper extends ContextWrapper {
    public static final String channelID = "NagaLandID";
    public static final String channelName = "NagalandLotteryChannel";
    private NotificationManager notificationManager;
    private final Data data;
    private NotificationCompat.Builder builder = null;

    public NotificationHelper(Context base, Data data) {
        super(base);
        this.data = data;
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    public NotificationCompat.Builder getChannelNotification() {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        Intent resultIntent;
        if (data.getActivity().equals("url"))
        {
            resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(data.getExtraValue()));
        }
        else
        {
            resultIntent = new Intent(this, LatestResults.class);
            resultIntent.putExtra("time",data.getExtraValue());
            resultIntent.putExtra("page","notify");
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Bitmap imageBitmap = null;
        try {
            InputStream inputStream = new URL(data.getIcon()).openStream();
            imageBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageBitmap!=null)
        {
            builder = new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle(data.getTitle())
                    .setContentText(data.getBody())
                    .setVibrate(new long[]{0,100,500,100})
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setAutoCancel(true)
                    .setLargeIcon(imageBitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(imageBitmap)
                            .bigLargeIcon(null))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);;
        }
        else
        {
            builder = new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle(data.getTitle())
                    .setContentText(data.getBody())
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.ic_logo);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound).setVibrate(new long[]{100, 200, 300, 400});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.shouldShowLights();
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            assert getManager() != null;
            getManager().createNotificationChannel(notificationChannel);
        }


        return builder;
    }
}