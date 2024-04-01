package com.javaerror.barcodebuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageToqr extends AppCompatActivity {
    Uri imageUri;

    Bitmap bitmaps;

    BitmapDrawable bitmapDrawable;
    ImageView img_profile,pickImage,image,download;
    Button make ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_toqr);

        img_profile = findViewById(R.id.img_profile);
        pickImage= findViewById(R.id.pickImage);
        image = findViewById(R.id.image);
        download = findViewById(R.id.download);
        make = findViewById(R.id.button2);

        Glide.with(this)
                .load(R.drawable.gene)
                .into(image);

        Glide.with(this)
                .load(R.drawable.bb)
                .into(download);

        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(img_profile), BarcodeFormat.QR_CODE,300,300);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);

                }catch (WriterException e){
                    throw new RuntimeException(e);
                }

            }
        });

        pickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImages.launch(intent);
                       });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapDrawable = (BitmapDrawable) image.getDrawable();
                bitmaps=bitmapDrawable.getBitmap();

                FileOutputStream fileOutputStream = null;

                File sdCard = Environment.getExternalStorageDirectory();
                File Directory = new File(sdCard.getAbsolutePath()+"/Download");
                Directory.mkdir();

                String fliename = String.format("%d.jpg",System.currentTimeMillis());
                File outfile = new File(Directory,fliename);

                Toast.makeText(ImageToqr.this, "QR code saved", Toast.LENGTH_SHORT).show();

                try {
                    fileOutputStream = new FileOutputStream(outfile);
                    bitmaps.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outfile));
                    sendBroadcast(intent);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
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