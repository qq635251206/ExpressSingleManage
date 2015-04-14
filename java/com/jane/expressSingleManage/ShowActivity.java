package com.jane.expressSingleManage;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jane.expressSingleManage.doman.ExpressSingle;
import com.jane.expressSingleManage.utils.ExportUtils;

import java.util.List;


public class ShowActivity extends ActionBarActivity implements OnFragmentInteractionListener {
    private int type = 0;
    private TextView tv_file_path = null;

    private final int EXPORT_OK = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case EXPORT_OK:
                    Toast.makeText(ShowActivity.this,"文件已导出到"+msg.obj,Toast.LENGTH_LONG).show();
                    tv_file_path.setText("文件已导出到"+msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent i = getIntent();
        type = i.getIntExtra("type",0);
        setTitle(type);

        Fragment fragment = null;
        if(type == R.string.text_laijian){
            fragment = new LaiJianFragment();
        }else if(type == R.string.text_qingdian){
            fragment = new QingDianFragment();
        }else if(type == R.string.text_chazhao){
            fragment = new ChaZhaoFragment();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.show_container, fragment, type + "").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
            return true;
        }else if(id == R.id.export_excel) {
            exportFile(R.string.export_type_excel);
            return true;
        } else if (id == R.id.export_txt) {
            exportFile(R.string.export_type_txt);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportFile(final int exportType){
        Fragment fragment = getFragmentManager().findFragmentByTag(type + "");
        List<ExpressSingle> list = null;
        switch (type) {
            case R.string.text_laijian:
                LaiJianFragment ljFragment = (LaiJianFragment) fragment;
                list = ljFragment.getListViewData();
                break;
            case R.string.text_qingdian:
                QingDianFragment qdFragment = (QingDianFragment) fragment;
                list = qdFragment.getListViewData();
                break;
            case R.string.text_chazhao:
                ChaZhaoFragment czFragment = (ChaZhaoFragment) fragment;
                list = czFragment.getListViewData();
                break;
        }
        if (list == null || list.size()==0) {
            Toast.makeText(this, "没有数据!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "正在导出，请稍后...", Toast.LENGTH_SHORT).show();
            if (tv_file_path == null) {
                tv_file_path = (TextView) findViewById(R.id.show_filePath);
            }
            tv_file_path.setText("正在导出，请稍后...");
            final List<ExpressSingle> listTmp = list;
            new Thread(){
                @Override
                public void run() {
                    String filePath = new ExportUtils().export(ShowActivity.this,listTmp,getString(exportType) );
                    Message msg = new Message();
                    msg.what = EXPORT_OK;
                    msg.obj = filePath;
                    handler.sendMessage(msg);
                }
            }.start();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
