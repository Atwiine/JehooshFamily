package com.example.jehooshfamily.ui.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.jehooshfamily.ui.AskSection.DrawQns;
import com.example.jehooshfamily.ui.Models.DrawQns_Model;
import com.example.jehooshfamily.ui.URLs.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewDrawQnsAdapter extends RecyclerView.Adapter<ViewDrawQnsAdapter.DrawViewHolder> implements Filterable {

    Context context;
    List<DrawQns_Model> mData;
    List<DrawQns_Model> draw_filter;
    RequestOptions options;
    Urls urls;

    private final Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DrawQns_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(draw_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (DrawQns_Model drawQns_model : draw_filter) {
                    if (drawQns_model.getQuestion().toLowerCase().contains(filterPattern)) {
                        filterexample.add(drawQns_model);
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
            mData.addAll((Collection<? extends DrawQns_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewDrawQnsAdapter(Context context, List<DrawQns_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.draw_filter = new ArrayList<>(mData);
        urls = new Urls();
    }

    @NonNull
    @Override
    public DrawViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_recent_essay, null, false);
        return new DrawViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawViewHolder holder, int position) {
        DrawQns_Model markets_model = mData.get(position);
        holder.question.setText(mData.get(position).getQuestion());
        holder.date.setText(mData.get(position).getDate());
        holder.id.setText(mData.get(position).getId());
        holder.essay_edit.setText(mData.get(position).getQuestion());
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
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.DELETE_DRAW_QUESTION,
                                response -> {
                                    try {
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        JSONObject object = new JSONObject(response);
                                        String success = object.getString("success");
                                        if (success.equals("1")) {
                                            Log.i("tagconvertstr", "[" + response + "]");
                                            Intent intent = new Intent(context, DrawQns.class);
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

        //            editing options
        holder.send_edit.setOnClickListener(v -> {
            final String bid = holder.id.getText().toString();
            final String qq = holder.essay_edit.getText().toString();
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Edit question")
                    .setMessage("Are you sure you want to edit this question?")
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("YES", (dialog, which) -> {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EDIT_DRAW_QUESTION,
                                response -> {
                                    try {
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        JSONObject object = new JSONObject(response);
                                        String success = object.getString("success");
                                        if (success.equals("1")) {
                                            Log.i("tagconvertstr", "[" + response + "]");
                                            Intent intent = new Intent(context, DrawQns.class);
                                            context.startActivity(intent);
                                            ((Activity) context).finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Question not edited, please try again " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }, error -> {
                            Toast.makeText(context, "Question not edited successfully, please check your network and try again", Toast.LENGTH_LONG).show();

                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("id", bid);
                                params.put("question", qq);
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
                holder.linear_edit.setVisibility(View.VISIBLE);
                holder.edit.setVisibility(View.GONE);
                holder.closeedit.setVisibility(View.VISIBLE);

            }
        });
        holder.closeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.linear_edit.setVisibility(View.GONE);
                holder.edit.setVisibility(View.VISIBLE);
                holder.closeedit.setVisibility(View.GONE);
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

    public void filterList(ArrayList<DrawQns_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class DrawViewHolder extends RecyclerView.ViewHolder {

        TextView question, answer, from, id, date;
        CardView card_essy,delete, edit,send_edit,closeedit;
        ImageView icon_recents;
        EditText essay_edit;
        LinearLayout linear_edit;

        public DrawViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.recent_essay);
            answer = itemView.findViewById(R.id.recent_response_essay);
            from = itemView.findViewById(R.id.recent_answer_from_who);
            id = itemView.findViewById(R.id.recent_reply_id);
            date = itemView.findViewById(R.id.date_essay);
            card_essy = itemView.findViewById(R.id.card_recentessy_qn);
            icon_recents = itemView.findViewById(R.id.icon_recents);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            essay_edit = itemView.findViewById(R.id.essay_edit);
            linear_edit = itemView.findViewById(R.id.linear_edit);
            send_edit = itemView.findViewById(R.id.send_edit);
            closeedit = itemView.findViewById(R.id.closeedit);
            icon_recents.setImageResource(R.drawable.ic_tools_icon);

        }
    }
}





