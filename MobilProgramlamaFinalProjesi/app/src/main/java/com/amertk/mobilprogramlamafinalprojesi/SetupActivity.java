package com.amertk.mobilprogramlamafinalprojesi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SetupActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        profileImageView = findViewById(R.id.profileImageView);
        Button selectImageBtn = findViewById(R.id.selectImageBtn);


        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {

                            Uri imageUri = result.getData().getData();
                            profileImageView.setImageURI(imageUri);

                            saveImageAsProfilePicture(imageUri);
                        }
                    }
                });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        loadDefaultProfileImage();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void saveImageAsProfilePicture(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File profileImageFile = new File(getFilesDir(), "profile_image.jpg");
            OutputStream outputStream = new FileOutputStream(profileImageFile);

            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultProfileImage() {
        Uri defaultImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.default_profile_image);
        profileImageView.setImageURI(defaultImageUri);
        saveImageAsProfilePicture(defaultImageUri);
    }
}

