package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Models.HistoryVoting_Model;
import com.example.jehooshfamily.ui.ResponseSection.AnswerVotes;
import com.example.jehooshfamily.ui.URLs.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewResponseVotingAdapter extends RecyclerView.Adapter<ViewResponseVotingAdapter.HistVotingViewHolder> implements Filterable {
    Urls urls;
    Context context;
    List<HistoryVoting_Model> mData;
    List<HistoryVoting_Model> essay_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HistoryVoting_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(essay_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HistoryVoting_Model essayQnsModel : essay_filter) {
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
            mData.addAll((Collection<? extends HistoryVoting_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewResponseVotingAdapter(Context context, List<HistoryVoting_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);
        urls = new Urls();

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public HistVotingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_objec_votes_response, null, false);
        return new HistVotingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistVotingViewHolder holder, int position) {
        final HistoryVoting_Model markets_model = mData.get(position);
        holder.question.setText(mData.get(position).getQuestion());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate());
        holder.o1.setText(mData.get(position).getOptions_a());
        holder.o2.setText(mData.get(position).getOptions_b());
        holder.o3.setText(mData.get(position).getOptions_c());
        holder.o4.setText(mData.get(position).getOptions_d());
        holder.o5.setText(mData.get(position).getOptions_e());
        holder.card_essy.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        //check for new answers
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.NEWANSWER_VOTES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);

                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject marketsObjects = tips.getJSONObject(i);

                                String cart = marketsObjects.getString("cartNo");

                                holder.relative_new_answers.setVisibility(View.VISIBLE);
                                holder.new_votes_answers.setText(cart);

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
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<HistoryVoting_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class HistVotingViewHolder extends RecyclerView.ViewHolder {

        TextView question, id, date, o1, o2, o3, o4, o5, new_votes_answers;
        CardView card_essy;
        Button indiv_results;
        RelativeLayout relative_new_answers;


        public HistVotingViewHolder(@NonNull View itemView) {
            super(itemView);
            relative_new_answers = itemView.findViewById(R.id.relative_new_answers);
            question = itemView.findViewById(R.id.history_qn);
            id = itemView.findViewById(R.id.history_id);
            card_essy = itemView.findViewById(R.id.card_obj_vot);
            date = itemView.findViewById(R.id.history_date);

            o1 = itemView.findViewById(R.id.oo1);
            o2 = itemView.findViewById(R.id.oo2);
            new_votes_answers = itemView.findViewById(R.id.new_answers);
            o3 = itemView.findViewById(R.id.oo3);
            o4 = itemView.findViewById(R.id.oo4);
            o5 = itemView.findViewById(R.id.oo5);

            card_essy.setCardBackgroundColor(Color.YELLOW);

            card_essy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, AnswerVotes.class);
                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
                    request.putExtra("date", mData.get(getAdapterPosition()).getDate());
                    request.putExtra("options_a", mData.get(getAdapterPosition()).getOptions_a());
                    request.putExtra("options_b", mData.get(getAdapterPosition()).getOptions_b());
                    request.putExtra("options_c", mData.get(getAdapterPosition()).getOptions_c());
                    request.putExtra("options_d", mData.get(getAdapterPosition()).getOptions_d());
                    request.putExtra("options_e", mData.get(getAdapterPosition()).getOptions_e());
                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getQuestion());
                    context.startActivity(request);
                }
            });

        }
    }
}





