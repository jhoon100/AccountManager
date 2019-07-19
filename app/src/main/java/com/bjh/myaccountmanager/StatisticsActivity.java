package com.bjh.myaccountmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bjh.myaccountmanager.util.StringUtil;

public class StatisticsActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;

    TextView txtSrhStartDate;
    TextView txtSrhEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        txtSrhStartDate = (TextView) findViewById(R.id.txtSrhStartDate);
        txtSrhEndDate = (TextView) findViewById(R.id.txtSrhEndDate);

        datePickerDialog = new DatePickerDialog(this, listener, Integer.valueOf(StringUtil.getCurYear()), Integer.valueOf(StringUtil.getCurMonth())-1, Integer.valueOf(StringUtil.getCurDay()));
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int argYear, int argMonthOfYear, int argDayOfMonth) {

        }
    };
}
