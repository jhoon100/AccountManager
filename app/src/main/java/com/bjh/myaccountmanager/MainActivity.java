package com.bjh.myaccountmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bjh.myaccountmanager.db.DatabaseAction;
import com.bjh.myaccountmanager.db.DatabaseColumns;
import com.bjh.myaccountmanager.db.DatabaseHelper;
import com.bjh.myaccountmanager.util.StringUtil;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase db;

    private String CHOICE_DAY;              // 선택한 날짜
    private boolean MOD_CHK = false;        // 등록 / 수정 체크
    private boolean SET_MOD_CHK = false;    // 기본 세팅 등록 / 수정 체크

    private int chooseYear;         // 선택 년
    private int chooseMonth;        // 선택 월
    private int chooseDayOfMonth;   // 선택 일

    private View settingView;   // 기본 세팅 화면

    private String strBaseTimeSection;  // 시 / 분 구분
    private String strBaseTime;         // 기본 근무 시간
    private int intBaseAmt;             // 기본 근무 금액
    private String strBAseDayOfMonth;   // 월 기준일

    CalendarView calendarView;      // 달력

    Button btnSave;                 // 일 저장 버튼

    ImageButton btnSetting;         // 기본 세팅 버튼
    ImageButton btnStatistics;      // 통계 버튼

    private TextView dailyTitle;            // 일자 선택 시 title
    private TextView monthTitle;            // 월별 근무시간 금액 title

    private EditText txtDailyWork;          // 일 근무 명
    private EditText txtDailyTimes;         // 일 근무 시간
    private EditText txtDailyAmount;        // 일 근무 금액

    DatabaseAction dbAction;                // DB CRUD

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("strBaseTimeSection", strBaseTimeSection);
        savedInstanceState.putString("strBaseTime", strBaseTime);
        savedInstanceState.putInt("intBaseAmt", intBaseAmt);
        savedInstanceState.putString("strBAseDayOfMonth", strBAseDayOfMonth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            strBaseTimeSection = savedInstanceState.getString("strBaseTimeSection");
            strBaseTime = savedInstanceState.getString("strBaseTime");
            intBaseAmt = savedInstanceState.getInt("intBaseAmt");
            strBAseDayOfMonth = savedInstanceState.getString("strBAseDayOfMonth");
        }

        calendarView = (CalendarView) findViewById(R.id.calendarView);  // CalendarView

        btnSave = (Button) findViewById(R.id.btnSave);                  // 저장 버튼
        btnSetting = (ImageButton) findViewById(R.id.btnSetting);       // 기본 세팅 버튼
        btnStatistics = (ImageButton) findViewById(R.id.btnStatistics); // 통계 버튼

        dailyTitle = (TextView) findViewById(R.id.txtDaily);            // 일 상세 타이틀
        monthTitle = (TextView) findViewById(R.id.monthTitle);          // 월별 근무시간 금액 title

        txtDailyWork = (EditText) findViewById(R.id.txtDailyWork);      // 일 근무 명
        txtDailyTimes = (EditText) findViewById(R.id.txtDailyTimes);    // 일 근무 시간
        txtDailyAmount = (EditText) findViewById(R.id.txtDailyAmount);  // 일 근무 금액

        chooseYear = Integer.parseInt(StringUtil.getDateYYYYMMDD(calendarView.getDate()).substring(0, 4));          // 선택 년도
        chooseMonth = Integer.parseInt(StringUtil.getDateYYYYMMDD(calendarView.getDate()).substring(5, 6));         // 선택 월
        chooseDayOfMonth = Integer.parseInt(StringUtil.getDateYYYYMMDD(calendarView.getDate()).substring(6, 8));    // 선택 일자

        databaseHelper = new DatabaseHelper(getApplicationContext());

        // 기본 세팅 시간 및 금액 불러오기
        getBaseSettingInfo();

        // 월 근무 시간 및 근무 금액 조회
        getTotalInfoToMonth(chooseYear, chooseMonth, chooseDayOfMonth);

        // 월, 일 시간 명 세팅 ( 시 or 분 )
        setTimeSectionName();

        // 달력 날짜 선택 이벤트
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                if((strBaseTimeSection == null || strBaseTimeSection.equals("")) || (strBaseTime == null || strBaseTime.equals("")) || (intBaseAmt <= 0)){
                    Toast.makeText(getApplicationContext(), R.string.settingMessage, Toast.LENGTH_SHORT).show();
                }

                chooseYear = year;              // 선택 년도
                chooseMonth = (month+1);        // 선택 월
                chooseDayOfMonth = dayOfMonth;  // 선택 일

                getTotalInfoToMonth(chooseYear, chooseMonth, chooseDayOfMonth);       // 월 근무 시간 및 근무 금액 조회

                String titleDate = chooseYear + "년 " + chooseMonth + "월 " + chooseDayOfMonth + "일";
                String titleMonth = chooseMonth + " 월 ";

                // 달력에 일자 선택 시 상세 정보에 일 세팅
                dailyTitle.setText(titleDate);

                // 월별 근무시간 / 금액 타이틀 세팅
                monthTitle.setText(titleMonth);

                CHOICE_DAY = chooseYear+""+(chooseMonth<10?"0"+chooseMonth:chooseMonth)+""+(chooseDayOfMonth<10?"0"+chooseDayOfMonth:chooseDayOfMonth);

                try{
                    db = databaseHelper.getReadableDatabase();
                    Cursor cursor = db.query(DatabaseColumns._TABLENAME1, new String[]{DatabaseColumns.WORK_NM, DatabaseColumns.WORK_TIME, DatabaseColumns.WORK_AMOUNT}, DatabaseColumns.WORK_DAY+" = ?", new String[]{CHOICE_DAY}, null, null, null);

                    // 일 근무 명
                    txtDailyWork = (EditText) findViewById(R.id.txtDailyWork);
                    // 일 근무 시간
                    txtDailyTimes = (EditText) findViewById(R.id.txtDailyTimes);
                    // 일 근무 금액
                    txtDailyAmount = (EditText) findViewById(R.id.txtDailyAmount);

                    if(cursor.getCount() > 0){
                        if(cursor.moveToFirst()){
                            txtDailyWork.setText(cursor.getString(0));
                            txtDailyTimes.setText(cursor.getString(1));
                            txtDailyAmount.setText(StringUtil.convertNumberToComma(cursor.getString(2)));
                        }

                        MOD_CHK = true;
                    } else {
                        txtDailyWork.setText("");
                        txtDailyTimes.setText("");
                        txtDailyAmount.setText("");
                        MOD_CHK = false;
                    }

                    cursor.close();
                    db.close();

                }catch(SQLiteException e){
                    Toast.makeText(getApplicationContext(), "Database unavailable onSelectedDayChange", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 일일 근무 시간 및 근무 금액 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean chk = true;

                if(dailyTitle.getText().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.msgChooseDay, Toast.LENGTH_LONG).show();
                    chk = false;
                }

                if(chk && (txtDailyWork.getText() == null || txtDailyWork.getText().toString().equals(""))){
                    Toast.makeText(getApplicationContext(), R.string.msgDailyValidationNm, Toast.LENGTH_LONG).show();
                    chk = false;
                }

                if(chk && (txtDailyTimes.getText() == null || txtDailyTimes.getText().toString().equals(""))){
                    Toast.makeText(getApplicationContext(), R.string.msgDailyValidationTime, Toast.LENGTH_LONG).show();
                    chk = false;
                }

                if(chk && (txtDailyAmount.getText() == null || txtDailyAmount.getText().toString().equals(""))){
                    Toast.makeText(getApplicationContext(), R.string.msgDailyValidationAmount, Toast.LENGTH_LONG).show();
                    chk = false;
                }

                // 포커스 클리어
                txtDailyWork.clearFocus();
                txtDailyTimes.clearFocus();
                txtDailyAmount.clearFocus();

                if(chk){
                    try{
                        db = databaseHelper.getReadableDatabase();

                        dbAction = new DatabaseAction();

                        long retVal;

                        if(MOD_CHK){   // 수정
                            retVal = dbAction.updateDailyColumn(db, CHOICE_DAY, txtDailyWork.getText().toString(), txtDailyTimes.getText().toString(), Integer.parseInt(txtDailyAmount.getText().toString().replaceAll(",", "")));
                        } else {        // 등록
                            retVal = dbAction.insertDailyColumn(db, CHOICE_DAY, txtDailyWork.getText().toString(), txtDailyTimes.getText().toString(), Integer.parseInt(txtDailyAmount.getText().toString().replaceAll(",", "")));
                        }

                        if(retVal == 0){
                            Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();

                            // 월 근무 시간 및 근무 금액 조회
                            getTotalInfoToMonth(chooseYear, chooseMonth, chooseDayOfMonth);

                            // 키보드 숨기기
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(txtDailyAmount.getWindowToken(), 0);
                        }

                    } catch(SQLiteException e){
                        Toast.makeText(getApplicationContext(), "Database unavailable btnSave onClick", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 세팅 버튼 클릭
        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                settingView = getLayoutInflater().inflate(R.layout.activity_setting, null);

                // 구분 라디오 버튼
                RadioButton radioHour = settingView.findViewById(R.id.radioHour);
                RadioButton radioMinute = settingView.findViewById(R.id.radioMinute);

                // 기준 시간 / 금액
                EditText txtBaseTime = settingView.findViewById(R.id.txtBaseTime);
                EditText txtBaseAmt = settingView.findViewById(R.id.txtBaseAmt);

                // 월 기준일
                EditText txtBaseDayOfMonth = settingView.findViewById(R.id.txtBaseDayOfMonth);

                if(strBaseTimeSection != null && strBaseTimeSection.equals("HOUR")){
                    radioHour.setChecked(true);
                    radioMinute.setChecked(false);
                } else if(strBaseTimeSection != null && strBaseTimeSection.equals("MINUTE")){
                    radioHour.setChecked(false);
                    radioMinute.setChecked(true);
                }

                txtBaseTime.setText(strBaseTime);
                txtBaseAmt.setText(StringUtil.convertNumberToComma(String.valueOf(intBaseAmt)));
                txtBaseDayOfMonth.setText(strBAseDayOfMonth);

                // dialog 세팅
                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.settingTitle)
                        .setView(settingView)
                        .setMessage(R.string.settingMessage)
                        .setPositiveButton(R.string.buttonSave, null)
                        .setNegativeButton(R.string.buttonCancel, null).create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button saveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        Button cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                        // 기본 세팅 저장 버튼 클릭
                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean chk = true;

                                String timeSection;

                                // 구분 라디오 버튼
                                RadioButton radioHour = settingView.findViewById(R.id.radioHour);
                                RadioButton radioMinute = settingView.findViewById(R.id.radioMinute);

                                // 기준 시간 / 금액
                                EditText txtBaseTime = settingView.findViewById(R.id.txtBaseTime);
                                EditText txtBaseAmt = settingView.findViewById(R.id.txtBaseAmt);
                                EditText txtBaseDayOfMonth = settingView.findViewById(R.id.txtBaseDayOfMonth);

                                if(!radioHour.isChecked() && !radioMinute.isChecked()){        // 구분 선택 체크
                                    Toast.makeText(getApplicationContext(), R.string.msgValidationSection, Toast.LENGTH_LONG).show();
                                    chk = false;
                                }

                                if(chk && (txtBaseTime.getText() == null || txtBaseTime.getText().toString().equals(""))){         // 시간 선택 체크
                                    Toast.makeText(getApplicationContext(), R.string.msgValidationTime, Toast.LENGTH_LONG).show();
                                    chk = false;
                                }

                                if(chk && (txtBaseAmt.getText() == null || txtBaseAmt.getText().toString().equals(""))){           // 금액 선택 체크
                                    Toast.makeText(getApplicationContext(), R.string.msgValidationAmount, Toast.LENGTH_LONG).show();
                                    chk = false;
                                }

                                if(txtBaseDayOfMonth.getText() == null || txtBaseDayOfMonth.getText().toString().equals("")){
                                    txtBaseDayOfMonth.setText("15");
                                }

                                if(radioHour.isChecked()){
                                    timeSection = "HOUR";
                                } else {
                                    timeSection = "MINUTE";
                                }

                                if(chk){
                                    try{
                                        db = databaseHelper.getReadableDatabase();

                                        dbAction = new DatabaseAction();

                                        long retVal;

                                        if(SET_MOD_CHK){   // 수정
                                            retVal = dbAction.updateBaseColumn(db, timeSection, txtBaseTime.getText().toString(), Integer.valueOf(txtBaseAmt.getText().toString().replaceAll(",", "")), txtBaseDayOfMonth.getText().toString());
                                        } else {        // 등록
                                            retVal = dbAction.insertBaseColumn(db, timeSection, txtBaseTime.getText().toString(), Integer.valueOf(txtBaseAmt.getText().toString().replaceAll(",", "")), txtBaseDayOfMonth.getText().toString());
                                        }

                                        if(retVal == 0){
                                            Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }

                                        // 월 근무 시간 및 근무 금액 조회
                                        getTotalInfoToMonth(chooseYear, chooseMonth, chooseDayOfMonth);

                                        // 기본 세팅 시간 및 금액 불러오기
                                        getBaseSettingInfo();

                                        // 월, 일 시간 명 세팅 ( 시 or 분 )
                                        setTimeSectionName();

                                    } catch(SQLiteException e){
                                        Toast.makeText(getApplicationContext(), "Database unavailable btnSave onClick", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        // 취소
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        EditText txtBaseDayOfMonth = settingView.findViewById(R.id.txtBaseDayOfMonth);

                        txtBaseDayOfMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(!hasFocus){
                                    Toast.makeText(getApplicationContext(), ((TextView)v).getText(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });

                dialog.show();
            }
        });

        // 통계 버튼 클릭
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statisticsIntent = new Intent(getApplicationContext(), StatisticsActivity.class);
                statisticsIntent.putExtra("strBaseTimeSection", strBaseTimeSection);
                statisticsIntent.putExtra("strBaseTime", strBaseTime);
                statisticsIntent.putExtra("intBaseAmt", intBaseAmt);
                statisticsIntent.putExtra("strBAseDayOfMonth", strBAseDayOfMonth);
                startActivity(statisticsIntent);
            }
        });

        // 시간 대비 금액 자동 계산 ( 포커스 변경 시 )
        txtDailyTimes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    double douDailyTime = ((EditText)v).getText().toString().equals("")?0:Integer.valueOf(((EditText)v).getText().toString());
                    txtDailyAmount.setText( StringUtil.convertNumberToComma(String.valueOf( (int)((douDailyTime / Double.valueOf(strBaseTime)) * intBaseAmt ))) );
                }
            }
        });
    }

    /**
     * 상단 월 전체 근무 시간 및 금액 출력 처리
     * @param chooseYear
     * @param chooseMonth
     * @param chooseDayOfMonth
     */
    public void getTotalInfoToMonth(int chooseYear, int chooseMonth, int chooseDayOfMonth){

        String strMonthTitle = chooseMonth + " 월 ";

        // 월별 근무시간 / 금액 타이틀 세팅
        monthTitle.setText(strMonthTitle);

        String startDate = StringUtil.getCalculatorDay(chooseYear, chooseMonth-1, Integer.valueOf(strBAseDayOfMonth), -15, Calendar.DAY_OF_YEAR);
        String endDate = StringUtil.getCalculatorDay(chooseYear, chooseMonth-1, Integer.valueOf(strBAseDayOfMonth), 15, Calendar.DAY_OF_YEAR);

        TextView txtSumTimes = (TextView) findViewById(R.id.txtSumTimes);
        TextView txtSumAmount = (TextView) findViewById(R.id.txtSumAmount);

        try{
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseColumns._TABLENAME1, new String[]{DatabaseColumns.WORK_TIME, DatabaseColumns.WORK_AMOUNT}, DatabaseColumns.WORK_DAY+" >= ? and "+DatabaseColumns.WORK_DAY+" <= ?", new String[]{startDate, endDate}, null, null, null);

            if(cursor.getCount() > 0){

                int intSumTime = 0;
                int intSumAmt = 0;

                for(int i=0; i<cursor.getCount(); i++){
                    if(cursor.moveToNext()){
                        intSumTime += cursor.getInt(0);
                        intSumAmt += cursor.getInt(1);
                    }
                }

                txtSumTimes.setText(String.valueOf(intSumTime));
                txtSumAmount.setText(StringUtil.convertNumberToComma(String.valueOf(intSumAmt)));
            } else {
                txtSumTimes.setText("");
                txtSumAmount.setText("");
            }

            cursor.close();
            db.close();

        } catch(SQLiteException e){
            Toast.makeText(getApplicationContext(), "Database unavailable getTotalInfoToMonth()", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 기본 세팅 시간 및 금액 불러오기
     */
    public void getBaseSettingInfo(){

        // 저장된 세팅값 가져오기
        try{

            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseColumns._TABLENAME0, new String[]{DatabaseColumns.TIME_SECTION, DatabaseColumns.BASE_TIME, DatabaseColumns.BASE_AMOUNT, DatabaseColumns.BASE_DAY_OF_MONTH}, null, null, null, null, null);

            if(cursor.getCount() > 0){
                if(cursor.moveToFirst()){
                    strBaseTimeSection = cursor.getString(0);
                    strBaseTime = cursor.getString(1);
                    intBaseAmt = cursor.getInt(2);
                    strBAseDayOfMonth = cursor.getString(3);
                }
                SET_MOD_CHK = true;
            } else {
                strBaseTimeSection = "MINUTE";
                strBaseTime = "0";
                intBaseAmt = 0;
                strBAseDayOfMonth = "15";
                SET_MOD_CHK = false;
            }

            cursor.close();
            db.close();

        } catch(SQLiteException e){
            Toast.makeText(getApplicationContext(), "Database unavailable btnSetting.setOnClickListener onclick()", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 월 / 일 시간 명 세팅
     */
    public void setTimeSectionName(){
        TextView timeComment1 = (TextView) findViewById(R.id.timeComment1);     // 월 시간 명
        TextView timeComment2 = (TextView) findViewById(R.id.timeComment2);     // 일 시간 명

        if(strBaseTimeSection != null && strBaseTimeSection.equals("HOUR")){
            timeComment1.setText(R.string.labelHour);   // 시
            timeComment2.setText(R.string.labelHour);   // 시
        } else if(strBaseTimeSection != null && strBaseTimeSection.equals("MINUTE")){
            timeComment1.setText(R.string.labelMinute); // 분
            timeComment2.setText(R.string.labelMinute); // 분
        } else {
            timeComment1.setText("");
            timeComment2.setText("");
        }
    }
}
