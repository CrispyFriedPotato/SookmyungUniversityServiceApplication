package com.example.sookchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity" ;
    private List<Comment> commentList  = new ArrayList<Comment>();
    private CommentAdapter adapter;

    int imageid;
    EditText addcomment;
    ImageView image_profile;
    TextView post;



    String postid;
    String publisherid;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        imageid = intent.getIntExtra("imageid", 0);
        Log.d(TAG, "onCreate: called.");
        Log.d(TAG, "imageid :" + imageid);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(adapter);

        getCommentList();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addcomment = findViewById(R.id.add_comment); //댓글입력창
        image_profile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);//post 클릭


        post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this,"You can't send empty comment", Toast.LENGTH_SHORT).show();
                }else{

                    String comment = addcomment.getText().toString();
                    addComment(imageid, comment);
                    commentList.clear();
                    getCommentList();
                }



            }
        });



    }

    public void onStart() {

        super.onStart();
    }


    public void getCommentList() {
        RetroFitApiInterface apiInterface = RetroFitApiClient.getClient().create(RetroFitApiInterface.class);
        Call<List<Comment>> call = apiInterface.getComment(imageid);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response == null) {
                    Toast.makeText(CommentActivity.this, "Somthing Went Wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    for (Comment comment : response.body()) {
                        commentList.add(comment);
                    }
                    Log.i("RESPONSE: ", "" + response.toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }


    public void addComment(int imageid, String comment) {
        RetroFitApiInterface apiInterface = RetroFitApiClient.getClient().create(RetroFitApiInterface.class);
        Call<Comment> call = apiInterface.postComment(imageid, comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response == null) {
                    Toast.makeText(CommentActivity.this, "Somthing Went Wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG,"onResponse "+response.body());
                }
                adapter.notifyDataSetChanged();
                addcomment.setText("");
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG,"ERROR: "+ t.getMessage());
            }
        });
    }

    /**

     public void onButtonClick(int position) {
     Log.d(TAG,"onBtnClick: clicked." + position);

     }**/




}
