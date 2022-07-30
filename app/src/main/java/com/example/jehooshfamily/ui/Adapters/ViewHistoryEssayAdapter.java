package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Models.HistoryEssay_Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewHistoryEssayAdapter extends RecyclerView.Adapter<ViewHistoryEssayAdapter.HistEssayViewHolder> implements Filterable {

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

    public ViewHistoryEssayAdapter(Context context, List<HistoryEssay_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);
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
        holder.edit.setVisibility(View.GONE);

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

        TextView question, id, date;
        CardView card_essy,edit;
        RelativeLayout hist_who;
        LinearLayout linear_options;
        RelativeLayout relative_new_answers;

        public HistEssayViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.history_qn);
            id = itemView.findViewById(R.id.history_id);
            card_essy = itemView.findViewById(R.id.card_objec);
            linear_options = itemView.findViewById(R.id.linear_options);
            hist_who = itemView.findViewById(R.id.show_history_of_who);
            date = itemView.findViewById(R.id.history_date);
            relative_new_answers = itemView.findViewById(R.id.relative_new_answers);
            edit = itemView.findViewById(R.id.edit);
            relative_new_answers.setVisibility(View.GONE);
            linear_options.setVisibility(View.GONE);
            hist_who.setBackgroundColor(Color.MAGENTA);


        }
    }
}





