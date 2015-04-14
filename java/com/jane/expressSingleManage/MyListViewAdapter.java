package com.jane.expressSingleManage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jane.expressSingleManage.doman.ExpressSingle;
import com.jane.expressSingleManage.utils.Constants;

import java.util.List;

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

    public void addInfo(ExpressSingle info){
        mList.add(info);
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
            holder.id = (TextView)convertView.findViewById(R.id.text_id);
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
            case Constants.CONSTANT_TYPE_DELIVERY:
                status = R.string.type_delivery;
                break;
            case Constants.CONSTANT_TYPE_SUCCESS:
                status = R.string.type_succes;
                break;
        }
        holder.id.setText(info.getId()+"");
        holder.status.setText(status);
        holder.addTime.setText(info.getCreateTime()+"");
        holder.updateTime.setText(info.getUpdateTime()+"");
        return convertView;
    }

    public final class ViewHolder{
        public TextView id;
        public TextView number;
        public TextView status;
        public TextView addTime;
        public TextView updateTime;
    }
}
