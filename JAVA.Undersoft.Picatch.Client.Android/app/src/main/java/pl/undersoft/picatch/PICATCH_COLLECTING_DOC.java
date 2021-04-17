package pl.undersoft.picatch;


import java.io.BufferedWriter;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;


public class PICATCH_COLLECTING_DOC extends Activity {

	public ArrayList<PicatchModel> arrayList;
	
	ListAdapter<PicatchModel> adapter;
	//GridView gridView;
	ListView listView;

	public String ip;

	public String licence;

	public int port;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picatch__collecting__doc);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		//System.out.println("p1");

		arrayList = new ArrayList<PicatchModel>();
//	    adapter = new ArrayAdapterB(this.getApplicationContext(), arrayList);
//	    		this, android.R.layout.simple_list_item_1, arrayList);
		//System.out.println("p2");
		adapter = new ListAdapter<PicatchModel>(
				this, R.layout.doc_list_layout, R.id.layout_1, arrayList);
		//System.out.println("p3");
//	    gridView = (GridView) findViewById(R.id.gridView_1);
//	    gridView.setAdapter(adapter);
//    
//	    gridView.setOnItemClickListener(new OnItemClickListener() {
//	    	public void onItemClick(AdapterView<?> parent, View v,
//	    	    int position, long id) {
//	    		System.out.println("position : " + position);
//	    		System.out.println("id : " + id);
////	    	        Toast.makeText(getApplicationContext(),
////	    	        		((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//	    	    }
//	    	}
//	    );

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

		listView = (ListView) findViewById(R.id.doc_list_1_1);
		listView.setAdapter(adapter);

//		listView.setOnTouchListener(new View.OnTouchListener() {        
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//            	//System.out.println("*******    onTouch");
//            	try {
//            		System.out.println("*******    onTouch v.getTag() = " + (String)v.getTag());
//            		System.out.println("*******    onTouch v.getId() = " + v.getId());
//            		System.out.println("*******    onTouch v.getX() = " + v.getX());
//            		System.out.println("*******    onTouch v.getY() = " + v.getY());
//            	} catch(Exception ex1) {
//            		System.out.println("*******    onTouch ex1 : " + ex1.toString());
//            	}
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // PRESSED
//                    	v.setSelected(true);
//                    	System.out.println("*******    ACTION_DOWN");
//                    	//scanningEnable();
//                        return true; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        // RELEASED
//                    	v.setSelected(false);
//                    	System.out.println("*******    ACTION_UP");
//                    	//scanningDisable();
//                        return true; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

//		listView.setOnItemClickListener(new OnItemClickListener() {
//	    	public void onItemClick(AdapterView<?> parent, View v,
//	    	    int position, long id) {
//	    		System.out.println("position : " + position);
//	    		System.out.println("id : " + id);
////	    	        Toast.makeText(getApplicationContext(),
////	    	        		((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//	    	    }
//	    	}
//	    );

		Button add = (Button) findViewById(R.id.add_b_1);

		add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(PICATCH_COLLECTING_DOC.this, PICATCH_COLLECTING_DOC_ADD.class));
			}
		});

		Button edit = (Button) findViewById(R.id.edit_b_1);

		edit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (adapter != null && adapter.selectedId != null
						&& adapter.selectedId.length() > 0) {
					Intent mIntent = new Intent(PICATCH_COLLECTING_DOC.this, PICATCH_COLLECTING_DOC_EDIT.class);
					mIntent.putExtra("keyId", "" + adapter.selectedId);
					startActivity(mIntent);
				} else {
					//Alert dialog : gdy brak zaznaczenia
					new AlertDialog.Builder(PICATCH_COLLECTING_DOC.this)
							.setTitle("Edit impossible")
							.setMessage("Are you sure you selected document ?")
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

		Button details = (Button) findViewById(R.id.details_b_1);

		details.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (adapter != null && adapter.selectedId != null
						&& adapter.selectedId.length() > 0) {
					Intent mIntent = new Intent(PICATCH_COLLECTING_DOC.this, PICATCH_COLLECTING_DOC_DETAILS.class);
					mIntent.putExtra("keyId", "" + adapter.selectedId);
					startActivity(mIntent);
					//startActivity(new Intent(PICATCH_COLLECTING_DOC.this, PICATCH_COLLECTING_DOC_DETAILS.class));
				} else {
					Toast.makeText(PICATCH_COLLECTING_DOC.this, "Zaznacz dokument, aby zobaczyć szczegóły", Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button delete = (Button) findViewById(R.id.delete_b_1);

		delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (adapter != null && adapter.selectedId != null
						&& adapter.selectedId.length() > 0) {

					//Alert dialog : gdy brak zaznaczenia
					new AlertDialog.Builder(PICATCH_COLLECTING_DOC.this)
							.setTitle("Delete document")
							.setMessage("Are you sure you want to delete document ?")
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
					Toast.makeText(PICATCH_COLLECTING_DOC.this, "Zaznacz dokument, aby zobaczyć szczegóły", Toast.LENGTH_SHORT).show();
				}
			}
		});


		Button send = (Button) findViewById(R.id.send_b_1);

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




		String dstAddress;
		int dstPort;
		String response = "";
		private Void[] arg0;
		TextView sendStatus;


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

		protected void onPostExecute(Long result) {
			//showDialog("Downloaded " + result + " bytes");
		}

		@Override
		protected Long doInBackground(URL... arg0) {
			//this.arg0 = arg0;
			sendStatus = (TextView) findViewById(R.id.sendProgress);

			try {

				System.out.println("*******   dzialam");
				publishProgress(Integer.valueOf(-2));

				SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);

				Cursor cursor = db.rawQuery("SELECT nazwadok, typ, data  FROM dok WHERE id = " + adapter.selectedId, null);

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

				cursor = db.rawQuery("SELECT dokid, kod, nazwa, cenazk, ilosc, stan, cenasp, vat  FROM bufor WHERE dokid = " + adapter.selectedId, null);

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
				e.printStackTrace();
			}


			Socket socket = null;

			try {



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

				SQLiteDatabase db2 = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);




				ios.close();
				outputStream.close();
				boolean send = true;
				db2.execSQL("Update dok Set send = 1 WHERE id = " + adapter.selectedId.toString());
				publishProgress(Integer.valueOf(-4));
				db2.close();
				System.out.println("*******   dzialam");
				publishProgress(Integer.valueOf(-4));

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				publishProgress(Integer.valueOf(-3));
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				publishProgress(Integer.valueOf(-3));
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}finally{
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
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
		db.delete("dok", "id=" + adapter.selectedId, null);


	}

    @Override
    protected void onResume() {
    	super.onResume();
    	
    	//adapter.clear();
    	arrayList.clear();
    	
    	SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_WORLD_READABLE, null);
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


