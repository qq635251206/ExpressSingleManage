package com.jane.expressSingleManage.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.jane.expressSingleManage.R;
import com.jane.expressSingleManage.doman.ExpressSingle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jane on 2015/4/4.
 */
public class ExportUtils {
    private static final String TAG = "ExportUtils";

    /**
     *
     * @param list
     * @param type .txt .excel
     */
    public String export(Context context,List<ExpressSingle> list,String type){
        String mType = context.getString(R.string.export_type_excel);
        String tmp = ",";
        if(context.getString(R.string.export_type_txt).equals(type)){
            mType = type;
            tmp = "                  ";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("单号").append(tmp);
        sb.append("创建时间").append(tmp);
        sb.append("更新时间").append(tmp);
        sb.append("状态");

        int len = list.size();
        for (int i=0;i<len;i++){
            sb.append("\r\n");
            ExpressSingle info = list.get(i);
            sb.append(info.getNumber()).append(tmp);
            sb.append(info.getCreateTime()).append(tmp);
            sb.append(info.getUpdateTime()).append(tmp);
            String status = "";
            switch (info.getStatus()){
                case Constants.CONSTANT_TYPE_PHONE_NOTCALL:
                    status = context.getString(R.string.type_phoneNotCall);
                    break;
                case Constants.CONSTANT_TYPE_PICKUP:
                    status = context.getString(R.string.type_pickUp);
                    break;
                case Constants.CONSTANT_TYPE_REJECTI:
                    status = context.getString(R.string.type_rejection);
                    break;
            }
            sb.append(status);
        }
//        File dir = new File(Environment.getExternalStorageDirectory()+"/Export/");
//        if(!dir.exists()){
//            dir.mkdirs();
//        }else{
//            if(dir.isFile()){
//                dir.delete();
//                dir.mkdirs();
//            }
//        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(new Date())+mType;
//        File file = new File(dir,fileName+mType);
        FileOutputStream os = null;
        try {
            os = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            os.write(sb.toString().getBytes());
            os.flush();
        } catch (IOException e) {
            Log.e(TAG,"IOException",e);
        }finally {
            try {
                if(os!=null){
                    os.close();
                    os = null;
                }
            } catch (IOException e) {
            }
        }
        return  context.getFilesDir()+"/"+fileName;
    }
}
