package com.jane.expressSingleManage;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class FristActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);

        Button laijian = (Button) findViewById(R.id.button_laijian);
        Button qingdian = (Button) findViewById(R.id.button_qingdian);
        Button chazhao = (Button) findViewById(R.id.button_chazhao);

        laijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startShowActivity(R.string.text_laijian);
            }
        });

        qingdian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShowActivity(R.string.text_qingdian);
            }
        });

        chazhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShowActivity(R.string.text_chazhao);
            }
        });

    }

    private void startShowActivity(int type){
        Intent i = new Intent(FristActivity.this,ShowActivity.class);
        i.putExtra("type",type);
        FristActivity.this.startActivity(i);
    }
}
