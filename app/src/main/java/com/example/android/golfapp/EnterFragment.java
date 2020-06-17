package com.example.android.golfapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import com.example.android.golfapp.Data.GolfViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "EnterFragment";
    //UI elements
    AutoCompleteTextView nameEditText;
    AutoCompleteTextView courseEditText;
    NumberPicker parNumberPicker;
    NumberPicker scoreNumberPicker;
    EditText dateEditText;
    Button enterButton;

    //Auto-complete arrays of strings for each auto Complete text view
    private String[] names = new String[]{};
    private String[] courses = new String[] {};

    //The Mainactivty context is the listener as it implements EnterFragmentListener
    private EnterFragmentListener listener;

    public EnterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_enter, container, false);
        //Assign the UI elements to variables
        enterButton = v.findViewById(R.id.enterButton);
        dateEditText = v.findViewById(R.id.date_edit_text);
        scoreNumberPicker = v.findViewById(R.id.score_number_picker);
        parNumberPicker = v.findViewById(R.id.par_number_picker);
        courseEditText = v.findViewById(R.id.course_edit_text);
        nameEditText = v.findViewById(R.id.name_edit_text);

        //Sets the max, min and current numbers of the numberpickers
        parNumberPicker.setMinValue(27);
        parNumberPicker.setMaxValue(80);
        scoreNumberPicker.setMinValue(25);
        scoreNumberPicker.setMaxValue(130);
        scoreNumberPicker.setValue(90);
        parNumberPicker.setValue(72);

        //Sets date to today by default. When user clicks it allow them to select date via a data picker alert
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateEditText.setText(dateFormat.format(new Date()));

        //Listener for when the date edit text view is clicked. When it is then the date picker dialog is created.
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Log.d(TAG, "onClick: date");
                    DialogFragment datePicker = new DatePickerFragment(EnterFragment.this);
                    datePicker.show(getActivity().getSupportFragmentManager(), getString(R.string.date_picker_tag));
                }
            }
        });
        //Viewmodel created that listens to the database for changes in the names and courses.
        //It changes the list returned into a set in both cases, this is done to remove duplicate values.
        //The set is then made into an array which is set to the variables in the fragment. These variables are then used for the autocomplete text fields
        //Hence the autocomplete text fields show auto complete text that is obtained from the database (i.e. based on previous entries)
        GolfViewModel viewModel = new ViewModelProvider(requireActivity()).get(GolfViewModel.class);
        viewModel.getNames().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> golfNames) {
                Log.d(TAG, "onChanged: " + "set names adapter");
                Set<String> removeDuplicatesSet = new HashSet<>(golfNames);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                setNames(arr);
            }
        });
        viewModel.getCourses().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> golfCourses) {
                Log.d(TAG, "onChanged: " + "set courses adapter");
                Set<String> removeDuplicatesSet = new HashSet<>(golfCourses);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                setCourses(arr);
            }
        });

        //Onclick listener for enter button
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tries to execute the code to send the data to the database
                try {
                    String name = nameEditText.getText().toString();
                    String course = courseEditText.getText().toString();
                    Log.d(TAG, "onClick: " + name);
                    //If the name or course field is empty then it prompts the user to enter one via a toast and does not try to submit the record
                    if (name.equals("") || course.equals("")) {
                        Toast.makeText(getContext(), R.string.toast_message_name_course_empty, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: failed due to name");
                        return;
                    }
                    //Checks if date not correct length
                    if (dateEditText.getText().toString().length() != 10) {
                        Toast.makeText(getContext(), R.string.toast_no_valid_date, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int par = parNumberPicker.getValue();
                    int score = scoreNumberPicker.getValue();
                    SimpleDateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = myDateFormat.parse(dateEditText.getText().toString());
                    if (listener != null) {
                        listener.onRecordSent(name, course, par, score, date);
                        //Clears the fields after the entry is made to the database
                        clearAllFields();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.toast_message_no_number_par_score, Toast.LENGTH_SHORT).show();
                } catch (Exception exc) {
                    //If the date is invalid this is thrown
                    exc.printStackTrace();
                    Toast.makeText(getContext(), R.string.toast_no_valid_date, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    //Clears all the UI fields ready for the next entry
    private void clearAllFields() {
        nameEditText.getText().clear();
        courseEditText.getText().clear();
        scoreNumberPicker.setValue(90);
        parNumberPicker.setValue(72);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateEditText.setText(dateFormat.format(new Date()));
    }

    //When names is set them it sets adapter to the edit text to show autocomplete values
    //This method is triggered from the viewmodel in the oncreateview method
    public void setNames(String[] names) {
        this.names = names;
        if (this.isAdded()) {
            ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, names);
            if (nameEditText != null) {
                nameEditText.setAdapter(namesAdapter);
                //Shows dropdown when has focus
                nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            nameEditText.showDropDown();
                        } else {
                            nameEditText.dismissDropDown();
                        }
                    }
                });
            }
        }
    }

    //When courses is set then it sets the courses to the edit text to show autocomplete values.
    //This method is triggered from the viewmodel in the oncreateview method
    public void setCourses(String[] courses) {
        this.courses = courses;
        if (this.isAdded()) {
            ArrayAdapter<String> coursesAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, courses);
            if (courseEditText !=null) {
                courseEditText.setAdapter(coursesAdapter);
                //Shows dropdown when has focus
                courseEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            courseEditText.showDropDown();
                        } else {
                            courseEditText.dismissDropDown();
                        }
                    }
                });
            }
        }
    }

    //This is the callback method for when the date id selected in the date picker dialog.
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "onDateSet: ");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        Log.d(TAG, "onDateSet: " + c);
        Date d = c.getTime();
        Log.d(TAG, "onDateSet: " + d);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateSet(dateFormat.format(d));
    }

    //This method is called from the method above to set the date selected to the text view in the chosen format
    public void dateSet(String format) {
        dateEditText.setText(format);
    }

    //When the fragment is attached to the activity then the context is checked to make sure it is of type MainActivity.
    //If it is then the context is cast into MainActivity and hidemenus() is called on it to hide the menu item as sort/filter is not required in this fragment.
    //The same is done to set the listener variable, which is how the record is sent to the activity to be put into the database via onrecord sent in the
    //click listener of the button which is set in oncreateview
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            activity.hideMenus();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EnterFragmentListener");
        }
        if (context instanceof EnterFragmentListener) {
            listener = (EnterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EnterFragmentListener");
        }
    }
    //The listener is set to null when the fragment detaches to stop memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //The EnterFragmentListener interface which is implemented by MainActivity
    public interface EnterFragmentListener {
        void onRecordSent(String name, String course, int par, int score, Date date);
    }
}
