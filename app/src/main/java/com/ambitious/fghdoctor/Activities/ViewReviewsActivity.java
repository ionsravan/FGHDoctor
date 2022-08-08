package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.AppointmentNumberAdapter;
import com.ambitious.fghdoctor.Adapters.ReviewsAdapter;
import com.ambitious.fghdoctor.Model.Reviews;
import com.ambitious.fghdoctor.Model.Tokens;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ViewReviewsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Notavailable;
    private RecyclerView rv_Reviews;
    private RelativeLayout rl_Loader;
    private ArrayList<Reviews> reviews;
    private ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        finds();

        if (getIntent().getExtras() != null) {

            String doc_id = getIntent().getStringExtra("doc_id");

            if (Utility.isNetworkConnected(mContext)) {
                getReviews(doc_id, iv_Bck);
            } else {
                AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                        "You don't have internet connection.", false);
            }

        }

    }

    private void getReviews(String doc_id, ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getReviews(doc_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            tv_Notavailable.setVisibility(View.GONE);
                            rv_Reviews.setVisibility(View.VISIBLE);

                            JSONArray array = object.optJSONArray("result");

                            reviews = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String rating_id = result.optString("rating_id");
                                String user_id = result.optString("user_id");
                                String doctor_id = result.optString("doctor_id");
                                String appointment_id = result.optString("appointment_id");
                                String rating = result.optString("rating");
                                String review = result.optString("review");
                                String name = result.optString("name");
                                String user_image = result.optString("user_image");
                                String time_ago = result.optString("time_ago");

                                Reviews rev = new Reviews();
                                rev.setRating_id(rating_id);
                                rev.setRating(rating);
                                rev.setReview(review);
                                rev.setName(name);
                                rev.setUser_image(user_image);
                                rev.setTime_ago(time_ago);

                                reviews.add(rev);

                            }

                            adapter = new ReviewsAdapter(mContext, reviews);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Reviews.setLayoutManager(manager);
                            rv_Reviews.setAdapter(adapter);


                        } else {
                            tv_Notavailable.setVisibility(View.VISIBLE);
                            rv_Reviews.setVisibility(View.GONE);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Profile " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        rv_Reviews = findViewById(R.id.rv_Reviews);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.iv_Bck:
                onBackPressed();
                break;

        }
    }
}