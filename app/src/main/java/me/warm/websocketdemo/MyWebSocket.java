package me.warm.websocketdemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 作者：warm
 * 时间：2017-10-10 09:14
 * 描述：
 */

public class MyWebSocket extends WebSocketListener {

    private static final String TAG = "MyWebSocket";

    public static final String URL = "ws://echo.websocket.org";

    public static final int STATE=0;
    public static final int MESSAGE=1;

    private static volatile MyWebSocket mChatWebSocket = null;
    private static OkHttpClient mOkHttpClient;

    private OnMessageListener onMessageListener;

    public void setOnMessageListener(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    private WebSocket mWebSocket;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (onMessageListener != null) {

                if (msg.arg1 == STATE) {
                    onMessageListener.onConnectState((CONNECT_STATE) msg.obj);

                    if (msg.obj==CONNECT_STATE.CONNECTED){
                        onMessageListener.onMessage("hi，连接成功");
                    }else if (msg.obj==CONNECT_STATE.DISCONNECT){
                        onMessageListener.onMessage("拜拜，我走了");
                    }else {
                        onMessageListener.onMessage("哦哦，出错了");

                    }
                } else   {
                    onMessageListener.onMessage(String.valueOf(msg.obj));

                }
            }
        }
    };

    private MyWebSocket() {
    }

    public static MyWebSocket getMyWebSocket() {
        if (mChatWebSocket == null) {
            synchronized (MyWebSocket.class) {
                if (mChatWebSocket == null) {
                    mOkHttpClient =new OkHttpClient.Builder()
                            .build();
                    mChatWebSocket = new MyWebSocket();
                }
            }
        }
        return mChatWebSocket;
    }

    public void connect() {
        Request request = new Request.Builder()
                .url(URL)
                .build();
        mOkHttpClient.newWebSocket(request, this);
    }


    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public void sendMessage(String send) {
        mWebSocket.send(send);
    }

    public void close(int code, String reason) {
        mWebSocket.close(code, reason);
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        mWebSocket = webSocket;

        Message message = Message.obtain();
        message.arg1=STATE;
        message.obj = CONNECT_STATE.CONNECTED;
        mHandler.sendMessage(message);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.d(TAG, "onMessage: " + Thread.currentThread().getName() + "\n");
        Message message = Message.obtain();
        message.arg1=MESSAGE;
        message.obj = text;
        mHandler.sendMessage(message);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        Log.d(TAG, "onClosed: " + Thread.currentThread().getName() + "\n");

        Message message = Message.obtain();
        message.arg1=STATE;
        message.obj = CONNECT_STATE.DISCONNECT;;
        mHandler.sendMessage(message);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        Log.d(TAG, "onFailure: " + Thread.currentThread().getName() + "\n");

        Message message = Message.obtain();
        message.arg1=STATE;
        message.obj = CONNECT_STATE.FAILURE;;
        mHandler.sendMessage(message);
    }

    public interface OnMessageListener {

        void onConnectState(CONNECT_STATE CONNECTState);
        void onMessage(String msg);
    }



    public enum CONNECT_STATE {
        CONNECTED,
        DISCONNECT,
        FAILURE
    }


}
