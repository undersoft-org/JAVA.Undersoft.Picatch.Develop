package pl.undersoft.picatch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class PICATCH_COLLECTING_DOC_DETAILS extends Activity {

	public ArrayList<PicatchModelDetails> arrayList;

	ListAdapterDetails<PicatchModelDetails> adapter;
	ListView listView;
	String valueId = "";
	public String ip;

	public String licence;

	public int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picatch__collecting__doc_details);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		valueId = getIntent().getExtras().getString("keyId");

		//System.out.println("p1");

		SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_WORLD_READABLE, null);
		String countQuery = "SELECT * FROM opcje WHERE id = 1";
		Cursor cursor = db.rawQuery(countQuery, null);
		if (cursor.moveToFirst()) {


			ip = "" + cursor.getString(3);


			port = 0 + cursor.getInt(9);


			licence = "" + cursor.getString(11);



		} else {
			System.out.println("brak rekordu");
		}

		db.close();

		arrayList = new ArrayList<PicatchModelDetails>();
		adapter = new ListAdapterDetails<PicatchModelDetails>(
				this, R.layout.doc_list_layout, R.id.layout_1, arrayList);

		listView = (ListView) findViewById(R.id.listView_2);
		listView.setAdapter(adapter);

		Button edit = (Button) findViewById(R.id.edit_b_2);

		edit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (adapter != null && adapter.selectedId != null
						&& adapter.selectedId.length() > 0) {
					Intent mIntent = new Intent(PICATCH_COLLECTING_DOC_DETAILS.this, PICATCH_COLLECTING_DOC_DETAILS_EDIT.class);
					mIntent.putExtra("keyId", "" + adapter.selectedId);
					startActivity(mIntent);
				} else {
					//Alert dialog : gdy brak zaznaczenia
					new AlertDialog.Builder(PICATCH_COLLECTING_DOC_DETAILS.this)
							.setTitle("Edit impossible")
							.setMessage("Are you sure you selected field ?")
							.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									//continue with delete

									//dialog.cancel();
								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();
				}
			}
		});


		Button collect = (Button) findViewById(R.id.collect_b_2);
		collect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (adapter != null) {
					Intent mIntent = new Intent(PICATCH_COLLECTING_DOC_DETAILS.this, PICATCH_COLLECTING_DOC_DETAILS_COLLECTING.class);
					mIntent.putExtra("keyId", "" + valueId);
					startActivity(mIntent);

//	        	listView.setAdapter(adapter);
//	            startActivity(new Intent(PICATCH_COLLECTING_DOC_DETAILS.this, PICATCH_COLLECTING_DOC_DETAILS_COLLECTING.class));
				} else {
					Toast.makeText(PICATCH_COLLECTING_DOC_DETAILS.this, "adapter == null.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button delete = (Button) findViewById(R.id.delete_b_2);

		delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (adapter != null && adapter.selectedId != null
						&& adapter.selectedId.length() > 0) {

					//Alert dialog : gdy brak zaznaczenia
					new AlertDialog.Builder(PICATCH_COLLECTING_DOC_DETAILS.this)
							.setTitle("Delete document detail")
							.setMessage("Are you sure you want to delete document detail ?")
							.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									//continue with delete
									deleteRow();
									onResume();
									//dialog.cancel();
								}
							})
							.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {


								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();


				} else {
					Toast.makeText(PICATCH_COLLECTING_DOC_DETAILS.this, "Zaznacz dokument, aby zobaczyć szczegóły", Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button send = (Button) findViewById(R.id.send_b_2);

		send.setOnClickListener(sendOnClickListener);
	}

	View.OnClickListener sendOnClickListener =
			new View.OnClickListener(){

				@Override
				public void onClick(View arg0) {
					System.out.println("*******   lacze");


					sendData SendData = new sendData(ip, port);

					SendData.execute();
				}};




	public class sendData extends AsyncTask<URL, Integer, Long> {



TextView sendStatus;
		String dstAddress;
		int dstPort;
		String response = "";
		private Void[] arg0;

		sendData(String addr, int port){
			dstAddress = addr;
			dstPort = port;
		}

		protected void onProgressUpdate(Integer... progress) {



			if(progress[0].intValue() == -2) {
				//progressBar.setActivated(true);
				sendStatus.setTextColor(Color.BLUE);
				sendStatus.setText("Sending");
			}else if (progress[0].intValue() == -3) {
				sendStatus.setTextColor(Color.RED);
				sendStatus.setText("Send Error");
				//df.setDecimalFormatSymbols(new DecimalFormatSymbols());
				//System.out.println("*******   dssss " + progress[0].intValue());
				//Toast.makeText(getApplicationContext(), "dodaje " + progress[0].intValue(), Toast.LENGTH_SHORT).show();
			} else {
				sendStatus.setTextColor(Color.GREEN);
				sendStatus.setText("Send Ok");

				//Toast.makeText(PICATCH_TCP_DOWNLOAD.this, "dodaje " + progress[0].intValue(), Toast.LENGTH_SHORT).show();

				//setProgressPercent(progress[0]);
			}
		}



		@Override
		protected Long doInBackground(URL... arg0) {
			//this.arg0 = arg0;
			sendStatus = (TextView) findViewById(R.id.sendProgress2);

			try {

				publishProgress(Integer.valueOf(-2));

				SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);

				Cursor cursor = db.rawQuery("SELECT nazwadok, typ, data  FROM dok WHERE id = " + valueId, null);

				File myFile = new File(getApplicationContext().getFilesDir() + "/export.txt");
				myFile.setWritable(true);
				myFile.setReadable(true);
				if (myFile.exists()) {
					System.out.println("*******   usuniety");
					myFile.delete();
				}


				FileWriter fileWriter = new FileWriter(myFile, true);
				BufferedWriter bw = new BufferedWriter(fileWriter);
				StringBuilder line = new StringBuilder();

				if (cursor.moveToFirst()) {

					line.append(cursor.getString(0));
					line.append(";");
					line.append(cursor.getString(1));
					line.append(";");
					line.append(cursor.getString(2));

					bw.write(line.toString());
					bw.newLine();
					line.delete(0, line.length());

				}

				cursor = db.rawQuery("SELECT dokid, kod, nazwa, cenazk, ilosc, stan, cenasp, vat  FROM bufor WHERE dokid = " + valueId, null);

				if (cursor.moveToFirst()) {
					do {

						line.append(cursor.getString(0));
						line.append(";");
						line.append(cursor.getString(1));
						line.append(";");
						line.append(cursor.getString(2));
						line.append(";");
						line.append(cursor.getString(3));
						line.append(";");
						line.append(cursor.getString(4));
						line.append(";");
						line.append(cursor.getString(5));
						line.append(";");
						line.append(cursor.getString(6));
						line.append(";");
						line.append(cursor.getString(7));
						bw.write(line.toString());
						bw.newLine();
						line.delete(0, line.length());
						System.out.println("*******   dodajedopliku");
					} while (cursor.moveToNext());
				}

				bw.close();
				db.close();
				fileWriter.close();

			} catch (IOException e) {
				publishProgress(Integer.valueOf(-3));
				// TODO Auto-generated catch block
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			Socket socket = null;

			try {

				SQLiteDatabase db2 = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);

				System.out.println("*******   dzialam");



				socket = new Socket(dstAddress, dstPort);

				byte[] sendorec = new byte[1];
				sendorec[0] = 1;
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(sendorec, 0, 1);


				byte[] buffer = new byte[1024];

				int bytesRead;


				//ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);


				System.out.println("*******   dzialam2");
    /*
     * notice:
     * inputStream.read() will block if no data return
     */





				/* while ((bytesRead = outputStream.write(buffer, 0, buffer.length);) > 0){
					System.out.println("*******   odbieram");
					//  byteArrayOutputStream.write(buffer);
					//  builder.append(byteArrayOutputStream.toString("UTF-8")) ;
					fileWriter.write();, 0, bytesRead);
				}
				fileWriter.flush();
				fileWriter.close();

*/
				System.out.println("*******   odebralem");
				//	SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);
				File myFile = new File(getApplicationContext().getFilesDir() + "/export.txt");
				//	byte[] filebyte = new byte[(int) myFile.length()];
				InputStream ios = null;

				ios = new FileInputStream(myFile);

				byte[] SendingBuffer = null;
				int NoOfPackets = (int) Math.ceil((double) myFile.length() / 1024);

				int TotalLength = (int)myFile.length(), CurrentPacketLength, counter = 0;
				for (int i = 0; i < NoOfPackets; i++)
				{
					if (TotalLength > 1024)
					{
						CurrentPacketLength = 1024;
						TotalLength = TotalLength - CurrentPacketLength;
					}
					else
					{
						CurrentPacketLength = TotalLength;
					}
					SendingBuffer = new byte[CurrentPacketLength];
					ios.read(SendingBuffer, 0, CurrentPacketLength);
					outputStream.write(SendingBuffer, 0, (int) SendingBuffer.length);
					System.out.println("*******   dodajedostrumienia");

				}


				ios.close();
				outputStream.close();
				System.out.println("*******   dzialam");
				boolean send = true;
				db2.execSQL("Update dok Set send = '1' WHERE id = " + valueId.toString());
				publishProgress(Integer.valueOf(-4));
				db2.close();

			} catch (UnknownHostException e) {
				publishProgress(Integer.valueOf(-3));
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				publishProgress(Integer.valueOf(-3));
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}finally{
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						publishProgress(Integer.valueOf(-3));
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}


	}


	private void deleteRow() {


		SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);
		db.delete("bufor", "id=" + adapter.selectedId, null);


	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	//adapter.clear();
    	arrayList.clear();
    	
    	SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_WORLD_READABLE, null);
    	//db.execSQL("SELECT * FROM dok;");
    	 
        String countQuery = "SELECT * FROM bufor WHERE dokid=" + valueId + ";";
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
    	
    	adapter.notifyDataSetChanged();
    	
    }
   
}

	
	
//	String[] numbers = new String[] { 
//			   "A", "B", "C", "D", "E",
//			   "F", "G", "H", "I", "J",
//			   "K", "L", "M", "N", "O",
//			   "P", "Q", "R", "S", "T",
//			   "Y", "Z"};
//	
//	public ArrayList<String> arrayList;
//	
//	ArrayAdapter<String> adapter;
//	GridView gridView;
//	
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picatch__collecting__doc_details);
//		
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//				|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//
//        arrayList = new ArrayList<String>();
//        for(int i = 0; i < numbers.length; i++) {
//        	arrayList.add(numbers[i]);
//        }
//        
////	    adapter = new ArrayAdapter<String>(
////		    	this, android.R.layout.simple_list_item_1, numbers);
//	    adapter = new ArrayAdapter<String>(
//	    		this, android.R.layout.simple_list_item_1, arrayList);
//        
//	    gridView = (GridView) findViewById(R.id.gridView_2);
//	    gridView.setAdapter(adapter);
//	    gridView.setOnItemClickListener(new OnItemClickListener() {
//	    	public void onItemClick(AdapterView<?> parent, View v,
//	    	    int position, long id) {
//	    	        Toast.makeText(getApplicationContext(),
//	    	        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//	    	    }
//	    	}
//	    );
//	    
//	    Button edit = (Button) findViewById(R.id.edit_b_2);
//	    edit.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View v) {
//	        	
//	        	try {
//	        		arrayList.add("Marlenka");
//	        	} catch(Exception ex1) {
//	        		System.out.println("ex1 = " + ex1.toString());
//	        	}
//	        	try {
//	     //   		adapter.add(new String("Marlenka"));
//	        	} catch(Exception ex2) {
//	        		System.out.println("ex2 = " + ex2.toString());
//	        	}
//	        	try {
//	        		adapter.notifyDataSetChanged();
//	        	} catch(Exception ex3) {
//	        		System.out.println("ex3 = " + ex3.toString());
//	        	}
//	        }
//	    });	  
//	    
//	    Button collect = (Button) findViewById(R.id.collect_b_2);
//	    collect.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View v) {
//	        	gridView.setAdapter(adapter);
//	            startActivity(new Intent(PICATCH_COLLECTING_DOC_DETAILS.this, PICATCH_COLLECTING_DOC_DETAILS_COLLECTING.class));
//	        }
//	    });
//    }
//    
//    @Override
//    protected void onResume() {
//    	super.onResume();
//    	
//    	arrayList.clear();
//    	
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	arrayList.add("Mar 1");
//    	arrayList.add("Mar 2");
//    	arrayList.add("Mar 3");
//    	adapter.notifyDataSetChanged();
//    	
//    }
//}
