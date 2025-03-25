package com.creditscore.sujeet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.creditscore.sujeet.samplecreditscore.R;
import com.creditscore.sujeet.dto.LiveCreditScoreDTO;



public class LiveCreditScoreAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LiveCreditScoreDTO singleDTO;
    private List<LiveCreditScoreDTO> listofDTO;

    public LiveCreditScoreAdaptor(List<LiveCreditScoreDTO> listofDTO)
    {
        this.listofDTO = listofDTO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_live_credit_score, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position >= 0)
        {
            try
            {
                singleDTO = listofDTO.get(position);
                ((ViewHolder) holder).name.setText(singleDTO.getName());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return listofDTO.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.details_live);

//            Intent intent = new Intent(v.getContext, ss.class)
//            v.getContext().startActivity(intent);
        }
    }
}
