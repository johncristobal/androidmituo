package miituo.com.miituo.cotizar;

import android.content.Context;
import android.graphics.Typeface;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by john.cristobal on 04/05/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public static Context context;
    public static Typeface tipo;
    public static Typeface tipobold;
    public static long tiempo;
    public static ViewPager pager;

    public boolean flag;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context c, ViewPager vp) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        context = c;
        this.tiempo = tiempo;
        pager = vp;
        flag = false;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Datosauto tab1 = new Datosauto();
                return tab1;
            case 1:
                Datospersona tab2 = new Datospersona();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
