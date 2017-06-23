package com.example.a1jengm22.solenteatout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends Activity {

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        mv = (MapView) findViewById(R.id.mvID);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(13);
        mv.getController().setCenter(new GeoPoint(50.9027, -1.4048));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.newResID){
            Intent intent = new Intent (this, NewRestaurant.class);
            startActivityForResult(intent, 0);

            return true;
        }
        if (item.getItemId() == R.id.saveResID){

            try {
                PrintWriter pw =
                        new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.csv", true));

                for(int i=0; i<items.size(); i++){
                    OverlayItem itm = items.getItem(i);

                    pw.println(itm.getTitle()+","+itm.getSnippet()+","+itm.getPoint().getLatitude()+","+itm.getPoint().getLongitude());
                }
                pw.close();

            }
            catch (IOException e){
                new AlertDialog.Builder(this).setMessage("ERROR: " + e).
                        setPositiveButton("OK", null).show();

            }

        }
        if (item.getItemId() == R.id.setPrefID){
            Intent intent = new Intent (this, PreferencesActivity.class);
            startActivityForResult(intent, 1);

            return true;

        }
        if (item.getItemId() == R.id.loadResID){
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.csv"));
                String line;
                while((line = reader.readLine()) != null)
                {
                    String[] components = line.split(",");
                    if(components.length==4)
                    {
                        Double lat = Double.valueOf(components[2]).doubleValue();
                        Double lon = Double.valueOf(components[3]).doubleValue();
                        /*OverlayItem itm = new OverlayItem(components[0], components[1], new GeoPoint(lat, lon));
                        items.addItem(itm);*/

                    }
                }
            }
            catch(IOException e)
            {
                new AlertDialog.Builder(this).setMessage("ERROR: " + e).
                        setPositiveButton("OK", null).show();

            }


            return true;

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                Bundle extras = data.getExtras();

                String name = data.getExtras().getString("com.example.a1jengm22.name_input");
                String address = data.getExtras().getString("com.example.a1jengm22.address_input");
                String cuisine = data.getExtras().getString("com.example.a1jengm22.cuisine_input");
                String rating = data.getExtras().getString("com.example.a1jengm22.rating_input");

                double lat = mv.getMapCenter().getLatitude();
                double lon = mv.getMapCenter().getLongitude();

                //add new marker
                items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(),markerGestureListener);

                OverlayItem newRestaurant = new OverlayItem(name, address+" "+cuisine+" "+rating, new GeoPoint(lat,lon));

                items.addItem(newRestaurant);
                mv.getOverlays().add(items);

                //save to file
                SharedPreferences customPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean autosave = customPrefs.getBoolean("autosave", true);

                if(autosave){

                    try {
                        PrintWriter pw =
                                new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.csv", true));

                        for(int i=0; i<items.size(); i++){
                            OverlayItem itm = items.getItem(i);

                            pw.println(itm.getTitle()+","+itm.getSnippet()+","+itm.getPoint().getLatitude()+","+itm.getPoint().getLongitude());
                        }
                        pw.close();

                    }
                    catch (IOException e){
                        new AlertDialog.Builder(this).setMessage("ERROR: " + e).
                                setPositiveButton("OK", null).show();

                    }
                }



            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();



    }
}
