package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.MarketPricesListAdapter;
import com.ambitious.fghdoctor.Adapters.PaymentHistoryAdapter;
import com.ambitious.fghdoctor.Model.HistoryModel;
import com.ambitious.fghdoctor.Model.MarketPriceList;
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

public class PaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = this;
    private ImageView iv_Bck;
    private RelativeLayout rl_Loader;
    private TextView tv_Notavailable;
    private RecyclerView recyclerList;
    private ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        finds();

        if (Utility.isNetworkConnected(mContext)) {
            uid = Utility.getSharedPreferences(mContext, "u_id");
            getHistory(uid);

        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        rl_Loader = findViewById(R.id.rl_Loader);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        recyclerList = findViewById(R.id.recyclerList);
        iv_Bck.setOnClickListener(this);


    }

    private void getHistory(String uid) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().walletTxn(uid);
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

                            JSONArray array = object.optJSONArray("result");

                            historyModelArrayList.clear();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String wallet_txn_id = result.optString("wallet_txn_id");
                                String user_id = result.optString("user_id");
                                String amount = result.optString("amount");
                                String nature = result.optString("nature");
                                String remark = result.optString("remark");
                                String entrydt = result.optString("entrydt");

                                Log.d("TAG", "onResponse: "+wallet_txn_id);
                                HistoryModel historyModel = new HistoryModel();
                                historyModel.setWallet_txn_id(wallet_txn_id);
                                historyModel.setUser_id(user_id);
                                historyModel.setAmount(amount);
                                historyModel.setNature(nature);
                                historyModel.setRemark(remark);
                                historyModel.setEntrydt(entrydt);
                                historyModelArrayList.add(historyModel);
                            }


                            PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(mContext, historyModelArrayList);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            recyclerList.setLayoutManager(manager);
                            recyclerList.setAdapter(adapter);
                            tv_Notavailable.setVisibility(View.GONE);
                        } else {
                            tv_Notavailable.setVisibility(View.VISIBLE);
                            CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment History " + e.getMessage());
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}