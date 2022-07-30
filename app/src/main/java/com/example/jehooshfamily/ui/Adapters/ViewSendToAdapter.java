package com.example.jehooshfamily.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Models.SentTo_Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewSendToAdapter extends RecyclerView.Adapter<ViewSendToAdapter.EssayViewHolder> implements Filterable {

    Context context;
    List<SentTo_Model> mData;
    List<SentTo_Model> sent_to_filter;
    RequestOptions options;
    private Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SentTo_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(sent_to_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SentTo_Model sentTo_model : sent_to_filter) {
                    if (sentTo_model.getNames().toLowerCase().contains(filterPattern)) {
                        filterexample.add(sentTo_model);
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
            mData.addAll((Collection<? extends SentTo_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewSendToAdapter(Context context, List<SentTo_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.sent_to_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public EssayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_send_to, null, false);
        return new EssayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EssayViewHolder holder, int position) {
        SentTo_Model markets_model = mData.get(position);
        holder.names.setText(mData.get(position).getNames());
        holder.phone.setText(mData.get(position).getPhone());
        holder.role.setText(mData.get(position).getRole());
        holder.boss_name.setText(mData.get(position).getBoss_name());
        holder.date.setText(mData.get(position).getDate_register());

//        String imageUrl = "http://letsfarmapp.com/image/FoodPics/" + markets_model.getId() + ".jpg";
//        Glide.with(context)
//                .load(imageUrl)
//                .into(holder.product_image);

        holder.card_essy.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<SentTo_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class EssayViewHolder extends RecyclerView.ViewHolder {

        TextView names, phone, role, boss_name, date;
        CardView card_essy;
        CheckBox checkBox;

        public EssayViewHolder(@NonNull View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.e_name);
            phone = itemView.findViewById(R.id.e_phone);
            role = itemView.findViewById(R.id.e_role);
            boss_name = itemView.findViewById(R.id.e_boss_name);
            date = itemView.findViewById(R.id.e_date);
            card_essy = itemView.findViewById(R.id.card_recentessy_qn);
            checkBox = itemView.findViewById(R.id.check_sendto);


//            handle the check button


        }
    }
}





