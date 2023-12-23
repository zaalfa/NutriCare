package com.example.FinalProject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Profile extends AppCompatActivity implements View.OnClickListener{
    private static final int RC_SIGN_IN = 999;
    private static final int RC_Take_Photo = 0;
    private static final int RC_Take_From_Gallery = 1;
    //TextView _txNama;
    ImageButton _takePic;
    Button _btLogOut;
    String currentPhotoPath;
    private ImageView imageView;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        imageView = findViewById(R.id.Imageprofile);
        //_txNama = findViewById(R.id.txNama);
        _takePic = findViewById(R.id.kamera);

        _btLogOut = findViewById(R.id.btn_logout);

        _takePic.setOnClickListener(this);
        _btLogOut.setOnClickListener(this);
        ImageButton backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Homepage.class);
                startActivity(intent);
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build()
            );

// Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        } else {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //_txNama.setText(user.getDisplayName());

            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fotoRef = mStorageRef.child(user.getUid() + "/image");

            Task<ListResult> listPageTask = fotoRef.list(1);
            listPageTask
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {

                            List<StorageReference> items = listResult.getItems();
                            if (!items.isEmpty()) { //menggunakan Library glide untuk memudahkan

                                Toast.makeText(Profile.this,
                                                "loading Foto",
                                                Toast.LENGTH_LONG)
                                        .show();

                                items.get(0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(Profile.this /* context */)
                                                .load(uri)
                                                .centerCrop()
                                                .into(imageView);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                            } else {
                                Toast.makeText(Profile.this,
                                                "Belum ada Foto",
                                                Toast.LENGTH_SHORT)
                                        .show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this,
                                            "can't get Image, " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                    .show();

                        }
                    });

        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void selectImage(final Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(Profile.this,
                                    "com.example.myapplication",
                                    photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePicture, RC_Take_Photo);
                        }
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, RC_Take_From_Gallery);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case RC_Take_Photo:
                    if (resultCode == RESULT_OK && currentPhotoPath != null) {

                        Glide.with(this).load(new File(currentPhotoPath)).centerCrop().into(imageView);

                        ////scaning masuk ke gallery android (opsional)//////////////////
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f = new File(currentPhotoPath);
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        this.sendBroadcast(mediaScanIntent);
                        //////////////////////////////////

                        //upload hasil foto ke storage database
                        uploadToStorage(contentUri);
                    }

                    break;
                case RC_Take_From_Gallery:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                Glide.with(this).load(new File(picturePath)).centerCrop().into(imageView);
                                cursor.close();
                            }
                            //upload file dari galeri ke storage
                            uploadToStorage(selectedImage);
                        }

                    }
                    break;
                case RC_SIGN_IN:
                    if (resultCode == RESULT_OK) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //_txNama.setText(user.getDisplayName());

                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference fotoRef = mStorageRef.child(user.getUid() + "/image");

                        Task<ListResult> listPageTask = fotoRef.list(1);


                        listPageTask
                                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                    @Override
                                    public void onSuccess(ListResult listResult) {

                                        List<StorageReference> items = listResult.getItems();
                                        if (!items.isEmpty()) { //menggunakan Library glide untuk memudahkan
                                            Toast.makeText(Profile.this,
                                                            "loading Foto",
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                            items.get(0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Glide.with(Profile.this /* context */)
                                                            .load(uri)
                                                            .centerCrop()
                                                            .into(imageView);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle any errors
                                                }
                                            });
                                        } else {
                                            Toast.makeText(Profile.this,
                                                            "Belum ada Foto",
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Profile.this,
                                                        "can't get Image, " + e.getMessage(),
                                                        Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                    } else {
                        Toast.makeText(this,
                                        "We couldn't sign you in. Please try again later.",
                                        Toast.LENGTH_LONG)
                                .show();

                    }

            }
        }
    }


    public void uploadToStorage(Uri file) {
        // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UploadTask uploadTask;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fotoRef = mStorageRef.child(user.getUid() + "/image/" + user.getUid() + ".jpg");
        uploadTask = fotoRef.putFile(file);
        Toast.makeText(Profile.this,
                        "uploading Image",
                        Toast.LENGTH_SHORT)
                .show();

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Profile.this,
                                "can't upload Image, " + exception.getMessage(),
                                Toast.LENGTH_LONG)
                        .show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(Profile.this,
                                "Image Uploaded",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.kamera) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] Permisions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(Permisions, 100);
                } else {
                    selectImage(Profile.this);

                }
            } else {
                selectImage(Profile.this);

            }
        } else if (v.getId() == R.id.btn_logout) {

            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Profile.this,
                                            "You have been signed out.",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();

                            Intent intent = new Intent(Profile.this, Landing_Page.class);
                            startActivity(intent);
                        }
                    });
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage(Profile.this);

        } else {
            Toast.makeText(this, "denied", Toast.LENGTH_LONG).show();
        }
    }
}
