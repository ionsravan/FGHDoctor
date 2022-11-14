package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambitious.fghdoctor.Fragments.AllDoctorListFragment;
import com.ambitious.fghdoctor.Fragments.TopDoctorListFragment;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewAppointmentCategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private TabLayout tabs;
    private ViewPager viewpager;
    private TextView tv_Head;
    private ImageView iv_Bck;
    public String wallet = "", donated = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main_appointment_category_list);
        finds();

        if (getIntent().getExtras() != null) {
            wallet = getIntent().getStringExtra("wallet");
            donated = getIntent().getStringExtra("donated");
        }

        tv_Head.setText(getIntent().getStringExtra("head"));

        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void finds() {

        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);
        tv_Head = findViewById(R.id.tv_Head);
        iv_Bck = findViewById(R.id.iv_Bck);

        iv_Bck.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllDoctorListFragment(), "All Doctors");
        adapter.addFragment(new TopDoctorListFragment(), "Top Doctors");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

        }

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  Animatoo.animateCard(mContext);
    }
}