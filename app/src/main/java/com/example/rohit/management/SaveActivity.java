package com.example.rohit.management;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.rohit.management.Adapter.OrderAdapter;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SaveActivity extends AppCompatActivity {
    String bNo,cname,cNo,tAmt,adAmt,bAmt,billdate,status,imgBase64="",type;
    EditText customerName,contactNo,totalAmt,advanceAmt;
    TextView balanceAmt;
    Spinner typeOfMurti;
    RadioButton pendinRadioBtn , completeRadioBtn ;
    ImageView edit,compressedImageView;
    Button btnSave;
    LinearLayout uploadImage;
    TextView billNo,billDate;
    Webservices webservices;
    private File actualImage;
    private File compressedImage;
    Bitmap bitmap ;
    common commonObj;
    ArrayAdapter<String> adp1;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        webservices = new Webservices();
        commonObj= new common();


        customerName = findViewById(R.id.custName);
        contactNo = findViewById(R.id.txtContactNo);
        typeOfMurti = findViewById(R.id.typeOfMurti);
        totalAmt = findViewById(R.id.txtTotalAmt);
        advanceAmt = findViewById(R.id.txtAdvanceAmt);
        balanceAmt = findViewById(R.id.txtBalanceAmt);
        billNo = findViewById(R.id.billNo);
        billDate = findViewById(R.id.billDate);
        btnSave =  findViewById(R.id.btnSave);

        pendinRadioBtn =findViewById(R.id.pending);
        completeRadioBtn = findViewById(R.id.complete);
        uploadImage = findViewById(R.id.uploadImageBtn);

        customerName.setEnabled(false);
        contactNo.setEnabled(false);
        typeOfMurti.setEnabled(false);
        totalAmt.setEnabled(false);
        advanceAmt.setEnabled(false);
        balanceAmt.setEnabled(false);

        pendinRadioBtn.setEnabled(false);
        completeRadioBtn.setEnabled(false);

        String [] types = {"lahan murti","mothi murti","medium murti"};
        adp1= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, types);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfMurti.setAdapter(adp1);

        compressedImageView = findViewById(R.id.before_compressed_image);
        edit =findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName.setEnabled(true);
                contactNo.setEnabled(true);
                typeOfMurti.setEnabled(true);
                totalAmt.setEnabled(true);
                advanceAmt.setEnabled(true);
                balanceAmt.setEnabled(true);

                pendinRadioBtn.setEnabled(true);
                completeRadioBtn.setEnabled(true);

                customerName.findFocus();
            }
        });


        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){

            billNo.setText(bundle.getString("billNo"));
            customerName.setText(bundle.getString("customerName"));
            totalAmt.setText(bundle.getString("totalAmount"));
            contactNo.setText(bundle.getString("contactNo"));
            billDate.setText(bundle.getString("orderDate"));
            advanceAmt.setText(bundle.getString("advanceAmt"));
            balanceAmt.setText(bundle.getString("pendingAmt"));
            typeOfMurti.setSelection(adp1.getPosition(bundle.getString("type")));
            String imgString = OrderAdapter.imageString;
            if(!imgString.isEmpty()){
                byte[] decodedString = Base64.decode(imgString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                compressedImageView.setImageBitmap(bitmap);
            }

            String status =bundle.getString("status");
            if(status.equals("Pending")){
                pendinRadioBtn.setChecked(true);
            }else {
                completeRadioBtn.setChecked(true);
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bNo =  billNo.getText().toString();
                cname = customerName.getText().toString();
                cNo = contactNo.getText().toString();
                tAmt = totalAmt.getText().toString();
                adAmt = advanceAmt.getText().toString();
                bAmt = balanceAmt.getText().toString();
                billdate = billDate.getText().toString();
                type = typeOfMurti.getSelectedItem().toString();

                if(pendinRadioBtn.isChecked()){
                    status = "Pending";
                }else {
                    status ="Completed" ;
                }

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new updateOrderAsync().execute();
                }
                else{
                    commonObj.showAlertForInetnet(SaveActivity.this);
                }



            }
        });

        pendinRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeRadioBtn.setChecked(false);
            }
        });

        completeRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendinRadioBtn.setChecked(false);
            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(view);
            }
        });

        totalAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int aAmt = Integer.parseInt(advanceAmt.getText().toString());
                int tAmt= Integer.parseInt(totalAmt.getText().toString());
                advanceAmt.setEnabled(true);
                try{
                if(tAmt > aAmt){
                   int bAmt =tAmt - aAmt;
                    balanceAmt.setText(bAmt+"");
                }
                else{
                   Toast.makeText(SaveActivity.this,"Please enter proper amount",Toast.LENGTH_SHORT).show();
                }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        advanceAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(totalAmt.getText().toString().isEmpty()){
                    Toast.makeText(SaveActivity.this,"Please fill Total Amount first",Toast.LENGTH_SHORT).show();
                    advanceAmt.setEnabled(false);
                }
                else{
                    try{
                        int bal = Integer.parseInt(totalAmt.getText().toString())-Integer.parseInt(String.valueOf(charSequence));
                        if(bal < 0){
                            Toast.makeText(SaveActivity.this,"Please enter valid amount",Toast.LENGTH_SHORT).show();
                        }else{
                            balanceAmt.setText(bal+"");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!");
                return;
            }
            try {
                actualImage = FileUtil.from(this, data.getData());
                compressImage();
            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        }
    }

    private void setCompressedImage() {
        compressedImageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
        bitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
    }


    public void chooseImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void compressImage() {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {

            // Compress image in main thread
            //compressedImage = new Compressor(this).compressToFile(actualImage);
            //setCompressedImage();

            // Compress image to bitmap in main thread
            //compressedImageView.setImageBitmap(new Compressor(this).compressToBitmap(actualImage));

            // Compress image using RxJava in background thread
            new Compressor(this)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            showError(throwable.getMessage());
                        }
                    });
        }
    }

    public void customCompressImage(View view) {
        if (actualImage == null) {
            showError("Please choose an image!");
        }
        else
        {
            // Compress image in main thread using custom Compressor
            try {
                compressedImage = new Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStorageDirectory().getAbsolutePath())
                        .compressToFile(actualImage);

                setCompressedImage();
            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getMessage());
            }

            // Compress image using RxJava in background thread with custom Compressor
            /*new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            showError(throwable.getMessage());
                        }
                    });*/
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


    class updateOrderAsync extends AsyncTask<Void,Void,Void> {
        String response=null;
        ProgressDialog pd ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(SaveActivity.this);
            pd.setMessage("Please wait... ");
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            imgBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

            response = webservices.updateOrder("update_order_details",bNo,billdate,cname,type,String.valueOf(tAmt),String.valueOf(bAmt),String.valueOf(adAmt),cNo,"pending",imgBase64);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pd.isShowing()){
                pd.dismiss();
            }
            if(response!=null){
                try {
                    JSONObject jObject = new JSONObject(response);
                    Log.d("response",response);
                    //txtBillNo.setText("");


                    new MaterialStyledDialog.Builder(SaveActivity.this)
                            .setTitle("Success!")
                            .setDescription("You have successfully updated")
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Log.d("MaterialStyledDialogs", "Do something!");
                                    finish();
                                }})
                            .show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

            }

        }
    }

}

