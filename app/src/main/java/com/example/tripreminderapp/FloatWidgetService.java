package com.example.tripreminderapp;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminderapp.database.note.Note;
import com.example.tripreminderapp.ui.trip_details.NotesAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FloatWidgetService extends Service implements View.OnClickListener {

    public static final String BROADCAST_ACTION = "magicbox";
    private static final int MAX_CLICK_DURATION = 200;
    NotesAdapter adapter;
    RecyclerView notesRecyclerView;
    TextView textTwo;
    private WindowManager mWindowManager;
    private View mFloatingWidget;
    private View collapsedView;
    private View expandedView;
    private String noteTwo = "text";
    private List<Note> noteList;
    private long startClickTime;

    public FloatWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        noteList = new ArrayList<>();
        noteList.clear();
        noteList = (ArrayList<Note>) intent.getSerializableExtra("notes");

        // noteTwo = intent.getExtras().getString("note2");
//        for(int i=0;i<noteList.size();i++){
//            noteTwo+=noteList.get(i).mNoteDescription;
//            noteTwo+="\n";
//            Log.d("TAG", "onStartCommand: " + noteTwo);
//        }
//        textTwo.setText(noteTwo);
        adapter.changeData(noteList);
        notesRecyclerView.setAdapter(adapter);
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, params);

        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingWidget.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingWidget.findViewById(R.id.layoutExpanded);

        mFloatingWidget.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);
        collapsedView.setOnClickListener(this);


        // textTwo = mFloatingWidget.findViewById(R.id.note_two);


        notesRecyclerView = mFloatingWidget.findViewById(R.id.notesRecyclerView);
        adapter = new NotesAdapter();


        mFloatingWidget.findViewById(R.id.layoutCollapsed).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        return false;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingWidget, params);
                        return false;
                }
                return false;
            }

        });


    }


    @Override
    public void onDestroy() {

        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpanded:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;
            case R.id.layoutCollapsed:
                //switching views
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonClose:
                //closing the widget
                stopSelf();
                break;
        }
    }

//    public void setNoteOne(String noteOne) {
//        this.noteOne = noteOne;
//    }

    public void setNoteTwo(String noteTwo) {
        this.noteTwo = noteTwo;
    }
}