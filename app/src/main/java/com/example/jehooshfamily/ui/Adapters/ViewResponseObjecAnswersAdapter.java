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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Models.AnswersObjectives_Model;
import com.example.jehooshfamily.ui.ResponseSection.DetailsAnswersObjectives;
import com.example.jehooshfamily.ui.URLs.Urls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewResponseObjecAnswersAdapter extends RecyclerView.Adapter<ViewResponseObjecAnswersAdapter.AnswerObjecViewHolder> implements Filterable {
    Urls urls;
    Context context;
    List<AnswersObjectives_Model> mData;
    List<AnswersObjectives_Model> essay_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AnswersObjectives_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(essay_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (AnswersObjectives_Model essayQnsModel : essay_filter) {
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
            mData.addAll((Collection<? extends AnswersObjectives_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewResponseObjecAnswersAdapter(Context context, List<AnswersObjectives_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public AnswerObjecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_answer_objective, null, false);
        return new AnswerObjecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerObjecViewHolder holder, int position) {
        final AnswersObjectives_Model markets_model = mData.get(position);
        urls = new Urls();
        holder.answer_employee.setText(mData.get(position).getAnswer_employee());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate_submission());
        holder.answer_boss.setText(mData.get(position).getAnswer_boss());
        holder.question_sent.setText(mData.get(position).getQuestion_sent());
        holder.bossname.setText(mData.get(position).getBoss_name());
        holder.from.setText(mData.get(position).getFrom());
        holder.card_objec_answer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        String em_answer = holder.answer_employee.getText().toString().toLowerCase();
        String boss_answer = holder.answer_boss.getText().toString().toLowerCase();


        if (!em_answer.equals(boss_answer)) {
            holder.check.setVisibility(View.VISIBLE);
            holder.check.setBackgroundColor(Color.RED);
            holder.incorrect.setVisibility(View.VISIBLE);
            holder.correct.setVisibility(View.GONE);
        } else {
            holder.check.setVisibility(View.VISIBLE);
            holder.check.setBackgroundColor(Color.GREEN);
            holder.incorrect.setVisibility(View.GONE);
            holder.correct.setVisibility(View.VISIBLE);
        }


        //check if the answer has been read
    /*    StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.READANSWER_OBJECTIVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);

                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject marketsObjects = tips.getJSONObject(i);

                                String cart = marketsObjects.getString("cartNo");
                                holder.obj_answer_new.setVisibility(View.VISIBLE);
                                holder.obj_answer_new.setText("New");


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

    public void filterList(ArrayList<AnswersObjectives_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class AnswerObjecViewHolder extends RecyclerView.ViewHolder {

        TextView answer_employee, id, date, from, answer_boss, question_sent,
                bossname, obj_answer_new, obj_answerbossid, obj_answerqnid;
        CardView card_objec_answer;
        RelativeLayout check;
        TextView correct, incorrect;

        public AnswerObjecViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_employee = itemView.findViewById(R.id.objec_answer);
            id = itemView.findViewById(R.id.objec_answer_id);
            card_objec_answer = itemView.findViewById(R.id.card_answer_objec);
            date = itemView.findViewById(R.id.objec_answer_date);
            from = itemView.findViewById(R.id.objec_answer_from);
            answer_boss = itemView.findViewById(R.id.objec_answer_boss);
            obj_answer_new = itemView.findViewById(R.id.obj_answer_new);
            question_sent = itemView.findViewById(R.id.objec_answer_sentqn);
            obj_answerbossid = itemView.findViewById(R.id.obj_answerbossid);
            obj_answerqnid = itemView.findViewById(R.id.obj_answerqnid);
            bossname = itemView.findViewById(R.id.objec_answer_bossname);
            check = itemView.findViewById(R.id.check_answer);
            correct = itemView.findViewById(R.id.correct);
            incorrect = itemView.findViewById(R.id.incorrect);


//            check if answer is correct
            String em_answer = answer_employee.getText().toString().trim();
            String boss_answer = answer_boss.getText().toString().trim();

            if (!em_answer.toLowerCase().equals(boss_answer.toLowerCase())) {
                check.setVisibility(View.VISIBLE);
                check.setBackgroundColor(Color.RED);
                incorrect.setVisibility(View.VISIBLE);
                correct.setVisibility(View.GONE);
            } else {
                check.setVisibility(View.VISIBLE);
                check.setBackgroundColor(Color.GREEN);
                incorrect.setVisibility(View.GONE);
                correct.setVisibility(View.VISIBLE);
            }

            card_objec_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, DetailsAnswersObjectives.class);
                    request.putExtra("answer_user", mData.get(getAdapterPosition()).getAnswer_employee());
                    request.putExtra("answer_boss", mData.get(getAdapterPosition()).getAnswer_boss());
                    request.putExtra("user_id", mData.get(getAdapterPosition()).getUser_id());
                    request.putExtra("date", mData.get(getAdapterPosition()).getDate_submission());
                    request.putExtra("from", mData.get(getAdapterPosition()).getFrom());
                    request.putExtra("options_a", mData.get(getAdapterPosition()).getOptions_a());
                    request.putExtra("options_b", mData.get(getAdapterPosition()).getOptions_b());
                    request.putExtra("options_c", mData.get(getAdapterPosition()).getOptions_c());
                    request.putExtra("options_d", mData.get(getAdapterPosition()).getOptions_d());
                    request.putExtra("options_e", mData.get(getAdapterPosition()).getOptions_e());
                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getQuestion_sent());
                    request.putExtra("sent_qn_id", mData.get(getAdapterPosition()).getSent_qn_id());
                    context.startActivity(request);
                }
            });

        }
    }
}





