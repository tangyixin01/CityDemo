package com.happy.wdwcity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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
    private View head_view;
    private EditText et_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获得recyclerview

        main_lv = (ListView) findViewById(R.id.main_lv);
        head_view = View.inflate(MainActivity.this, R.layout.rcview_head, null);

        et_city = (EditText) findViewById(R.id.et_city);
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


        //根据输入框输入值的改变来过滤搜索
        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
                if(s!=null){
                    main_lv.removeHeaderView(head_view);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    main_lv.addHeaderView(head_view);

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

    private void filterData(String filterStr) {
        List<CityData> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = citylist;
        } else {
            mSortList.clear();
            for (CityData sortModel : citylist) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }
}
