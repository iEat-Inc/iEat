package lv.ieatinc.ieat.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.Constants;

public class FirebaseDB {

    public static final String TAG = "FIREBASE DB";

    public static void readData(FirestoreCallback callback, FirebaseFirestore db) {
        Map<String, Object> documents = new HashMap<>();

        db.collection(Constants.DATABASE_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                documents.put(document.getId(), document.getData());
                            }
                            callback.onCallback(documents);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public interface FirestoreCallback {
        void onCallback(Map<String, Object> data);
    }
}
