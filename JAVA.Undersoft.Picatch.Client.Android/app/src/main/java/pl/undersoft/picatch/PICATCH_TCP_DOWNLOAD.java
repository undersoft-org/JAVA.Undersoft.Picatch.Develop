package pl.undersoft.picatch;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class PICATCH_TCP_DOWNLOAD extends Activity {


//	public ProgressBar downloadProgressBar;
//	public Handler downloadHandler = new Handler();
//	public int downloadStatus = 0;
//	
//	public ProgressBar insertProgressBar;
//	public Handler insertHandler = new Handler();
//	public int insertStatus = 0;

	public Context con = null;
	
	public static int jj = 0;

    public String ip;

    public String licence;

    public int port;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__data_transfer);


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


           Button buttonConnect = (Button)findViewById(R.id.download_data_b);

            buttonConnect.setOnClickListener(buttonConnectOnClickListener);
            
            con = this.getApplicationContext();
        }

        OnClickListener buttonConnectOnClickListener =
                new OnClickListener(){

                    @Override
                    public void onClick(View arg0) {
                        System.out.println("*******   lacze");
                        DownloadData downloadData = new DownloadData(ip, port);//, PICATCH_TCP_DOWNLOAD.this);
                        downloadData.execute();
                    }};

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    
    public void showToast(int j) {
    	Toast.makeText(con, "" + j, Toast.LENGTH_SHORT).show();
    }

    private class DownloadData extends AsyncTask<URL, Integer, Long> {

            String dstAddress;
            int dstPort;
            String response = "";
            private Void[] arg0;
            public PICATCH_TCP_DOWNLOAD aPICATCH_TCP_DOWNLOAD = null;

            TextView textView;
            //ProgressBar progressBar;
        	public ProgressBar downloadProgressBar;
        	public Handler downloadHandler = new Handler();
        	public int downloadStatus = 0;
        	
        	public ProgressBar insertProgressBar;
        	public Handler insertHandler = new Handler();
        	public int insertStatus = 0;

            
            
            DownloadData(String addr, int port) {
            //DownloadData(String addr, int port, PICATCH_TCP_DOWNLOAD lPICATCH_TCP_DOWNLOAD){
                dstAddress = addr;
                dstPort = port;
                //aPICATCH_TCP_DOWNLOAD = lPICATCH_TCP_DOWNLOAD;
                
                textView = (TextView) findViewById(R.id.textView_dt_3);
                downloadProgressBar = (ProgressBar) findViewById(R.id.download_progressBar);
                insertProgressBar = (ProgressBar) findViewById(R.id.insert_progressBar);
                
            }

            protected void onProgressUpdate(Integer... progress) {
            	
            	
            	
            	if(progress[0].intValue() == -2) {
            		//progressBar.setActivated(true);
            		downloadProgressBar.setIndeterminate(true);
            	} else if(progress[0].intValue() == -1) {
            		//progressBar.setActivated(false);
            		downloadProgressBar.setIndeterminate(false);
            		downloadProgressBar.setProgress(100);
            	} else if(progress[0].intValue() == -3) {
            		
            		//System.out.println("*******   progress[0].intValue() = " + progress[0].intValue());
            		//System.out.println("*******   progress[1].intValue() = " + progress[1].intValue());
            		
            		insertProgressBar.setMax(progress[1].intValue());
            	} else if(progress[0].intValue() == -4) {
            		insertProgressBar.setProgress(insertProgressBar.getMax());
            	} else {
            	
            		
        			insertProgressBar.setProgress(progress[0].intValue());
        		
        			DecimalFormat df = new DecimalFormat("0.0");
        			df.setDecimalSeparatorAlwaysShown(true);
        			//df.setDecimalFormatSymbols(new DecimalFormatSymbols());
	            	//System.out.println("*******   dssss " + progress[0].intValue());
	            	//Toast.makeText(getApplicationContext(), "dodaje " + progress[0].intValue(), Toast.LENGTH_SHORT).show();
            		textView.setText("" + df.format((double)(progress[0].intValue()/100000)/10.0) + " MB");
            		
            		//Toast.makeText(PICATCH_TCP_DOWNLOAD.this, "dodaje " + progress[0].intValue(), Toast.LENGTH_SHORT).show();
            	
            		//setProgressPercent(progress[0]);
            	}
            }
            
            protected void onPostExecute(Long result) {
                //showDialog("Downloaded " + result + " bytes");
            }            
            
            @Override
            protected Long doInBackground(URL... urls) {
                this.arg0 = arg0;

                Socket socket = null;

                try {


                    System.out.println("*******   dzialam");
                    publishProgress(Integer.valueOf(-2));

                    socket = new Socket(dstAddress, dstPort);

                    byte[] sendorec = new byte[1];
                    sendorec[0] = 2;
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(sendorec, 0, 1);

                    byte[] buffer = new byte[1024];

                    int bytesRead;

                    //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);

                    InputStream inputStream = socket.getInputStream();
                    System.out.println("*******   dzialam2");
    //
    // notice:
    // inputStream.read() will block if no data return
    //
                    
                    File myFile =  new File(getApplicationContext().getFilesDir() + "/import.txt");
                    if(myFile.exists()) {
                        myFile.delete();
                        System.out.println("*******   usuniety");
                    }

                    FileOutputStream fileWriter = null;

                    fileWriter = getBaseContext().openFileOutput("import.txt", Context.MODE_PRIVATE);

                 //   StringBuilder builder = new StringBuilder();


                    while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0){
                        System.out.println("*******   odbieram");
                     //  byteArrayOutputStream.write(buffer);
                     //  builder.append(byteArrayOutputStream.toString("UTF-8")) ;
                       fileWriter.write(buffer, 0, bytesRead);
                    }
                    fileWriter.flush();
                    fileWriter.close();

                    System.out.println("*******   odebralem");
                    
                    publishProgress(Integer.valueOf(-1));
                    
                    
                    
                    SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);
                    db.execSQL("DELETE FROM dane;");

                    FileReader fileReader = new FileReader(myFile);
                    BufferedReader br = new BufferedReader(fileReader);

//                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    int i = 0;
                    String line = null;
                    // if no more lines the readLine() returns null

                    int j = 0;
                    String[] rowdata = null;
                    ContentValues values = null;
                    
                    //int dlugoscPliku = (int)myFile.getTotalSpace();
                    publishProgress(Integer.valueOf(-3), Integer.valueOf((int)myFile.length()));
                    
                    int sumaOdczytanych = 0;
                    
                    db.beginTransaction();
                    try {
                    
	                    while ((line = br.readLine()) != null) {
	                        // reading lines until the end of the file
	                    //String[] rows = builder.toString().split("\\n");
	                    //for(String row : rows)
	                    //{
	                       // String[] rowdata = row.split(";");
	
	                        i++;
	                        sumaOdczytanych += line.length();
	
	                        rowdata = line.split(";");
	
	                        values = new ContentValues();
	                        values.put("typ", rowdata[0]);
	                        values.put("kod", rowdata[1]);
	                        values.put("nazwa", rowdata[2]);
	                        values.put("stan", rowdata[3]);
	                        values.put("cenazk", rowdata[4]);
	                        values.put("cenasp", rowdata[5]);
	                        values.put("vat", rowdata[6]);
	                        //values.put("data", rowdata[7] );
//	                        values.put("datazmian", rowdata[7] );
	                        
	
	                        db.insert("dane", null, values);
	
	                        if (i == 100) {
	                            System.out.println("******* aa  dodaje " + j);
	
	                            //publishProgress(Integer.valueOf(j));
	                            publishProgress(Integer.valueOf(sumaOdczytanych));
	                            
	                            i = 0;
	                            j++;
	                        }
	
	                    }
	                    publishProgress(Integer.valueOf(-4));
                    
	                    db.setTransactionSuccessful();
                    } catch (Exception insert_e) {
                    	insert_e.printStackTrace();
                    } finally {
                        db.endTransaction();
                    }

                    

                    db.close();

                    fileReader.close();
 

                    
                    inputStream.close();
                    br.close();

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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


}
