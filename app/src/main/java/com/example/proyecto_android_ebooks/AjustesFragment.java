package com.example.proyecto_android_ebooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjustesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjustesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AjustesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjustesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AjustesFragment newInstance(String param1, String param2) {
        AjustesFragment fragment = new AjustesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View v = inflater.inflate(R.layout.fragment_ajustes, container, false);
        final Switch s = v.findViewById(R.id.dayNight);
        final MainActivity ma  = (MainActivity)getActivity();
        SharedPreferences sp = ma.getSharedPreferences("sp", ma.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int theme  = sp.getInt("Theme", 1);
        if (theme==1){
            s.setChecked(false);
        }else{
            s.setChecked(true);
        }
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.isChecked()){
                    editor.putInt("Theme", 0);
                }else{
                    editor.putInt("Theme", 1);
                }
                editor.commit();
                ma.setDayNight();
            }
        });

        Button b = (Button) v.findViewById(R.id.bExit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                editor.putInt("Theme", 0);
                editor.commit();
            }
        });

        return v;

    }


}