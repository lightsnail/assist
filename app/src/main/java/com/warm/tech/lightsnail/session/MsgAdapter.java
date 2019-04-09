package com.warm.tech.lightsnail.session;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warm.tech.lightsnail.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by My on 2017/3/3.
 */

class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private ArrayList<Msg> mMsgList;

    MsgAdapter(ArrayList<Msg> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.session_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        holder.OnlyShow(msg.type  ,msg.content);
    }


    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_receive;
        private TextView tv_send;
        private TextView tv_aside;
        private HashMap<Integer,TextView> map = new HashMap<Integer, TextView>();
        ViewHolder(View itemView) {
            super(itemView);
            tv_receive = (TextView) itemView.findViewById(R.id.tv_receive);
            tv_send = (TextView) itemView.findViewById(R.id.tv_send);
            tv_aside = (TextView)itemView.findViewById(R.id.tv_aside);
            map.put(Msg.TYPE_RECEIVE,tv_receive);
            map.put(Msg.TYPE_SEND,tv_send);
            map.put(Msg.TYPE_ASIDE,tv_aside);
        }
        public void OnlyShow(int type, String content) {
            for (Integer key : map.keySet()) {
                TextView tv = map.get(key);
                if(type == key){
                    tv.setText(content);
                    tv.setVisibility(View.VISIBLE);
                }else{
                    tv.setVisibility(View.GONE);
                }
                //System.out.println(key);
            }
        }
    }
}
