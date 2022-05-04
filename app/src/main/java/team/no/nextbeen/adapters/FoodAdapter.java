package team.no.nextbeen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.models.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    List<Food> foods;
    public FoodAdapter( List<Food> foods)
    {
        this.foods=foods;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_search,parent,false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food=foods.get(position);
        if(food==null)
        {
            return;
        }
        holder.txt_item_search_nameFood.setText(food.getDistrict());
        holder.txt_item_search_totalFood.setText(food.getAmount() + " quan an");
    }

    @Override
    public int getItemCount() {
        if(foods!=null)
        {
            return foods.size();
        }
        return 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{
        TextView txt_item_search_totalFood,txt_item_search_nameFood;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_item_search_totalFood=itemView.findViewById(R.id.txt_item_search_totalFood);
            txt_item_search_nameFood=itemView.findViewById(R.id.txt_item_search_nameFood);
        }
    }
}
