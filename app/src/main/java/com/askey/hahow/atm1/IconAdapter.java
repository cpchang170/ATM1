package com.askey.hahow.atm1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder>{

    private final Context context;
    private List<Function> functions;

    public IconAdapter(Context context) {
        this.context = context;
        setup_functions();
       // functions = context.getResources().getStringArray(R.array.functions);
    }
    private void setup_functions() {
        functions = new ArrayList<>();
        String func[] = context.getResources().getStringArray(R.array.functions);
        functions.add(new Function(func[0],R.drawable.func_transaction));
        functions.add(new Function(func[1],R.drawable.func_balance));
        functions.add(new Function(func[2],R.drawable.func_finance));
        functions.add(new Function(func[3],R.drawable.func_contacts));
        functions.add(new Function(func[4],R.drawable.func_exit));
    }
    @NonNull
    @Override
    public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IconAdapter.IconHolder(LayoutInflater.from(context).inflate(R.layout.item_icon,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull IconHolder holder, int position) {
        final Function func = functions.get(position);
        holder.item_name.setText(func.getName());
        holder.item_icon.setImageResource(func.getIcon());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+ func.getName());
                itemClicked(func);
            }
        });
    }

    private void itemClicked(Function func) {
        switch(func.getIcon()){
            case R.drawable.func_balance:
                break;
            case R.drawable.func_transaction:
                Intent trans_intent = new Intent(this.context,TransActivity.class);
                this.context.startActivity(trans_intent);
                break;
            case R.drawable.func_finance:
                break;
            case R.drawable.func_contacts:
                Intent contact_intent = new Intent(this.context,ContactActivity.class);
                this.context.startActivity(contact_intent);
                break;
                case R.drawable.func_exit:
                ((Activity)context).finish();
                break;

        }
    }

    @Override
    public int getItemCount() {
        return functions.size();
    }

    public class IconHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        ImageView item_icon;
        public IconHolder(View itemView) {
            super(itemView);
            item_icon=itemView.findViewById(R.id.item_icon);
            item_name=itemView.findViewById(R.id.item_name);
        }
    }
}
