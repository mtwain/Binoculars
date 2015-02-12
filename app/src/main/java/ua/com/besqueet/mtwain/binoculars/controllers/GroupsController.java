package ua.com.besqueet.mtwain.binoculars.controllers;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import ua.com.besqueet.mtwain.binoculars.events.CreateDialogSuccessEvent;
import ua.com.besqueet.mtwain.binoculars.events.LoadDialogFinishEvent;

public enum GroupsController {
    INSTANCE;

    private final String L = "GroupsController";

    public ArrayList<QBDialog>allDialogs = new ArrayList<>();

    public void loadAllDialogs(){
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setPagesLimit(100);

        QBChatService.getChatDialogs(null, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle args) {
                allDialogs = dialogs;
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BusController.INSTANCE.getBus().post(new LoadDialogFinishEvent());
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                Log.d(L,"Error getting dialogs");
            }
        });
    }

    public void createGroupDialog(ArrayList<QBUser>users){
        ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        String name="";
        for (int i=0;i<users.size();i++){
            name = name+" "+users.get(i).getLogin();
            occupantIdsList.add(users.get(i).getId());
        }
        name = name + " dialog";
        //TODO: змінити ім'я

        QBDialog dialog = new QBDialog();
        dialog.setName(name);
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);

        QBGroupChatManager groupChatManager = QBChatService.getInstance().getGroupChatManager();
        groupChatManager.createDialog(dialog, new QBEntityCallbackImpl<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BusController.INSTANCE.getBus().post(new CreateDialogSuccessEvent());
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                Log.d(L,"Error creating dialog");
            }
        });
    }

    public void deleteDialogById(String id){

        QBGroupChatManager.deleteDialog(id, new QBEntityCallbackImpl<Void>() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(List<String> errors) {

            }
        });
    }

}
