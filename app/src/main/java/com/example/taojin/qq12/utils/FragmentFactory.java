package com.example.taojin.qq12.utils;

import com.example.taojin.qq12.common.BaseFragment;
import com.example.taojin.qq12.view.ContactsFragment;
import com.example.taojin.qq12.view.ConversationFragment;
import com.example.taojin.qq12.view.PluginFragment;

/**
 * Created by taojin on 2016/9/9.11:40
 */
public class FragmentFactory {
    private static ConversationFragment sConversationFragment;
    private static ContactsFragment sContactsFragment;
    private static PluginFragment sPluginFragment;
    private static ConversationFragment getConversationFragment(){
        if (sConversationFragment==null){
            sConversationFragment = new ConversationFragment();
        }
        return  sConversationFragment;
    }
    private static ContactsFragment getContactsFragment(){
        if (sContactsFragment==null){
            sContactsFragment = new ContactsFragment();
        }
        return  sContactsFragment;
    }
    private static PluginFragment getPluginFragment(){
        if (sPluginFragment==null){
            sPluginFragment = new PluginFragment();
        }
        return  sPluginFragment;
    }

    public static BaseFragment getFragment(int index){
        switch (index){
            case 0:
                return getConversationFragment();
            case 1:
                return getContactsFragment();
            case 2:
                return getPluginFragment();
        }
        return  null;
    }

}
