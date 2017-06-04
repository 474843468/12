package com.example.taojin.qq12.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taojin.qq12.dao.ContactsOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taojin on 2016/9/9.16:11
 */
public class DBUtils {

    private static ContactsOpenHelper sContactsOpenHelper;
    private static boolean inited = false;

    public static void init(Context context){
        sContactsOpenHelper = new ContactsOpenHelper(context);
        inited = true;
    }

    public static void saveContacts(String username,List<String> contacts){
        if (!inited){
            throw  new RuntimeException("还未初始化DBUtils,请先初始化再使用");
        }
        SQLiteDatabase writableDatabase = sContactsOpenHelper.getWritableDatabase();
        //开启事务
        writableDatabase.beginTransaction();
        writableDatabase.delete("t_contact","username=?",new String[]{username});
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        for(int i =0;i<contacts.size();i++){
            contentValues.put("contact",contacts.get(i));
            writableDatabase.insert("t_contact",null,contentValues);
        }
        //提交事务
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        writableDatabase.close();
    }

    public static List<String> getContacts(String username){
        List<String> contactsList = new ArrayList<>();
        SQLiteDatabase readableDatabase = sContactsOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query("t_contact", new String[]{"contact"}, "username=?", new String[]{username}, null, null, "contact");
        while (cursor.moveToNext()){
            String contact = cursor.getString(0);
            contactsList.add(contact);
        }
        cursor.close();

        return  contactsList;
    }


}
