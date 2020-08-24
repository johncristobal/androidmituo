package miituo.com.miituo.tuto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import miituo.com.miituo.R;
import miituo.com.miituo.activities.SyncActivity;
import miituo.com.miituo.threats.GetPDFSync;
import miituo.com.miituo.utils.LogHelper;
import miituo.com.miituo.utils.SimpleCallBack;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnNext;
    private PrefManager prefManager;
    public VideoView video;

    public VideoView mVideoView1,mVideoView2,mVideoView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_view_pager_demo);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnNext = (Button) findViewById(R.id.btn_next);
        video=(VideoView)findViewById(R.id.videoViewT);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
            R.layout.welcome_slide1,
            R.layout.welcome_slide2,
            R.layout.welcome_slide3};
        //R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.log(TutorialActivity.this,LogHelper.user_interaction,"TutorialActivity.btnNext","","", "","","");
                launchHomeScreen();
            }
        });
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        //startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        startActivity(new Intent(TutorialActivity.this, SyncActivity.class));
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
        //finish();
    }

    //viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            LogHelper.log(TutorialActivity.this,LogHelper.user_interaction,"TutorialActivity.pager","position: "+position,"", "","","");
            String uri = "android.resource://" + getPackageName() + "/";
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                btnNext.setVisibility(View.VISIBLE);
            } else {
                btnNext.setVisibility(View.INVISIBLE);
            }

            if(position == 0){
                video.pause();
                video.setVideoPath(uri+R.raw.miituoonb12);
                video.setZOrderOnTop(true);
                video.requestFocus();
                video.start();
            }else if (position == 1){
                video.pause();
                video.setVideoPath(uri+R.raw.miituoonb2);
                video.setZOrderOnTop(true);
                video.requestFocus();
                video.start();
            }else if (position == 2){
                video.pause();
                video.setVideoPath(uri+R.raw.onb3);
                video.setZOrderOnTop(true);
                video.requestFocus();
                video.start();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;
        String resource;
        Context c;
        String flag;

        public MyViewPagerAdapter(Context c) {
            this.c=c;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater = LayoutInflater.from(c);
//            View view = layoutInflater.inflate(layouts[position], container, false);
            ViewGroup view = (ViewGroup) layoutInflater.inflate(layouts[position], container, false);

            try {
//                if (position == 0) {
//                    mVideoView1 = (VideoView) view.findViewById(R.id.videoView2);
//                    String uri = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12a;
//                    if (mVideoView1 != null) {
//                        mVideoView1.setVideoPath(uri);
//                        mVideoView1.setZOrderOnTop(true);
//                        mVideoView1.requestFocus();
//                        mVideoView1.start();
//                    }
//                } else if (position == 1) {
//                    mVideoView2 = (VideoView) view.findViewById(R.id.videoView3);
//                    String uri = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb2b;
//                    if (mVideoView2 != null) {
//                        mVideoView2.setVideoPath(uri);
//                        mVideoView2.setZOrderOnTop(true);
//                        mVideoView2.requestFocus();
//                    }
//                } else if (position == 2) {
//                    mVideoView3 = (VideoView) view.findViewById(R.id.videoView4);
//                    String uri = "android.resource://" + getPackageName() + "/" + R.raw.onb32;
//                    if (mVideoView3 != null) {
//                        mVideoView3.setVideoPath(uri);
//                        mVideoView3.setZOrderOnTop(true);
//                        mVideoView3.requestFocus();
//                    }
//                }
                container.addView(view);
            }catch(Exception e){
                e.printStackTrace();
            }
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}