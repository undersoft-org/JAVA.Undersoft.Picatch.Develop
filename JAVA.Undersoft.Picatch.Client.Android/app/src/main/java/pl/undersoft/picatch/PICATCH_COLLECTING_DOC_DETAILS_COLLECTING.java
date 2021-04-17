package pl.undersoft.picatch;

import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.widget.Toast;

import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

import android.view.inputmethod.InputMethodManager;

public class PICATCH_COLLECTING_DOC_DETAILS_COLLECTING extends Activity {

    private Camera mCamera;
    private PICATCH_CAMERA mPreview;
    private Handler autoFocusHandler;
    private boolean scanState = false;
    
    EditText scanText;

    EditText cenasp;
    EditText cenazk;
    EditText stan;
    EditText nazwa;
    EditText tax;
    EditText ilosc;
    EditText count;
//    Button scanButton;

    ImageScanner scanner;

String   valueId;

    String kodbuf;

    //private boolean barcodeScanned = false;
    private boolean previewing = true;
    private boolean flashEnabled = false;
    private boolean haveFlash = false;
    
    //private boolean flashRunning = false;
    
    static {
        System.loadLibrary("iconv");
    }
    
    MediaPlayer mediaPlayer;
    //long soundOffset = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__collecting__doc_details_collecting);

        System.out.println("*******    onCreate");



		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
///		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "pl.undersoft.picatch");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        valueId = getIntent().getExtras().getString("keyId");
        
        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        
        scanner.setConfig(Symbol.CODABAR, Config.ENABLE, 0);
        //scanner.setConfig(Symbol.EAN13, Config.ENABLE, 0);
        
       // scanner.setConfig(Symbol.CODE128, Config.ENABLE, 0);
        //scanner.setConfig(Symbol.CODE39, Config.ENABLE, 0);
       // scanner.setConfig(Symbol.CODE93, Config.ENABLE, 0);
        scanner.setConfig(Symbol.DATABAR, Config.ENABLE, 0);
        scanner.setConfig(Symbol.DATABAR_EXP, Config.ENABLE, 0);
       // scanner.setConfig(Symbol.EAN8, Config.ENABLE, 0);
        scanner.setConfig(Symbol.I25, Config.ENABLE, 0);
        scanner.setConfig(Symbol.ISBN10, Config.ENABLE, 0);
        scanner.setConfig(Symbol.ISBN13, Config.ENABLE, 0);
       // scanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
       // scanner.setConfig(Symbol.PARTIAL, Config.ENABLE, 0);
        scanner.setConfig(Symbol.PDF417, Config.ENABLE, 0);
       // scanner.setConfig(Symbol.QRCODE, Config.ENABLE, 0);
       // scanner.setConfig(Symbol.UPCA, Config.ENABLE, 0);
       // scanner.setConfig(Symbol.UPCE, Config.ENABLE, 0);
        
        mPreview = new PICATCH_CAMERA(this, mCamera, previewCb, autoFocusCB);
        mPreview.cancelLongPress();

        //
        //Camera.Parameters parameters = mCamera.getParameters();
        //parameters.setZoom(4);
		//parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        
        FrameLayout preview = (FrameLayout)findViewById(R.id.frameLayout);
        preview.addView(mPreview);

        scanText = (EditText)findViewById(R.id.barcode_t);
        cenasp = (EditText)findViewById(R.id.cenasp_t);
        cenazk = (EditText)findViewById(R.id.cenazk_t);
        stan = (EditText)findViewById(R.id.stan_t);
        tax = (EditText)findViewById(R.id.tax_t);
        nazwa = (EditText)findViewById(R.id.col_details_edit_name_t);
        ilosc = (EditText)findViewById(R.id.ilosc_t);
        count = (EditText)findViewById(R.id.count_t);

        final Button flash = (Button) findViewById(R.id.col_flash_b);

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("*******   flash" );
               if (haveFlash) {
                   if (flashEnabled) {
                       flashEnabled = false;
                       flash.setBackgroundResource(R.drawable.ic_action_flash_disabled);
                   } else {
                       flashEnabled = true;
                       flash.setBackgroundResource(R.drawable.ic_action_name2);
                   }
               }


            }
        });

        flashEnabled = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(flashEnabled) {
        	System.out.println("*******   flashEnabled   true");
            flash.setBackgroundResource(R.drawable.ic_action_name2);
            haveFlash = true;

        } else {
        	System.out.println("*******   flashEnabled   false");
            flash.setBackgroundResource(R.drawable.ic_action_flash_disabled);
            haveFlash = false;
        }
        //flashEnabled = true;


        Button search = (Button) findViewById(R.id.search_b_2_1);

        search.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          System.out.println("*******   " + scanText.getText());
                                          FindIndex();
                                      }
                                  });

        Button save = (Button) findViewById(R.id.ok_b_2_1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("*******   saved" );
                SaveScan();
            }
        });

        Button clear = (Button) findViewById(R.id.clear_b_2_1);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("*******   clear" );
                finish();
            }
        });
        
        Button scan = (Button) findViewById(R.id.scan_b_2_1);

        scan.setOnTouchListener(new View.OnTouchListener() {        
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	//System.out.println("*******    onTouch");
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                    	v.setSelected(true);
                    	System.out.println("*******    ACTION_DOWN");
                    	scanningEnable();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                    	v.setSelected(false);
                    	System.out.println("*******    ACTION_UP");
                    	scanningDisable();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        
        try {
        	//mediaPlayer = new MediaPlayer();//.create(this, R.raw.sound_file_1);
        	//AssetFileDescriptor afd = getAssets().openFd("filip23.mp2");
        	//soundOffset = afd.getStartOffset();
        	//mediaPlayer.setDataSource(afd.getFileDescriptor(), soundOffset, afd.getLength());
        	//afd.close();
        	mediaPlayer = new MediaPlayer().create(this, R.raw.filip21);
        	//mediaPlayer.prepare();
        
        } catch(Exception ex1) {
        	System.out.println("ex1 : " + ex1.toString());
        }
        
    }
    
    public void scanningEnable() {
    	try {
    		if(flashEnabled) {
    			mPreview.resetPreview(true, true);
    		}
            else {
                mPreview.resetPreview(true, false);
            }
    	} catch (Exception ex2){
    		System.out.println("ex2 : " + ex2.toString());
    	}    	
    	try {
    		//mediaPlayer.seekTo((int)soundOffset);
    		//mediaPlayer.seekTo(0);
    	} catch (Exception ex3){
    		System.out.println("ex3 : " + ex3.toString());
    	}    	
    	
    	scanState = true;
    	scanText.setText("");
    }

    public void scanningDisable() {
    	try {
    		if(flashEnabled) {
    			mPreview.resetPreview(false, true);
    			if(!scanState) {
    				Thread.sleep(100);
    			}
            } else {
                mPreview.resetPreview(false, false);
                if(!scanState) {
                    Thread.sleep(100);
                }
            }
	    } catch (Exception ex4){
	    	System.out.println("ex4 : " + ex4.toString());
	    }
    	
    	scanState = false;
    }
    
    public void onPause() {
        super.onPause();
        releaseCamera();
    }
    
    public void onStop() {
        super.onStop();
        try {
        	mediaPlayer.stop();
        } catch(Exception ex5) {
        	System.out.println("ex5 : " + ex5.toString());
        }
        try {
        	mediaPlayer.release();
        } catch(Exception ex6) {
        	System.out.println("ex6 : " + ex6.toString());
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
            
            Camera.Parameters parameters = c.getParameters();
            parameters.setZoom(4);
    		//parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
    		c.setParameters(parameters);
    		
        } catch (Exception ex7){
        	System.out.println("ex7 : " + ex7.toString());
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    int sumFrequency = 0;
    
    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
        	System.out.println("*******    onPreviewFrame");
        	if(scanState) {
        		System.out.println("*******    scanState");
	            Camera.Parameters parameters = camera.getParameters();
	            Size size = parameters.getPreviewSize();
                Image barcode = new Image(size.width, size.height, "Y800");
	            barcode.setData(data);
	
	            int result = scanner.scanImage(barcode);
	            
	            if (result != 0) {
	            	System.out.println("*******    result=" + result);
	            	try {
	            		//mediaPlayer.prepare();
	            		mediaPlayer.start();
	            	} catch(Exception ex8) {
	            		System.out.println("ex8 : " + ex8.toString());
	            	}
//	                previewing = false;
//	                mCamera.setPreviewCallback(null);
//	                mCamera.stopPreview();
	                
	                SymbolSet syms = scanner.getResults();
	                for (Symbol sym : syms) {
	                	scanText.setText("" + sym.getData());

	                    //barcodeScanned = true;
	                }

	                scanState = false;

                    FindIndex();
	            }


        	}
        }
    };

    private void FindIndex() {



        kodbuf = "" + String.valueOf(scanText.getText());
        if (kodbuf != null && kodbuf != "") {
            int wagaflag = 0;
            String czywag = kodbuf.substring(0, 2);
            String waga = "";
            String kodwag = "";
            String kodwag2 = "";
            if (czywag == "27" || czywag == "28" || czywag == "29") {
                if (kodbuf.length() == 13) {
                    waga = kodbuf.substring(kodbuf.length() - 6, 5);
                    kodwag = kodbuf.substring(0, 6);
                    kodwag2 = kodbuf.substring(2, 4);
                    wagaflag = 1;
                }
            }

            if (wagaflag == 0) {
                String table = "dane";
                String[] col = {"kod", "nazwa", "stan", "cenazk", "cenasp", "vat"};
                String[] testkod = {kodbuf};
                SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);
                Cursor cursor = db.query(table, col, "kod = ?", testkod, null, null, null);
                if (cursor.moveToFirst()) {

                    nazwa.setText(cursor.getString(1));
                    cenasp.setText(cursor.getString(4));
                    cenazk.setText(cursor.getString(3));
                    tax.setText(cursor.getString(5));
                    stan.setText(cursor.getString(2));
                    ilosc.setText("1");
                    ilosc.requestFocus();
                    ilosc.selectAll();
                showKeyBoard();
                } else {
                    Toast.makeText(getApplicationContext(), "Nie znaleziono towaru", Toast.LENGTH_SHORT).show();
                    nazwa.setText("<< Nowy Towar >>");
                    ilosc.setText("1");
                    cenasp.setText("0.00");
                    cenazk.setText("0.00");
                    tax.setText("0");
                    stan.setText("0.00");
                    ilosc.requestFocus();
                    ilosc.selectAll();
                showKeyBoard();
                    }
                table = "bufor";
                String[] colb = {"kod", "dokid", "ilosc", "nazwa", "stan", "cenazk", "cenasp", "vat"};
                String[] testkodb = {kodbuf, valueId};
                cursor = db.query(table, colb, "kod = ? AND dokid = ?", testkodb, null, null, null);
                if (cursor.moveToFirst()) {

                    count.setText(cursor.getString(2));
                } else {

                    count.setText("0");
                }




            }
        }else {
            Toast.makeText(getApplicationContext(), "Wprowadź kod towaru", Toast.LENGTH_SHORT).show();
        }

    }

    private void SaveScan() {

        String nazwa_s = String.valueOf(nazwa.getText());
        if (kodbuf != "" && kodbuf != null && ilosc.getText().toString() != "") {
            Double ilosc_d = Double.parseDouble(String.valueOf(ilosc.getText()));
            Double count_d = Double.parseDouble(String.valueOf(count.getText()));
            SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_MULTI_PROCESS, null);

            if (kodbuf != "" && kodbuf != null && ilosc_d > 0) {

                ContentValues values = new ContentValues();
                String table = "bufor";
                String[] col = {"id", "kod", "dokid", "nazwa", "stan", "cenazk", "cenasp", "vat"};
                String[] testkod = {kodbuf, valueId};
                Cursor cursor = db.query(table, col, "kod = ? AND dokid = ?", testkod, null, null, null);
                if (cursor.moveToFirst()) {

                    String id = cursor.getString(0);

                    values.put("dokid", valueId);
                    values.put("kod", kodbuf);
                    values.put("nazwa", nazwa_s);
                    values.put("stan", String.valueOf(stan.getText()));
                    values.put("cenazk", String.valueOf(cenazk.getText()));
                    values.put("cenasp", String.valueOf(cenasp.getText()));
                    values.put("vat", String.valueOf(tax.getText()));
                    values.put("ilosc", ilosc_d + count_d);

                    db.update("bufor", values, "id=" + id, null);

                } else {

                    values.put("dokid", valueId);
                    values.put("kod", kodbuf);
                    values.put("nazwa", nazwa_s);
                    values.put("stan", String.valueOf(stan.getText()));
                    values.put("cenazk", String.valueOf(cenazk.getText()));
                    values.put("cenasp", String.valueOf(cenasp.getText()));
                    values.put("vat", String.valueOf(tax.getText()));
                    values.put("ilosc", ilosc_d);

                    db.insert("bufor", null, values);

                }

                kodbuf = "";
                nazwa.setText("");
                scanText.setText("");
                cenazk.setText("");
                cenasp.setText("");
                tax.setText("");
                stan.setText("");
                ilosc.setText("");
                count.setText("");
                scanText.requestFocus();

                db.close();

            }

        } else {

            Toast.makeText(this, "Cannot save empty data !!!", Toast.LENGTH_SHORT).show();


        }
    }

    public void showKeyBoard() {
        InputMethodManager mgr = (InputMethodManager)  getSystemService(this.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        mgr.showSoftInput(ilosc, InputMethodManager.SHOW_IMPLICIT);


    }

    public void hideKeyBoard() {
        InputMethodManager mgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(ilosc.getWindowToken(), 0);
    }

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 300);
        }
    };
}
