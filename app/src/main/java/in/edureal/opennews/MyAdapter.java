package in.edureal.opennews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_horizontal, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItem listItem=listItems.get(position);

        Glide.with(context).load(listItem.getImageUrl()).into(holder.imageIV);
        holder.titleTV.setText(listItem.getTitle());
        holder.nameTV.setText(listItem.getName());

        holder.singleArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listItem.getUrl()));
                context.startActivity(browserIntent);
            }
        });

        holder.singleArticle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, listItem.getTitle(), Toast.LENGTH_SHORT).show();
                //Intent i=new Intent(Intent.ACTION_SEND);
                //i.setType("text/plain");
                //i.putExtra(Intent.EXTRA_SUBJECT, "Article on Open News");
                //i.putExtra(Intent.EXTRA_TEXT, listItem.getUrl());
                //context.startActivity(Intent.createChooser(i,"Share Article"));
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIV;
        TextView titleTV;
        TextView nameTV;
        LinearLayout singleArticle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageIV=(ImageView) itemView.findViewById(R.id.image);
            titleTV=(TextView) itemView.findViewById(R.id.title);
            nameTV=(TextView) itemView.findViewById(R.id.name);
            singleArticle=(LinearLayout) itemView.findViewById(R.id.singleArticle);

        }
    }

}
