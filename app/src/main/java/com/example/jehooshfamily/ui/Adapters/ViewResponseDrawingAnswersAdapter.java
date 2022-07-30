package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Models.AnswersDrawing_Model;
import com.example.jehooshfamily.ui.ResponseSection.DetailsAnswersDrawing;
import com.example.jehooshfamily.ui.URLs.Urls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewResponseDrawingAnswersAdapter extends RecyclerView.Adapter<ViewResponseDrawingAnswersAdapter.AnswerEssayViewHolder> implements Filterable {
    Urls urls;
    Context context;
    List<AnswersDrawing_Model> mData;
    List<AnswersDrawing_Model> draw_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AnswersDrawing_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(draw_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (AnswersDrawing_Model answersDrawing : draw_filter) {
                    if (answersDrawing.getSent_question().toLowerCase().contains(filterPattern)) {
                        filterexample.add(answersDrawing);
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
            mData.addAll((Collection<? extends AnswersDrawing_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewResponseDrawingAnswersAdapter(Context context, List<AnswersDrawing_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.draw_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public AnswerEssayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_answer_drawing, null, false);
        return new AnswerEssayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerEssayViewHolder holder, int position) {
        final AnswersDrawing_Model markets_model = mData.get(position);
        urls = new Urls();

        //loading the image
//        Glide.with(context)
//                .load(markets_model.getAnswer())
//                .into(holder.answer);

//        String imageUrl = "http://letsfarmapp.com/image/FoodPics/" + markets_model.getId() + ".jpg";
//        Glide.with(context)
//                .load(imageUrl)
//                .into(holder.product_image);

        String imageUrl = urls.hhtp + "drawingAdmin/" + markets_model.getAnswer();

        Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.android_developer)
                .error(R.drawable.ic_terrain) //6
                .fallback(R.drawable.frutorials)
                .into(holder.answer);

        holder.id.setText(mData.get(position).getId());
        holder.date.setText(mData.get(position).getDate_submission());
        holder.sendqn.setText(mData.get(position).getSent_question());
        holder.sentqnid.setText(mData.get(position).getSent_qn_id());
        holder.bossname.setText(mData.get(position).getBoss_name());
        holder.bossid.setText(mData.get(position).getBoss_id());
        holder.from.setText(mData.get(position).getFrom_who());
        holder.imagename.setText(mData.get(position).getAnswer());
        holder.card_essy_answer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


        //check if the answer has been read
       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.READANSWER_DRAWING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);

                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject marketsObjects = tips.getJSONObject(i);

                                String cart = marketsObjects.getString("cartNo");
                                holder.draw_answer_new.setVisibility(View.VISIBLE);
                                holder.draw_answer_new.setText("New");


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

    public void filterList(ArrayList<AnswersDrawing_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class AnswerEssayViewHolder extends RecyclerView.ViewHolder {

        TextView id, date, from, sentqnid, sendqn, bossname, bossid, imagename, draw_answer_new;
        CardView card_essy_answer;
        ImageView answer;

        public AnswerEssayViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.response_drawing);
            id = itemView.findViewById(R.id.essay_answer_id);
            card_essy_answer = itemView.findViewById(R.id.card_answer_essay);
            date = itemView.findViewById(R.id.essay_answer_date);
            from = itemView.findViewById(R.id.essay_answer_from);
            sentqnid = itemView.findViewById(R.id.essay_answer_sentqn_id);
            draw_answer_new = itemView.findViewById(R.id.draw_answer_new);
            sendqn = itemView.findViewById(R.id.essay_answer_sentqn);
            bossid = itemView.findViewById(R.id.essay_answer_bossid);
            bossname = itemView.findViewById(R.id.essay_answer_bossname);
            imagename = itemView.findViewById(R.id.imagename);

//             final String imageUrl = urls.hhtp+"bossApp/drawingAdmin/" + mData.get(getAdapterPosition()).getAnswer();


            card_essy_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, DetailsAnswersDrawing.class);
                    request.putExtra("photo", urls.hhtp + "drawingAdmin/" + mData.get(getAdapterPosition()).getAnswer());
                    request.putExtra("date", mData.get(getAdapterPosition()).getDate_submission());
                    request.putExtra("from", mData.get(getAdapterPosition()).getFrom_who());
                    request.putExtra("user_id", mData.get(getAdapterPosition()).getUser_id());
                    request.putExtra("sent_question", mData.get(getAdapterPosition()).getSent_question());
                    request.putExtra("sent_qn_id", mData.get(getAdapterPosition()).getSent_qn_id());
                    request.putExtra("imagename", mData.get(getAdapterPosition()).getAnswer());
                    request.putExtra("sent_qn_id", mData.get(getAdapterPosition()).getSent_qn_id());
                    request.putExtra("explanation", mData.get(getAdapterPosition()).getExplanation());
                    context.startActivity(request);
                }
            });

        }
    }
}





