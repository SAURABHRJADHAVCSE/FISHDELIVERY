package com.example.fishdelivery.Admin.Fish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fishdelivery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FishAdd extends AppCompatActivity {

    private Button uploadBtn;

    private ImageView imageView;

    EditText itemNameEditTxt, itemPriceEditTxt, hotelLocationEditTxt;

    String itemNameEditTxtToString, itemPriceEditTxtToString;

    private ProgressBar progressBar;

    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Fish");

    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_add);

        imageView = findViewById(R.id.AddItemImg);
        itemNameEditTxt = findViewById(R.id.AddItemNamEditeTxt);
        itemPriceEditTxt = findViewById(R.id.AddItemPriceEditTxt);
        uploadBtn = findViewById(R.id.uploadItem);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(FishAdd.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadToFirebase(Uri imageUri) {
        final StorageReference fileRef = reference.child("Fish/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        itemNameEditTxtToString = itemNameEditTxt.getText().toString().trim();
                        itemPriceEditTxtToString = itemPriceEditTxt.getText().toString().trim();

                        FishModel model = new FishModel(uri.toString(), itemNameEditTxtToString, itemPriceEditTxtToString);
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(FishAdd.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.ic_launcher_background);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(FishAdd.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}