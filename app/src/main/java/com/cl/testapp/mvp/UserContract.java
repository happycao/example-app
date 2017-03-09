package com.cl.testapp.mvp;

import android.content.Context;

import com.cl.testapp.model.User;

import java.util.List;

/**
 * View and Presenter
 * Created by CL on 2017-03-01.
 */
public interface UserContract {

    interface View extends BaseView<Presenter> {

        void showUsers(List<User> users);

        void showAddUser();

        void showNoUsers();

        void showSuccess();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadUsers(Context context);
    }
}
