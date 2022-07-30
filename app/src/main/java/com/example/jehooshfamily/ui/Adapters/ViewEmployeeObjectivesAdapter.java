package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.EmployeeSection.AnswerEmployeeObjectives;
import com.example.jehooshfamily.ui.Models.HistoryObjectives_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ViewEmployeeObjectivesAdapter extends RecyclerView.Adapter<ViewEmployeeObjectivesAdapter.HistObjectivesViewHolder> implements Filterable {

    Context context;
    List<HistoryObjectives_Model> mData;
    List<HistoryObjectives_Model> essay_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HistoryObjectives_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(essay_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HistoryObjectives_Model essayQnsModel : essay_filter) {
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
            mData.addAll((Collection<? extends HistoryObjectives_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewEmployeeObjectivesAdapter(Context context, List<HistoryObjectives_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public HistObjectivesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_my_history, null, false);
        return new HistObjectivesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistObjectivesViewHolder holder, int position) {
        HistoryObjectives_Model markets_model = mData.get(position);
        holder.question.setText(mData.get(position).getQuestion());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate());
        holder.op_a.setText(mData.get(position).getOptions_a());
        holder.op_b.setText(mData.get(position).getOptions_b());
        holder.op_c.setText(mData.get(position).getOptions_c());
        holder.op_d.setText(mData.get(position).getOptions_d());
        holder.op_e.setText(mData.get(position).getOptions_e());
        holder.answerboss.setText(mData.get(position).getAnswer_boss());
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

    public void filterList(ArrayList<HistoryObjectives_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class HistObjectivesViewHolder extends RecyclerView.ViewHolder {

        TextView question, id, date, op_a, op_b, op_c, op_d, op_e, answerboss, new_qn, sameday;
        CardView card_essy;
        RelativeLayout hist_who;
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String dates;
        ImageView current_qn;

        public HistObjectivesViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.history_qn);
            op_a = itemView.findViewById(R.id.history_options_a);
            op_b = itemView.findViewById(R.id.history_options_b);
            op_c = itemView.findViewById(R.id.history_options_c);
            op_d = itemView.findViewById(R.id.history_options_d);
            op_e = itemView.findViewById(R.id.history_options_e);
            id = itemView.findViewById(R.id.history_id);
            card_essy = itemView.findViewById(R.id.card_objec);
            sameday = itemView.findViewById(R.id.sameday);
            date = itemView.findViewById(R.id.history_date);
            answerboss = itemView.findViewById(R.id.history_answerboss);
            new_qn = itemView.findViewById(R.id.new_qn);
            current_qn = itemView.findViewById(R.id.current_qn);
            hist_who = itemView.findViewById(R.id.show_history_of_who);

            hist_who.setBackgroundColor(Color.BLUE);

            card_essy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, AnswerEmployeeObjectives.class);
                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
                    request.putExtra("date", mData.get(getAdapterPosition()).getDate());
                    request.putExtra("options_a", mData.get(getAdapterPosition()).getOptions_a());
                    request.putExtra("options_b", mData.get(getAdapterPosition()).getOptions_b());
                    request.putExtra("options_c", mData.get(getAdapterPosition()).getOptions_c());
                    request.putExtra("options_d", mData.get(getAdapterPosition()).getOptions_d());
                    request.putExtra("options_e", mData.get(getAdapterPosition()).getOptions_e());
                    request.putExtra("answer_boss", mData.get(getAdapterPosition()).getAnswer_boss());
                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getQuestion());
                    context.startActivity(request);

                }
            });

        }


    }
}





