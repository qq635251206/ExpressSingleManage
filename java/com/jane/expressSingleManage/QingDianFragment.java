package com.jane.expressSingleManage;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jane.expressSingleManage.db.DBExpressSingle;
import com.jane.expressSingleManage.doman.ExpressSingle;
import com.jane.expressSingleManage.utils.Common;
import com.jane.expressSingleManage.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QingDianFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QingDianFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinnerType = null;
    private ListView mListView = null;
    private MyListViewAdapter myListViewAdapter= null;
    private DBExpressSingle db = null;
    private List<ExpressSingle> mList = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QingDianFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QingDianFragment newInstance(String param1, String param2) {
        QingDianFragment fragment = new QingDianFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QingDianFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_qing_dian, container, false);
        spinnerType = (Spinner) root.findViewById(R.id.qd_spinner_type);
        String[] types = new String[]{
                getString(R.string.type_phoneNotCall),
                getString(R.string.type_rejection),
                getString(R.string.type_pickUp),
                getString(R.string.type_succes)};
        ArrayAdapter typesAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,types);
        spinnerType.setAdapter(typesAdapter);

        final EditText editNumber = (EditText) root.findViewById(R.id.qd_edit_number);
        editNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(event.getAction()==KeyEvent.ACTION_UP){
                        String number = editNumber.getText()+"";
                        if(number!=null && number.length()>0){
                            ExpressSingle info = db.findByNumber(number);
                            if(info==null){
                                Toast.makeText(getActivity(),"没有该条数据!",Toast.LENGTH_SHORT).show();
                            }else{
                                if(mList==null){
                                    mList = new ArrayList<ExpressSingle>();
                                    myListViewAdapter = new MyListViewAdapter(getActivity(),mList);
                                    mListView.setAdapter(myListViewAdapter);
                                }
                                int status = spinnerType.getSelectedItemPosition();
                                if(info.getStatus()!=status){
                                    if(status == Constants.CONSTANT_TYPE_SUCCESS){
                                        info.setUpdateTime(Common.getTime(Common.PATTERN_TIME));
                                    }
                                    info.setStatus(status);
                                    db.update(info);
                                    myListViewAdapter.addInfo(info);
                                }else{
                                    Toast.makeText(getActivity(),"已清点为该分类下!",Toast.LENGTH_SHORT).show();
                                }
                            }
                            editNumber.setText("");
                        }else{
                            Toast.makeText(getActivity(),"请输入单号!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        //list qd_list
        db = DBExpressSingle.getInstance(getActivity());
        //展示数据的ListView
        mListView = (ListView) root.findViewById(R.id.qd_list);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public List<ExpressSingle> getListViewData(){
        if(myListViewAdapter!=null){
            return myListViewAdapter.getList();
        }
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
