package com.happy.wdwcity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView main_lv;
    private SideBar MySideBar;

    private MyCityAdapter adapter;//适配器
    private List<CityData> citylist;//列表集合
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获得recyclerview

        main_lv = (ListView) findViewById(R.id.main_lv);
        View head_view = View.inflate(MainActivity.this, R.layout.rcview_head, null);
        main_lv.addHeaderView(head_view);
        MySideBar = (SideBar) findViewById(R.id.MySideBar);
        initData();

        MySideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    main_lv.setSelection(position + 1);
                }
            }
        });

    }

    private void initData() {
        // 绑定控件
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        getCity();

    }
    /**
     * 获得城市列表并添加到适配器
     */
    private void getCity() {
        // 获取城市的信息
        getCityList cityList = new getCityList(characterParser);
        // 获得城市的数据列表
        citylist = cityList.filledData(getResources().getStringArray(
                R.array.date));
        // 根据a-z进行排序源数据
        Collections.sort(citylist, pinyinComparator);
        adapter = new MyCityAdapter(MainActivity.this, citylist);
        main_lv.setAdapter(adapter);
    }
}
