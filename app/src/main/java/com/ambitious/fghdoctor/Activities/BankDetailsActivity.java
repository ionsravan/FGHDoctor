package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ambitious.fghdoctor.R;

public class BankDetailsActivity extends AppCompatActivity {
    EditText etFirstName,etLastName,etAccountNumber,etIfscCode,etUpiId,etPaymentMobile;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etIfscCode = findViewById(R.id.etIfscCode);
        etUpiId = findViewById(R.id.etUpiId);
        etPaymentMobile = findViewById(R.id.etPaymentMobile);
        btnSubmit = findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(v -> {

            if (etFirstName.getText().toString().isEmpty()){
                etFirstName.setError("Enter First Name");
                return;
            }

            if (etLastName.getText().toString().isEmpty()){
                etLastName.setError("Enter Last Name");
                return;
            }

            if (etAccountNumber.getText().toString().isEmpty()){
                etAccountNumber.setError("Enter Account Number");
                return;
            }

            if (etIfscCode.getText().toString().isEmpty()){
                etIfscCode.setError("Enter IFSC Code");
                return;
            }

            if (etPaymentMobile.getText().toString().isEmpty()){
                etPaymentMobile.setError("Enter Payment Mobile");
                return;
            }

            Intent intent=new Intent();
            intent.putExtra("firstName",etFirstName.getText().toString());
            intent.putExtra("lastName",etLastName.getText().toString());
            intent.putExtra("accountNumber",etAccountNumber.getText().toString());
            intent.putExtra("ifscCode",etIfscCode.getText().toString());
            intent.putExtra("upiId",etUpiId.getText().toString());
            intent.putExtra("paymentMobile",etPaymentMobile.getText().toString());
            setResult(RESULT_OK,intent);
            finish();//finishing activity
        });

    }
}