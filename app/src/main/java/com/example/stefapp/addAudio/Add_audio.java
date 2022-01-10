package com.example.stefapp.addAudio;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stefapp.MainActivity;
import com.example.stefapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_audio extends AppCompatActivity {
    private static final int PICK_AUDIO = 1;
    TextView textViewAudioName, editTextAudioName;
    ProgressBar progressBar;
    Uri audioURI;
    MediaMetadataRetriever metadataRetriever;
    FirebaseFirestore db;
    StorageReference storageReference;
    UploadTask uploadTask;
    AudioProperties audioProperties;
    Spinner sTopic, sChapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audio);
        textViewAudioName = findViewById(R.id.textViewAudioName);
        editTextAudioName = findViewById(R.id.editTextAudioName);
        progressBar= findViewById(R.id.progressBarAudioAdd);
        sTopic = findViewById(R.id.spinnerTopic);
        sChapter = findViewById(R.id.spinner_chapter);
        metadataRetriever = new MediaMetadataRetriever();
        storageReference = FirebaseStorage.getInstance().getReference("audio");
        db = FirebaseFirestore.getInstance();
        audioProperties = new AudioProperties();


    }

    public void choseAudio(View view) {
            Intent intent = new Intent();
         intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_AUDIO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO || resultCode == RESULT_OK ||
                data != null || data.getData() != null) {
            audioURI = data.getData();
            String audioName = getFileName(audioURI);
            textViewAudioName.setText(audioName);


        }

    }

    private String getFileName(Uri uri) {
        String result = null;
        if(uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }

        }
        if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut!=-1){
                result = result.substring(cut+1);

            }

        }
        return result;
    }


    public void upload(View view) {
        if(textViewAudioName.getText().toString().isEmpty()){
            Toast.makeText(this, "Пожалуйста, выберите аудио файл для загрузки", Toast.LENGTH_SHORT).show();

        }
        else{
    if(uploadTask != null && uploadTask.isInProgress()){
        Toast.makeText(this, "Ожидайте, идет загрузка", Toast.LENGTH_SHORT).show();
    }
            else {
                uploadFiles();
            }
        }
    }


private void uploadFiles()  {

     if (audioURI != null && !editTextAudioName.getText().toString().isEmpty()) {
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExt(audioURI));
        uploadTask = reference.putFile(audioURI);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                 double progress = (100.0*uploadTask.getSnapshot().getBytesTransferred()/uploadTask.getSnapshot().getTotalByteCount());
                 progressBar.setProgress((int) progress);
             }
         });


        Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            progressBar.setVisibility(View.INVISIBLE);
                            audioProperties.setName(editTextAudioName.getText().toString());
                            audioProperties.setAudioUrl(downloadUrl.toString());
                            audioProperties.setTopic(sTopic.getSelectedItem().toString());
                            audioProperties.setChapter(sChapter.getSelectedItem().toString());
                            db.collection("audio")
                                    .document(audioProperties.getTopic())
                                    .collection(audioProperties.getChapter())
                                    .add(audioProperties)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(Add_audio.this, "Данные загружены", Toast.LENGTH_SHORT).show();
                                            editTextAudioName.setText("");
                                            textViewAudioName.setText("");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });

                        } else {
                            Toast.makeText(Add_audio.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                );
    } else {
        Toast.makeText(this, "Необходимо заполнить все поля", Toast.LENGTH_SHORT).show();
    }
}

    private String getExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void back(View view) {
        Intent i = new Intent(Add_audio.this, MainActivity.class);
        startActivity(i);
    }
}