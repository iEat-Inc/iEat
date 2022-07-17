package lv.ieatinc.ieat.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        HashMap<String, Object> documents = new HashMap<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        Log.i(TAG, UID);

        db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.USER_DOCUMENT)
                .collection(Constants.USER_COLLECTION)
                .document(UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> data = document.getData();
                                ArrayList<String> owned_restaurants = (ArrayList<String>) data.get("OwnedRestaurants");

                                if (owned_restaurants != null) {
                                    db.collection(Constants.DATABASE_COLLECTION)
                                            .document(Constants.RESTAURANT_DOCUMENT)
                                            .collection(Constants.RESTAURANT_COLLECTION)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            for (int i = 0; i < owned_restaurants.size(); i++) {
                                                                if (document.getId().equals(owned_restaurants.get(i))) {
                                                                    documents.put(document.getId(), document.getData());
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        callback.onCallback(documents);
                                                    } else {
                                                        Log.w(TAG, "Error getting restaurants.", task.getException());
                                                        callback.onCallback(documents);
                                                    }
                                                }
                                            });
                                } else {
                                    callback.onCallback(documents);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                                callback.onCallback(documents);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            callback.onCallback(documents);
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

    public static void addRestaurant(AddRestaurantCallback onCallback, FirebaseFirestore db, Map<String, String> data) {
        Task<DocumentReference> dr = db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .add(data);

        // Split into 2 statements, so that we can get the auto-generated ID from the statement above
        dr.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Registers the restaurant to current user
                        // Appends user OwnedRestaurants array with current restaurants id
                        db.collection(Constants.DATABASE_COLLECTION)
                                .document(Constants.USER_DOCUMENT)
                                .collection(Constants.USER_COLLECTION)
                                .document(FirebaseAuth.getInstance().getUid())
                                .update("OwnedRestaurants", FieldValue.arrayUnion(dr.getResult().getId()));

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
        void onCallback(HashMap<String, Object> data);
    }

    public interface AddUserCallback {
        void onComplete(Boolean status);
    }

    public interface AddRestaurantCallback {
        void onComplete(Boolean status);
    }
}
