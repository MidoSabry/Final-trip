package com.example.tripreminderapp.ui.add_trip;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminderapp.R;
import com.example.tripreminderapp.database.TripDatabase;
import com.example.tripreminderapp.database.trip.Trip;
import com.example.tripreminderapp.databinding.ActivityAddTripBinding;
import com.example.tripreminderapp.reminder.MyService;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddTripActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityAddTripBinding binding;
    private static final int REQ_CODE = 111;
    final Calendar c = Calendar.getInstance();
    private AddTripViewModel viewModel;
   // private Spinner spinner;
    private Trip trip = new Trip();
    private Calendar calendar;
     Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(AddTripViewModel.class);
        getSupportActionBar().hide();
        calendar =Calendar.getInstance();

//        spinner = findViewById(R.id.trip_type);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, R.layout.spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);

        aSwitch = findViewById(R.id.switch1);

        syncDataWithFirebaseDatabase(TripDatabase.getInstance(getApplicationContext()).tripDao().getAll());


        Places.initialize(getApplicationContext(), getString(R.string.api_places_key));
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddTripActivity.this);

        viewModel.getIsInsertedLiveData().observe(this,aBoolean -> {
            if (aBoolean)
                finish();
        });
        binding.edStartPoint.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQ_CODE);
            }
        });
        binding.edEndPoint.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQ_CODE + 1);
            }
        });

        binding.edDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        String _year = String.valueOf(year);
//                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
//                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
//                        String _pickedDate = _year + "-" + _month + "-" + _date;
//                        Log.e("PickedDate: ", "Date: " + _pickedDate);
                        calendar.set(Calendar.YEAR , year);
                        calendar.set(Calendar.MONTH , month);
                        calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                        String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                        binding.edDate.getEditText().setText(date);
                    }
                } , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                dialog.show();
            }

        });

        binding.edTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        calendar.set(Calendar.MINUTE , minute);
                        calendar.set(Calendar.HOUR_OF_DAY , selectedHour);
                        calendar.set(Calendar.SECOND , 0);
                        binding.edTime.getEditText().setText(selectedHour+ " : " + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        binding.addNewTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MyService.class);
                PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

                AlarmManager alarm = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                   // alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
                    alarm.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pintent);
                }





                trip.setName(binding.edName.getEditText().getText().toString());
                trip.setStartPoint(binding.edStartPoint.getEditText().getText().toString());
                trip.setEndPoint(binding.edEndPoint.getEditText().getText().toString());
                trip.setDate(binding.edDate.getEditText().getText().toString());
                trip.setTime(binding.edTime.getEditText().getText().toString());

                trip.setDate_time(binding.edDate.getEditText().getText().toString()+" "+binding.edTime.getEditText().getText().toString());
               // trip.setType(getResources().getStringArray(R.array.types)[spinner.getSelectedItemPosition()]);



                if(validateError() == true) {
                    viewModel.insertTrip(trip);
                }

            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false){
                    binding.edDate2.getEditText().setEnabled(false);
                    binding.edTime2.getEditText().setEnabled(false);




                }else{
                    binding.edDate2.getEditText().setEnabled(true);
                    binding.edDate2.getEditText().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar c = Calendar.getInstance();
                            DatePickerDialog dialog = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        String _year = String.valueOf(year);
//                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
//                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
//                        String _pickedDate = _year + "-" + _month + "-" + _date;
//                        Log.e("PickedDate: ", "Date: " + _pickedDate);
                                    calendar.set(Calendar.YEAR , year);
                                    calendar.set(Calendar.MONTH , month);
                                    calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                                    String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                    binding.edDate2.getEditText().setText(date);
                                }
                            } , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                            dialog.show();
                        }

                    });



                    binding.edTime2.getEditText().setEnabled(true);
                    binding.edTime2.getEditText().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    calendar.set(Calendar.MINUTE , minute);
                                    calendar.set(Calendar.HOUR_OF_DAY , selectedHour);
                                    calendar.set(Calendar.SECOND , 0);
                                    binding.edTime2.getEditText().setText(selectedHour+ " : " + selectedMinute);
                                }
                            }, hour, minute, true);//Yes 24 hour time
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();

                        }
                    });


                    binding.addNewTripBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getApplicationContext(), MyService.class);
                            PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

                            AlarmManager alarm = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                                // alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
                                alarm.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pintent);
                            }





                            trip.setName(binding.edName.getEditText().getText().toString());
                            trip.setStartPoint(binding.edStartPoint.getEditText().getText().toString());
                            trip.setEndPoint(binding.edEndPoint.getEditText().getText().toString());
                            trip.setDate(binding.edDate.getEditText().getText().toString());
                            trip.setTime(binding.edTime.getEditText().getText().toString());

                            trip.setDate_time(binding.edDate.getEditText().getText().toString()+" "+binding.edTime.getEditText().getText().toString());
                            //       trip.setType(getResources().getStringArray(R.array.types)[spinner.getSelectedItemPosition()]);





                            if(validateError() == true) {
                                viewModel.insertTrip(trip);
                            }

                            trip.setName(binding.edName.getEditText().getText().toString()+"Round trip");
                            trip.setStartPoint(binding.edEndPoint.getEditText().getText().toString());
                            trip.setEndPoint(binding.edStartPoint.getEditText().getText().toString());
                            trip.setDate(binding.edDate2.getEditText().getText().toString());
                            trip.setTime(binding.edTime2.getEditText().getText().toString());

                            trip.setDate_time(binding.edDate2.getEditText().getText().toString()+" "+binding.edTime2.getEditText().getText().toString());
                            if(validateError() == true) {
                                viewModel.insertTrip(trip);
                            }

                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.edStartPoint.getEditText().setText(place.getAddress());
            trip.setStartLatitude( place.getLatLng().latitude);
            trip.setStartLongitude(place.getLatLng().longitude);

        } else if (requestCode == REQ_CODE + 1 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.edEndPoint.getEditText().setText(place.getAddress());
            trip.setEndLatitude( place.getLatLng().latitude);
            trip.setEndLongitude(place.getLatLng().longitude);
        } else {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, "error  " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private Boolean validateError() {
        String tNameVal = binding.edName.getEditText().getText().toString();
        String spointVal = binding.edStartPoint.getEditText().getText().toString();
        String epointVal = binding.edEndPoint.getEditText().getText().toString();
        String dateVal = binding.edDate.getEditText().getText().toString();
        String timeVal = binding.edTime.getEditText().getText().toString();
        if(tNameVal.isEmpty() ){
            binding.edName.setError("TripName Required");
            binding.edName.requestFocus();
            return false;
        }
        else if(spointVal.isEmpty()) {
            binding.edStartPoint.setError("Start Point required");
            binding.edStartPoint.requestFocus();

            return false;
        }
        else if(epointVal.isEmpty()) {
            binding.edEndPoint.setError("End Point required");
            binding.edEndPoint.requestFocus();
            return false;
        }
        else if(dateVal.isEmpty()) {
            binding.edDate.setError("date reqquired");
            binding.edDate.requestFocus();
            return false;
        }
        else if(timeVal.isEmpty()) {
            binding.edTime.setError("Time required");
            binding.edTime.requestFocus();
            return false;
        }
        else {
            binding.edName.setError(null);
            binding.edName.setErrorEnabled(false);

            binding.edStartPoint.setError(null);
            binding.edStartPoint.setErrorEnabled(false);

            binding.edEndPoint.setError(null);
            binding.edEndPoint.setErrorEnabled(false);

            binding.edDate.setError(null);
            binding.edDate.setErrorEnabled(false);

            binding.edTime.setError(null);
            binding.edTime.setErrorEnabled(false);

            return true;
        }

    }

    void syncDataWithFirebaseDatabase(final List<Trip> tripList) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        for (int indx = 0; indx < tripList.size(); ++indx) {

            Trip trip = tripList.get(indx);
            reference.child("trips").child("mahmoud").push().setValue(trip).addOnCompleteListener(task -> {
                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            });
        }
    }

}




