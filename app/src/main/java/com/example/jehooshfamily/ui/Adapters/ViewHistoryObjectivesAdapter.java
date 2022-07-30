package com.example.jehooshfamily.ui.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.AskSection.EditObjectives;
import com.example.jehooshfamily.ui.HistorySection.HistoryObjectives;
import com.example.jehooshfamily.ui.Models.HistoryObjectives_Model;
import com.example.jehooshfamily.ui.URLs.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewHistoryObjectivesAdapter extends RecyclerView.Adapter<ViewHistoryObjectivesAdapter.HistObjectivesViewHolder> implements Filterable {

    Context context;
    List<HistoryObjectives_Model> mData;
    List<HistoryObjectives_Model> essay_filter;
    RequestOptions options;
    Urls urls;
    private final Filter examplefilter = new Filter() {

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

    public ViewHistoryObjectivesAdapter(Context context, List<HistoryObjectives_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.essay_filter = new ArrayList<>(mData);
        urls = new Urls();
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

        //            deleting options
        holder.delete.setOnClickListener(v -> {
            final String bid = holder.id.getText().toString();
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Delete question")
                    .setMessage("Are you sure you want to delete this question permanently?")
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("YES", (dialog, which) -> {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.DELETE_OBJECTIVE_QUESTION,
                                response -> {
                                    try {
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        JSONObject object = new JSONObject(response);
                                        String success = object.getString("success");
                                        if (success.equals("1")) {
                                            Log.i("tagconvertstr", "[" + response + "]");
                                            Toast.makeText(context, "Question deleted", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(context, HistoryObjectives.class);
                                            context.startActivity(intent);
                                            ((Activity) context).finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Question not deleted, please try again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }, error -> {
                            Toast.makeText(context, "Question not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();

                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("id", bid);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    })
                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            //Creating dialog box
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qq = holder.question.getText().toString();
                String id = holder.id.getText().toString();
                String op_a = holder.op_a.getText().toString();
                String op_b = holder.op_b.getText().toString();
                String op_c = holder.op_c.getText().toString();
                String op_d = holder.op_d.getText().toString();
                String op_e = holder.op_e.getText().toString();
                String answerboss = holder.answerboss.getText().toString();

                Intent dd = new Intent(context, EditObjectives.class);
                dd.putExtra("question",qq);
                dd.putExtra("id",id);
                dd.putExtra("op_a",op_a);
                dd.putExtra("op_b",op_b);
                dd.putExtra("op_c",op_c);
                dd.putExtra("op_d",op_d);
                dd.putExtra("op_e",op_e);
                dd.putExtra("answerboss",answerboss);
                context.startActivity(dd);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<HistoryObjectives_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public static class HistObjectivesViewHolder extends RecyclerView.ViewHolder {

        TextView question, id, date, op_a, op_b, op_c, op_d, op_e, answerboss;
        CardView card_essy,edit,delete;
        RelativeLayout hist_who;


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
            date = itemView.findViewById(R.id.history_date);
            answerboss = itemView.findViewById(R.id.history_answerboss);
            hist_who = itemView.findViewById(R.id.show_history_of_who);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

            hist_who.setBackgroundColor(Color.BLUE);


        }
    }
}





