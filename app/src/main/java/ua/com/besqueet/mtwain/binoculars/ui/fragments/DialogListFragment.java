package ua.com.besqueet.mtwain.binoculars.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ua.com.besqueet.mtwain.binoculars.R;

public class DialogListFragment extends Fragment{

    public DialogListFragment(){}

    @InjectView(R.id.listDialogs)
    ListView listDialogs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_list,container,false);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

}
