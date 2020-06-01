package com.learning.skilclasses.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.UploadAssignment;
import com.learning.skilclasses.models.AssignmentBean;

import java.util.ArrayList;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ItemsViewHolder> implements Filterable {

    private Context context;
    private List<AssignmentBean> list;
    private List<AssignmentBean> listFull;

    public AssignmentAdapter(Context context, List<AssignmentBean> list) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.assignment_recycler, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        AssignmentBean assignmentBean = list.get(position);
        // holder.aid.setText(assignmentBean.getAid());
        holder.aname.setText(assignmentBean.getAssignment_descp());
        String sub = assignmentBean.getSubcategory();
        holder.aofclass.setText("[ " + sub.substring(0, 1).toUpperCase() + sub.substring(1) + " ]");
        holder.relativeLayout.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setDataAndType(Uri.parse("http://www.digitalcatnyx.store/Coaching/admin/uploads/assignments/" + assignmentBean.getAssign_file()), "text/html");
            Intent chooser = Intent.createChooser(browserIntent, "Open PDF using");
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional
            context.startActivity(chooser);
        });
        holder.uploadassignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, UploadAssignment.class);
                intent.putExtra("subject",assignmentBean.getSubcategory());
                context.startActivity(intent);

            }
        });
        //holder.adesp.setText("Description|"+assignmentBean.getAdesp());
        //holder.adate.setText(assignmentBean.getAdate());
        // holder.asub.setText("Subject: "+assignmentBean.getAsub());
        // holder.apath.setText("path: "+assignmentBean.getApath());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView aid, aname, aofclass, adesp, adate, asub, apath;

        LinearLayout relativeLayout,uploadassignment;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            //aid=itemView.findViewById(R.id.aid);
            aname = itemView.findViewById(R.id.aname);
            aofclass = itemView.findViewById(R.id.aofclass);
            relativeLayout = itemView.findViewById(R.id.open_pdf);
            uploadassignment=itemView.findViewById(R.id.assignmentupload);
            //adesp=itemView.findViewById(R.id.adesp);
            // adate=itemView.findViewById(R.id.adate);
            //asub=itemView.findViewById(R.id.asub);
            //apath=itemView.findViewById(R.id.apath);
        }
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AssignmentBean> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (AssignmentBean items : listFull) {
                    if (items.getAssignment_descp().toLowerCase().contains(filterPattern)) {
                        filteredList.add(items);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
