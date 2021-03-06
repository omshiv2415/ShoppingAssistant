package assistance.shopping.msc.assistant.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.main.NavigationActivity;
import assistance.shopping.msc.assistant.model.MyDetails;
import assistance.shopping.msc.assistant.model.User;
import assistance.shopping.msc.assistant.support.BaseActivity;
import assistance.shopping.msc.assistant.support.FragmentSupport;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {


    // [END declare_database_ref]
    private static final String TAG = "MyProfileFragment";
    private static final String REQUIRED = "Required";
    private static View view;
    FragmentSupport fragmentSupport = new FragmentSupport();
    final String userId = fragmentSupport.getUid();
    private DatabaseReference mDatabase;
    private DatabaseReference mUserFirstName;
    private DatabaseReference mUserLastName;
    private DatabaseReference mUserDateOfBirth;
    private DatabaseReference mUserGender;
    private DatabaseReference mUserProfile;
    private String[] state = {"Male", "Female", "Others"};
    private int mYear;
    private int mMonth;
    private int mDay;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mDateOfBirth;
    private AutoCompleteTextView mGender;
    private Button btnSubmit;
    private FirebaseAuth mAuth;
    public ImageView uProfile;
    public String mPhotoUrl;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            view = inflater.inflate(R.layout.fragment_my_profile, container, false);
            // [START initialize_database_ref]
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mUserFirstName = mDatabase.child("users").child(userId).child("FirstName");
            mUserLastName = mDatabase.child("users").child(userId).child("LastName");
            mUserDateOfBirth = mDatabase.child("users").child(userId).child("DateOfBirth");
            mUserGender = mDatabase.child("users").child(userId).child("Gender");
            mUserProfile = mDatabase.child("users").child(userId).child("UserPhoto");

            // [END initialize_database_ref]
            mAuth = FirebaseAuth.getInstance();

            mFirstName = (EditText) view.findViewById(R.id.firstName);
            mLastName = (EditText) view.findViewById(R.id.lastName);
            mDateOfBirth = (EditText) view.findViewById(R.id.dateOfBirth);
            uProfile = (ImageView) view.findViewById(R.id.profilePicture);

            //  mGender = (EditText) view.findViewById(R.id.gender);
            btnSubmit = (Button) view.findViewById(R.id.profileButton);


            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice, state);
            //Find TextView control
            mGender = (AutoCompleteTextView) view.findViewById(R.id.gender);
            //Set the number of characters the user must type before the drop down list is shown
            mGender.setThreshold(1);
            //Set the adapter
            mGender.setAdapter(adapter);
            mDateOfBirth.setInputType(0);
            mDateOfBirth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mDateofbirth = Calendar.getInstance();
                    mYear = mDateofbirth.get(Calendar.YEAR);
                    mMonth = mDateofbirth.get(Calendar.MONTH);
                    mDay = mDateofbirth.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog mDatePicker;

                    mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                            mDateOfBirth.setFocusable(false);
                            mDateOfBirth.setText(i3 + "/" + (i2 + 1) + "/" + i);
                        }
                    }, mDay, mMonth, mYear);//Yes 24 hour time

                    mDatePicker.setTitle("Select Date of Birth");
                    mDatePicker.updateDate(mYear - 18, mMonth, mDay);
                    mDateofbirth.set(mYear - 18, mMonth, mDay);
                    //mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    mDatePicker.getDatePicker().setMaxDate(mDateofbirth.getTimeInMillis());

                    mDatePicker.show();

                }
            });
            mUserFirstName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String text = dataSnapshot.getValue(String.class);
                    mFirstName.setText(text);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mUserLastName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String text = dataSnapshot.getValue(String.class);
                    mLastName.setText(text);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mUserDateOfBirth.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String text = dataSnapshot.getValue(String.class);
                    mDateOfBirth.setText(text);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mUserGender.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String text = dataSnapshot.getValue(String.class);
                    mGender.setText(text);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mUserProfile.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String text = dataSnapshot.getValue(String.class);
                    new DownloadImageTask((ImageView) view.findViewById(R.id.profilePicture))
                            .execute(String.valueOf(text));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    submitProfile();

                }
            });
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        return view;
    }

    private void submitProfile() {

        final String sFirstName = mFirstName.getText().toString();
        final String sLastName = mLastName.getText().toString();
        final String sDateOfBirth = mDateOfBirth.getText().toString();
        final String sGender = mGender.getText().toString();


        // FirstName is required
        if (TextUtils.isEmpty(sFirstName)) {
            mFirstName.setError(REQUIRED);
            return;
        }

        // LastName is required
        if (TextUtils.isEmpty(sLastName)) {
            mLastName.setError(REQUIRED);
            return;
        }
        // DateOfBirth is required
        if (TextUtils.isEmpty(sDateOfBirth)) {
            mDateOfBirth.setError(REQUIRED);
            return;
        }

        // Gender is required
        if (TextUtils.isEmpty(sGender)) {
            mGender.setError(REQUIRED);
            return;
        }

        final String sEmail = mAuth.getCurrentUser().getEmail();
        final String sUserName = usernameFromEmail(mAuth.getCurrentUser().getEmail());
        final String authentication = FirebaseInstanceId.getInstance().getToken();
        final String uGPhoto = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());


        if (mAuth.getCurrentUser().getProviders().contains("password")) {

            final String uPhoto;
            uPhoto = "https://lh3.googleusercontent.com/-et8-_Jd3MiY/AAAAAAAAAAI/AAAAAAAAAAs/9OWsA3w5ZGw/s96-c/photo.jpg";
            final String userId = fragmentSupport.getUid();

            mDatabase.child("users");
            mDatabase.child(userId);

            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {
                                // User is null, error out
                                Log.e(TAG, "User " + userId + " is unexpectedly null");
                                Toast.makeText(getActivity(),
                                        "Error: could not fetch user.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Update User Details
                                final Double TotalshoppingPoints = 00.00;

                                if (user.TotalshoppingPoints == null) {

                                    writeToProfile(userId, sFirstName, sLastName, sDateOfBirth, sGender, sEmail, sUserName, uPhoto, authentication, TotalshoppingPoints);
                                    Toast.makeText(getActivity(), "Profile updated Successfully", Toast.LENGTH_LONG).show();
                                    Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                    startActivity(takeUserHome);


                                } else {

                                    writeToProfile(userId, sFirstName, sLastName, sDateOfBirth, sGender, sEmail, sUserName, uPhoto, authentication, user.TotalshoppingPoints);
                                    Toast.makeText(getActivity(), "Profile updated Successfully", Toast.LENGTH_LONG).show();
                                    Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                    startActivity(takeUserHome);


                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
            // [END single_value_read]


        } else {

            // [START single_value_read]
            final String userId = fragmentSupport.getUid();

            mDatabase.child("users");
            mDatabase.child(userId);

            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {
                                // User is null, error out
                                Log.e(TAG, "User " + userId + " is unexpectedly null");
                                Toast.makeText(getActivity(),
                                        "Error: could not fetch user.",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                final Double TotalshoppingPoints = 00.00;

                                if (user.TotalshoppingPoints == null) {

                                    writeToProfile(userId, sFirstName, sLastName, sDateOfBirth, sGender, sEmail, sUserName, uGPhoto, authentication, TotalshoppingPoints);
                                    Toast.makeText(getActivity(), "Profile updated Successfully", Toast.LENGTH_LONG).show();
                                    Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                    startActivity(takeUserHome);


                                } else {

                                    writeToProfile(userId, sFirstName, sLastName, sDateOfBirth, sGender, sEmail, sUserName, uGPhoto, authentication, user.TotalshoppingPoints);
                                    Toast.makeText(getActivity(), "Profile updated Successfully", Toast.LENGTH_LONG).show();
                                    Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                    startActivity(takeUserHome);


                                }

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
            // [END single_value_read]


        }

    }

    //creating username from email
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }

    }

    public void writeToProfile(String userId, String firstname, String lastname, String dateofbirth, String gender
            , String email, String username, String uPhoto, String authentication, Double TotalshoppingPoints) {


        MyDetails myDetails = new MyDetails(userId, firstname, lastname, dateofbirth, gender, email, username, uPhoto, authentication, TotalshoppingPoints);
        Map<String, Object> myDetailsValues = myDetails.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/users/" + userId + "/", myDetailsValues);

        mDatabase.updateChildren(childUpdates);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}


