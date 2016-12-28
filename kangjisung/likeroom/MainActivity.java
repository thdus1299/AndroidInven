package com.example.kangjisung.likeroom;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kangjisung.likeroom.SQLiteDatabaseControl.ClientDataBase;
import com.example.kangjisung.likeroom.inventory.sales.salesVolume;

import static com.example.kangjisung.likeroom.R.id.StoreAddress;
import static com.example.kangjisung.likeroom.R.id.StoreName;
import static com.example.kangjisung.likeroom.R.id.StorePhone;
import static com.example.kangjisung.likeroom.SQLiteDatabaseControl.ClientDataBase.DBstring;


public class MainActivity extends ActionBarActivity{

    String PriNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ClientDataBase("select `매장번호` from `매장`;",1,1,getApplicationContext());
        int cnt=0;
        while(true) {
            if (DBstring[cnt] != null) {
                PriNum=DBstring[cnt];
                cnt++;
            }
            else if(DBstring[cnt]==null) break;
        }
        /*if(PriNum==null) {
            Intent StoreAdd = new Intent(this, StoreAdd.class);
            startActivity(StoreAdd);
        }*/
        Intent StoreAdd = new Intent(this, StoreAdd.class);
        startActivity(StoreAdd);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
