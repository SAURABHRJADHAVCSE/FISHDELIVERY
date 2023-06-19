package com.example.fishdelivery.Customer.CustomerAddress;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fishdelivery.Customer.Notification.FCMSend;
import com.example.fishdelivery.Customer.TrackOrder.OrderCompleted;
import com.example.fishdelivery.Customer.TrackOrder.TrackOrder;
import com.example.fishdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GetAddress extends AppCompatActivity {

    private TextInputLayout Name, Username, Email, PhoneNo, editTextAddress, orderName, orderPrice, orderQuantity;
    private Button buttonGetLocation, submit;

    private ProgressBar progressBar;

    private static final int REQUEST_LOCATION_PERMISSION = 1;


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    String userId;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("UserOrders");

    DatabaseReference tokensRef = firebaseDatabase.getReference("tokens");

    String getLatitude, getLongitude;


    private String retrieveToken;

    String USERID = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);

        Name = findViewById(R.id.ProfileName);
        Username = findViewById(R.id.ProfileUsername);
        Email = findViewById(R.id.ProfileEmail);
        PhoneNo = findViewById(R.id.ProfilePhoneN0);
        orderName = findViewById(R.id.orderName);
        orderPrice = findViewById(R.id.orderPrice);
        orderQuantity = findViewById(R.id.OrderQuantity);
        submit = findViewById(R.id.buttonSubmit);


        editTextAddress = findViewById(R.id.editTextAddress);
        buttonGetLocation = findViewById(R.id.buttonGetLocation);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = firebaseAuth.getCurrentUser().getUid();
        firebaseUser = firebaseAuth.getCurrentUser();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());


        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                Name.getEditText().setText(documentSnapshot.getString("FullName"));
                Username.getEditText().setText(documentSnapshot.getString("UserName"));
                Email.getEditText().setText(documentSnapshot.getString("UserEmail"));
                PhoneNo.getEditText().setText(documentSnapshot.getString("PhoneNumber"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        Intent intent = getIntent();

        String foodTitlePass = intent.getStringExtra("foodTitle");
        String foodPricePass = intent.getStringExtra("result");
        String foodQuantity = intent.getStringExtra("quantity");
        String imageUrlPass = intent.getStringExtra("imageUrl");
        String tempPrice = intent.getStringExtra("tempPrice");

        orderName.getEditText().setText(foodTitlePass);
        orderPrice.getEditText().setText(foodPricePass);
        orderQuantity.getEditText().setText(foodQuantity + "Kg");

        Log.d("GetAddress", "Image URL: " + imageUrlPass);


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (Geocoder.isPresent()) {
            // Perform geocoding operations
        } else {
            Toast.makeText(this, "Geocoder not available", Toast.LENGTH_SHORT).show();
        }

        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(GetAddress.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GetAddress.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_PERMISSION);
                } else {
                    progressBar.setVisibility(View.VISIBLE); // Show the progress bar
                    getLocationAddress();
                }
            }
        });

        tokensRef.child("AdminToken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.getValue(String.class);
                    retrieveToken = token;
                } else {
                    Toast.makeText(GetAddress.this, "Error Token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = firebaseAuth.getCurrentUser().getUid();

                UserOrders user = new UserOrders();
                user.setName(Name.getEditText().getText().toString());
                user.setUsername(Username.getEditText().getText().toString());
                user.setEmail(Email.getEditText().getText().toString());
                user.setPhoneNo(PhoneNo.getEditText().getText().toString());
                user.setAddress(editTextAddress.getEditText().getText().toString());
                user.setOrderName(orderName.getEditText().getText().toString());
                user.setOrderPrice(orderPrice.getEditText().getText().toString());
                user.setOrderPrice(orderPrice.getEditText().getText().toString());
                user.setOrderQuantity(orderQuantity.getEditText().getText().toString());
                user.setTempPrice(tempPrice);
                user.setOrderImageUrl(imageUrlPass);
                user.setOrderDate(currentDate);
                user.setOrderTime(currentTime);
                user.setLatitude(getLatitude);
                user.setLongitude(getLongitude);

                uploadTextToFirebase(tempPrice);


                String orderId = FirebaseDatabase.getInstance().getReference("UserOrders").push().getKey();

                // Set the value under the new order ID
                databaseReference.child(userId).child(orderId).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data successfully submitted to the database

                                FirebaseDatabase.getInstance().getReference("TotalOrders").child(orderId)
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(GetAddress.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to submit data to the database
                                Toast.makeText(GetAddress.this, "Failed to submit data", Toast.LENGTH_SHORT).show();
                            }
                        });

                FirebaseDatabase.getInstance().getReference("MyOrders").child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GetAddress.this, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


                startActivity(new Intent(GetAddress.this, OrderCompleted.class));


                // Notification

                String title = "Order Recived";
                String message = "New Order";

                FCMSend.pushNotification(
                        GetAddress.this,
                        retrieveToken,
                        title,
                        message
                );
            }
        });



        // Notification

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    if (token != null) {
                        Log.d("FCMToken", "Token" + token); // Log the FCM token
                    } else {
                        Toast.makeText(GetAddress.this, "Failed to get FCM token", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GetAddress.this, "Failed to get FCM token", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                getLatitude = String.valueOf(latitude);
                getLongitude = String.valueOf(longitude);

                Log.d(TAG, "Latitude: " + latitude + ", Longitude: " + longitude);

                progressBar.setVisibility(View.GONE);

                Geocoder geocoder = new Geocoder(GetAddress.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String locationAddress = address.getAddressLine(0);

                        Log.d(TAG, "Address: " + locationAddress);

                        editTextAddress.getEditText().setText(locationAddress);
                    } else {
                        Toast.makeText(GetAddress.this, "No address found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(GetAddress.this, "Error retrieving address", Toast.LENGTH_SHORT).show();
                }

                // Stop receiving location updates
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    locationManager.removeUpdates(locationListener);
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void getLocationAddress() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location services disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAddress();
            }
        }
    }
    private void uploadTextToFirebase(String text) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a new node in the Firebase Realtime Database
        DatabaseReference textRef = FirebaseDatabase.getInstance().getReference("Texts").push();
        textRef.setValue(text)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload text
                    }
                });
    }
}