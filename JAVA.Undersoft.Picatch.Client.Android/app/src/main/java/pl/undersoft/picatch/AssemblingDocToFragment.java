package pl.undersoft.picatch;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class AssemblingDocToFragment extends Fragment {
 
	public ArrayList<PicatchModel> arrayList;
	
	ListAdapter<PicatchModel> adapter;
	//GridView gridView;
	ListView listView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_assembling_doc_to, container, false);
         
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

		arrayList = new ArrayList<PicatchModel>();

		adapter = new ListAdapter<PicatchModel>(
				getActivity(), R.layout.doc_list_layout, R.id.layout_1, arrayList);
		
		listView = (ListView) getActivity().findViewById(R.id.listView_a_doc_1);
		listView.setAdapter(adapter);

		Button add = (Button) getActivity().findViewById(R.id.a_doc_add_b_1);

		add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), PICATCH_COLLECTING_DOC_ADD.class));
			}
		});
    }

    @Override
    public void onResume() {
    	super.onResume();
    	
    	//adapter.clear();
    	arrayList.clear();
    	
    	SQLiteDatabase db = getActivity().openOrCreateDatabase("Baza", getActivity().MODE_WORLD_READABLE, null);
    	//db.execSQL("SELECT * FROM dok;");
    	 
        String countQuery = "SELECT * FROM dok;";
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        //int numerWiersza = 0;
        PicatchModel model;
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
            	model = new PicatchModel(
            		"" + cursor.getString(0),
            		"" + cursor.getString(1),
            		"" + cursor.getString(2),
            		"" + cursor.getString(3),
            		"" + cursor.getString(4));
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
