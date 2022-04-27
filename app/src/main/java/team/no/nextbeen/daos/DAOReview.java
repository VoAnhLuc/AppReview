package team.no.nextbeen.daos;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import team.no.nextbeen.models.ReviewModel;

public class DAOReview {

    private final DatabaseReference databaseReference;
    private static final String TABLE_NAME = "Reviews";

    public DAOReview() {
        databaseReference = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    public Task<Void> addReviewAsync(ReviewModel review) {
        return databaseReference.push().setValue(review);
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
