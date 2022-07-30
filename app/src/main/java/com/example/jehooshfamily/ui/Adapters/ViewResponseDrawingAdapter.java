package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
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
import com.example.jehooshfamily.ui.Models.HistoryDrawing_Model;
import com.example.jehooshfamily.ui.ResponseSection.AnswersDrawing;
import com.example.jehooshfamily.ui.URLs.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewResponseDrawingAdapter extends RecyclerView.Adapter<ViewResponseDrawingAdapter.HistDrawViewHolder> implements Filterable {
    Urls urls;
    Context context;
    List<HistoryDrawing_Model> mData;
    List<HistoryDrawing_Model> hdraw_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HistoryDrawing_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(hdraw_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HistoryDrawing_Model drawing_model : hdraw_filter) {
                    if (drawing_model.getQuestion().toLowerCase().contains(filterPattern)) {
                        filterexample.add(drawing_model);
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
            mData.addAll((Collection<? extends HistoryDrawing_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewResponseDrawingAdapter(Context context, List<HistoryDrawing_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.hdraw_filter = new ArrayList<>(mData);
        urls = new Urls();
//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public HistDrawViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_my_history, null, false);
        return new HistDrawViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistDrawViewHolder holder, int position) {
        final HistoryDrawing_Model markets_model = mData.get(position);
        holder.question.setText(mData.get(position).getQuestion());
        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate());
        holder.card_essy.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.linear_optionss.setVisibility(View.GONE);
//check for new answers
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.NEWANSWER_DRAWING,
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
                                holder.new_essay_answers.setText(cart);

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

    public void filterList(ArrayList<HistoryDrawing_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class HistDrawViewHolder extends RecyclerView.ViewHolder {

        TextView question, id, date, new_essay_answers;
        CardView card_essy;
        RelativeLayout hist_who;
        LinearLayout linear_options,linear_optionss;
        RelativeLayout relative_new_answers;

        public HistDrawViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.history_qn);
            id = itemView.findViewById(R.id.history_id);
            card_essy = itemView.findViewById(R.id.card_objec);
            date = itemView.findViewById(R.id.history_date);
            hist_who = itemView.findViewById(R.id.show_history_of_who);
            new_essay_answers = itemView.findViewById(R.id.new_answers);
            relative_new_answers = itemView.findViewById(R.id.relative_new_answers);
            linear_options = itemView.findViewById(R.id.linear_options);
            linear_optionss = itemView.findViewById(R.id.linear_optionss);

            card_essy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, AnswersDrawing.class);
                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
                    request.putExtra("date", mData.get(getAdapterPosition()).getDate());
                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getQuestion());
                    context.startActivity(request);
                }
            });
            hist_who.setBackgroundColor(Color.MAGENTA);
            linear_options.setVisibility(View.GONE);

// to see if the cart is empty or not
            String nqn = new_essay_answers.getText().toString();
            if (nqn.equals("")) {
                relative_new_answers.setVisibility(View.GONE);
            } else {
                relative_new_answers.setVisibility(View.VISIBLE);
            }


        }


    }
}





