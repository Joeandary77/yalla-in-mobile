package com.example.yallain;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class stadiumAdapter extends RecyclerView.Adapter<stadiumAdapter.ViewHolder> {

    private List<StadiumModel> dataList;
    private Context context;

    public stadiumAdapter(Context context, List<StadiumModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stadium_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StadiumModel item = dataList.get(position);
        holder.bind(item.photoUrl, item.name, item.price);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private ImageView stadiumPic;
        private TextView selectTime;
        private TextView price_txt;
        private TextView stadiumName;
        private Button rentBtn;
        private Calendar selectedDateTime;
        private   final int SELECT_TIME_ID = R.id.selectTime_txt;
        private  final int RENT_BTN_ID = R.id.rent_btn;
        boolean timeSelected=false;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stadiumPic = itemView.findViewById(R.id.stadiumPic);
            selectTime = itemView.findViewById(R.id.selectTime_txt);
            price_txt = itemView.findViewById(R.id.price_txt);
            stadiumName = itemView.findViewById(R.id.stadiumname_txt);
            rentBtn = itemView.findViewById(R.id.rent_btn);

            selectTime.setOnClickListener((clicked)->{
                showDateTimePicker();
            });
            rentBtn.setOnClickListener((clicked)->{
                if(timeSelected){
                    handleRentButtonClick();
                }else{
                    Toast.makeText(context, "Select Rent Time Before", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void showDateTimePicker() {
            final Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            int hour = currentDate.get(Calendar.HOUR_OF_DAY);
            int minute = currentDate.get(Calendar.MINUTE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            selectedDateTime = Calendar.getInstance();
                            selectedDateTime.set(year, monthOfYear, dayOfMonth);

                            TimePickerDialog timePickerDialog = new TimePickerDialog(itemView.getContext(),
                                    new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                            selectedDateTime.set(Calendar.MINUTE, minute);
                                            timeSelected=true;
                                            // Update the selectTime TextView with selected date and time
                                            selectTime.setText(formatDateTime(selectedDateTime));
                                        }
                                    }, hour, minute, true);
                            timePickerDialog.show();
                        }
                    }, year, month, day);
            datePickerDialog.show();
        }
        public  String formatCalendar(Calendar calendar) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(calendar.getTime());
        }
        private String formatDateTime(Calendar dateTime) {
            // Format the selected date and time as needed
            // Example format: "Selected Time: 2024-05-10 14:30"
            return "Selected Time: " + formatCalendar(dateTime).toString();
        }

        private void handleRentButtonClick() {
            // Inflate the dialog layout
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_item, null);

            // Find views
            TextView titleTextView = dialogView.findViewById(R.id.text_view_title);
            TextView messageTextView = dialogView.findViewById(R.id.text_view_message);
            EditText input = dialogView.findViewById(R.id.edit_text_hours);
            Button rentButton = dialogView.findViewById(R.id.button_rent);
            Button cancelButton = dialogView.findViewById(R.id.button_cancel);

            // Set title and message
            titleTextView.setText("Rent Stadium");
            messageTextView.setText("Please enter the number of hours you would like to rent the stadium:");

            // Build the dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);

            // Set up the buttons
            AlertDialog alertDialog = builder.create();

            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hoursString = input.getText().toString();
                    if (!TextUtils.isEmpty(hoursString)) {
                        double hours = Double.parseDouble(hoursString);
                        StartRentProcess(alertDialog,hours);

                    } else {
                        // Handle empty input
                        Toast.makeText(context, "Please enter a valid number of hours", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }
        public void StartRentProcess(AlertDialog dialog,double hour_and_minutes){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            HashMap<String, Object> data = new HashMap<>();
            Calendar currentTime = Calendar.getInstance();
            Calendar endTime = (Calendar) selectedDateTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, (int) hour_and_minutes);
            int minutes = (int) ((hour_and_minutes % 1) * 60);
            endTime.add(Calendar.MINUTE, minutes);
            data.put("time", formatCalendar(currentTime));
            data.put("start_time", formatCalendar(selectedDateTime));
            data.put("end_time", formatCalendar(endTime));
            db.collection(stadiumName.getText().toString().replaceAll(" ",""))
                    .whereLessThan("start_time", formatCalendar(endTime))
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            List<DocumentSnapshot> rentsBeforeEndTime = task1.getResult().getDocuments();
                            // Query for rents where end time is greater than selected start time
                            db.collection(stadiumName.getText().toString().replaceAll(" ",""))
                                    .whereGreaterThan("end_time", formatCalendar(selectedDateTime))
                                    .get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            List<DocumentSnapshot> rentsAfterStartTime = task2.getResult().getDocuments();
                                            // Check for overlapping rents
                                            boolean hasOverlappingRents = false;
                                            for (DocumentSnapshot rentBeforeEndTime : rentsBeforeEndTime) {
                                                for (DocumentSnapshot rentAfterStartTime : rentsAfterStartTime) {
                                                    if (rentBeforeEndTime.getId().equals(rentAfterStartTime.getId())) {
                                                        hasOverlappingRents = true;
                                                        break;
                                                    }
                                                }
                                                if (hasOverlappingRents) {
                                                    break;
                                                }
                                            }
                                            if (hasOverlappingRents) {
                                                // Overlapping rent found
                                                Toast.makeText(context, "There is already a rent in this time", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // No overlapping rents found, proceed to add new rent
                                                db.collection(stadiumName.getText().toString().replaceAll(" ",""))
                                                        .document(Preferences.GetUserEmail(context))
                                                        .set(data)
                                                        .addOnCompleteListener(addTask -> {
                                                            if (addTask.isSuccessful()) {
                                                                // Successfully added rent
                                                                HashMap<String, Object> rentHistoryData = new HashMap<>();
                                                                rentHistoryData.put("stadiumName", stadiumName.getText().toString());
                                                                rentHistoryData.put("startTime", formatCalendar(selectedDateTime));
                                                                rentHistoryData.put("endTime", formatCalendar(endTime));

                                                                // Add rent history as a sub-collection under renter's document
                                                                db.collection("RentHistory")
                                                                        .document(Preferences.GetUserEmail(context))
                                                                        .collection("RentTransactions")
                                                                        .add(rentHistoryData)
                                                                        .addOnSuccessListener(documentReference -> {
                                                                            // Successfully added rent history
                                                                            dialog.dismiss();
                                                                            Toast.makeText(context, "Rent Success from "+formatCalendar(selectedDateTime)+" to "+formatCalendar(endTime), Toast.LENGTH_SHORT).show();
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            // Failed to add rent history
                                                                            Toast.makeText(context, "Failed to add rent history", Toast.LENGTH_SHORT).show();
                                                                        });
                                                            } else {
                                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
        public void bind(String photo, String name, double price) {
            // Update UI elements with data
            stadiumPic.setImageBitmap(ImageUtils.decodeFromBase64(photo));
            stadiumName.setText(name);
            price_txt.setText("Price : " + price + "$/hour");
        }
    }

}



