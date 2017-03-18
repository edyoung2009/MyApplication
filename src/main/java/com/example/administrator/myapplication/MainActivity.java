package com.example.administrator.myapplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity implements HttpGetData,OnClickListener {
    private HttpData  httpdata;
    private List<ListData> list;
    private ListView lv;
    private Button  send_btn;
    private EditText sendtext;
    private String content_str;
    private TextAdapter adapter;
    private String [] welcomeArray;
    private double currenttime,oldTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initeview();
    }
    private void initeview(){
        list=new ArrayList<ListData>();
        lv=(ListView) findViewById(R.id.lv);
        send_btn=(Button) findViewById(R.id.send_btn);
        sendtext=(EditText) findViewById(R.id.senText);
        send_btn.setOnClickListener(this);
        adapter=new TextAdapter(list,this);
        lv.setAdapter(adapter);
        ListData listData = null;
        listData=new ListData(getRandomWelcomeTips(), listData.receiver,getTime());
        System.out.println("ʱ��"+listData);
        list.add(listData);

    }

    public void getDataUrl(String data) {
        // TODO Auto-generated method stub
        parseText(data);
    }
    public void parseText(String str){

        try {
            JSONObject jb=new JSONObject(str);
			/*System.out.println(jb.getString("code"));
			System.out.println(jb.getString("text"));*/
            ListData listData = null;
            listData=new ListData(jb.getString("text"),listData.receiver,getTime());
            System.out.println("ʱ��"+listData);
            list.add(listData);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void onClick(View v) {
        content_str=sendtext.getText().toString();
        sendtext.setText("");
        String dropk=content_str.replace(" ", "");
        String droph=dropk.replace("\n", "");
        ListData  listdata = null;
        listdata=new ListData(content_str,listdata.send,getTime());
        System.out.println("sfds"+listdata);
        if (TextUtils.isEmpty(content_str))
        {
            Toast.makeText(this, "您还没有填写信息呢...", Toast.LENGTH_SHORT).show();
            return;
        }
        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive())
        {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }

        list.add(listdata);
        if(list.size()>30){
            for(int i=0;i<list.size();i++){
                list.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        httpdata=(HttpData) new HttpData(
                "http://www.tuling123.com/openapi/api?key=a50fdd55f0d3409f8efa42753afa6f68&info="+droph,this).execute();

    }
    private String  getRandomWelcomeTips(){
        String welcome_tipe=null;
        welcomeArray=this.getResources().getStringArray(R.array.welcome_tips);
        int  index=(int) (Math.random()*(welcomeArray.length-1));
        welcome_tipe=welcomeArray[index];
        return welcome_tipe;
    }
    private String  getTime(){
        currenttime-=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss");
        //SimpleDateFormat format=new SimpleDateFormat("HH:mm");
        Date curdata=new Date();
        String str=format.format(curdata);
        if(currenttime - oldTime >=5*60*1000){
            oldTime=currenttime;
            return  str;
        }else{
            return "";
        }


    }
}
