package team.no.nextbeen.daos;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team.no.nextbeen.models.UserModel;

public class DAOUser {
    private final DatabaseReference databaseReference;
    private static final String TABLE_NAME = "Users";

    public DAOUser() {
        databaseReference = FirebaseDatabase.getInstance().getReference(TABLE_NAME);
    }

    public Task<Void> addUserAsync(UserModel user) {
        return databaseReference.child(user.getId()).setValue(user);
    }

    public Task<DataSnapshot> getUserInforByUserIdAsync(String userId) {
        return databaseReference.child(userId).get();
    }
}
