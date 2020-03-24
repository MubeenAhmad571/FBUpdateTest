package com.example.fbupdatetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore objectFirebaseFirestore;
    private CollectionReference ObjectCollectioReference;
    private Dialog objectDialog;
    private EditText documentET,cityNameET,cityDetailsET;
    private TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectFirebaseFirestore=FirebaseFirestore.getInstance();

        objectDialog=new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait);

        documentET=findViewById(R.id.documentIDET);
        cityNameET=findViewById(R.id.cityNameET);
        cityDetailsET=findViewById(R.id.cityDetailsET);

        textViewData = findViewById(R.id.text_view_data);
        textViewData.setMovementMethod(new ScrollingMovementMethod());
        try {
             objectFirebaseFirestore=FirebaseFirestore.getInstance();
             ObjectCollectioReference=objectFirebaseFirestore.collection("NewCities");
        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void addValues(View v)
    {
        try {
            if(!documentET.getText().toString().isEmpty() && !cityNameET.getText().toString().isEmpty()
                    && !cityDetailsET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("city_name", cityNameET.getText().toString());
                objectMap.put("city_details", cityDetailsET.getText().toString());
                objectFirebaseFirestore.collection("NewCities")
                        .document(documentET.getText().toString()).set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to add data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "addValues:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    String allData ="";
    public void loadValues(View v) {
        try {
                 objectDialog.show();
                 ObjectCollectioReference.get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         for(DocumentSnapshot objectDocumentReference:queryDocumentSnapshots) {
                             String cityID=objectDocumentReference.getId();
                             String cityName=objectDocumentReference.getString("city_name");
                             String cityDetails=objectDocumentReference.getString("city_details");
                             allData+="City ID : "+cityID+'\n'+"City Name : "+cityName+'\n'+"City Detail : "+cityDetails+'\n'+"___________________________________"+'\n';
                         }
                         textViewData.setText(allData);
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         objectDialog.dismiss();
                      // Toast.makeText(this,"Fails to retriveve Collection",Toast.LENGTH_LONG).show();
                     }
                 });
        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}
