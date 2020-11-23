package edu.hubu.yousee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.myviewholder>{

    private Context context;
    private List<String> mlist = new ArrayList<>();
    private int expandPosition = -1;
    private myviewholder mViewholder;

    public adapter(Context context) {
        this.context  = context;
    }

    public void setExpandCollapseDataList(List<String> list) {
        mlist = list;
        notifyDataSetChanged();
    }

    @Override
    public adapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.connectitem,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(final adapter.myviewholder viewHolder, int position) {
        viewHolder.tvTeam.setText(mlist.get(position));
        viewHolder.tvTeamChild.setText(mlist.get(position)+" phonenumber isn't assigned yet");

        final boolean isExpand = position == expandPosition;
        viewHolder.rlChild.setVisibility(isExpand ? View.VISIBLE : View.GONE);
        viewHolder.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewholder !=null){
                    mViewholder.rlChild.setVisibility(View.GONE);
                    notifyItemChanged(expandPosition);
                }
                expandPosition = isExpand ? -1 : viewHolder.getAdapterPosition();
                mViewholder = isExpand ? null : viewHolder;
                notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }


    public static class myviewholder extends RecyclerView.ViewHolder {
        RelativeLayout rlParent,rlChild;
        TextView tvTeam,tvTeamChild;
        public myviewholder(View itemView) {
            super(itemView);
            rlParent = itemView.findViewById(R.id.rl_parent);
            rlChild = itemView.findViewById(R.id.rl_child);
            tvTeam = itemView.findViewById(R.id.tv_team);
            tvTeamChild = itemView.findViewById(R.id.tv_team_child);
        }
    }
}