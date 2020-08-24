package miituo.com.miituo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import miituo.com.miituo.R;
import miituo.com.miituo.VehicleModelAdapter;
import miituo.com.miituo.adapters.DescriptionAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static miituo.com.miituo.activities.MainActivity.result;

public class DescriptionSellector extends AppCompatActivity {

    public Typeface tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_sellector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Descripción");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tipo = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");

        ListView listDesc=(ListView)findViewById(R.id.listDesc);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        DescriptionAdapter vadapter = new DescriptionAdapter(getApplicationContext(), CotizarAutoActivity.tokenDescripciones.subList(1,CotizarAutoActivity.tokenDescripciones.size()),typeface);
        listDesc.setAdapter(vadapter);
        listDesc.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent returnIntent = new Intent();
                                                returnIntent.putExtra("result","OK");
                                                returnIntent.putExtra("resultInt",position+1);
                                                setResult(AppCompatActivity.RESULT_OK,returnIntent);
                                                finish();
                                            }
                                        }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            Intent returnIntent = new Intent();
            setResult(AppCompatActivity.RESULT_CANCELED, returnIntent);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
                super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(AppCompatActivity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
