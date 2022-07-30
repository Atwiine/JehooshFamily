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
import com.example.jehooshfamily.ui.Accounts.UserAccount;
import com.example.jehooshfamily.ui.Models.RegisteredEmployee_Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewRegisteredEmployeesAdapter extends RecyclerView.Adapter<ViewRegisteredEmployeesAdapter.RegEmploViewHolder> implements Filterable {

    Context context;
    List<RegisteredEmployee_Model> mData;
    List<RegisteredEmployee_Model> regemployee_filter;
    RequestOptions options;
    private final Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RegisteredEmployee_Model> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(regemployee_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (RegisteredEmployee_Model registeredEmployee_model : regemployee_filter) {
                    if (registeredEmployee_model.getNames().toLowerCase().contains(filterPattern)) {
                        filterexample.add(registeredEmployee_model);
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
            mData.addAll((Collection<? extends RegisteredEmployee_Model>) results.values);
            notifyDataSetChanged();
        }
    };

    public ViewRegisteredEmployeesAdapter(Context context, List<RegisteredEmployee_Model> mData) {
        this.context = context;
        this.mData = mData;
        this.regemployee_filter = new ArrayList<>(mData);

//        options = new RequestOptions().centerCrop().placeholder(R.drawable.markets).error(R.drawable.markets);
    }

    @NonNull
    @Override
    public RegEmploViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_reg_employee, null, false);
        return new RegEmploViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegEmploViewHolder holder, int position) {
        RegisteredEmployee_Model markets_model = mData.get(position);
        holder.names.setText(mData.get(position).getNames());
        holder.phone.setText(mData.get(position).getPhone());
        holder.role.setText(mData.get(position).getRole());
        holder.boss_name.setText(mData.get(position).getBoss_name());
        holder.date.setText(mData.get(position).getDate_register());

        holder.linear_see_employees.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<RegisteredEmployee_Model> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public class RegEmploViewHolder extends RecyclerView.ViewHolder {

        TextView names, phone, role, boss_name, date;
        CardView linear_see_employees;

        public RegEmploViewHolder(@NonNull View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.e_name);
            phone = itemView.findViewById(R.id.e_phone);
            role = itemView.findViewById(R.id.e_role);
            boss_name = itemView.findViewById(R.id.e_boss_name);
            date = itemView.findViewById(R.id.e_date);
            linear_see_employees = itemView.findViewById(R.id.linear_see_employees);

            linear_see_employees.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent request = new Intent(context, UserAccount.class);
                    request.putExtra("id", mData.get(getAdapterPosition()).getId());
                    request.putExtra("name", mData.get(getAdapterPosition()).getNames());
                    request.putExtra("phone", mData.get(getAdapterPosition()).getPhone());
                    request.putExtra("role", mData.get(getAdapterPosition()).getRole());
                    request.putExtra("email", mData.get(getAdapterPosition()).getEmail());
                    context.startActivity(request);
                }
            });

        }
    }
}





