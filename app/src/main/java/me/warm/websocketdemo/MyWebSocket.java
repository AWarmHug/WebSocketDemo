package me.warm.websocketdemo;

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

    public static final String URL="ws://echo.websocket.org";

    private static volatile MyWebSocket  mChatWebSocket=null;

    private WebSocket mWebSocket;

    private MyWebSocket() {
    }

    public static MyWebSocket getMyWebSocket(){
        if (mChatWebSocket==null){
            synchronized (MyWebSocket.class){
                if (mChatWebSocket==null) {
                    mChatWebSocket = new MyWebSocket();
                }
                mChatWebSocket.load();
            }
        }
        return mChatWebSocket;
    }

    private void load(){
        Request request=new Request.Builder()
                .url(URL)
                .build();
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();
        okHttpClient.newWebSocket(request,this);
        okHttpClient.dispatcher().executorService().shutdown();
    }


    public void sendMessage(String send){
        mWebSocket.send(send);
    }
    public void close(int code, String reason){
        mWebSocket.close(code, reason);
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        mWebSocket=webSocket;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.d(TAG, "onMessage: "+Thread.currentThread().getName()+"\n");
        Log.d(TAG, "onMessage: "+text);
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
        Log.d(TAG, "onClosed: "+Thread.currentThread().getName()+"\n");
        Log.d(TAG, "onClosed: "+code+"reason"+reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        Log.d(TAG, "onFailure: "+Thread.currentThread().getName()+"\n");
        Log.d(TAG, "onFailure: "+"Throwable:"+t.getMessage());

    }



}
