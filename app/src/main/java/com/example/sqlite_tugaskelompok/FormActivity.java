package com.example.sqlite_tugaskelompok;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {
    EditText inputNim, inputName, inputTgl, inputGender, inputAlamat;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateformatter;
    Button btnSubmit;
    Boolean edit;
    HashMap<String, String> passedInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Input Data");

        initView();
    }

    private void initView() {
        inputNim = findViewById(R.id.edtNim);
        inputName = findViewById(R.id.edtName);
        inputTgl = findViewById(R.id.edtTgl);
        inputGender = findViewById(R.id.edtGender);
        inputAlamat = findViewById(R.id.edtAlamat);
        btnSubmit = findViewById(R.id.btnSubmit);

        Intent intent = getIntent();

        dateformatter = new SimpleDateFormat("dd/mm/yyyy", Locale.US);

        inputTgl.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        edit = intent.getBooleanExtra("edit", false);
        if (edit) {
            getSupportActionBar().setTitle("Update Data");
            passedInfo = (HashMap<String, String>) intent.getSerializableExtra("passedInfo");

            inputNim.setText(passedInfo.get("nim"));
            inputNim.setEnabled(false);
            inputName.setText(passedInfo.get("name"));
            inputTgl.setText(passedInfo.get("tglLahir"));
            inputGender.setText(passedInfo.get("gender"));
            inputAlamat.setText(passedInfo.get("alamat"));
        }

        btnSubmit.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                inputTgl.setText(dateformatter.format(newDate.getTime()));

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            Mahasiswa currentPerson = new Mahasiswa();

            if (edit) {
                currentPerson.setNim(inputNim.getText().toString());
                currentPerson.setName(inputName.getText().toString());
                currentPerson.setTglLahir(inputTgl.getText().toString());
                currentPerson.setGender(inputGender.getText().toString());
                currentPerson.setAlamat(inputAlamat.getText().toString());
                db.update(currentPerson);

                Intent intent = new Intent(this, ContainerActivity.class);
                startActivity(intent);
            } else {
                currentPerson.setNim(inputNim.getText().toString());
                currentPerson.setName(inputName.getText().toString());
                currentPerson.setTglLahir(inputTgl.getText().toString());
                currentPerson.setGender(inputGender.getText().toString());
                currentPerson.setAlamat(inputAlamat.getText().toString());
                db.insert(currentPerson);

                inputNim.setText("");
                inputName.setText("");
                inputTgl.setText("");
                inputGender.setText("");
                inputAlamat.setText("");
                inputNim.setFocusable(true);
            }
        }
    }
}