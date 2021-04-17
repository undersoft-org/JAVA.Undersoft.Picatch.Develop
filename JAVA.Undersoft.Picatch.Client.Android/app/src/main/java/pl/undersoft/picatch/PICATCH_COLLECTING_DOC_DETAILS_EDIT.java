package pl.undersoft.picatch;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class PICATCH_COLLECTING_DOC_DETAILS_EDIT extends Activity {

	String valueId = "";

    EditText edit_details_name;
    EditText edit_details_barcode;
    EditText edit_details_count;
    EditText edit_details_qty;
    EditText edit_details_stock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__collecting__doc_details_edit);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        valueId = getIntent().getExtras().getString("keyId");
        
    	SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);
        String countQuery = "SELECT * FROM bufor WHERE id=" + valueId + ";";
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
        	
            edit_details_name = (EditText) findViewById(R.id.col_details_edit_name_t);
            edit_details_name.setText("" + cursor.getString(3));
        	

            edit_details_barcode = (EditText) findViewById(R.id.col_details_edit_barcode_t);
            edit_details_barcode.setText("" + cursor.getString(2));

            edit_details_count = (EditText) findViewById(R.id.count_e_t);
            edit_details_count.setText("" + String.valueOf(cursor.getDouble(5)));

            edit_details_qty = (EditText) findViewById(R.id.ilosc_e_t);
            edit_details_qty.setText("" + String.valueOf(cursor.getDouble(5)));

            edit_details_stock = (EditText) findViewById(R.id.stan_e_t);
            edit_details_stock.setText("" + cursor.getString(6));

            EditText edit_details_sell = (EditText) findViewById(R.id.cenasp_e_t);
            edit_details_sell.setText("" + cursor.getString(7));

            EditText edit_details_buy = (EditText) findViewById(R.id.cenazk_e_t);
            edit_details_buy.setText("" + String.valueOf(cursor.getDouble(4)));

            EditText edit_details_tax = (EditText) findViewById(R.id.tax_e_t);
            edit_details_tax.setText("" + cursor.getString(8));

            //edit_details_qty.requestFocus();
            //edit_details_qty.selectAll();



        } else {
        	System.out.println("brak rekordu");
        }
        //showingKeyBoard();
        db.close();
        
        Button update = (Button) findViewById(R.id.update_e_b);

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SaveScan();
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

    private void SaveScan() {



            Double ilosc_d = Double.parseDouble(String.valueOf(edit_details_qty.getText()));
            String kodbuf = edit_details_barcode.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);


                ContentValues values = new ContentValues();
                String table = "bufor";
                String[] col = {"id", "kod", "dokid", "nazwa", "stan", "cenazk", "cenasp", "vat"};
                String[] testkod = {kodbuf, valueId};
                Cursor cursor = db.query(table, col, "kod = ? AND id = ?", testkod, null, null, null);
                if (cursor.moveToFirst()) {

                    String id = cursor.getString(0);


                    values.put("ilosc", ilosc_d);

                    db.update("bufor", values, "id=" + id, null);

                }


        db.close();






    }

    private void showingKeyBoard() {
        InputMethodManager mgr = (InputMethodManager)  getSystemService(this.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        mgr.showSoftInput(edit_details_qty, InputMethodManager.SHOW_IMPLICIT);


    }

    private void hidingKeyBoard() {
        InputMethodManager mgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(edit_details_qty.getWindowToken(), 0);
    }

}
