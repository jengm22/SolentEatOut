package com.example.a1jengm22.solenteatout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewRestaurant extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant);

        Button add = (Button) findViewById(R.id.addButtonID);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addButtonID){

            EditText name = (EditText) findViewById(R.id.rnameid);
            EditText address = (EditText) findViewById(R.id.raddressid);
            EditText cuisine = (EditText) findViewById(R.id.rcuisineid);
            EditText rating= (EditText) findViewById(R.id.rratingid);

            String sname = name.getText().toString();
            String saddress = address.getText().toString();
            String scuisine = cuisine.getText().toString();
            String srating = rating.getText().toString();

            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putString("com.example.a1jengm22.name_input", sname);
            bundle.putString("com.example.a1jengm22.address_input", saddress);
            bundle.putString("com.example.a1jengm22.cuisine_input", scuisine);
            bundle.putString("com.example.a1jengm22.rating_input", srating);

            intent.putExtras(bundle);

            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
