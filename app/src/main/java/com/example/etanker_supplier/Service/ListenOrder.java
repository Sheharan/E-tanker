package com.example.etanker_supplier.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.etanker_supplier.R;
import com.example.etanker_supplier.TankerList;
import com.example.etanker_supplier.Utils.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class ListenOrder extends Service implements EventListener {
    FirebaseFirestore fstore;
    FirebaseAuth firebaseAuth;
    DocumentReference doc;
    CollectionReference collectionReference;
    String UserId="";

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAuth= FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        UserId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        collectionReference=fstore.collection(Common.Suppliers).document(UserId).collection(Common.OrderDetails)
                ;//.document("30000").collection(Common.QueueOrder);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        fstore.collectionGroup(Common.QueueOrder).addSnapshotListener(new EventListener<QuerySnapshot>() {
//           @Override
//           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//               for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
//                   switch (doc.getType()){
//                       case ADDED:
//                           //showNotification();
//                           break;
//                       case MODIFIED:
//                           showNotification();
//                           break;
//                       case REMOVED:
//                           break;
//                   }
//               }
//           }
//       });



        fstore.collectionGroup(Common.QueueOrder)
                .whereEqualTo("supplierID",UserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        Log.d("My Activity","Inside OnEvent");
                        if(e!=null){
                            Log.d("My Activity", Objects.requireNonNull(e.getMessage()));
                        }else {
                            assert queryDocumentSnapshots != null;

                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                switch (doc.getType()){
                                    case ADDED:
                                        //showNotification();
                                        Log.d("My Activity","Document Added");
                                        break;
                                    case REMOVED:
                                        Log.d("My Activity","Document Removed");
                                        break;
                                    case MODIFIED:
                                        if (doc.getDocument().get("paymentToken")==null){
                                            String name= Objects.requireNonNull(doc.getDocument().get("name")).toString();
                                            String tanker= Objects.requireNonNull(doc.getDocument().get("tankerNumber")).toString();
                                            showNotification(name,tanker);
                                        }else{
                                            String name= Objects.requireNonNull(doc.getDocument().get("name")).toString();
                                            String tanker= Objects.requireNonNull(doc.getDocument().get("tankerNumber")).toString();
                                            showPaymentNotification(name,tanker);
                                        }

                                        break;
                                }

                            }
                        }
                    }
                });



        collectionReference.addSnapshotListener(this);

        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(String name,String tankerNumber) {

        Intent intent=new Intent(getBaseContext(), TankerList.class);
        intent.putExtra("activity_id",1001);

        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //A PendingIntent is a token that you give to a foreign application
        // (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager,
        // or other 3rd party applications), which allows the foreign application
        // to use your application's permissions to execute a predefined piece of code

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=
                    new NotificationChannel("tankerOrder","Water Tanker", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext(),"tankerOrder");

        builder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Water Tanker")
                .setContentTitle("Order Status")
                .setContentText(" your tanker number "+tankerNumber +" has been ordered for the service by "+name )
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher_foreground);


        NotificationManager notificationManager=(NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1,builder.build());


    }


    private void showPaymentNotification(String name,String tanker){
        Intent intent=new Intent(getBaseContext(), TankerList.class);
        intent.putExtra("activity_id",1001);

        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //A PendingIntent is a token that you give to a foreign application
        // (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager,
        // or other 3rd party applications), which allows the foreign application
        // to use your application's permissions to execute a predefined piece of code

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=
                    new NotificationChannel("tankerOrder","Water Tanker", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext(),"tankerOrder");

        builder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Water Tanker")
                .setContentTitle("Payment Info")
                .setContentText("Payment has been done by "+name+" for the service of the tanker "+tanker)
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher_foreground);


        NotificationManager notificationManager=(NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1,builder.build());
    }


    @Override
    public void onEvent(@Nullable Object o, @Nullable FirebaseFirestoreException e) {
        Log.d("My Activity",collectionReference.getPath());
        Log.d("My Activity","Notification point 1");




        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("My Activity","Inside OnEvent");
                if(e!=null){
                    Log.d("My Activity", Objects.requireNonNull(e.getMessage()));
                }else {
                    assert queryDocumentSnapshots != null;

                    for(DocumentSnapshot doc12:queryDocumentSnapshots.getDocuments()){

                    }
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (doc.getType()){
                            case ADDED:
                                //showNotification();
                                Log.d("My Activity","Document Added");
                                break;
                            case REMOVED:
                                Log.d("My Activity","Document Removed");
                                break;
                            case MODIFIED:
                                //showNotification();
                                break;
                        }

                    }
                }

            }
        });//.remove();




    }


}