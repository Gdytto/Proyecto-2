package com.daniel.tods;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {



    DatePickerDialog dpd;
    TaskDBHelper baseD;
    int Year = 0;
    int Month = 0;
    int Day = 0;
    String dFinal;
    String nFinal;
    Intent intent;
    Boolean isUpdate;
    Boolean isDelete;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_new);
        baseD = new TaskDBHelper(getApplicationContext());
        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        isDelete = intent.getBooleanExtra("isDelete", false);



        dFinal = todayDateString();
        Date your_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(your_date);
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH);
        Day = cal.get(Calendar.DAY_OF_MONTH);

        if (isUpdate) {
            init_update();
        }
        if (isDelete){
            init_delete();
        }
    }

    private void init_delete() {
        id = intent.getStringExtra("id");
        TextView toolbar_task_add_title = (TextView) findViewById(R.id.toolbar_task_add_title);
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        toolbar_task_add_title.setText("Update");
        Cursor task = baseD.getDataSpecific(id);

        task_name.destroyDrawingCache();
        task_date.destroyDrawingCache();

    }


    public void init_update() {
        id = intent.getStringExtra("id");
        TextView toolbar_task_add_title = (TextView) findViewById(R.id.toolbar_task_add_title);
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        toolbar_task_add_title.setText("Update");
        Cursor task = baseD.getDataSpecific(id);

        //Una vez que se actualiza, se comprueba su nueva posicion
        if (task != null) {
            task.moveToFirst();

            task_name.setText(task.getString(1).toString());
            Calendar cal = Functionality.Calender(task.getString(2).toString());
            Year = cal.get(Calendar.YEAR);
            Month = cal.get(Calendar.MONTH);
            Day = cal.get(Calendar.DAY_OF_MONTH);
            task_date.setText(Functionality.DateString(task.getString(2).toString(), "dd/MM/yyyy"));

        }

    }

    public String todayDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());

        return dateFormat.toString();

    }


    public void closeAddTask(View v) {
        finish();
    }


    public void doneAddTask(View v) {
        int errorStep = 0;
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        nFinal = task_name.getText().toString();
        dFinal = task_date.getText().toString();


  /* Para Verificar si el usuario agregó un nombre a la actividad y fecha valida*/
        if (nFinal.trim().length() < 1) {
            errorStep++;
            task_name.setError("Ingresa un nombre porfavor");
        }

        if (dFinal.trim().length() < 4) {
            errorStep++;
            task_date.setError("Ingresa una fecha porfavor");
        }

        if (errorStep == 0) {
            if (isUpdate) {
                baseD.updateContact(id, nFinal, dFinal);
                Toast.makeText(getApplicationContext(), "Actividad actualizada", Toast.LENGTH_SHORT).show();
            } else {
                baseD.insertContact(nFinal, dFinal);
                Toast.makeText(getApplicationContext(), "Actividad agregada", Toast.LENGTH_SHORT).show();
            }

            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Intenta nuevamente", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("startDatepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    //Metodo para agregar la fecha
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Year = year;
        Month = monthOfYear;
        Day = dayOfMonth;
        int monthAddOne = Month + 1;
        String date = (Day < 10 ? "0" + Day : "" + Day) + "/" +
                (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) + "/" +
                Year;
        EditText task_date = (EditText) findViewById(R.id.task_date);
        task_date.setText(date);
    }


    //Metodo para activar y que se muestre el calendario

    public void showStartDatePicker(View v) {
        dpd = DatePickerDialog.newInstance(AddTask.this, Year, Month, Day);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
    }


    //Metodo para borrar una tarea

    public void deleteTask(View v) {

        View parent = (View)v.getParent();
        id = intent.getStringExtra("id");
        TextView itemName = (TextView) parent.findViewById(R.id.task_name);
        TextView itemDate = (TextView) parent.findViewById(R.id.task_date);

        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        nFinal = task_name.getText().toString();
        dFinal = task_date.getText().toString();

        String task = String.valueOf(itemName.getText());
        String task2 = String.valueOf(itemDate.getText());

        SQLiteDatabase db = baseD.getWritableDatabase();

        db.delete(id,nFinal, new String[]{dFinal});

        db.close();


       Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();


    }
}
