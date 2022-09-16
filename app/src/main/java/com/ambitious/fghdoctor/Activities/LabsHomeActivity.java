package com.ambitious.fghdoctor.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.documentfile.provider.DocumentFile;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LabsHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private TextView tv_Head, tv_Addpatient, tv_Available, tv_Date;
    private ImageView iv_More, iv_One, iv_Two, iv_Three, iv_Four, iv_Five, iv_Bck;
    private RelativeLayout rl_Loader;
    private Switch switch_Available;
    private EditText et_name, et_Mobile, et_Hospital, et_Deasc;
    private Button btn_Add;
    private String path1 = "", path2 = "", path3 = "", path4 = "", path5 = "", docpath1 = "", docpath2 = "", docpath3 = "", docpath4 = "", docpath5 = "", typ = "";
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar = Calendar.getInstance();
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/uploadPdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labs_home);
        finds();

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            //getProfile(uid, iv_More);
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        switch_Available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                if (isChecked) {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "1", iv_More);
                } else {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "0", iv_More);
                }
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                tv_Date.setText(sdf.format(myCalendar.getTime()));
                String dateString = sdf.format(myCalendar.getTime());
            }

        };
    }

    private void finds() {

        tv_Head = findViewById(R.id.tv_Head);
        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Addpatient = findViewById(R.id.tv_Addpatient);
        tv_Available = findViewById(R.id.tv_Available);
        tv_Date = findViewById(R.id.tv_Date);
        iv_More = findViewById(R.id.iv_More);
        iv_One = findViewById(R.id.iv_One);
        iv_Two = findViewById(R.id.iv_Two);
        iv_Three = findViewById(R.id.iv_Three);
        iv_Four = findViewById(R.id.iv_Four);
        iv_Five = findViewById(R.id.iv_Five);
        btn_Add = findViewById(R.id.btn_Add);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Hospital = findViewById(R.id.et_Hospital);
        et_Deasc = findViewById(R.id.et_Deasc);
        rl_Loader = findViewById(R.id.rl_Loader);
        switch_Available = findViewById(R.id.switch_Available);

        iv_Bck.setOnClickListener(this);
        iv_More.setOnClickListener(this);
        iv_One.setOnClickListener(this);
        iv_Two.setOnClickListener(this);
        iv_Three.setOnClickListener(this);
        iv_Four.setOnClickListener(this);
        iv_Five.setOnClickListener(this);
        btn_Add.setOnClickListener(this);
        tv_Date.setOnClickListener(this);

    }

    private void requesToChangeAvailablity(String uid, String stts, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().cahngeAvailablity(uid, stts);
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

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");
                            String available = result.optString("available");

                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
                            }

                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_More:
                showPopupMenu(v);
                break;

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.btn_Add:
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    valiidate(uid, v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.iv_One:
                showOptionDialog(1111);
                break;

            case R.id.iv_Two:
                showOptionDialog(2222);
                break;

            case R.id.iv_Three:
                showOptionDialog(3333);
                break;

            case R.id.iv_Four:
                showOptionDialog(4444);
                break;

            case R.id.iv_Five:
                showOptionDialog(5555);
                break;

            case R.id.tv_Date:
                DatePickerDialog dialog = new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
                break;

        }

    }

    private void showOptionDialog(final int i) {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_imageoperation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_Changeimg = dialog.findViewById(R.id.tv_Changeimg);
        TextView tv_Removeimg = dialog.findViewById(R.id.tv_Removeimg);
        View view = dialog.findViewById(R.id.view);

        tv_Changeimg.setText("Choose Image");
        tv_Removeimg.setText("Choose Document");

        dialog.show();

        tv_Changeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, i);
            }
        });

        tv_Removeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int fi = 0;

                if (i == 1111) {
                    fi = 1010;
                } else if (i == 2222) {
                    fi = 2020;
                } else if (i == 3333) {
                    fi = 3030;
                } else if (i == 4444) {
                    fi = 4040;
                } else if (i == 5555) {
                    fi = 5050;
                }


                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF file"), fi);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            if (!docpath1.equalsIgnoreCase("")) {
                docpath1 = "";
            }
            path1 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path1);
            if (imgFile.exists()) {
                if (typ.equalsIgnoreCase("")) {
                    typ = "image";
                } else {
                    typ = typ + ",image";
                }
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_One.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path1).into(iv_One);
            }
        }
        if (requestCode == 2222 && resultCode == Activity.RESULT_OK) {
            if (!docpath2.equalsIgnoreCase("")) {
                docpath2 = "";
            }
            path2 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path2);
            if (imgFile.exists()) {
                if (typ.equalsIgnoreCase("")) {
                    typ = "image";
                } else {
                    typ = typ + ",image";
                }
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Two.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 2 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path2).into(iv_Two);
            }
        }
        if (requestCode == 3333 && resultCode == Activity.RESULT_OK) {
            if (!docpath3.equalsIgnoreCase("")) {
                docpath3 = "";
            }
            path3 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path3);
            if (imgFile.exists()) {
                if (typ.equalsIgnoreCase("")) {
                    typ = "image";
                } else {
                    typ = typ + ",image";
                }
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Three.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 3 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path3).into(iv_Three);
            }
        }
        if (requestCode == 4444 && resultCode == Activity.RESULT_OK) {
            if (!docpath4.equalsIgnoreCase("")) {
                docpath4 = "";
            }
            path4 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path4);
            if (imgFile.exists()) {
                if (typ.equalsIgnoreCase("")) {
                    typ = "image";
                } else {
                    typ = typ + ",image";
                }
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Four.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 4 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path4).into(iv_Four);
            }
        }
        if (requestCode == 5555 && resultCode == Activity.RESULT_OK) {
            if (!docpath5.equalsIgnoreCase("")) {
                docpath5 = "";
            }
            path5 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path5);
            if (imgFile.exists()) {
                if (typ.equalsIgnoreCase("")) {
                    typ = "image";
                } else {
                    typ = typ + ",image";
                }
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Five.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 5 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path5).into(iv_Four);
            }
        }

        if (requestCode == 1010 && resultCode == Activity.RESULT_OK) {
            if (!path1.equalsIgnoreCase("")) {
                path1 = "";
            }

           /* Uri sUri = data.getData();
            String sPath = sUri.getPath();

            docpath1 = getRealPathFromURI(mContext, data.getData());*/

            Uri uri = data.getData();
            docpath1 = getFilePathFromURI(LabsHomeActivity.this,uri);

            Log.d("TAG", "onActivityResult: "+docpath1);
            if (docpath1 != null) {
                File imgFile = new File(docpath1);
                if (imgFile.exists()) {
                    if (typ.equalsIgnoreCase("")) {
                        typ = "pdf";
                    } else {
                        typ = typ + ",pdf";
                    }
                    iv_One.setImageResource(R.drawable.ic_file);

                } else {
                    Toast.makeText(mContext, "DocFile 1 is not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                docpath1 = "";
                Toast.makeText(mContext, "Error to upload!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 2020 && resultCode == Activity.RESULT_OK) {
            if (!path2.equalsIgnoreCase("")) {
                path2 = "";
            }
           // docpath2 = getRealPathFromURI(mContext, data.getData());
            Uri uri = data.getData();
            docpath2 = getFilePathFromURI(LabsHomeActivity.this,uri);
            if (docpath2 != null) {
                File imgFile = new File(docpath2);
                if (imgFile.exists()) {
                    if (typ.equalsIgnoreCase("")) {
                        typ = "pdf";
                    } else {
                        typ = typ + ",pdf";
                    }
                    iv_Two.setImageResource(R.drawable.ic_file);

                } else {
                    Toast.makeText(mContext, "DocFile 2 is not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                docpath2 = "";
                Toast.makeText(mContext, "Error to upload!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 3030 && resultCode == Activity.RESULT_OK) {
            if (!path3.equalsIgnoreCase("")) {
                path3 = "";
            }
           // docpath3 = getRealPathFromURI(mContext, data.getData());
            Uri uri = data.getData();
            docpath3 = getFilePathFromURI(LabsHomeActivity.this,uri);
            if (docpath3 != null) {
                File imgFile = new File(docpath3);
                if (imgFile.exists()) {
                    if (typ.equalsIgnoreCase("")) {
                        typ = "pdf";
                    } else {
                        typ = typ + ",pdf";
                    }
                    iv_Three.setImageResource(R.drawable.ic_file);

                } else {
                    Toast.makeText(mContext, "DocFile 3 is not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                docpath3 = "";
                Toast.makeText(mContext, "Error to upload!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 4040 && resultCode == Activity.RESULT_OK) {
            if (!path4.equalsIgnoreCase("")) {
                path4 = "";
            }
           // docpath4 = getRealPathFromURI(mContext, data.getData());
            Uri uri = data.getData();
            docpath4 = getFilePathFromURI(LabsHomeActivity.this,uri);
            if (docpath4 != null) {
                File imgFile = new File(docpath4);
                if (imgFile.exists()) {
                    if (typ.equalsIgnoreCase("")) {
                        typ = "pdf";
                    } else {
                        typ = typ + ",pdf";
                    }
                    iv_Four.setImageResource(R.drawable.ic_file);

                } else {
                    Toast.makeText(mContext, "DocFile 4 is not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                docpath4 = "";
                Toast.makeText(mContext, "Error to upload!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 5050 && resultCode == Activity.RESULT_OK) {
            if (!path5.equalsIgnoreCase("")) {
                path5 = "";
            }
           // docpath5 = getRealPathFromURI(mContext, data.getData());
            Uri uri = data.getData();
            docpath5 = getFilePathFromURI(LabsHomeActivity.this,uri);
            if (docpath5 != null) {
                File imgFile = new File(docpath5);
                if (imgFile.exists()) {
                    if (typ.equalsIgnoreCase("")) {
                        typ = "pdf";
                    } else {
                        typ = typ + ",pdf";
                    }
                    iv_Five.setImageResource(R.drawable.ic_file);

                } else {
                    Toast.makeText(mContext, "DocFile 5 is not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                docpath5 = "";
                Toast.makeText(mContext, "Error to upload!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("PATH:-", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }

    private void valiidate(String uid, View v) {

        String name = et_name.getText().toString();
        String number = et_Mobile.getText().toString();
        String date = tv_Date.getText().toString();
        String hospital = et_Hospital.getText().toString();
        String desc = et_Deasc.getText().toString();

        if (name.equalsIgnoreCase("")) {
            et_name.setError("Can't be Empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can't be Empty!");
            et_Mobile.requestFocus();
        } else if (date.equalsIgnoreCase("")) {
            tv_Date.requestFocus();
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select date report!");
        } else if (hospital.equalsIgnoreCase("")) {
            et_Hospital.setError("Can't be Empty!");
            et_Hospital.requestFocus();
        } else if (path1.equalsIgnoreCase("") && path2.equalsIgnoreCase("")
                && path3.equalsIgnoreCase("") && path4.equalsIgnoreCase("")
                && path5.equalsIgnoreCase("") && docpath1.equalsIgnoreCase("")
                && docpath2.equalsIgnoreCase("") && docpath3.equalsIgnoreCase("")
                && docpath4.equalsIgnoreCase("") && docpath5.equalsIgnoreCase("")) {

            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select atleast one Image/pdf of patient report!");

        } else {

            ArrayList<MultipartBody.Part> parts = new ArrayList<>();
            MultipartBody.Part bdy = null;

            if (!path1.equalsIgnoreCase("")) {
                File file = new File(path1);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!path1.equalsIgnoreCase("")) {
                File file = new File(path1);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                bdy = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            }

            if (!path2.equalsIgnoreCase("")) {
                File file = new File(path2);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!path3.equalsIgnoreCase("")) {
                File file = new File(path3);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!path4.equalsIgnoreCase("")) {
                File file = new File(path4);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!path5.equalsIgnoreCase("")) {
                File file = new File(path5);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!docpath1.equalsIgnoreCase("")) {
                File file = new File(docpath1);
                Log.d("TAG", "valiidate: "+file);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!docpath2.equalsIgnoreCase("")) {
                File file = new File(docpath2);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!docpath3.equalsIgnoreCase("")) {
                File file = new File(docpath3);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!docpath4.equalsIgnoreCase("")) {
                File file = new File(docpath4);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }
            if (!docpath5.equalsIgnoreCase("")) {
                File file = new File(docpath5);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
                parts.add(body);
            }

//            Log.e("Type=>", "" + typ);
            requestToUpload(uid, name, number, date, hospital, desc, typ, parts, bdy, v);

        }

    }

    private void requestToUpload(String uid, String name, String number, String date, String h_name, String desc, String typ, ArrayList<MultipartBody.Part> parts, MultipartBody.Part part, View view) {

        rl_Loader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = AppConfig.loadInterface().addPatientreport(uid, name, number, date, h_name, desc, typ, parts);
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
                        System.out.println("AddReport" + object);

                        if (status.equalsIgnoreCase("1")) {

                            CustomSnakbar.showDarkSnakabar(mContext, view, "Report Added Successfully.");

//                            JSONObject result = object.optJSONObject("result");
//                            String lab_id = result.optString("lab_id");

                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Animatoo.animateSlideLeft(mContext);
                            startActivity(intent);
                            finish();

                           /* new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recreate();
                                }
                            }, 1500);*/


                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
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

    private void showPopupMenu(View button) {

        PopupMenu popup = new PopupMenu(mContext, button);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.update:
                        startActivity(new Intent(mContext, LabUpdateActivity.class));
                        Animatoo.animateCard(mContext);
                        break;

                    case R.id.logout:
                        logout();
                        break;

                }
//                Toast.makeText(mContext, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();

    }

    private void logout() {

        new AlertDialog.Builder(mContext)
                .setTitle(getResources().getString(R.string.logout))
                .setMessage(getResources().getString(R.string.logoutmsg))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        rl_Loader.setVisibility(View.VISIBLE);
                        Utility.setSharedPreference(mContext, "u_id", "");
                        Utility.setSharedPreference(mContext, "u_name", "");
                        Utility.setSharedPreference(mContext, "u_img", "");
                        Utility.setSharedPreference(mContext, "u_email", "");
                        Utility.setSharedPreference(mContext, "location", "");
                        Utility.setSharedPreference(mContext, "user_type", "");
                        Utility.setSharedPreferenceBoolean(mContext, "islogin", false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rl_Loader.setVisibility(View.GONE);
                                Intent intent = new Intent(mContext, LabLoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Animatoo.animateSlideLeft(mContext);
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);

                    }
                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

    private void getProfile(String uid, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getProfile(uid);
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

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String department = result.optString("department");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            String available = result.optString("available");

                            tv_Head.setText(name);
                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
                            }


                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
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
}