package com.neckromatics.listenleaf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<MyModel> masterDataList;
    private StorageReference storageReference;
    VideoView videoBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);
        videoBackground = findViewById(R.id.t_background);
        videoBackground.setMediaController(null);
        videoBackground.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.t_background);
        videoBackground.start();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // Fetch data from Firebase Storage
        fetchFirebaseData();
    }

    private void fetchFirebaseData() {
        masterDataList = new ArrayList<MyModel>();

        // Assuming you have a list of files in Firebase Storage under "myData"
        StorageReference listRef = storageReference.child("Books");

        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Get the download URL and other metadata if needed
                            String title = item.getName().replace(".pdf","").replace("-"," ");
                            String description = "";

                            String image = "";
                            String imagePath="";// = convertPdfToImage(item);
                            MyModel model = new MyModel(title,description,image,imagePath,uri.toString());
                            masterDataList.add(model);
                            //Log.e("asset information",uri.toString());
                            // Check if all data has been fetched
                            if (masterDataList.size() == listResult.getItems().size()) {
                                // Set the adapter for the RecyclerView
                                recyclerView.setAdapter(new HorizontalAdapter(masterDataList));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        });
    }

    public class MyModel {
        private String title;       // Title for the card
        private String uri;  // url of the file
        private String description; // Description for the card
        private String imageUrl;    // URL for the cover image
        private String imagePath;



        // Constructor with parameters
        public MyModel(String title, String description, String imageUrl,String imagePath,String uri) {
            this.title = title;
            this.description = description;
            this.imageUrl = imageUrl;
            this.imagePath = imagePath;
            this.uri = uri;
        }
        // You can add more fields here depending on what information you want to display

        // Empty constructor is needed for Firebase when you're pulling data directly from the database

        // Getter and setter for title
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        // Getter and setter for description
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        // Add getters and setters for any additional fields
        public String getImageUrl() {
            return imageUrl;
        }

        // Setter for imageUrl
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    private class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {
        private List<MyModel> masterDataList;

        public HorizontalAdapter(List<MyModel> masterDataList) {
            this.masterDataList = masterDataList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate your item layout here and return a new ViewHolder
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            return new MyViewHolder(itemView);
        }


        @Override
        public int getItemCount() {
            return masterDataList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, description;
            public ImageView image;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
                description = view.findViewById(R.id.description);
                image = view.findViewById(R.id.image);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            // Get the data model based on position
            MyModel model = masterDataList.get(position);

            // Set item views based on your views and data model
            holder.title.setText(model.getTitle());
            //holder.description.setText(model.getDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click event here
                    // Extract the URI and send it to the next activity
                    Intent intent = new Intent(view.getContext(), Book_Reader.class);
                    intent.putExtra("uri", model.getUri()).putExtra("Title",model.getTitle());
                    view.getContext().startActivity(intent);
                }
            });
            // Load the converted image into the CardView
            Glide.with(holder.image.getContext())
                    .load(new File(model.getImagePath()))
                    .into(holder.image);
        }
    }

}
