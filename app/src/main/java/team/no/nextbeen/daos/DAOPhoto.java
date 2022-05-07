package team.no.nextbeen.daos;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DAOPhoto {
    private final StorageReference storageReference;
    private static final String TABLE_NAME = "Photos";

    public DAOPhoto() {
        storageReference = FirebaseStorage.getInstance().getReference(TABLE_NAME);
    }

    public Task<UploadTask.TaskSnapshot> uploadPhotoAsync(Uri uri, String fileName) {
        return storageReference.child(fileName).putFile(uri);
    }

    public Task<Uri> getPhotoURL(String fileName) {
        return storageReference.child(fileName).getDownloadUrl();
    }
}
