package pl.undersoft.picatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class PICATCH_MENU extends Activity {

	private PowerManager.WakeLock mWakeLock;
	
	int numerWZ = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picatch__menu);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "pl.undersoft.picatch");
        
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
        openbaza();
        
        Button options = (Button) findViewById(R.id.options_b);

        options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PICATCH_MENU.this, PICATCH_OPTIONS.class));

            }
        });

        Button collecting = (Button) findViewById(R.id.collecting_b);

        collecting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PICATCH_MENU.this, PICATCH_COLLECTING_DOC.class));

            }
        });
        
        Button assembling = (Button) findViewById(R.id.assembling_b);

        assembling.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(new Intent(PICATCH_MENU.this, PICATCH_ASSEMBLING_DOC.class));

            	//dodanie rekordu 
            	//SQLiteDatabase db = openOrCreateDatabase("Baza", MODE_WORLD_WRITEABLE, null);
            	//(id int primary key, nazwadok nvarchar (120), nazwadok nvarchar(10), data datetime, send bit);");
            	//db.execSQL("INSERT INTO dok (nazwadok, typ, data, send) VALUES ('WZ" + numerWZ + "' ,'WZ', '1999-01-01', 0);");
            	//numerWZ++;
            	//db.close(); 
            }
        });
        
        Button registers = (Button) findViewById(R.id.registers_b);

        registers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PICATCH_MENU.this, PICATCH_REGISTERS_PRODUCTS.class));

            }
        });

        Button transfer = (Button) findViewById(R.id.transfer_b);


        transfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PICATCH_MENU.this, PICATCH_TCP_DOWNLOAD.class));

            }
        });
    }
    
    public void openbaza() {
        try {
            SQLiteDatabase db = this.openOrCreateDatabase("Baza", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS dane (typ nvarchar (7), kod nvarchar (15) not null, nazwa nvarchar(40), stan nvarchar(10), cenazk nvarchar(10), cenasp nvarchar(10), vat nvarchar(5), devstat nvarchar(10), bad_cena bit, bad_stan bit, cenapolka numeric(6,3), zliczono numeric(6,3), datazmian datetime, cenahurt nvarchar(10), cenaoryg nvarchar(10));");
            db.execSQL("CREATE TABLE IF NOT EXISTS bufor (id integer primary key autoincrement, dokid int not null, kod nvarchar(15) not null, nazwa nvarchar (40), cenazk nvarchar(10), ilosc numeric(10,3), stan nvarchar(10), cenasp nvarchar(10), vat nvarchar(10));");
            db.execSQL("CREATE TABLE IF NOT EXISTS dok (id integer primary key autoincrement, nazwadok nvarchar (120), typ nvarchar(10), data datetime, send bit);");
            db.execSQL("CREATE TABLE IF NOT EXISTS opcje (id integer primary key autoincrement, transfer nvarchar (20), com nvarchar(20), ip nvarchar(20), ufile nvarchar (120), dfile nvarchar(120), bdll nvarchar(50), bflag bit, ipflag bit, port int, skaner nvarchar(120), licence nvarchar(40), devstat nvarchar(20), impdata datetime);");
            db.execSQL("INSERT INTO opcje (transfer, com, ip, ufile, dfile, bdll, bflag, ipflag, port, skaner, licence, impdata) VALUES ('bluetooth', 'COM5', '0.0.0.0', 'Inwent.imp', 'Inwent.exp', 'MSStack', 0, 0, 8790, 'program_czytnika.exe', '0000000000', '1999-01-01');");
            db.execSQL("CREATE TABLE IF NOT EXISTS edihead (id integer primary key autoincrement, FileName nvarchar(40), TypPolskichLiter nvarchar(20), TypDok nvarchar (20), NrDok nvarchar(30), Data nvarchar(30), DataRealizacji nvarchar (30), Magazyn nvarchar(30), SposobPlatn nvarchar(10), TerminPlatn  nvarchar(10), IndeksCentralny nvarchar(10), NazwaWystawcy  nvarchar(120), AdresWystawcy  nvarchar(120), KodWystawcy nvarchar(120), MiastoWystawcy nvarchar(120), UlicaWystawcy nvarchar(120), NIPWystawcy nvarchar(120), BankWystawcy nvarchar(120), KontoWystawcy nvarchar(120), TelefonWystawcy nvarchar(30), NrWystawcyWSieciSklepow nvarchar(20), NazwaOdbiorcy nvarchar(120), AdresOdbiorcy nvarchar(120), KodOdbiorcy nvarchar(20), MiastoOdbiorcy nvarchar(120), UlicaOdbiorcy nvarchar(120), NIPOdbiorcy nvarchar(120), BankOdbiorcy nvarchar(120), KontoOdbiorcy nvarchar(120), TelefonOdbiorcy nvarchar(120), NrOdbiorcyWSieciSklepow nvarchar(20), DoZaplaty nvarchar(20), status nvarchar(20), complete bit);");
            db.execSQL("CREATE TABLE IF NOT EXISTS edibody (id integer primary key autoincrement, NrDok nvarchar(30), Nazwa nvarchar (120), kod nvarchar(20), Vat nvarchar(7), Jm nvarchar (7), Asortyment nvarchar(120), Sww nvarchar(30), PKWiU nvarchar(30), Ilosc nvarchar(10), Cena nvarchar(10), Wartosc nvarchar(10), IleWOpak nvarchar(10), CenaSp nvarchar(10), status nvarchar(20), complete bit);");
            db.execSQL("CREATE TABLE IF NOT EXISTS ediend (id integer primary key autoincrement, NrDok nvarchar(30), Vat nvarchar (20), SumaNet nvarchar(20), SumaVat nvarchar(20), status nvarchar(20), complete bit);");
            db.execSQL("CREATE TABLE IF NOT EXISTS fedihead (id integer primary key autoincrement, ehid int, FileName nvarchar(40), TypPolskichLiter nvarchar(20), TypDok nvarchar (20), NrDok nvarchar(30), Data nvarchar(30), DataRealizacji nvarchar (30), Magazyn nvarchar(30), SposobPlatn nvarchar(10), TerminPlatn  nvarchar(10), IndeksCentralny nvarchar(10), NazwaWystawcy  nvarchar(120), AdresWystawcy  nvarchar(120), KodWystawcy nvarchar(120), MiastoWystawcy nvarchar(120), UlicaWystawcy nvarchar(120), NIPWystawcy nvarchar(120), BankWystawcy nvarchar(120), KontoWystawcy nvarchar(120), TelefonWystawcy nvarchar(30), NrWystawcyWSieciSklepow nvarchar(20), NazwaOdbiorcy nvarchar(120), AdresOdbiorcy nvarchar(120), KodOdbiorcy nvarchar(20), MiastoOdbiorcy nvarchar(120), UlicaOdbiorcy nvarchar(120), NIPOdbiorcy nvarchar(120), BankOdbiorcy nvarchar(120), KontoOdbiorcy nvarchar(120), TelefonOdbiorcy nvarchar(120), NrOdbiorcyWSieciSklepow nvarchar(20), DoZaplaty nvarchar(20), status nvarchar (20), complete bit);");
            db.execSQL("CREATE TABLE IF NOT EXISTS fedibody (id integer primary key autoincrement, ebid int, NrDok nvarchar(30), Nazwa nvarchar (120), kod nvarchar(20), Vat nvarchar(7), Jm nvarchar (7), Asortyment nvarchar(120), Sww nvarchar(30), PKWiU nvarchar(30), Wymagane nvarchar(10), Ilosc nvarchar(10), Cena nvarchar(10), Wartosc nvarchar(10), IleWOpak nvarchar(10), CenaSp nvarchar(10), status nvarchar, complete bit);");
            db.execSQL("CREATE TABLE IF NOT EXISTS fediend (id integer primary key autoincrement, eeid int, NrDok nvarchar(30), Vat nvarchar (20), SumaNet nvarchar(20), SumaVat nvarchar(20), status nvarchar(20), complete bit);");
            db.execSQL("CREATE TABLE IF NOT EXISTS magazyn (id integer primary key autoincrement, Nazwa nvarchar (20), MagId int);");
            db.execSQL("CREATE TABLE IF NOT EXISTS stany (id integer primary key autoincrement, kod nvarchar (15) not null, stan nvarchar(20), MagId int not null, Nazwa nvarchar(20), datazmian datetime);");
            db.close();
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
	    } 

    }


}
