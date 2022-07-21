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

    public static void getStorages(GetStorageCallback onCallback, FirebaseFirestore db, String restaurantId) {
        HashMap<String, Object> documents = new HashMap<>();

        Log.e(TAG, Constants.DATABASE_COLLECTION + " -> " + Constants.RESTAURANT_DOCUMENT + " -> " + Constants.RESTAURANT_COLLECTION + " -> " + restaurantId + " -> " + Constants.STORAGE_COLLECTION);

        db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .document(restaurantId)
                .collection(Constants.STORAGE_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, String.valueOf(task.getResult().size()));
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documents.put(document.getId(), document.getData());
                                Log.i(TAG, document.getId() + " =>" + document.getData());
                            }
                            onCallback.onCallback(documents);
                        } else {
                            Log.e(TAG, "Error getting storages.", task.getException());
                        }
                    }
                });

    }

    public static void getShelves(GetShelvesCallback onCallback, FirebaseFirestore db, String restaurantId, String storageId) {
        HashMap<String, Object> documents = new HashMap<>();

        db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .document(restaurantId)
                .collection(Constants.STORAGE_COLLECTION)
                .document(storageId)
                .collection(Constants.SHELF_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documents.put(document.getId(), document.getData());
                            }
                            onCallback.onCallback(documents);
                        } else {
                            Log.w(TAG, "Error getting shelves.", task.getException());
                        }
                    }
                });
    }

    public static void getEmployees(GetEmployeeCallback onCallback, FirebaseFirestore db, String restaurantId) {
        HashMap<String, Object> documents = new HashMap<>();

        db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .document(restaurantId)
                .collection(Constants.EMPLOYEES_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documents.put(document.getId(), document.getData());
                            }
                            onCallback.onCallback(documents);
                        } else {
                            Log.w(TAG, "Error getting employees.", task.getException());
                        }
                    }
                });
    }

    public static void addUser(AddUserCallback onCallback, FirebaseFirestore db, String UID, Map<String, String> data) {
        HashMap<String, Object> documents = new HashMap<>();

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

                Map<String,Object> id = new HashMap<>();
                id.put("Id", dr.getResult().getId());

                db.collection(Constants.DATABASE_COLLECTION)
                        .document(Constants.RESTAURANT_DOCUMENT)
                        .collection(Constants.RESTAURANT_COLLECTION)
                        .document(dr.getResult().getId())
                        .update(id);

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

    public static void addStorage(AddStorageCallback onCallback, FirebaseFirestore db, String restaurantId, Map<String, String> data) {
        Task<DocumentReference> dr = db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .document(restaurantId)
                .collection(Constants.STORAGE_COLLECTION)
                .add(data);

        dr.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Map<String,Object> id = new HashMap<>();
                id.put("Id", dr.getResult().getId());

                db.collection(Constants.DATABASE_COLLECTION)
                        .document(Constants.RESTAURANT_DOCUMENT)
                        .collection(Constants.RESTAURANT_COLLECTION)
                        .document(restaurantId)
                        .collection(Constants.STORAGE_COLLECTION)
                        .document(dr.getResult().getId())
                        .update(id);

                onCallback.onComplete(true);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCallback.onComplete(false);
                    }
                });
    }

    public static void addShelf(AddShelfCallback onCallback, FirebaseFirestore db, String restaurantId, String storageId, Map<String, String> data) {
        Task<DocumentReference> dr = db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .document(restaurantId)
                .collection(Constants.STORAGE_COLLECTION)
                .document(storageId)
                .collection(Constants.SHELF_COLLECTION)
                .add(data);

        dr.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Map<String,Object> id = new HashMap<>();
                        id.put("Id", dr.getResult().getId());

                        db.collection(Constants.DATABASE_COLLECTION)
                                .document(Constants.RESTAURANT_DOCUMENT)
                                .collection(Constants.RESTAURANT_COLLECTION)
                                .document(restaurantId)
                                .collection(Constants.STORAGE_COLLECTION)
                                .document(storageId)
                                .collection(Constants.SHELF_COLLECTION)
                                .document(dr.getResult().getId())
                                .update(id);

                        onCallback.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCallback.onComplete(false);
                    }
                });
    }

    public static void addEmployee(AddStorageCallback onCallback, FirebaseFirestore db, String restaurantId, Map<String, String> data) {
        Task<DocumentReference> dr = db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.RESTAURANT_DOCUMENT)
                .collection(Constants.RESTAURANT_COLLECTION)
                .document(restaurantId)
                .collection(Constants.EMPLOYEES_COLLECTION)
                .add(data);

        dr.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        onCallback.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCallback.onComplete(false);
                    }
                });
    }

    public static void findUserByEmail(FirebaseFirestore db, String email) {

    }

    public static void addOwnedRestaurant(FirebaseFirestore db, String restaurantId, String uId) {
        // Registers the restaurant to current user
        // Appends user OwnedRestaurants array with current restaurants id
        db.collection(Constants.DATABASE_COLLECTION)
                .document(Constants.USER_DOCUMENT)
                .collection(Constants.USER_COLLECTION)
                .document(uId)
                .update("OwnedRestaurants", FieldValue.arrayUnion(restaurantId));
    }

    public interface GetRestaurantCallback {
        void onCallback(HashMap<String, Object> data);
    }

    public interface GetStorageCallback {
        void onCallback(HashMap<String, Object> data);
    }

    public interface GetShelvesCallback {
        void onCallback(HashMap<String, Object> data);
    }

    public interface GetEmployeeCallback {
        void onCallback(HashMap<String, Object> data);
    }

    public interface AddUserCallback {
        void onComplete(Boolean status);
    }

    public interface AddRestaurantCallback {
        void onComplete(Boolean status);
    }

    public interface AddStorageCallback {
        void onComplete(Boolean status);
    }

    public interface AddShelfCallback {
        void onComplete(Boolean status);
    }

    public interface AddEmployeeCallback {
        void onComplete(Boolean status);
    }
}
