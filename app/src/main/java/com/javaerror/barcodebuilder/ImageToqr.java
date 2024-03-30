package com.javaerror.barcodebuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ImageToqr extends AppCompatActivity {
    Uri imageUri;
    ImageView img_profile,pickImage,image,download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_toqr);

        img_profile = findViewById(R.id.img_profile);
        pickImage= findViewById(R.id.pickImage);
        image = findViewById(R.id.image);
        download = findViewById(R.id.download);

        Glide.with(this)
                .load(R.drawable.gene)
                .into(image);

        Glide.with(this)
                .load(R.drawable.bb)
                .into(download);

        pickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImages.launch(intent);
                       });

    }


    ActivityResultLauncher<Intent> pickImages = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        imageUri = result.getData().getData();
                        img_profile.setImageURI(imageUri);
                    } catch (Exception e) {
                        Toast.makeText(ImageToqr.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
    }
            );
}