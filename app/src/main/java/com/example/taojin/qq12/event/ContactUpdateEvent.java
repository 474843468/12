package com.example.taojin.qq12.event;

/**
 * Created by taojin on 2016/9/10.16:04
 */
        public class ContactUpdateEvent {
            public boolean isAdded;
            public String username;

            public ContactUpdateEvent(boolean isAdded, String username) {
                this.isAdded = isAdded;
                this.username = username;
    }
}
