package com.cl.testapp;

import com.cl.testapp.mvc.HttpResult;
import com.cl.testapp.mvc.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * Created by Administrator on 2016-11-28.
 */

public class Test {

    public static void main(String[] args){
        System.out.println("aaa");

        String json = "{\"code\":200,\"data\":[{\"account\":\"污撩之始\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"birthDate\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":0,\"id\":6,\"lastLoginTime\":\"1481218817\",\"level\":1,\"name\":\"\",\"phone\":\"15570772073\",\"photo\":\"Dl0=.jpg\",\"recordTime\":\"2016-09-28 21:34:04.0\",\"updateTime\":\"2016-12-09 01:40:17.0\"},{\"account\":\"月下茶叶\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"birthDate\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":0,\"id\":7,\"lastLoginTime\":\"1480528497\",\"level\":1,\"name\":\"\",\"phone\":\"18616313725\",\"photo\":\"D1w=.jpg\",\"recordTime\":\"2016-09-29 18:30:33.0\",\"updateTime\":\"2016-12-08 22:12:21.0\"},{\"account\":\"\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"birthDate\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":0,\"id\":9,\"lastLoginTime\":\"1477738170\",\"level\":1,\"name\":\"\",\"phone\":\"15800602307\",\"recordTime\":\"2016-10-29 18:49:30.0\",\"updateTime\":\"2016-10-29 18:49:30.0\"},{\"account\":\"huyuxiang\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"birthDate\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":0,\"id\":10,\"lastLoginTime\":\"1478485662\",\"level\":1,\"name\":\"\",\"phone\":\"13167253562\",\"recordTime\":\"2016-11-07 10:27:29.0\",\"updateTime\":\"2016-11-07 10:27:42.0\"},{\"account\":\"醋醋子\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"birthDate\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":0,\"id\":11,\"lastLoginTime\":\"1481239283\",\"level\":1,\"name\":\"\",\"phone\":\"15957354769\",\"recordTime\":\"2016-11-08 00:02:32.0\",\"updateTime\":\"2016-12-09 07:21:23.0\"},{\"account\":\"123\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"birthDate\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":0,\"id\":12,\"lastLoginTime\":\"1478568332\",\"level\":1,\"name\":\"\",\"phone\":\"18382255444\",\"recordTime\":\"2016-11-08 07:53:00.0\",\"updateTime\":\"2016-11-08 09:25:32.0\"},{\"account\":\"此起彼伏op\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":1,\"id\":15,\"lastLoginTime\":\"1481034333\",\"level\":1,\"name\":\"\",\"photo\":\"VFVTBw==.jpg\",\"recordTime\":\"2016-12-01 01:42:57.0\",\"updateTime\":\"2016-12-06 22:25:33.0\"},{\"account\":\"宇天\",\"actionGold\":\"0\",\"actionPoint\":\"0\",\"cityId\":\"\",\"email\":\"\",\"exp\":0,\"gender\":1,\"id\":16,\"lastLoginTime\":\"1481216868\",\"level\":1,\"name\":\"\",\"photo\":\"B1QAAQ==.jpg\",\"recordTime\":\"2016-12-09 01:07:48.0\",\"updateTime\":\"2016-12-09 01:07:48.0\"}],\"msg\":\"ok\"}";
        Gson gson = new Gson();
        Type jsonType = new TypeToken<HttpResult<List<UserInfo>>>() {
        }.getType();
        HttpResult<List<UserInfo>> httpResponse = gson.fromJson(json, jsonType);
        int code = httpResponse.getCode();
        System.out.println("onResponse: " + code);
        List<UserInfo> userList = httpResponse.getData();
        for (UserInfo user: userList) {
            System.out.println(user.getAccount());
        }
    }

}
