package com.jane.expressSingleManage;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jane.expressSingleManage.db.DBExpressSingle;
import com.jane.expressSingleManage.doman.ExpressSingle;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChaZhaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChaZhaoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
     * @return A new instance of fragment ChaZhaoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChaZhaoFragment newInstance(String param1, String param2) {
        ChaZhaoFragment fragment = new ChaZhaoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChaZhaoFragment() {
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
        View root = inflater.inflate(R.layout.fragment_cha_zhao, container, false);

        db = DBExpressSingle.getInstance(getActivity());
        //展示数据的ListView
        mListView = (ListView) root.findViewById(R.id.cz_list);

        final EditText editNumber = (EditText) root.findViewById(R.id.cz_edit_number);
        editNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(event.getAction()==KeyEvent.ACTION_UP){
                        String number = editNumber.getText()+"";
                        if(number!=null && number.length()>0){
                            List<ExpressSingle> list = db.findBy(DBExpressSingle.NUMBER, number);
                            if(list!=null && list.size()>0){
                                if(mList==null){
                                    mList = list;
                                    myListViewAdapter = new MyListViewAdapter(getActivity(),mList);
                                    mListView.setAdapter(myListViewAdapter);
                                }else{
                                    myListViewAdapter.setList(list);
                                }
                            }else{
                                Toast.makeText(getActivity(),"没有查到相关数据!",Toast.LENGTH_SHORT).show();
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
