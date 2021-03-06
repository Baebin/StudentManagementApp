package com.techtown.studentmanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtown.studentmanagementapp.entity.AdminInfo;
import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.entity.Time;
import com.techtown.studentmanagementapp.manager.FirebaseManager;
import com.techtown.studentmanagementapp.manager.LunchParsingManager;
import com.techtown.studentmanagementapp.manager.StudentManager;
import com.techtown.studentmanagementapp.manager.TimeManager;
import com.techtown.studentmanagementapp.service.FirebaseMessagingService;
import com.techtown.studentmanagementapp.util.SharedPreferenceUtil;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
    static public String TAG = "MainActivity";

    public static Student student = null;
    public static AdminInfo admin = null;

    private DrawerLayout drawerLayout;
    private View view_drawer;

    private TextView tv_name;
    private TextView tv_number;

    private ImageView iv_profile;
    private Button button_lunch;
    private Button button_school;
    private Button button_calendar;
    private Button button_logout;

    private View view;
    private View main_layout;

    private PlayerView pv_main;
    private SimpleExoPlayer player;

    private Button button_1;
    private Button button_2;
    private Button button_3;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        // Hide Action Bar
        getSupportActionBar().hide();

        // SharedPreferences
        SharedPreferenceUtil.init(this);
        student = SharedPreferenceUtil.getStudent();
        Log.d(TAG, "Student Updated");

        admin = SharedPreferenceUtil.getAdminInfo();
        Log.d(TAG, "AdminInfo Updated");

        if (!SharedPreferenceUtil.checkStudent() || StudentManager.checkError(student)) {
            Log.d(TAG, "checkError(): True");
            sendIntent("start_init");
            finish();
            return;
        }

        // Firebase
        FirebaseManager.init(FirebaseDatabase.getInstance());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isComplete()) {
                    String token = task.getResult();
                    Log.d(TAG, "Token: " + token);

                    FirebaseManager.setToken(token);
                } else Log.d(TAG, "Token: Error");
            }
        });

        // activity_drawer_main.xml
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(drawerListener);

        view_drawer = findViewById(R.id.drawer_view);
        view_drawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        tv_name = findViewById(R.id.tv_name);
        tv_number = findViewById(R.id.tv_number);

        iv_profile = findViewById(R.id.iv_profile);
        iv_profile.setImageResource(R.drawable.human);

        button_lunch = findViewById(R.id.button_lunch);
        button_school = findViewById(R.id.button_school);
        button_calendar = findViewById(R.id.button_calendar);
        button_logout = findViewById(R.id.button_logout);

        button_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("lunch");
            }
        });
        button_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("school");
            }
        });
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("calendar");
            }
        });
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("start");
            }
        });

        // activity_main.xml
        view = findViewById(R.id.view);
        main_layout = findViewById(R.id.main_layout);

        pv_main = findViewById(R.id.pv_main);

        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_1");
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_2");
            }
        });
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_3");
            }
        });

        ImageButton btnOpenDrawer = (ImageButton) findViewById(R.id.btn_OpenDrawer);

        // Drawer Opening Listener
        btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(view_drawer);
            }
        });

        initProfile();
        initLunchManager();
    }

    private DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            Log.d(TAG, "onDrawerSlide()");
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            Log.d(TAG, "onDrawerOpened()");
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            Log.d(TAG, "onDrawerClosed");
            main_layout.setEnabled(true);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            Log.d(TAG, "onDrawerStateChanged");
            main_layout.setEnabled(false);
        }
    };

    private void initVideo() throws RawResourceDataSource.RawResourceDataSourceException {
        Log.d(TAG, "initVideo()");

        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        pv_main.setPlayer(player);

        DataSource.Factory factory= new DefaultDataSourceFactory(this,"Ex89VideoAndExoPlayer");

        RawResourceDataSource resource = new RawResourceDataSource(getApplicationContext());
        resource.open(new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.video)));

        ProgressiveMediaSource source = new ProgressiveMediaSource.Factory(factory)
            .createMediaSource(
                resource.getUri()
                /*
                Uri.parse(
                    "android.resource"
                        + "://"
                        + getApplicationContext().getPackageName()
                        + "/"
                        + R.raw.video
                )
                */
            );
        player.prepare(source);
        //player.setPlayWhenReady(true);
    }

    private void initProfile() {
        Log.d(TAG, "initProfile()");

        if (StudentManager.checkAdmin(student)) {
            String name = StartActivity.develop_mode ? "?????????" : "?????????";
            String number = StartActivity.develop_mode ? "77777" : "33333";

            tv_name.setText(
                    getString(R.string.drawer_name)
                            + name
                            + " "
            );
            tv_number.setText(
                    getString(R.string.drawer_number)
                            + number
                            + " "
            );

            checkAdmin();
            return;
        }

        tv_name.setText(
                getString(R.string.drawer_name)
                        + student.getName_()
                        + " "
        );
        tv_number.setText(
                getString(R.string.drawer_number)
                        + StudentManager.getGCN(student)
                        + " "
        );
    }

    private void initLunchManager() {
        Log.d(TAG, "initLunchManager()");
        try {
            Log.d(TAG, "Completed");
            Time time = new Time(LunchActivity.getTime()).setCalendar();
            LunchActivity.manager = new LunchParsingManager(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendIntent(String id) {
        Log.d(TAG, "sendIntent(" + id + ")");
        Intent intent = null;

        if (id.contains("call_")) {
            intent = new Intent(MainActivity.this, CallActivity.class);
        }

        switch (id) {
            case "call_1":
                intent.putExtra("grade", 1);
                break;
            case "call_2":
                intent.putExtra("grade", 2);
                break;
            case "call_3":
                intent.putExtra("grade", 3);
                break;
            case "lunch":
                intent = new Intent(MainActivity.this, LunchActivity.class);
                break;
            case "school":
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        getString(R.string.school)
                ));
                break;
            case "calendar":
                String uri = getString(R.string.calendar)
                        + new TimeManager().setFormat("yyyyMM").getTime();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        uri
                ));

                Log.d(TAG, "uri: " + uri);
                break;
            case "start":
                logout();
                intent = new Intent(MainActivity.this, StartActivity.class);
                break;
            case "start_init":
                intent = new Intent(MainActivity.this, StartActivity.class);
                break;
            case "popup":
                intent = new Intent(MainActivity.this, PopupActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void checkAdmin() {
        Log.d(TAG, "checkAdmin()");

        String id = admin.getID();
        String pw = admin.getPW();

        Log.d(TAG, "id: " + id
                    + ", pw: " + pw);

        FirebaseManager.ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String user_id = dataSnapshot.getKey();
                    Log.d(TAG, "user_id: " + user_id);
                    if (user_id.equals(id)) {
                        String user_pw = dataSnapshot.getValue().toString();
                        Log.d(TAG, "user_pw: " + user_pw);
                        if (user_pw.equals(pw)) {
                            Log.d(TAG, "Admin: true");
                        }
                        return;
                    }
                }

                Log.d(TAG, "Admin: false");
                showToast("????????? ????????? ?????????????????????.\n?????? ?????????????????????.");
                sendIntent("start");
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "The read failed: " + error.getCode());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void logout() {
        Log.d(TAG, "logout()");

        FirebaseManager.removeStudent();
        SharedPreferenceUtil.removeStudent();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        try {
            initVideo();
        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        if (player != null && player.isPlaying()) player.stop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        if (player != null) player.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        // Disable Back Button
        //super.onBackPressed();

        sendIntent("popup");
    }

    @Override
    public Intent getIntent() {
        Log.d(TAG, "getIntent()");

        Intent intent = super.getIntent();

        boolean exit = intent.getBooleanExtra("exit", false);
        Log.d(TAG, "exit: " + exit);
        if (exit) this.finish();

        if (intent.getBooleanExtra("FMService", false)) {
            Log.d(TAG, "soundPlayer Stopped");
            FirebaseMessagingService.soundPlayer.stop();
        }

        return intent;
    }

    private void showToast(String data) {
        Log.d(TAG, "showToast(" + data + ")");
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(String data) {
        Log.d(TAG, "showSnackbar(" + data + ")");
        final Snackbar snackbar = Snackbar.make(view, data, Snackbar.LENGTH_SHORT);
        snackbar.setAction("??????", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        }).show();
    }
}