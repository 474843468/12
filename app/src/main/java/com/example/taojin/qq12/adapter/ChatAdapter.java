package com.example.taojin.qq12.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by taojin on 2016/9/12.10:37
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<EMMessage> emMessageList;

    public ChatAdapter(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }


    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = emMessageList.get(position);
        return emMessage.direct() == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return emMessageList == null ? 0 : emMessageList.size();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_receive, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_send, parent, false);
        }
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        EMMessage emMessage = emMessageList.get(position);
        long msgTime = emMessage.getMsgTime();
        if (position==0){
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        }else{
            EMMessage preMsg = emMessageList.get(position - 1);
            long preMsgMsgTime = preMsg.getMsgTime();

            if (DateUtils.isCloseEnough(msgTime,preMsgMsgTime)){
                holder.tvTime.setVisibility(View.GONE);
            }else {
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
            }
        }
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody){
            EMTextMessageBody textMessageBody = (EMTextMessageBody) body;
            holder.tvMsg.setText(textMessageBody.getMessage());
        }else {
            holder.tvMsg.setText("接收到非文本类型消息"+body.toString());
        }

        int itemViewType = getItemViewType(position);
        if (itemViewType==1){//如果是发送的消息
            //判断当前消息的状态
            switch (emMessage.status()){
                case INPROGRESS:
                    holder.ivMsgState.setVisibility(View.VISIBLE);
                    holder.ivMsgState.setImageResource(R.drawable.msg_sending_anim);
                    AnimationDrawable animationDrawable = (AnimationDrawable) holder.ivMsgState.getDrawable();
                    animationDrawable.start();
                    break;
                case SUCCESS:
                    holder.ivMsgState.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.ivMsgState.setVisibility(View.VISIBLE);
                    holder.ivMsgState.setImageResource(R.mipmap.msg_error);
                    break;
            }
        }


    }


    class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        TextView tvMsg;
        ImageView ivMsgState;

        public ChatViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            ivMsgState = (ImageView) itemView.findViewById(R.id.iv_msg_state);
        }
    }
}
