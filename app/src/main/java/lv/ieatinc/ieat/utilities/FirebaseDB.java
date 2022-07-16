package lv.ieatinc.ieat.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import lv.ieatinc.ieat.Constants;

public class FirebaseDB {

    public static final String TAG = "FIREBASE DB";

//    public static void readData(FirestoreCallback callback, FirebaseFirestore db) {
//        HashMap<Integer, Object> documents = new HashMap<>();
//
//        db.collection(Constants.DATABASE_COLLECTION)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                documents.put(Integer.parseInt(document.getId()), document.getData());
//                            }
//                            callback.onCallback(documents);
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }

    public static void getRestaurants(GetRestaurantCallback callback, FirebaseFirestore db) {
        HashMap<Integer, Object> documents = new HashMap<>();

        db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                documents.put(Integer.parseInt(document.getId()), document.getData());
                            }
                            callback.onCallback(documents);
                        } else {
                            Log.w(TAG, "Error getting restaurants.", task.getException());
                        }
                    }
                });
    }

    public static void addUser(AddUserCallback onCallback, FirebaseFirestore db, String UID, Map<String, String> data) {
        db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.USER_DOCUMENT)
                .collection(Constants.USER_COLLECTION)
                .document(UID)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onCallback.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        onCallback.onComplete(false);
                    }
                });
    }

    public interface GetRestaurantCallback {
        void onCallback(HashMap<Integer, Object> data);
    }

    public interface AddUserCallback {
        void onComplete(Boolean status);
    }
}
