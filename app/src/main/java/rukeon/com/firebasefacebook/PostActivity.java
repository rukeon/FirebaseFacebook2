package rukeon.com.firebasefacebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity implements
        View.OnClickListener {

    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDesc;

    private Button mSubmitBtn;

    private static final int GALLARY_REQUEST = 1;
    private Uri mImageUri = null;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Blog");

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        mProgress = new ProgressDialog(this);


        mSelectImage.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.imageSelect) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLARY_REQUEST);
        }
        if (i == R.id.submitBtn) {
            startPosting();
        }
    }

    private void startPosting() {
        mProgress.setMessage("Posting to Blog...");

        final String title = mPostTitle.getText().toString().trim();
        final String desc = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && mImageUri != null) {
            mProgress.show();

            StorageReference filepath = mStorageRef.child("Blog_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadURL = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabaseRef.push();
                    newPost.child("title").setValue(title);
                    newPost.child("desc").setValue(desc);
                    newPost.child("image").setValue(downloadURL.toString());
                    //newPost.child("uid").setValue(FirebaseAuth.getCurrentUser.getUid()); -> like this way we can add uid info

                    mProgress.dismiss();

                    startActivity(new Intent(PostActivity.this, BlogActivity.class ));
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }
}

