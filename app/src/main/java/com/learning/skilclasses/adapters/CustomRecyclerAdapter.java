package com.learning.skilclasses.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.skilclasses.R;
import com.learning.skilclasses.models.AssignmentUtils;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.core.content.ContextCompat.startActivity;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<AssignmentUtils> assignmentUtils;

    public CustomRecyclerAdapter(Context context, List<AssignmentUtils> assignmentUtils) {
        this.context = context;
        this.assignmentUtils = assignmentUtils;
    }
    @NonNull
    @Override
    public CustomRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);
        ViewHolder viewHolder1=new ViewHolder(view);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(assignmentUtils.get(position));
        AssignmentUtils au=assignmentUtils.get(position);

        holder.subcategory.setText(au.getSubCategory());
        holder.classname.setText(au.getClassName());

    }

    @Override
    public int getItemCount() {
        try{
        return assignmentUtils.size();
    }catch (Exception e){
            e.printStackTrace();
        }
return  0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public TextView subcategory;
        public TextView classname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subcategory=itemView.findViewById(R.id.subcategory);
            classname=itemView.findViewById(R.id.classname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AssignmentUtils auu=(AssignmentUtils) view.getTag();
                    Toast.makeText(view.getContext(),auu.getAssignment_path(),Toast.LENGTH_SHORT).show();
                    //ContextCompat.startActivity(,new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(auu.getAssignment_path()),"application/pdf").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), Bundle.EMPTY);
                    /*PDFView pdfView = (PDFView)view.findViewById(R.id.pdfView);
                    pdfView.fromUri(Uri.parse(auu.getAssignment_path()));
                    pdfView.loadPages();*/
                    File pdfFile = new File(auu.getAssignment_path());
                    Uri path = Uri.fromFile(pdfFile);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                    try{
                        startActivity(view.getContext(),pdfIntent, Bundle.EMPTY);
                    }catch(ActivityNotFoundException e){
                        Toast.makeText(view.getContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
