package com.bjh.myaccountmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bjh.myaccountmanager.util.StringUtil;

public class StatisticsActivity extends AppCompatActivity{

    TextView txtSrhStartDate;       // 조회 시작일
    TextView txtSrhEndDate;         // 조회 종료일

    String strRadioSrhChoose;       // 조회 기준년월 값 세팅

    RadioButton radioSrhYear;       // 기준년월의 년
    RadioButton radioSrhMonth;      // 기준년월의 월

    Button btnSearch;               // 조회 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        txtSrhStartDate = (TextView) findViewById(R.id.txtSrhStartDate);
        txtSrhEndDate = (TextView) findViewById(R.id.txtSrhEndDate);

        radioSrhYear = (RadioButton) findViewById(R.id.radioSrhYear);
        radioSrhMonth = (RadioButton) findViewById(R.id.radioSrhMonth);

        btnSearch = (Button) findViewById(R.id.btnSearch);

        // 조회 시작일
        String strPreDate;

        // 현재 년월일 yyyy-MM-dd
        String currentDate = StringUtil.getCurYear() + "-" + StringUtil.getCurMonth() + "-" + StringUtil.getCurDay();

        if(radioSrhYear.isChecked()){
            strPreDate = (Integer.valueOf(StringUtil.getCurYear()) - 3) + "-01-01";
            strRadioSrhChoose = "YEAR";
            txtSrhStartDate.setText(strPreDate);
            txtSrhEndDate.setText(currentDate);
        } else if(radioSrhMonth.isChecked()){
            strPreDate = (Integer.valueOf(StringUtil.getCurYear()) - 1) + "-01-01";
            strRadioSrhChoose = "MONTH";
            txtSrhStartDate.setText(strPreDate);
            txtSrhEndDate.setText(currentDate);
        }

        // 통계 조회 시작일 선택 시 달력 팝업 실행 후 선택하면 선택된 데이터 입력 처리
        txtSrhStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] chooseDate = txtSrhStartDate.getText().toString().split("-");
                DatePickerDialog datePickerDialog =  new DatePickerDialog(StatisticsActivity.this, startListener, Integer.valueOf(chooseDate[0]), Integer.valueOf(chooseDate[1])-1, Integer.valueOf(chooseDate[2]));
                datePickerDialog.show();
            }
        });

        // 통계 조회 종료일 선택 시 달력 팝업 실행 후 선택하면 선택된 데이터 입력 처리
        txtSrhEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =  new DatePickerDialog(StatisticsActivity.this, endListener, Integer.valueOf(StringUtil.getCurYear()), Integer.valueOf(StringUtil.getCurMonth())-1, Integer.valueOf(StringUtil.getCurDay()));
                datePickerDialog.show();
            }
        });

        // 조회버튼 클릭
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    // 통계 조회 시작일 선택
    private DatePickerDialog.OnDateSetListener startListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int argYear, int argMonthOfYear, int argDayOfMonth) {
            String strStartDate = argYear + "-" + ((argMonthOfYear+1)<10?"0"+(argMonthOfYear+1):(argMonthOfYear+1)) + "-" + (argDayOfMonth<10?"0"+argDayOfMonth:argDayOfMonth);
            txtSrhStartDate.setText(strStartDate);
        }
    };

    // 통계 조회 종료일 선택
    private DatePickerDialog.OnDateSetListener endListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int argYear, int argMonthOfYear, int argDayOfMonth) {
            String strEndDate = argYear + "-" + ((argMonthOfYear+1)<10?"0"+(argMonthOfYear+1):(argMonthOfYear+1)) + "-" + (argDayOfMonth<10?"0"+argDayOfMonth:argDayOfMonth);
            txtSrhEndDate.setText(strEndDate);
        }
    };
}
