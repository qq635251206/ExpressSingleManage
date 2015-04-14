package com.jane.expressSingleManage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jane.expressSingleManage.db.DBExpressSingle;
import com.jane.expressSingleManage.doman.ExpressSingle;
import com.jane.expressSingleManage.utils.Constants;
import com.jane.expressSingleManage.utils.ExportUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jane on 2015/4/4.
 */
public class ExpressSingleManageActivity extends Activity {
    private static final String TAG = "ExpressSingleManageActivity";
    private ListView mListView = null;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private MyListViewAdapter myListViewAdapter = null;
    private Spinner spinnerType = null;
    private EditText editNumber = null;

    private DBExpressSingle db = null;
    private Button export = null;

    private final int EXPORT_OK = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case EXPORT_OK:
                    Toast.makeText(ExpressSingleManageActivity.this,"文件已导出到"+msg.obj,Toast.LENGTH_LONG).show();
                    export.setText("导出");
                    export.setEnabled(true);
                    TextView filePath = (TextView)findViewById(R.id.text_filePath);
                    filePath.setText("文件已导出到："+msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add);

        db = DBExpressSingle.getInstance(this);
        editNumber = (EditText) findViewById(R.id.eidt_number);

        //下拉框
        spinnerType = (Spinner) findViewById(R.id.spinner_type);
        String[] types = new String[]{
                getString(R.string.type_phoneNotCall),
                getString(R.string.type_rejection),
                getString(R.string.type_pickUp)};
        ArrayAdapter typesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,types);
        spinnerType.setAdapter(typesAdapter);

        //展示数据的ListView
        mListView = (ListView) findViewById(R.id.list);
        final List<ExpressSingle> list = getData();
//        if(list!=null && list.size()>0){
            myListViewAdapter = new MyListViewAdapter(this,list);
            mListView.setAdapter(myListViewAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ExpressSingle item = (ExpressSingle) myListViewAdapter.getItem(position);
                    updateView(item);
                }
            });
//        }
        //导出类型
        final Spinner spinnerExportType = (Spinner) findViewById(R.id.spinner_export_type);
        String[] exportTypes = new String[]{
                getString(R.string.export_type_excel),
                getString(R.string.export_type_txt)};
        final ArrayAdapter exportTypesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,exportTypes);
        spinnerExportType.setAdapter(exportTypesAdapter);
        //添加
        Button add = (Button) findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editNumber.getText()+"";
                if(number!=null && number.length()>0){
                    ExpressSingle info = db.findByNumber(number);
                    if(info != null){
                        Toast.makeText(ExpressSingleManageActivity.this,"数据已存在，您可以修改它的状态！",Toast.LENGTH_SHORT).show();
                    }else {
                        info = new ExpressSingle();
                        info.setNumber(number);
                        String time = getTime();
                        info.setCreateTime(time);
                        info.setUpdateTime(time);
                        info.setStatus(spinnerType.getSelectedItemPosition());
                        db.insert(info);
                        myListViewAdapter.setList(getData());
                    }
                }else{
                    Toast.makeText(ExpressSingleManageActivity.this,"请输入单号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //修改
        Button update = (Button) findViewById(R.id.button_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editNumber.getText()+"";
                if(number!=null && number.length()>0){
                    ExpressSingle info = db.findByNumber(number);
                    if(info == null){
                        Toast.makeText(ExpressSingleManageActivity.this,"没有这条数据，请先添加!",Toast.LENGTH_SHORT).show();
                    }else{
                        info.setUpdateTime(getTime());
                        info.setStatus(spinnerType.getSelectedItemPosition());
                        db.update(info);
                        myListViewAdapter.setList(getData());
                    }
                }else{
                    Toast.makeText(ExpressSingleManageActivity.this,"请输入单号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //查询
        Button find = (Button) findViewById(R.id.button_find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editNumber.getText()+"";
                if(number==null || number.length()==0){
                    int statusType = spinnerType.getSelectedItemPosition();
                    List<ExpressSingle> listTmp = db.findBy(DBExpressSingle.STATUS,statusType+"");
                    if(listTmp==null || listTmp.size()==0){
                        Toast.makeText(ExpressSingleManageActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
                    }else{
                        myListViewAdapter.setList(listTmp);
                    }
                }else if("0".equals(number)){
                    //查询全部
                    myListViewAdapter.setList(getData());
                }else if("1".equals(number)){
                    //查询今天的
                    List<ExpressSingle> listTmp = db.findBy(DBExpressSingle.CREATETIME, getDate());
                    if(listTmp==null || listTmp.size()==0){
                        Toast.makeText(ExpressSingleManageActivity.this,"今天还没有新数据",Toast.LENGTH_SHORT).show();
                    }else{
                        myListViewAdapter.setList(listTmp);
                    }
                }else{
                    List<ExpressSingle> listTmp = db.findBy(DBExpressSingle.NUMBER, number);
                    if(listTmp==null || listTmp.size()==0){
                        Toast.makeText(ExpressSingleManageActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
                    }else{
                        myListViewAdapter.setList(listTmp);
                    }
                }
            }
        });

        //导出
        export = (Button) findViewById(R.id.button_export);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int exportType = spinnerExportType.getSelectedItemPosition();
                String typeTmp = getString(R.string.export_type_excel);
                switch (exportType){
                    case 0:
                        typeTmp = getString(R.string.export_type_excel);
                        break;
                    case 1:
                        typeTmp = getString(R.string.export_type_txt);
                        break;
                }
                final String type = typeTmp;
                export.setText("导出中...");
                export.setEnabled(false);
                new Thread(){
                    @Override
                    public void run() {
                        String filePath = new ExportUtils().export(ExpressSingleManageActivity.this,myListViewAdapter.getList(), type);
                        Message msg = new Message();
                        msg.what = EXPORT_OK;
                        msg.obj = filePath;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    public void updateView(ExpressSingle info){
        editNumber.setText(info.getNumber()+"");
        spinnerType.setSelection(info.getStatus());
    }

    public List<ExpressSingle> getData(){
        List<ExpressSingle> list = new ArrayList<ExpressSingle>();
//        for (int i=0;i<50;i++){
//            ExpressSingle info = new ExpressSingle();
//            info.setNumber(100000+i);
//            info.setCreateTime(10000000+i);
//            info.setUpdateTime(1000000000+i);
//            info.setStatus(2);
//            list.add(info);
//        }
        list = db.findAll();
        return list;
    }

    public String getTime(){
        String time = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        time = format.format(new Date());
        return time;
    }

    public String getDate(){
        String time = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        time = format.format(new Date());
        return time;
    }

    class MyListViewAdapter extends BaseAdapter {
        private List<ExpressSingle> mList = null;
        private LayoutInflater mInflater = null;

        public MyListViewAdapter(){

        }

        public MyListViewAdapter(Context context,List<ExpressSingle> list){
            mList = list;
            mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setList(List<ExpressSingle> list){
            mList = list;
            this.notifyDataSetChanged();
        }

        public List<ExpressSingle> getList(){return mList;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ExpressSingle info = mList.get(position);
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView =  mInflater.inflate(R.layout.list_item, null);
                holder.number = (TextView) convertView.findViewById(R.id.text_number);
                holder.status = (TextView) convertView.findViewById(R.id.text_status);
                holder.addTime = (TextView) convertView.findViewById(R.id.text_addTime);
                holder.updateTime = (TextView) convertView.findViewById(R.id.text_updateTime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.number.setText(info.getNumber()+"");
            int status = 0;
            switch (info.getStatus()){
                case Constants.CONSTANT_TYPE_PHONE_NOTCALL:
                    status = R.string.type_phoneNotCall;
                    break;
                case Constants.CONSTANT_TYPE_PICKUP:
                    status = R.string.type_pickUp;
                    break;
                case Constants.CONSTANT_TYPE_REJECTI:
                    status = R.string.type_rejection;
                    break;
            }
            holder.status.setText(status);
            holder.addTime.setText(info.getCreateTime()+"");
            holder.updateTime.setText(info.getUpdateTime()+"");
            return convertView;
        }
    }

    public final class ViewHolder{
        public TextView number;
        public TextView status;
        public TextView addTime;
        public TextView updateTime;
    }
}
