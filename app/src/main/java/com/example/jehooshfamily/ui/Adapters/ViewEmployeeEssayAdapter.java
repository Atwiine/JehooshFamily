package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.EmployeeSection.AnswerEmployeeEssay;
import com.example.jehooshfamily.ui.Models.HistoryEssay_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ViewEmployeeEssayAdapter extends RecyclerView.Adapter<ViewEmployeeEssayAdapter.HistEssayViewHolder> implements Filterable {

    Context context;
    List<HistoryEssay_Model> mData;
    List<HistoryEssay_Model> essay_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HistoryEssay_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(essay_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HistoryEssay_Model essayQnsModel : essay_filter) {
                    if (essayQnsModel.getQuestion().toLowerCase().contains(filterPattern)) {
                        filterexample.add(essayQnsModel);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filterexample;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mData.clear();
            mData.addAll((Collection<? extends HistoryEssay_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewEmployeeEssayAdapter(Context context, List<HistoryEssay_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public HistEssayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_my_history, null, false);
        return new HistEssayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistEssayViewHolder holder, int position) {
        HistoryEssay_Model markets_model = mData.get(position);
        holder.question.setText(mData.get(position).getQuestion());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate());
        holder.card_essy.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        //for time and then change card color
        String dbdate = holder.date.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date1 = dateFormat.parse(dbdate);
//                if (new Date().after(date1)){
//                    Toast.makeText(context, "after", Toast.LENGTH_SHORT).show();
//                }
            if (System.currentTimeMillis() > date1.getTime()) {
                Toast.makeText(context, "asasasd", Toast.LENGTH_SHORT).show();
                holder.current_qn.setVisibility(View.GONE);
            } else if (System.currentTimeMillis() == date1.getTime()) {
                Toast.makeText(context, "wewewrw", Toast.LENGTH_SHORT).show();
                holder.current_qn.setVisibility(View.VISIBLE);
            } else {
                holder.current_qn.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<HistoryEssay_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class HistEssayViewHolder extends RecyclerView.ViewHolder {

        TextView question, id, date, new_qn;
        CardView card_essy;
        LinearLayout linear_options;
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String dates;
        ImageView current_qn;


        public HistEssayViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.history_qn);
            id = itemView.findViewById(R.id.history_id);
            card_essy = itemView.findViewById(R.id.card_objec);
            linear_options = itemView.findViewById(R.id.linear_options);
            date = itemView.findViewById(R.id.history_date);
            new_qn = itemView.findViewById(R.id.new_qn);
            current_qn = itemView.findViewById(R.id.current_qn);

            linear_options.setVisibility(View.GONE);
            card_essy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, AnswerEmployeeEssay.class);
                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
                    request.putExtra("date", mData.get(getAdapterPosition()).getDate());
                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getQuestion());
                    context.startActivity(request);
                }
            });

            //check to see if the date from thr database is the same as the current data
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            dates = dateFormat.format(calendar.getTime());

            String dbDate = date.getText().toString();
            if (dates.equals(dbDate)) {
                new_qn.setVisibility(View.VISIBLE);
            }


        }
    }
}





