package com.example.ozlem.travel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


    public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

        private RecyclerView mRecyclerView;
        private ImageAdapter mAdapter;

        private ProgressBar mProgressCircle;

        private FirebaseStorage mStorage;
        private ValueEventListener mValueListener;

        private DatabaseReference mDatabaseRef;
        private List<Upload> mUploads;




        ArrayList<String> userlikeFromFB;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_images);

            mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mProgressCircle= (ProgressBar) findViewById(R.id.progress_circle);

            mUploads=new ArrayList<>();

            mAdapter=new ImageAdapter(com.example.ozlem.travel.ImagesActivity.this,mUploads);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(com.example.ozlem.travel.ImagesActivity.this);

            mStorage=FirebaseStorage.getInstance();

            mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");



            mValueListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUploads.clear();




                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Upload upload=postSnapshot.getValue(Upload.class);
                        upload.setKey(postSnapshot.getKey());
                        mUploads.add(upload);

                    }

                    mAdapter.notifyDataSetChanged();
                    mProgressCircle.setVisibility(View.INVISIBLE);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(com.example.ozlem.travel.ImagesActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        public void onItemClick(int position) {



            Toast.makeText(this, "Beğenme" + position, Toast.LENGTH_SHORT).show();




        }

        @Override
        public void onWhatEverClick(int position) {

            Toast.makeText(this,"Herhangi Tıklama"+position,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDeleteClick(int position) {
            Upload selectedItem=mUploads.get(position);
            final String selectedKey=selectedItem.getKey();

            StorageReference imageRef=mStorage.getReferenceFromUrl(selectedItem.getImageUrl());

            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(com.example.ozlem.travel.ImagesActivity.this,"Seçilen Silindi",Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mDatabaseRef.removeEventListener(mValueListener);
        }


}
