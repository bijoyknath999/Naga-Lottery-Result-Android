package nagalandlottery.result.daily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import nagalandlottery.result.daily.api.models.Winner;
import nagalandlottery.result.daily.databinding.ItemWinnerBinding;
import nagalandlottery.result.daily.utils.Tools;

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.ViewHolder> {

    private Context context;
    private List<Winner> winnerList;
    private ItemWinnerBinding binding;

    public WinnerAdapter(Context context, List<Winner> winnerList) {
        this.context = context;
        this.winnerList = winnerList;
    }

    @NonNull
    @Override
    public WinnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemWinnerBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WinnerAdapter.ViewHolder holder, int position) {

        Winner winner = winnerList.get(position);

        if (winner!=null)
        {
            String winnerImgPath = Tools.getString(context, "winnerImgPATH");
            Glide.with(context)
                    .load(winnerImgPath+winner.getProfileImage())
                    .into(holder.binding.itemWinnerImg);

            holder.binding.itemWinnerName.setText(winner.getName());
            holder.binding.itemWinnerPosition.setText(winner.getWinnerPosition());
            holder.binding.itemWinnerDrawName.setText(winner.getDrawName());
            holder.binding.itemWinnerAmount.setText(winner.getWinAmount());
            holder.binding.itemWinnerTicketNumber.setText(winner.getTicketNumber());
            holder.binding.itemWinnerDate.setText(winner.getDrawDate());
        }

    }

    @Override
    public int getItemCount() {
        return winnerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemWinnerBinding binding;

        public ViewHolder(@NonNull ItemWinnerBinding binding) {
            super(binding.getRoot());
            this.binding =  binding;
        }
    }
}
