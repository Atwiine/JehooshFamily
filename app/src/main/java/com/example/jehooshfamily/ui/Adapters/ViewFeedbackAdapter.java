package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
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
import com.example.jehooshfamily.ui.Models.Feedback_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ViewFeedbackAdapter extends RecyclerView.Adapter<ViewFeedbackAdapter.FeedbackViewHolder> implements Filterable {

    Context context;
    List<Feedback_Model> mData;
    List<Feedback_Model> essay_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Feedback_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(essay_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Feedback_Model essayQnsModel : essay_filter) {
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
            mData.addAll((Collection<? extends Feedback_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewFeedbackAdapter(Context context, List<Feedback_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_feedback, null, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback_Model markets_model = mData.get(position);
        holder.question.setText(mData.get(position).getQuestion());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate());
        holder.op_a.setText(mData.get(position).getOptions_a());
        holder.op_b.setText(mData.get(position).getOptions_b());
        holder.op_c.setText(mData.get(position).getOptions_c());
        holder.op_d.setText(mData.get(position).getOptions_d());
        holder.op_e.setText(mData.get(position).getOptions_e());
        holder.userid.setText(mData.get(position).getUser_id());
        holder.username.setText(mData.get(position).getUser_name());
        holder.answeruser.setText(mData.get(position).getAnswer_user());
        holder.feedback_self.setText(mData.get(position).getFeedback());
        holder.card_feedb.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

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

    public void filterList(ArrayList<Feedback_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView question;
        TextView id;
        TextView date;
        TextView op_a;
        TextView op_b;
        TextView op_c;
        TextView op_d;
        TextView answeruser;
        TextView op_e;
        TextView username;
        TextView userid;
        CardView card_feedb;
        TextView feedback_self;
        RelativeLayout show_feedback_of_who;
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String dates;
        ImageView current_qn;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.fd_qn);
            op_a = itemView.findViewById(R.id.fd_options_a);
            op_b = itemView.findViewById(R.id.fd_options_b);
            op_c = itemView.findViewById(R.id.fd_options_c);
            op_d = itemView.findViewById(R.id.fd_options_d);
            op_e = itemView.findViewById(R.id.fd_options_e);
            id = itemView.findViewById(R.id.fd_id);
            card_feedb = itemView.findViewById(R.id.card_feedb);
            feedback_self = itemView.findViewById(R.id.feedback_self);

            date = itemView.findViewById(R.id.fd_date);
            answeruser = itemView.findViewById(R.id.fd_useranswer);
            username = itemView.findViewById(R.id.user_name);
            userid = itemView.findViewById(R.id.user_id);
            current_qn = itemView.findViewById(R.id.current_qn);
            show_feedback_of_who = itemView.findViewById(R.id.show_feedback_of_who);

//            show_feedback_of_who.setBackgroundColor(Color.GREEN);

//            card_essy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent request = new Intent(context, AnswerEmployeeObjectives.class);
//                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
//                    request.putExtra("date", mData.get(getAdapterPosition()).getDate());
//                    request.putExtra("options_a", mData.get(getAdapterPosition()).getOptions_a());
//                    request.putExtra("options_b", mData.get(getAdapterPosition()).getOptions_b());
//                    request.putExtra("options_c", mData.get(getAdapterPosition()).getOptions_c());
//                    request.putExtra("options_d", mData.get(getAdapterPosition()).getOptions_d());
//                    request.putExtra("options_e", mData.get(getAdapterPosition()).getOptions_e());
//                    request.putExtra("answer_boss", mData.get(getAdapterPosition()).getAnswer_boss());
//                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getQuestion());
//                    context.startActivity(request);
//
//                }
//            });

        }


    }
}





