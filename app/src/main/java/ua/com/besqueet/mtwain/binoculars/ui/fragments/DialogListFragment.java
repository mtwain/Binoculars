package ua.com.besqueet.mtwain.binoculars.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.chat.model.QBDialog;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.binoculars.R;
import ua.com.besqueet.mtwain.binoculars.controllers.AuthController;
import ua.com.besqueet.mtwain.binoculars.controllers.BusController;
import ua.com.besqueet.mtwain.binoculars.controllers.ContextController;
import ua.com.besqueet.mtwain.binoculars.controllers.GroupsController;
import ua.com.besqueet.mtwain.binoculars.controllers.UsersController;
import ua.com.besqueet.mtwain.binoculars.events.CreateDialogSuccessEvent;
import ua.com.besqueet.mtwain.binoculars.events.LoadDialogFinishEvent;
import ua.com.besqueet.mtwain.binoculars.events.auth.LoginFailEvent;
import ua.com.besqueet.mtwain.binoculars.events.auth.LoginSuccessEvent;
import ua.com.besqueet.mtwain.binoculars.events.auth.SignOutSuccessEvent;

public class DialogListFragment extends Fragment{

    public DialogListFragment(){}

    @InjectView(R.id.listDialogs)
    ListView listDialogs;
    @OnClick(R.id.btnSignOut) void onBtnSignOutClick(){
        signOutBtnClick();
    }
    @OnClick(R.id.btnCreateDialog) void onBtnCreateDialogClick(){
        ContextController.INSTANCE.getMainActivity().showFragmentAbove(new UserListFragment());
    }
    DialogListAdapter dialogListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusController.INSTANCE.getBus().register(this);
        dialogListAdapter = new DialogListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_list,container,false);
        ButterKnife.inject(this, rootView);
        listDialogs.setAdapter(dialogListAdapter);
        return rootView;
    }

    @Subscribe
    public void onLoginFail(LoginFailEvent event){
        Toast.makeText(getActivity(),"Wrong login or password",Toast.LENGTH_SHORT).show();
        ContextController.INSTANCE.getMainActivity().showFragment(new SignInFragment());
    }

    @Subscribe
    public void onLoginSuccess(LoginSuccessEvent event){
        Toast.makeText(getActivity(),"Connected",Toast.LENGTH_SHORT).show();
        GroupsController.INSTANCE.loadAllDialogs();
        UsersController.INSTANCE.loadAllUsers();
        dialogListAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onSignOutSuccess(SignOutSuccessEvent event){
        ContextController.INSTANCE.getMainActivity().showFragment(new SignInFragment());
    }

    @Subscribe
    public void onCreateDialogSuccess(CreateDialogSuccessEvent event){
        GroupsController.INSTANCE.loadAllDialogs();
    }

    @Subscribe
    public void onLoadDialogsFinish(LoadDialogFinishEvent event){
        dialogListAdapter.notifyDataSetChanged();
    }

    private void signOutBtnClick() {
        AuthController.INSTANCE.signOut();
    }

    private void createDialogBtnClick(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusController.INSTANCE.getBus().unregister(this);
        AuthController.INSTANCE.stopAutoSendPresence();
    }


    class DialogListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return GroupsController.INSTANCE.allDialogs.size();
        }

        @Override
        public Object getItem(int i) {
            return GroupsController.INSTANCE.allDialogs.get(i);
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
                vi = inflater.inflate(R.layout.dialog_cell, viewGroup, false);
                holder = new ViewHolder();
                holder.textName = (TextView) vi.findViewById(R.id.textName);
                vi.setTag(holder);
            }else {
                holder = (ViewHolder) vi.getTag();
            }
            holder.textName.setText(((QBDialog) getItem(i)).getName());
            return vi;
        }
    }

    static class ViewHolder {
        TextView textName;
    }

}
