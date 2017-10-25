package com.line.sidebardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.line.sidebardemo.bean.Contact;
import com.line.sidebardemo.widget.sidebar.SideBar;
import com.line.sidebardemo.widget.sidebar.SideBarSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    SideBar sideBar;

    List<Contact> contacts;

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initContact();
        recyclerView = (RecyclerView) findViewById(R.id.rv_contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        sideBar = (SideBar) findViewById(R.id.side_bar);
        sideBar.setSelectedTextColor(getResources().getColor(R.color.colorAccent));
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 得到匹配字母的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recyclerView.scrollToPosition(position);
                }
            }
        });

    }

    private void initContact(){
        contacts = new ArrayList<>();
        for (char i = 'A'; i < 'Z'; i++) {
            for (int j = 0; j < 3; j++) {
                contacts.add(new Contact(String.valueOf(i) + Math.random()));
            }
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvSection;
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSection = itemView.findViewById(R.id.tv_section);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements SectionIndexer {

        public MyAdapter() {
            handleData();
        }

        /**
         * 数据变化后，需要调用该方法进行数据处理
         */
        private void handleData() {
            SideBarSupport.filledData(contacts);
            // 排序
            SideBarSupport.PinyinComparator pinyinComparator = new SideBarSupport.PinyinComparator();
            Collections.sort(contacts, pinyinComparator);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_contact, parent, false);
            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.tvSection.setVisibility(View.VISIBLE);
                holder.tvSection.setText(contacts.get(position).getSection());
            } else {
                holder.tvSection.setVisibility(View.GONE);
            }

            holder.tvName.setText(contacts.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }



        @Override
        public Object[] getSections() {
            return new Object[0];
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            if (contacts != null && contacts.size() > 0) {
                for (int i = 0; i < contacts.size(); i++) {
                    String sortStr = contacts.get(i).getSection();
                    if (sortStr != null) {
                        char firstChar = sortStr.toUpperCase().charAt(0);
                        if (firstChar == sectionIndex) {
                            return i;
                        }
                    }
                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            int ch = -1;
            if (position >= 0 && position < contacts.size() && contacts.size() > 0) {
                String section = contacts.get(position).getSection();
                if (section != null) {
                    ch = section.charAt(0);
                }
            }
            return ch;
        }
    }



}
