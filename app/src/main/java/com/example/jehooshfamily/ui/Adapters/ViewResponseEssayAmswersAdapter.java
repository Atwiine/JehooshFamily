package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Models.AnswersEssay_Model;
import com.example.jehooshfamily.ui.ResponseSection.DetailsAnswersEssays;
import com.example.jehooshfamily.ui.URLs.Urls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewResponseEssayAmswersAdapter extends RecyclerView.Adapter<ViewResponseEssayAmswersAdapter.AnswerEssayViewHolder> implements Filterable {
    Urls urls;
    Context context;
    List<AnswersEssay_Model> mData;
    List<AnswersEssay_Model> essay_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AnswersEssay_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(essay_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (AnswersEssay_Model essayQnsModel : essay_filter) {
                    if (essayQnsModel.getSent_question().toLowerCase().contains(filterPattern)) {
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
            mData.addAll((Collection<? extends AnswersEssay_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewResponseEssayAmswersAdapter(Context context, List<AnswersEssay_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public AnswerEssayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_answer_essay, null, false);
        return new AnswerEssayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerEssayViewHolder holder, int position) {
        final AnswersEssay_Model markets_model = mData.get(position);
        urls = new Urls();
        holder.answer.setText(mData.get(position).getAnswer());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate_submission());
        holder.sendqn.setText(mData.get(position).getSent_question());
        holder.sentqnid.setText(mData.get(position).getSent_qn_id());
        holder.bossname.setText(mData.get(position).getBoss_name());
        holder.bossid.setText(mData.get(position).getBoss_id());
        holder.from.setText(mData.get(position).getFrom_who());
        holder.card_essy_answer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


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
                                if (cart.equals("0")) {
                                    holder.newanswer.setVisibility(View.GONE);
                                    holder.newanswer.setText("Read");
                                } else {
                                    holder.newanswer.setVisibility(View.VISIBLE);
                                    holder.newanswer.setText("New");
                                }

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
        requestQueue.add(stringRequest);

*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<AnswersEssay_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class AnswerEssayViewHolder extends RecyclerView.ViewHolder {

        TextView answer, id, date, from, sentqnid, sendqn, bossname, bossid, newanswer;
        CardView card_essy_answer;

        public AnswerEssayViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.essay_answer);
            id = itemView.findViewById(R.id.essay_answer_id);
            card_essy_answer = itemView.findViewById(R.id.card_answer_essay);
            newanswer = itemView.findViewById(R.id.essay_answer_new);
            date = itemView.findViewById(R.id.essay_answer_date);
            from = itemView.findViewById(R.id.essay_answer_from);
            sentqnid = itemView.findViewById(R.id.essay_answer_sentqn_id);
            sendqn = itemView.findViewById(R.id.essay_answer_sentqn);
            bossid = itemView.findViewById(R.id.essay_answer_bossid);
            bossname = itemView.findViewById(R.id.essay_answer_bossname);


            card_essy_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, DetailsAnswersEssays.class);
                    request.putExtra("answer", mData.get(getAdapterPosition()).getAnswer());
                    request.putExtra("date", mData.get(getAdapterPosition()).getDate_submission());
                    request.putExtra("from", mData.get(getAdapterPosition()).getFrom_who());
                    request.putExtra("user_id", mData.get(getAdapterPosition()).getUser_id());
                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getSent_question());
                    request.putExtra("sent_qn_id", mData.get(getAdapterPosition()).getSent_qn_id());
                    context.startActivity(request);
                }
            });

        }
    }
}





