package com.sgc.clock.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;

import com.sgc.clock.R;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.util.AlarmClockConverter;
import com.sgc.clock.util.Constants;
import com.sgc.clock.util.ParcelableUtil;

import java.util.ArrayList;

import static com.sgc.clock.util.Constants.TAG_SEND_ALARM_CLOCK;
import static com.sgc.clock.util.Constants.isDebug;


public class OrderReminderNotificationService extends Service {

    public final static String BROADCAST_ACTION = "Clock_Broadcast_Intent";

    NotificationChannel channel = null;
    NotificationManager notificationManager;
    MediaPlayer player;
    AlarmManager alarmManager;
    AlarmClock alarmClock;

    ArrayList<Integer> clockArrayId = new ArrayList<>();

    protected void playRingtone(Intent intent) {
        byte[] alarmClockByteArray = intent.getByteArrayExtra(TAG_SEND_ALARM_CLOCK);
        alarmClock = AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray);

        if (alarmClock.getActive()) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            // not start music player when app work in debug mode
            if (!Constants.isDebug)
                startPlayer();

            sendNotification();
        }
    }

    private void startPlayer() {
        Uri alarmUri = getRingoUri();
        if (player == null) {
            player = MediaPlayer.create(this, alarmUri);
            player.setLooping(true);
            setMaxVolume();
        }
        player.start();
    }

    private void sendNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = createBuilder(getPendingIntent());
        setChannel(builder);
        notificationManager.notify(alarmClock.get_id(), builder.build());
    }

    private Uri getRingoUri() {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        return alarmUri;
    }

    private void setMaxVolume() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    private void setChannel(NotificationCompat.Builder builder) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setChannelId("com.sgc");
            channel = new NotificationChannel(
                    "com.sgc",
                    "sgc",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private PendingIntent getPendingIntent() {
        Intent iClose = new Intent(OrderReminderNotificationService.BROADCAST_ACTION);
        clockArrayId.add(alarmClock.get_id());
        PendingIntent piClose = PendingIntent.getBroadcast(this, 0, iClose, PendingIntent.FLAG_ONE_SHOT);
        return piClose;
    }

    private NotificationCompat.Builder createBuilder(PendingIntent piClose) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("Будильник")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(piClose)
                .addAction(R.drawable.cross, "Стоп", piClose)
                .setDeleteIntent(piClose);
        return builder;
    }

    void registerReceiver() {
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (int i = 0; i < clockArrayId.size(); i++) {
                    int alarmId = clockArrayId.get(i);
                    AlarmClock alarm = AlarmClockDataBaseHelper.getInstance(getApplicationContext()).getAlarmClock(alarmId);
                    notificationManager.cancel(alarmId);
                    disableAlarmClock(alarm);
                    AlarmClockDataBaseHelper.getInstance(getApplicationContext()).updateAlarmClockToDataBase(alarm);
                }

                clockArrayId.removeAll(clockArrayId);
                if (!Constants.isDebug)
                    player.pause();
            }
        };
        this.onDestroy();
        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, filter);
    }

    private void disableAlarmClock(AlarmClock alarm) {
        if (alarm.getAlarmClockDaysOfWeek().equals(AlarmClock.DaysOfWeek.NOREPEAT.getCode())) {
            alarm.setActive(false);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver();
        playRingtone(intent);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
