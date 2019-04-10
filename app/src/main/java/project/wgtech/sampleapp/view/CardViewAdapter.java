package project.wgtech.sampleapp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import project.wgtech.sampleapp.R;
import project.wgtech.sampleapp.model.NASAImageRepo;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private final static String TAG = CardViewAdapter.class.getSimpleName();

    private Context context;
    private NASAImageRepo repo;
    private ArrayList<NASAImageRepo> repos;

    public CardViewAdapter(Context context, ArrayList<NASAImageRepo> repos) {
        this.context = context;
        this.repos = repos;
    }

    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        repo = repos.get(position);

        Intent i = new Intent(context, DetailActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", position);
        i.putExtra("url", repo.url);

        Glide.with(context)
                .asBitmap()
                .load(repo.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Log.d(TAG, "onLoadFailed: Glide Exception! " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null){

                            Palette.from(resource).generate(palette -> {
                                if (palette == null) return;

                                Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();

                                if (vibrantSwatch != null) {
                                    int backgroundColor = vibrantSwatch.getRgb();
                                    int titleTextColor = vibrantSwatch.getTitleTextColor();
                                    int bodyTextColor = vibrantSwatch.getBodyTextColor();

                                    holder.llCompat.setBackgroundColor(backgroundColor);
                                    holder.llCompat.setAlpha(0.9f);
                                    holder.tvTitle.setTextColor(titleTextColor);
                                    holder.tvDate.setTextColor(titleTextColor);
                                    holder.btnDelete.setTextColor(bodyTextColor);
                                }
                            });
                        }
                        return false;
                    }
                })
                .thumbnail(0.01f)
                .centerCrop()
                .into(holder.ivImage);


        holder.tvTitle.setText(repo.title);
        holder.tvDate.setText(repo.date);

        holder.btnDelete.setOnClickListener(view -> {
            repos.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
            Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();
        });

        holder.cardView.setOnClickListener(view -> {
            context.startActivity(i);


            /**
             * TODO
             */
//            Pair<View, String> pair = new Pair<>(view, "DetailView");
//            ActivityOptionsCompat compat = ActivityOptionsCompat
//                                            .makeSceneTransitionAnimation(activity, pair);
//            context.startActivity(i, compat.toBundle());

            //activity.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    @Override
    public long getItemId(int position) {
        //return super.getItemId(position);
        return position;
    }



    public class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayoutCompat llCompat;
        AppCompatImageView ivImage;
        AppCompatTextView tvTitle;
        AppCompatTextView tvDate;
        AppCompatButton btnDelete;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_item);
            llCompat = itemView.findViewById(R.id.ll_inner_cv);
            ivImage = itemView.findViewById(R.id.iv_inner_cv);
            tvTitle = itemView.findViewById(R.id.tv_cv_inner);
            tvDate = itemView.findViewById(R.id.tv2_cv_inner);
            btnDelete = itemView.findViewById(R.id.btn_cv_inner);
        }
    }
}
