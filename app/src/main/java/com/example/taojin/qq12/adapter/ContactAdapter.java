package com.example.taojin.qq12.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.example.taojin.qq12.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taojin on 2016/9/9.16:51
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements SectionIndexer{

    private SparseIntArray sparseIntArray = new SparseIntArray();

    private List<String> data;

    public List<String> getData(){
        return  data;
    }

    public ContactAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final String username = data.get(position);
        String inital = StringUtils.getInital(username);
        holder.tvUsername.setText(username);
        holder.tvSection.setText(inital);
        if (position==0){
            holder.tvSection.setVisibility(View.VISIBLE);
        }else {
            String preUsername = data.get(position - 1);
            String preInital = StringUtils.getInital(preUsername);
            if (inital.equals(preInital)){
                holder.tvSection.setVisibility(View.GONE);
            }else {
                holder.tvSection.setVisibility(View.VISIBLE);
            }
        }
        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(username);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(username);
                    return true;
                }
            });
        }
    }

    @Override
    public String[] getSections() {
        ArrayList<String> sections = new ArrayList<>();
        //对数据进行一个分区
        for(int i = 0;i<data.size();i++){
            String inital = StringUtils.getInital(data.get(i));
            if (!sections.contains(inital)){
                sections.add(inital);
                sparseIntArray.put(sections.size()-1,i);
            }
        }
        return sections.toArray(new String[sections.size()]);
    }

    //传递一个分区索引，返回一个该分区下第一个条目在所在ListView中的索引
    @Override
    public int getPositionForSection(int sectionIndex) {//1 - >2
        return  sparseIntArray.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {//4-->1
        return 0;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvSection;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvSection = (TextView) itemView.findViewById(R.id.tv_section);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String username);
        void onItemLongClick(String username);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
