package com.bjh.myaccountmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

        datePickerDialog =  new DatePickerDialog(this, listener, Integer.valueOf(StringUtil.getCurYear()), Integer.valueOf(StringUtil.getCurMonth())-1, Integer.valueOf(StringUtil.getCurDay()));

        datePickerDialog.show();

        // 통계 조회 시작일 선택 시 달력 팝업 실행 후 선택하면 선택된 데이터 입력 처리
        txtSrhStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 통계 조회 종료일 선택 시 달력 팝업 실행 후 선택하면 선택된 데이터 입력 처리
        txtSrhEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int argYear, int argMonthOfYear, int argDayOfMonth) {

        }
    };
}
