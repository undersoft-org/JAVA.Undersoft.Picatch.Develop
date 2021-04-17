package pl.undersoft.picatch;


import android.app.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PICATCH_COLLECTING_DOC_EDIT extends Activity {

	String valueId = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__collecting__doc_edit);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        valueId = getIntent().getExtras().getString("keyId");
        
    	SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_WORLD_READABLE, null);
        String countQuery = "SELECT * FROM dok WHERE id=" + valueId + ";";
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
        	
            EditText edit_doc_name = (EditText) findViewById(R.id.col_details_edit_name_t);
            edit_doc_name.setText("" + cursor.getString(1));
        	
            Spinner spinner = (Spinner) findViewById(R.id.doc_type_s_1_1);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.doc_types, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(cursor.getString(2));
            spinner.setSelection(spinnerPosition);
        	
            EditText edit_doc_date = (EditText) findViewById(R.id.doc_date_t);
            edit_doc_date.setText("" + cursor.getString(3));
        } else {
        	System.out.println("brak rekordu");
        }
        
        db.close();
        
        Button update = (Button) findViewById(R.id.update_b);

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	update_doc();
                Toast.makeText(getApplicationContext(), "Document updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date(System.currentTimeMillis());
//        return dateFormat.format(date);
//    }

    public void update_doc() {
        try {
            EditText edit_doc_name = (EditText) findViewById(R.id.col_details_edit_name_t);
            String doc_name = edit_doc_name.getText().toString();
            Spinner edit_doc_type = (Spinner) findViewById(R.id.doc_type_s_1_1);
            String doc_type = edit_doc_type.getSelectedItem().toString();
            
            SQLiteDatabase db = openOrCreateDatabase("Baza", SQLiteDatabase.OPEN_READWRITE, null);
            ContentValues values = new ContentValues();
            values.put("nazwadok", doc_name);
            values.put("typ", doc_type);
            //values.put("data", getDateTime());
            EditText edit_doc_date = (EditText) findViewById(R.id.doc_date_t);
            values.put("data", edit_doc_date.getText().toString());
            values.put("send", 0);

            db.update("dok", values, "id=" + valueId, null);
            //db.insert("dok", null, values);
            
            db.close();
            System.out.println("*******   updated   true");
        } catch (Exception ex) {
            System.out.println("*******   updated   false");
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
