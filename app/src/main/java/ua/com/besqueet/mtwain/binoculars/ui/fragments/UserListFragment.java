package ua.com.besqueet.mtwain.binoculars.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.binoculars.R;
import ua.com.besqueet.mtwain.binoculars.controllers.BusController;
import ua.com.besqueet.mtwain.binoculars.controllers.ContextController;
import ua.com.besqueet.mtwain.binoculars.controllers.GroupsController;
import ua.com.besqueet.mtwain.binoculars.controllers.UsersController;
import ua.com.besqueet.mtwain.binoculars.events.LoadUsersFinishEvent;


public class UserListFragment extends Fragment {

    public UserListFragment(){}

    ArrayList<QBUser> selectedUsers = new ArrayList<>();
    UserListAdapter userListAdapter;

    @InjectView(R.id.listUsers)
    ListView listUsers;

    @OnClick(R.id.btnCreate)void onBtnCreateClick(){
        btnCreateClick();
    }
    @OnClick(R.id.btnCancel)void onBtnCancelClick(){
        btnCancelClick();
    }

    @Subscribe
    public void onLoadUsersFinish(LoadUsersFinishEvent event){
        userListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusController.INSTANCE.getBus().register(this);
        userListAdapter = new UserListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_list,container,false);
        ButterKnife.inject(this, rootView);
        listUsers.setAdapter(userListAdapter);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusController.INSTANCE.getBus().unregister(this);
    }

    public void btnCreateClick(){
        if (selectedUsers.size()!=0) {
            GroupsController.INSTANCE.createGroupDialog(selectedUsers);
            getFragmentManager().popBackStack();
        }
    }

    public void btnCancelClick(){
        getFragmentManager().popBackStack();
    }

    class UserListAdapter extends BaseAdapter{



        @Override
        public int getCount() {
            return UsersController.INSTANCE.allUsers.size();
        }

        @Override
        public Object getItem(int i) {
            return UsersController.INSTANCE.allUsers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = ContextController.INSTANCE.getMainActivity().getLayoutInflater();
            View vi = view;
            ViewHolder holder = null;
            if (vi == null) {
                vi = inflater.inflate(R.layout.user_cell, viewGroup, false);
                holder = new ViewHolder();
                holder.checkBox = (CheckBox) vi.findViewById(R.id.checkBox);
                holder.textName = (TextView) vi.findViewById(R.id.textName);
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            selectedUsers.add(((QBUser) getItem(i)));
                        }else {
                            selectedUsers.remove(((QBUser) getItem(i)));
                        }
                    }
                });
                vi.setTag(holder);
            }else {
                holder = (ViewHolder) vi.getTag();
            }
            holder.textName.setText(((QBUser) getItem(i)).getLogin());
            return vi;
        }
    }

    static class ViewHolder {
        TextView textName;
        CheckBox checkBox;
    }


}
