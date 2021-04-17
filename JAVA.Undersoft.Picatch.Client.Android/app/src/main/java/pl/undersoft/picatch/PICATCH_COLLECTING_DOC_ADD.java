package pl.undersoft.picatch;


import android.app.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PICATCH_COLLECTING_DOC_ADD extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__collecting__doc_add);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Button save = (Button) findViewById(R.id.save_b);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insert_doc();
                Toast.makeText(getApplicationContext(), "Document Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.doc_type_s_1_1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.doc_types, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        EditText edit_doc_date = (EditText) findViewById(R.id.doc_date_t);
        edit_doc_date.setText(getDateTime());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    public void insert_doc() {
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

            db.insert("dok", null, values);
            
            db.close();
            System.out.println("*******   saved   true");
        } catch (Exception ex) {
            System.out.println("*******   saved   false");
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
