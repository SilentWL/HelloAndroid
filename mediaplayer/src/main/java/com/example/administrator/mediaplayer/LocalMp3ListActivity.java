package com.example.administrator.mediaplayer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mp3download.FileUtils;
import mp3parser.Mp3Info;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class LocalMp3ListActivity extends ListFragment {
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
    public static LocalMp3ListActivity newInstance() {
        LocalMp3ListActivity fragment = new LocalMp3ListActivity();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        aboutBtn = (Button)view.findViewById(R.id.LocalAboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        GetMp3Infos(mp3Infos);
        ShowMp3ListView(mp3Infos);
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mp3Infos.isEmpty() == false){
            Mp3Info mp3Info = mp3Infos.get(position);
            Intent intent = new Intent();

            intent.putExtra("mp3info", mp3Info);
            intent.setClass(getActivity(), Mp3PlayerActivity.class);
            startActivity(intent);
        }
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_local_mp3_list, container, false);
    }

    private void GetMp3Infos(List<Mp3Info> mp3Infos){
        mp3Infos.clear();
        FileUtils fileUtils = new FileUtils();
        fileUtils.GetMp3Infos("mp3", mp3Infos);
    }
    private void ShowMp3ListView(List<Mp3Info> mp3Infos){
        if (mp3Infos != null){
            List<HashMap<String ,String>> list = new ArrayList<HashMap<String ,String>>();

            for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();){
                Mp3Info mp3Info = (Mp3Info)iterator.next();
                HashMap<String, String> map = new HashMap<String , String>();
                map.put("mp3_name", mp3Info.getName());
                map.put("mp3_size", mp3Info.getSize());
                list.add(map);
                Log.i("Mp3InfoXMLParser", ", Mp3Info = " + mp3Info.toString());
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.mp3_item, new String[]{"mp3_name", "mp3_size"}, new int[]{R.id.mp3_name, R.id.mp3_size});
            setListAdapter(simpleAdapter);
        }
        else{
            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
        }
    }
}
