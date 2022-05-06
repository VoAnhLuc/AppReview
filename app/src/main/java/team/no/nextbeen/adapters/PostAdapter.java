package team.no.nextbeen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    List<Post> posts;
    public PostAdapter(List<Post> posts)
    {
        this.posts=posts;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post=posts.get(position);
        if(post==null)
        {
            return;
        }
        holder.txt_post_item.setText(post.getTitle());
    }

    @Override
    public int getItemCount() {
        if(posts!=null)
        {
            return posts.size();
        }
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_post_item;
        private TextView txt_post_item;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            img_post_item=itemView.findViewById(R.id.img_post_item);
            txt_post_item=itemView.findViewById(R.id.txt_post_item);
        }
    }
}
