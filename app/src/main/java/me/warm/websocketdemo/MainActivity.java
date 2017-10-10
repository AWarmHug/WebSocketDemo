package me.warm.websocketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mStart;
    private Button mClose;
    private Button mSend;
    private TextView mContent;
    private MyWebSocket mWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStart = (Button) findViewById(R.id.start);
        mClose = (Button) findViewById(R.id.close);
        mSend= (Button) findViewById(R.id.send);
        mContent = (TextView) findViewById(R.id.content);
        mStart.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mSend.setOnClickListener(this);


//        okHttpClient.dispatcher().executorService().shutdown();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                mWebSocket = MyWebSocket.getMyWebSocket();
                break;
            case R.id.send:
                if (mWebSocket==null){
                    Toast.makeText(this, "请先连接", Toast.LENGTH_SHORT).show();
                }else {
                    Random random=new Random();
                    mWebSocket.sendMessage("大家好，我今年"+random.nextInt(10)+"岁了");
                }
                break;
            case R.id.close:
                mWebSocket.close(1000, "我走了");
                break;
        }
    }
}
