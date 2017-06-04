package com.example.taojin.qq12.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.example.taojin.qq12.model.User;

import java.util.List;

/**
 * Created by taojin on 2016/9/10.11:35
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<User> userList;//搜索出来的结果
    private List<String> contactsList;//当前用户的好友

    public SearchAdapter(List<User> userList, List<String> contactsList) {
        this.userList = userList;
        this.contactsList = contactsList;
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }


    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sear, parent, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());
        holder.tvTime.setText(user.getCreatedAt());
        if (contactsList.contains(user.getUsername())){
            holder.btnAdd.setText("已经好友");
            holder.btnAdd.setEnabled(false);
        }else {
            holder.btnAdd.setText("添加");
            holder.btnAdd.setEnabled(true);
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (onAddFriendClickListener!=null){
                       onAddFriendClickListener.onClick(user.getUsername());
                   }
                }
            });
        }
    }

    public interface OnAddFriendClickListener {
        void onClick(String username);
    }
    private OnAddFriendClickListener onAddFriendClickListener;
    public void setOnAddFriendClickListener(OnAddFriendClickListener onAddFriendClickListener){
        this.onAddFriendClickListener = onAddFriendClickListener;
    }




    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvTime;
        Button btnAdd;

        public SearchViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            btnAdd = (Button) itemView.findViewById(R.id.btn_add);
        }
    }
}
