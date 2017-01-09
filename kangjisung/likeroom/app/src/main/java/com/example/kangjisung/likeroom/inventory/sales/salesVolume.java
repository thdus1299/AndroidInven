package com.example.kangjisung.likeroom.inventory.sales;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.kangjisung.likeroom.R;
import com.example.kangjisung.likeroom.SQLiteDatabaseControl.ClientDataBase;
import com.example.kangjisung.likeroom.inventory.InvenList.InvenAdapter;
import com.example.kangjisung.likeroom.inventory.calc;
import java.util.Calendar;

import static com.example.kangjisung.likeroom.SQLiteDatabaseControl.ClientDataBase.DBstring;

/**
 * Created by kangjisung on 2016-12-05.
 */
/////////////////////////////////////////판매량 보여주는 클레스
public class salesVolume extends AppCompatActivity {

    ListView Blistview ;
    InvenAdapter ivAdapter;
    calc c;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bread);



        Blistview = (ListView) findViewById(R.id.Breadlist);

        ivAdapter = new InvenAdapter();
        Blistview.setAdapter(ivAdapter);
        c = calc.getInstance();

        //제품 이름,날짜,판매량 불러오기
        new ClientDataBase("select `제품정보`.`이름`,`제품판매량`.`날짜`,`제품판매량`.`판매량` from `제품정보` join `제품판매량` on `제품정보`.`제품코드`= `제품판매량`.`제품코드` where `제품판매량`.`날짜`=\"" + c.cal.get(Calendar.YEAR) + "-" + (c.cal.get(Calendar.MONTH)+1) + "-" + (c.cal.get(Calendar.DATE)-1) + "\";",1,3,getApplicationContext());
        int cnt=0;
        while(true){
            if(DBstring[cnt]!=null) {
                ivAdapter.addItem(DBstring[cnt], DBstring[cnt + 1], DBstring[cnt + 2]);
                cnt += 3;
            }
            else if(DBstring[cnt]==null) break;
        }

        ////////////////////////////버튼



    }

}
