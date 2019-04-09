package com.warm.tech.lightsnail.session;
/**
 * Created by My on 2017/3/3.
 */

class Msg {
    static final int TYPE_RECEIVE = 1;
    static final int TYPE_SEND = 2;
    static final int TYPE_ASIDE = 3;//旁白
    String content;
    int type;

    Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
}
