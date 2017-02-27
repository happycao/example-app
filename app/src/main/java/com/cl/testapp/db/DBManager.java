package com.cl.testapp.db;

import android.content.Context;

import com.cl.testapp.model.User;
import com.cl.testapp.model.gen.DaoMaster;
import com.cl.testapp.model.gen.DaoSession;
import com.cl.testapp.model.gen.UserDao;

import java.util.List;

/**
 * 数据库管理
 * Created by Administrator on 2017-02-24.
 */

public class DBManager {

    private static final String mDBName = "xl_db";
    private DaoMaster mDaoMaster;
    private DaoMaster.DevOpenHelper mOpenHelper;
    private DaoSession mDaoSession;
    private static DBManager mInstance;

    public DBManager(Context context){
        mOpenHelper = new DaoMaster.DevOpenHelper(context, mDBName);
        mDaoMaster = new DaoMaster(mOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static DBManager getInstance(Context context) {
        if(mInstance == null){
            synchronized (DBManager.class){
                mInstance = new DBManager(context);
            }
        }
        return mInstance;
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void closeDB(){
        if (mOpenHelper != null){
            mOpenHelper.close();
        }
    }

    /**
     * 插入用户，更多内容查看博文，http://www.jianshu.com/p/4ad196f2e299
     * 多表关联：http://www.jianshu.com/p/dbec25bd575f
     * @param user {@link User}
     * @throws Exception
     */
    public void insertUser(User user) throws Exception{
        UserDao userDao = mDaoSession.getUserDao();
        userDao.insert(user);
    }

    public List<User> getUserList(){
        UserDao userDao = mDaoSession.getUserDao();
        return userDao.queryBuilder().build().list();
    }
}
