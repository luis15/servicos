package com.example.l156435_p107855.servicos;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private MyIntentService myIntentService;
    private TextView textView;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyIntentService.MyBinder myBinder = (MyIntentService.MyBinder) service;
            myIntentService = myBinder.getMyIntentService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText(intent.getStringExtra("message"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.EditText);
        textView = (TextView) findViewById(R.id.textView);

        IntentFilter myFilter = new IntentFilter("MyIntentServiceBroadcast");

        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver, myFilter);

    }
    public void onClick(View view){
        String text = editText.getText().toString();
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("message", text);
        startService(intent);
    }
    @Override
    public void onStop(){
        super.onStop();
        unbindService(connection);
        Log.v("My intent services", "Pausado");
    }
    @Override
    public void onStart(){
        super.onStart();
        Intent intent = new Intent(this, MyIntentService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Log.v("My intent services", "Iniciado");
    }
    public void onFastClick(View view){
        String text = editText.getText().toString();
        myIntentService.SendMessage(text);
    }
}
