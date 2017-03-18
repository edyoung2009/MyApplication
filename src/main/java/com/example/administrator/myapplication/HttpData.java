package com.example.administrator.myapplication;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Administrator on 2017/3/16.
 */





    public class HttpData extends AsyncTask<String, Void, String> {
    private HttpClient httpClient;
    private HttpPost httpPost;
   // private HttpGet httpGet;
    private String url;
    private HttpResponse httpResponse;
    private HttpEntity httpEntity;
    private InputStream in;
    private HttpGetData listener;

    //�����urL
    public HttpData(String url, HttpGetData listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            httpClient = new DefaultHttpClient();
           // httpGet = new HttpGet(url);
           // httpResponse = httpClient.execute(httpGet);
            httpPost = new HttpPost(url);
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            in = httpEntity.getContent();//��ȡʵ������
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        listener.getDataUrl(result);
        super.onPostExecute(result);
    }
}
