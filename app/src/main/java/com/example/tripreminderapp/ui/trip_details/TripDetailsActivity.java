package com.example.tripreminderapp.ui.trip_details;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminderapp.R;
import com.example.tripreminderapp.database.TripDatabase;
import com.example.tripreminderapp.database.note.Note;
import com.example.tripreminderapp.database.trip.Trip;
import com.example.tripreminderapp.databinding.ActivityTripDetailsBinding;
import com.example.tripreminderapp.ui.upcoming_trips.UpcomingTripsFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



public class TripDetailsActivity extends AppCompatActivity   implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private final int REQ_CODE = 2615;
    private NotesAdapter notesAdapter = new NotesAdapter();
    private boolean isEditable = false;
    private Trip currentTrip;
    private ActivityTripDetailsBinding binding;
    private TripDetailsViewModel viewModel;
    //private Spinner spinner;

    private AlertDialog notesDialog=null;


    //Mido.com
    final Calendar myCalendar = Calendar.getInstance();
    private AlertDialog addNoteDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(TripDetailsViewModel.class);
        getSupportActionBar().hide();

//        spinner = findViewById(R.id.trip_type);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, R.layout.spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        changeBehaviour(false);

        Places.initialize(getApplicationContext(), getString(R.string.api_places_key));
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(this);

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


//        notesAdapter = new NotesAdapter();
//        binding.detailsRvNote.setAdapter(notesAdapter);

        currentTrip = (Trip) getIntent().getSerializableExtra(UpcomingTripsFragment.UPCOMING_DETAILS_EXTRA);
        binding.edName.getEditText().setText(currentTrip.getName());
        binding.edStartPoint.getEditText().setText(currentTrip.getStartPoint());
        binding.edEndPoint.getEditText().setText(currentTrip.getEndPoint());
        binding.edDate.getEditText().setText(currentTrip.getDate());
        binding.edTime.getEditText().setText(currentTrip.getTime());


        //spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(currentTrip.getType()));


        viewModel.getNotesFromDatabase(currentTrip.getId());

        viewModel.getNotesLiveData().observe(this,notes -> {
            notesAdapter.changeData(notes);
        });

        viewModel.getNotesLiveData().observe(this,notes -> {
            showNotesDialog(notes);
        });

        viewModel.getIsEditable().observe(this,aBoolean -> {
            Toast.makeText(TripDetailsActivity.this, aBoolean+"", Toast.LENGTH_SHORT).show();
                changeBehaviour(aBoolean);

        });

        binding.detailsBtnShownotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getNotesFromDatabase(currentTrip.getId());
            }
        });

        notesAdapter.setNoteDeleteClickListener = new NotesAdapter.NoteDeleteClickListener() {
            @Override
            public void onClick(Note note) {
                viewModel.deleteNoteFromDatabase(note);
                viewModel.getNotesFromDatabase(currentTrip.getId());
            }
        };

        notesAdapter.setNoteClickListener = new NotesAdapter.NoteClickListener() {
            @Override
            public void onClick(Note note) {
                //notesDialog.dismiss();
                showNoteDetailsDialog(note);
            }
        };

        binding.detailsBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   String type = getResources().getStringArray(R.array.types)[spinner.getSelectedItemPosition()];
//                String name = binding.edName.getEditText().getText().toString();
//                String startPoint = binding.edStartPoint.getEditText().getText().toString();
//                String endPoint = binding.edEndPoint.getEditText().getText().toString();
//                String date = binding.edDate.getEditText().getText().toString();
//                String time = binding.edTime.getEditText().getText().toString();
//
//
//
//                Trip trip = new Trip(name, startPoint, endPoint, date, time,date+" "+time,type);
//
//                viewModel.updateTripInDatabase(trip);

                currentTrip.setName( binding.edName.getEditText().getText().toString());
                currentTrip.setStartPoint( binding.edStartPoint.getEditText().getText().toString());
                currentTrip.setEndPoint( binding.edEndPoint.getEditText().getText().toString());
                currentTrip.setDate( binding.edDate.getEditText().getText().toString());
                currentTrip.setTime( binding.edTime.getEditText().getText().toString());
                currentTrip.setDate_time(binding.edDate.getEditText().getText()+" "+ binding.edTime.getEditText().getText().toString());
                viewModel.updateTripInDatabase(currentTrip);

            }
        });

        //Mido
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        binding.edDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(TripDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = _year + "-" + _month + "-" + _date;
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                        binding.edDate.getEditText().setText(_pickedDate);
                        //updateLabel();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
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
                mTimePicker = new TimePickerDialog(TripDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.edTime.getEditText().setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.edStartPoint.getEditText().setText(place.getAddress());
        } else if (requestCode == REQ_CODE + 1 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.edEndPoint.getEditText().setText(place.getAddress());
        } else {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, "error  " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void changeBehaviour(Boolean isEditable) {
        if (isEditable) {
            binding.edName.getEditText().setEnabled(true);
            binding.edStartPoint.getEditText().setEnabled(true);
            binding.edEndPoint.getEditText().setEnabled(true);
            binding.edDate.getEditText().setEnabled(true);
            binding.edTime.getEditText().setEnabled(true);
            binding.detailsBtnEdit.setText("save");
        } else {
            binding.edName.getEditText().setEnabled(false);
            binding.edStartPoint.getEditText().setEnabled(false);
            binding.edEndPoint.getEditText().setEnabled(false);
            binding.edDate.getEditText().setEnabled(false);
            binding.edTime.getEditText().setEnabled(false);
            binding.detailsBtnEdit.setText("update");
        }
    }


    //Mido
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.edDate.getEditText().setText(sdf.format(myCalendar.getTime()));
    }


    public void showNotesDialog(List<Note> data) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.dialog_show_notes, null);
        notesDialog = new AlertDialog.Builder(this).create();
        notesDialog.setView(view);
        RecyclerView recyclerView = view.findViewById(R.id.details_rv_note);
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.changeData(data);
        notesDialog.show();
    }


    public void showNoteDetailsDialog(Note note) {
        final int[] flag = {0};
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.dialog_add_note, null);
        addNoteDialog = new AlertDialog.Builder(this).create();
        addNoteDialog.setView(view);
        TextInputLayout title = view.findViewById(R.id.ed_note_ttle);
        TextInputLayout description = view.findViewById(R.id.ed_note_disc);
        Button save = view.findViewById(R.id.btn_add_note);
        title.getEditText().setText(note.getTitle());
        description.getEditText().setText(note.getDescription());

        title.getEditText().setEnabled(false);
        description.getEditText().setEnabled(false);
        save.setText("update");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0] == 0) {
                    title.getEditText().setEnabled(true);
                    description.getEditText().setEnabled(true);
                    save.setText("confirm");
                    flag[0] = 1;
                }else {
                    note.setTitle(title.getEditText().getText().toString());
                    note.setDescription(description.getEditText().getText().toString());
                    TripDatabase.getInstance(TripDetailsActivity.this).noteDao().update(note);
                    addNoteDialog.dismiss();
                }

            }
        });
        addNoteDialog.show();
    }





    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}