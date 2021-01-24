package my.edu.utem.ftmk.foodorderv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import my.edu.utem.ftmk.foodorderv2.view.category.CategoryActivity;
import my.edu.utem.ftmk.foodorderv2.view.detail.DetailActivity;
import my.edu.utem.ftmk.foodorderv2.view.home.HomeActivity;

public class OrderActivity extends AppCompatActivity implements LocationListener {

    TextView textTitle, textCategory, txtLoc;
    Button btn_loc, btn_cancel, btn_payy;
    FusedLocationProviderClient fusedLocationProviderClient;
    String title, category;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        textTitle = findViewById(R.id.txtTitle);
        textCategory = findViewById(R.id.txtCat);
        txtLoc = findViewById(R.id.txtLocation);
        btn_loc = findViewById(R.id.btn_location);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_payy = findViewById(R.id.btn_pay);
        btn_payy.setEnabled(false);

        SharedPreferences sharedpreferences = getSharedPreferences(DetailActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        title = sharedpreferences.getString("title", "NO TITLE");
        category = sharedpreferences.getString("category", "NO CATEGORY");

        textTitle.setText(title);
        textCategory.setText(category);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(OrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(OrderActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderActivity.this, HomeActivity.class));
                finish();
            }
        });

        btn_payy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                builder.setTitle("Thank You!");
                builder.setMessage("Enjoy Your Food! Have a nice day. Here is your " + title + ". This will be delivered at " + txtLoc.getText().toString() + ".");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(OrderActivity.this, CategoryActivity.class));
                    finish();

                    dbInsert();
                }
            });
            builder.show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, OrderActivity.this);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dbInsert() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String location = txtLoc.getText().toString();
        String usrID = currentFirebaseUser.getUid();

        SQLiteDatabase orderdb = getApplicationContext().openOrCreateDatabase("CustOrder", MODE_PRIVATE, null);

        orderdb.execSQL("create table if not exists Cust_Order(User_ID varchar, Food_Name varchar, Food_Category varchar, Location varchar)");

        orderdb.execSQL("insert into Cust_Order(User_ID, Food_Name, Food_Category, Location) values ('"+ usrID +"', '"+ title +"', '"+ category +"', '"+ location +"')");

        orderdb.close();

        Toast.makeText(getApplicationContext(), "SUCCESS INSERT INTO DB!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();

        try {
            Geocoder geocoder = new Geocoder(OrderActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            txtLoc.setText(address);
            btn_payy.setEnabled(true);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}