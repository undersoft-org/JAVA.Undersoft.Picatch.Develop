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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class PICATCH_REGISTERS_PRODUCTS extends Activity {

    public ArrayList<PicatchModelDetails> arrayList;

    ListAdapterDetails<PicatchModelDetails> adapter;
    //GridView gridView;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__registers_products);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);



        arrayList = new ArrayList<PicatchModelDetails>();
        adapter = new ListAdapterDetails<PicatchModelDetails>(
                this, R.layout.doc_list_layout, R.id.layout_1, arrayList);

        listView = (ListView) findViewById(R.id.reg_prod_list);
        listView.setAdapter(adapter);


        Spinner spinner = (Spinner) findViewById(R.id.spinner_filter);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_filter_list, R.layout.spinner_list_products);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);




    }

//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date(System.currentTimeMillis());
//        return dateFormat.format(date);
//    }


    @Override
    protected void onResume() {
        super.onResume();

        //adapter.clear();
        arrayList.clear();

        SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_WORLD_READABLE, null);
        //db.execSQL("SELECT * FROM dok;");

        String countQuery = "SELECT * FROM bufor;";
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        //int numerWiersza = 0;
        PicatchModelDetails model;
        if (cursor.moveToFirst()) {
            do {
                //System.out.println("numerWiersza : " + numerWiersza);
//            	System.out.println("0:" + cursor.getString(0));//Integer.parseInt(cursor.getString(0)));
//
//            	System.out.println("getCount:" + cursor.getCount());
//            	System.out.println("getType:" + cursor.getType(0));
//            	try {
//            		System.out.println("parseInt:" + Integer.parseInt(cursor.getString(0)));
//            	} catch(Exception ex0) {
//            		System.out.println("ex0:" + ex0.toString());
//            	}
//            	try {
//            		System.out.println("getInt(0):" + cursor.getInt(0));
//            	} catch(Exception ex1) {
//            		System.out.println("ex1:" + ex1.toString());
//            	}
//
//            	System.out.println("1:" + cursor.getString(1));
//            	System.out.println("2:" + cursor.getString(2));
//            	System.out.println("3:" + cursor.getString(3));
//            	System.out.println("4:" + cursor.getString(4));
                //System.out.println("4:" + Integer.parseInt(cursor.getString(4)));

//            	arrayList.add("" + cursor.getString(0));
//            	arrayList.add("" + cursor.getString(1));
//            	arrayList.add("" + cursor.getString(2));
//            	arrayList.add("" + cursor.getString(3));
//            	arrayList.add("" + cursor.getString(4));
                model = new PicatchModelDetails(
                        "" + cursor.getString(0),
                        "" + cursor.getString(1),
                        "" + cursor.getString(2),
                        "" + cursor.getString(3),
                        "" + cursor.getString(5),
                        "" + cursor.getString(6),
                        "" + cursor.getString(7)
                );
                arrayList.add(model);
                //numerWiersza++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        //adapter = new ArrayAdapterB(this.getApplicationContext(), arrayList);
        //this, android.R.layout.simple_list_item_1, arrayList);
        //gridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }


}
