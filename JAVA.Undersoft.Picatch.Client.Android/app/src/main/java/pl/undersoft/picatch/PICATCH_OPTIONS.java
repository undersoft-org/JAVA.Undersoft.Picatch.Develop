package pl.undersoft.picatch;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class PICATCH_OPTIONS extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__options);
        
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_WORLD_READABLE, null);
        String countQuery = "SELECT * FROM opcje WHERE id = 1";
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {

            EditText depot_ip = (EditText) findViewById(R.id.depot_ip_t);
            depot_ip.setText("" + cursor.getString(3));

            EditText depot_port = (EditText) findViewById(R.id.depot_port_t);
            depot_port.setText("" + cursor.getInt(9));

            EditText depot_licence = (EditText) findViewById(R.id.depot_licence_t);
            depot_licence.setText("" + cursor.getString(11));



        } else {
            System.out.println("brak rekordu");
        }

        db.close();

        Button save_b = (Button) findViewById(R.id.options_save_b);

        save_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save_options();
                Toast.makeText(getApplicationContext(), "Opitons saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }



    public void save_options() {
        try {


            EditText depot_ip = (EditText) findViewById(R.id.depot_ip_t);
            String ip = depot_ip.getText().toString();
            EditText depot_port = (EditText) findViewById(R.id.depot_port_t);
            int port = Integer.valueOf(depot_port.getText().toString());
            EditText depot_licence = (EditText) findViewById(R.id.depot_licence_t);


            SQLiteDatabase db = openOrCreateDatabase("Baza", SQLiteDatabase.OPEN_READWRITE, null);
            ContentValues values = new ContentValues();
            values.put("ip", "" + ip);
            values.put("port", 0 + port);
            //values.put("data", getDateTime());
            values.put("licence", "" + depot_licence.getText().toString());

            db.update("opcje", values, "id=1", null);
            //db.insert("dok", null, values);

            db.close();
            System.out.println("*******   updated   true");
        } catch (Exception ex) {
            System.out.println("*******   updated   false");
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
  //  @Override
   /* public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picatch__warehouse2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
