package edu.hubu.yousee;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
//import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private LinearLayout mTabYouSee;
    private LinearLayout mTabFine;
    private  LinearLayout mTabConnect;
    private LinearLayout mTabSetting;

    private ImageButton mImageYouSee;
    private  ImageButton mImageFine;
    private ImageButton mImageConnect;
    private ImageButton mImageSetting;

    private Fragment mTab0 = new youseeFragment();
    private Fragment mTab1 = new connectFragment();
    private Fragment mTab2 = new fineFragment();
    private Fragment mTab3 = new settingFragment();
    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();
        initevxnt();
        SelectFragment(0);
    }

    private void initFragment() {
        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.id_content,mTab0);
        transaction.add(R.id.id_content,mTab1);
        transaction.add(R.id.id_content,mTab2);
        transaction.add(R.id.id_content,mTab3);
        transaction.commit();
    }

    private void initView(){
        mTabYouSee = (LinearLayout) findViewById(R.id.id_tab_yousee);
        mTabConnect = (LinearLayout) findViewById(R.id.id_tab_connect);
        mTabFine = (LinearLayout) findViewById(R.id.id_tab_fine);
        mTabSetting = (LinearLayout) findViewById(R.id.id_tab_setting);

        mImageYouSee = (ImageButton) findViewById(R.id.id_tab_yousee_image);
        mImageConnect = (ImageButton) findViewById(R.id.id_tab_connect_image);
        mImageFine = (ImageButton) findViewById(R.id.id_tab_fine_image);
        mImageSetting = (ImageButton) findViewById(R.id.id_tab_setting_image);
    }

    private  void initevxnt(){
        mTabYouSee.setOnClickListener(this);
        mTabConnect.setOnClickListener(this);
        mTabFine.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);
    }
    private void SelectFragment(int i){
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        //
        //
        switch (i){
            case 0:
                //log.d("setSelect","2");yousee
                transaction.show(mTab0);
                mImageYouSee.setImageResource(R.drawable.eyeopen_);
                break;
            case 1:
                //log.d("setSelect","3");connect
                transaction.show(mTab1);
                mImageConnect.setImageResource(R.drawable.connect);
                break;
            case 2:
                //log.d("setSelect","2");fine
                transaction.show(mTab2);
                mImageFine.setImageResource(R.drawable.music_);
                break;
            case 3:
                //log.d("setSelect","3");setting
                transaction.show(mTab3);
                mImageSetting.setImageResource(R.drawable.setting_);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        transaction.hide(mTab0);
        transaction.hide(mTab1);
        transaction.hide(mTab2);
        transaction.hide(mTab3);
    }

    @Override
    public void onClick(View v) {
        resetimg();
        switch (v.getId()){
            case R.id.id_tab_yousee:
                SelectFragment(0);
                break;

            case R.id.id_tab_connect:
                SelectFragment(1);
                break;

            case R.id.id_tab_fine:
                SelectFragment(2);
                break;

            case R.id.id_tab_setting:
                SelectFragment(3);
                break;

        }
    }

    public void resetimg(){
        mImageYouSee.setImageResource(R.drawable.eyeopen);
        mImageConnect.setImageResource(R.drawable.connect_);
        mImageFine.setImageResource(R.drawable.music);
        mImageSetting.setImageResource(R.drawable.setting);
    }

}