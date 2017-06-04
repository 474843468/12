package com.example.taojin.qq12.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by taojin on 2016/9/12.16:52
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<EMConversation> emConversationList;

    public ConversationAdapter(List<EMConversation> emConversationList) {
        this.emConversationList = emConversationList;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_conversation, parent, false);

        ConversationViewHolder conversationViewHolder = new ConversationViewHolder(view);

        return conversationViewHolder;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        EMConversation conversation = emConversationList.get(position);
        final String userName = conversation.getUserName();
        holder.tvUsername.setText(userName);
        int unreadMsgCount = conversation.getUnreadMsgCount();
        if (unreadMsgCount>99){
            holder.tvUnread.setText("99+");
            holder.tvUnread.setVisibility(View.VISIBLE);
        }else if (unreadMsgCount>0){
            holder.tvUnread.setText(unreadMsgCount+"");
            holder.tvUnread.setVisibility(View.VISIBLE);
        }else {
            holder.tvUnread.setVisibility(View.GONE);
        }
        EMMessage lastMessage = conversation.getLastMessage();
        holder.tvTime.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));

        EMMessageBody body = lastMessage.getBody();
        if (body instanceof EMTextMessageBody){
            EMTextMessageBody textMessageBody = (EMTextMessageBody) body;
            holder.tvMsg.setText(textMessageBody.getMessage());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConversationItemClickListener!=null){
                    onConversationItemClickListener.onItemClick(userName);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return emConversationList == null ? 0 : emConversationList.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvMsg;
        TextView tvTime;
        TextView tvUnread;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvUnread = (TextView) itemView.findViewById(R.id.tv_unread);
        }
    }

    public interface OnConversationItemClickListener{
        void onItemClick(String username);
    }
    private OnConversationItemClickListener onConversationItemClickListener;
    public void setOnConversationItemClickListener(OnConversationItemClickListener onConversationItemClickListener){
        this.onConversationItemClickListener = onConversationItemClickListener;
    }


}
