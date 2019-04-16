package project.wgtech.sampleapp.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import project.wgtech.sampleapp.R;

public class LottieViewAdapter extends RecyclerView.Adapter<LottieViewAdapter.LottieViewHolder> {
    private final static String TAG = LottieViewAdapter.class.getSimpleName();

    @NonNull
    @Override
    public LottieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_animation, parent, false);
        return new LottieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LottieViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: 추가");
        holder.view.setSpeed(0.5f);
        holder.view.setRepeatCount(0);
        holder.view.playAnimation();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class LottieViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView view;

        public LottieViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.lottie_main);
        }
    }
}
