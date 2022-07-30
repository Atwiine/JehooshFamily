package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Models.AnswersVoting_Model;
import com.example.jehooshfamily.ui.URLs.Urls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewResponseVotingAnswersAdapter extends RecyclerView.Adapter<ViewResponseVotingAnswersAdapter.AnswerVoteViewHolder> implements Filterable {
    Urls urls;
    Context context;
    List<AnswersVoting_Model> mData;
    List<AnswersVoting_Model> essay_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AnswersVoting_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(essay_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (AnswersVoting_Model essayQnsModel : essay_filter) {
                    if (essayQnsModel.getFrom().toLowerCase().contains(filterPattern)) {
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
            mData.addAll((Collection<? extends AnswersVoting_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewResponseVotingAnswersAdapter(Context context, List<AnswersVoting_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public AnswerVoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_answer_voting, null, false);
        return new AnswerVoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerVoteViewHolder holder, int position) {
        AnswersVoting_Model markets_model = mData.get(position);
        urls = new Urls();
        holder.answer_employee.setText(mData.get(position).getAnswer_employee());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate_submission());
        holder.boss_id.setText(mData.get(position).getBoss_id());
        holder.question_sent.setText(mData.get(position).getQuestion_sent());
        holder.bossname.setText(mData.get(position).getBoss_name());
//        holder.options.setText(mData.get(position).getOptions());
        holder.from.setText(mData.get(position).getFrom());
        holder.card_objec_answer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


        //check if the answer has been read
       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.READANSWER_ESSAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);

                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject marketsObjects = tips.getJSONObject(i);

                                String cart = marketsObjects.getString("cartNo");
                                holder.newanswer.setVisibility(View.VISIBLE);
                                holder.newanswer.setText(cart);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, " error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                could not load your objective responses, please check your connection and try again"
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sent_qn_id", markets_model.getId());
                params.put("boss_id", markets_model.getBoss_id());
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);*/


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<AnswersVoting_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class AnswerVoteViewHolder extends RecyclerView.ViewHolder {
        TextView answer_employee, id, date, from, boss_id,
                question_sent, bossname, vote_qnid;
        CardView card_objec_answer;
        RelativeLayout check;
        TextView correct, incorrect;

        public AnswerVoteViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_employee = itemView.findViewById(R.id.vote_answer);
            id = itemView.findViewById(R.id.vote_answer_id);
            card_objec_answer = itemView.findViewById(R.id.card_answer_voting);
            date = itemView.findViewById(R.id.vote_answer_date);
            from = itemView.findViewById(R.id.vote_answer_from);
            boss_id = itemView.findViewById(R.id.vote_answer_bossid);
            question_sent = itemView.findViewById(R.id.vote_answer_sentqn);
            vote_qnid = itemView.findViewById(R.id.vote_qnid);
            bossname = itemView.findViewById(R.id.vote_answer_bossname);

//            card_objec_answer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent request = new Intent(context, DetailsAnswersObjectives.class);
//                    request.putExtra("answer_employee", mData.get(getAdapterPosition()).getAnswer_employee());
//                    request.putExtra("answer_boss", mData.get(getAdapterPosition()).getAnswer_boss());
//                    request.putExtra("date", mData.get(getAdapterPosition()).getDate_submission());
//                    request.putExtra("options", mData.get(getAdapterPosition()).getOptions());
//                    request.putExtra("from", mData.get(getAdapterPosition()).getFrom());
//                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getQuestion_sent());
//                    context.startActivity(request);
//                }
//            });

        }
    }
}





