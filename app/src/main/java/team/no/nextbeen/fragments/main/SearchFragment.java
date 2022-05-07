package team.no.nextbeen.fragments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import team.no.nextbeen.R;
import team.no.nextbeen.adapters.FoodAdapter;
import team.no.nextbeen.models.Food;

public class SearchFragment extends Fragment {


    RecyclerView rcvSearch;
    private List<Food> foods;
    private FoodAdapter foodAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        rcvSearch=view.findViewById(R.id.rcvSearch);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rcvSearch.setLayoutManager(linearLayoutManager);
        foods=new ArrayList<>();
        foodAdapter=new FoodAdapter(requireContext(), foods);
        rcvSearch.setAdapter(foodAdapter);
        getInformationFoodFromRealTimeDatabase();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void getInformationFoodFromRealTimeDatabase()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Foods");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Food food=dataSnapshot.getValue(Food.class);
                    foods.add(food);
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}