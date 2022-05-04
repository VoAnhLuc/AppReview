package team.no.nextbeen.fragments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

//    ListView lsvSearch;
////        @Override
////        public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_search);
//        lsvSearch= findViewById(R.id.lsvSearch);
//        ArrayList<SearchFragment> arr= new ArrayList<>();
//        while (arr.size()<10){
//            arr.add (new SearchFragment());
//        }
//        SearchAdapter adapter = new SearchAdapter(this, 0,arr) ;
//            lsvSearch.setAdapter(adapter);
//    }
//
//    private void setContentView(int main_search) {
//    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView rcvSearch;
    private List<Food> foods;
    private FoodAdapter foodAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        rcvSearch=view.findViewById(R.id.rcvSearch);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rcvSearch.setLayoutManager(linearLayoutManager);
        foods=new ArrayList<>();
        foodAdapter=new FoodAdapter(foods);
        rcvSearch.setAdapter(foodAdapter);
        getInformationFoodFromRealTimeDatabase();
        return view;
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