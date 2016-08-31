package com.example.administrator.mediaplayer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import com.example.administrator.mediaplayer.service.DownloadService;
import mp3download.HttpDownload;
import mp3parser.Mp3Info;
import xml.Mp3ListContenthandler;

public class Mp3ListActivity extends android.support.v4.app.ListFragment {
    private Button updateBtn = null;
    private Button aboutBtn = null;
    private List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    public static Mp3ListActivity newInstance() {
        Mp3ListActivity fragment = new Mp3ListActivity();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1){

        }else if (item.getItemId() == 2){

        }
        return super.onOptionsItemSelected(item);
    }
    private void StartDownLoadThread(){
        mp3Infos.clear();
        new Thread(DownloadTask).start();
        Log.i("DownloadTask", "Start!");
    }
    private Runnable DownloadTask = new Runnable() {
        @Override
        public void run() {
            HttpDownload httpDownloader = new HttpDownload();
            String result = httpDownloader.DownLoad("http://192.168.199.18:8080/mp3/resources.xml");
            System.out.println(result);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("DownloadString", result);
            msg.setData(data);
            DownloadHandler.sendMessage(msg);
        }
    };

    private Handler DownloadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            String result = data.getString("DownloadString");
            Log.i("DownloadHander", "result = " + result );
            if (result == null){
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
            }
            else {
                mp3Infos = Mp3InfoXMLParser(result);

                if (mp3Infos == null) {
                    Toast.makeText(getActivity(), "解析失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "解析成功", Toast.LENGTH_SHORT).show();
                    ShowMp3ListView(mp3Infos);
                }
            }
            super.handleMessage(msg);
        }
    };

    private void ShowMp3ListView(List<Mp3Info> mp3Infos){
        if (mp3Infos != null){
            List<HashMap<String ,String>> list = new ArrayList<HashMap<String ,String>>();

            for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();){
                Mp3Info mp3Info = (Mp3Info)iterator.next();
                HashMap<String, String> map = new HashMap<String , String>();
                map.put("mp3_name", mp3Info.getName());
                map.put("mp3_size", mp3Info.getSize());
                list.add(map);
                Log.i("ShowMp3ListView", ", Mp3Info = " + mp3Info.toString());
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.mp3_item, new String[]{"mp3_name", "mp3_size"}, new int[]{R.id.mp3_name, R.id.mp3_size});
            setListAdapter(simpleAdapter);
        }
        else{
            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Mp3Info> Mp3InfoXMLParser(String xmlStr){
        SAXParserFactory mp3InfoFactory = SAXParserFactory.newInstance();
        try{
            XMLReader mp3Reader = mp3InfoFactory.newSAXParser().getXMLReader();

            Mp3ListContenthandler mp3ListContenthandler = new Mp3ListContenthandler(mp3Infos);
            mp3Reader.setContentHandler(mp3ListContenthandler);
            mp3Reader.parse(new InputSource(new StringReader(xmlStr)));

            return mp3Infos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mp3Infos.isEmpty()) {
            StartDownLoadThread();
        }

        updateBtn = (Button)view.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDownLoadThread();
            }
        });
        aboutBtn = (Button)view.findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_remote_mp3_list, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Mp3Info mp3Info = mp3Infos.get(position);

        Log.i("onListItemClick","mp3Info:" + mp3Info.toString());
        Intent intent = new Intent();
        intent.putExtra("mp3Info", mp3Info);
        intent.setClass(getActivity(), DownloadService.class);

        getActivity().getApplicationContext().startService(intent);
        super.onListItemClick(l, v, position, id);
    }
}
