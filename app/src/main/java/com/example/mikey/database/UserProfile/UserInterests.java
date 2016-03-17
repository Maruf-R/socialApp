package com.example.mikey.database.UserProfile;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikey.database.R;

public class UserInterests extends AppCompatActivity {

    private RadioGroup radioGroupAvatar;
    private RadioButton radioAvatar;
    private Button btnFirstLayout,
            btnSecondLayoutBack, btnSecondLayoutNext,
            btnThirdLayoutBack, btnThirdLayoutNext,
            btnFourthLayoutBack, btnFourthLayoutNext,
            btnFifthLayoutBack, btnFifthLayoutNext,
            btnSixthLayoutBack, btnSixthLayoutNext,
            btnSeventhLayoutBack, btnSeventhLayoutNext;
    private RelativeLayout firstLayout, secondLayout, thirdLayout, fourthLayout, fifthLayout, sixthLayout, seventhLayout;
    private EditText editBiography;

    //Variables that need to be stored in the database
    private String userAvatar;
    private String userBiography;
    private String userMusic;
    private String userMovies;
    private String userSports;
    private String userFood;
    private String userHobbies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interests);
        firstLayout = (RelativeLayout)findViewById(R.id.firstLayout);
        firstLayout.setVisibility(View.VISIBLE);
        setFirstLayout();
        setSecondLayout();
        setThirdLayout();
        setFourthLayout();
        setFifthLayout();
        setSixthLayout();
        setSeventhLayout();

        /**
         * TODO fix avatar
         * TODO get rid of toast messages
         * TODO Link with other activities
         */

    }

    /**
     * This method is used to create the avatar selection layout.
     * User has to pick an avatar before they can proceed to fill out
     * any other details.
     * @Result stores the id of the avatar that is chose by user.
     */
    private void setFirstLayout() {
        secondLayout = (RelativeLayout)findViewById(R.id.secondLayout);
        secondLayout.setVisibility(View.INVISIBLE);
        radioGroupAvatar = (RadioGroup)findViewById(R.id.radioGroupAvatar);
        btnFirstLayout = (Button)findViewById(R.id.btnFirstLayout);
        btnFirstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroupAvatar.getCheckedRadioButtonId();
                radioAvatar = (RadioButton) findViewById(selectedId);

                try {

                    userAvatar = radioAvatar.getText().toString();
                    // TODO remove toast
                    Toast.makeText(UserInterests.this, userAvatar, Toast.LENGTH_SHORT).show();
                    Log.d("Godam avatar text", radioAvatar.getText().toString());
                    firstLayout.setVisibility(View.INVISIBLE);
                    secondLayout.setVisibility(View.VISIBLE);

                } catch (NullPointerException e) {
                    Toast.makeText(UserInterests.this, "Please select an avatar.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * This method is used to learn a bit more about the user.
     * They are asked to write about themselves in a text box.
     * @Result If anything is entered in the text box it is stored in
     * userBiography variable which will be shown on their profile.
     */
    private void setSecondLayout() {
        thirdLayout = (RelativeLayout)findViewById(R.id.thirdLayout);
        thirdLayout.setVisibility(View.INVISIBLE);
        btnSecondLayoutBack = (Button)findViewById(R.id.btnSecondLayoutBack);
        btnSecondLayoutNext = (Button)findViewById(R.id.btnSecondLayoutNext);
        editBiography = (EditText)findViewById(R.id.editBiography);

        btnSecondLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondLayout.setVisibility(View.INVISIBLE);
                firstLayout.setVisibility(View.VISIBLE);
            }
        });

        btnSecondLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdLayout.setVisibility(View.VISIBLE);
                secondLayout.setVisibility(View.INVISIBLE);
                userBiography = editBiography.getText().toString();
                Log.d("Bio", userBiography);
            }
        });
    }


    /**
     * This method asks the user what genre of music they like.
     * @Result all tickboxes which are ticked have their text appended
     * into a userMusic string which is in turn stored on te database.
     */
    private void setThirdLayout() {
        fourthLayout = (RelativeLayout)findViewById(R.id.fourthLayout);
        fourthLayout.setVisibility(View.INVISIBLE);
        btnThirdLayoutBack = (Button)findViewById(R.id.btnThirdLayoutBack);
        btnThirdLayoutNext = (Button) findViewById(R.id.btnThirdLayoutNext);

        btnThirdLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdLayout.setVisibility(View.INVISIBLE);
                secondLayout.setVisibility(View.VISIBLE);
            }
        });

        btnThirdLayoutNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();

                for(int i = 1; i < 10; i++) {
                    int resId = getResources().getIdentifier("checkMusic" + i, "id", getPackageName());
                    CheckBox box = (CheckBox)findViewById(resId);
                    if(box.isChecked() == true) {
                        result.append(box.getText().toString() + ", ");

                    }
                }

                userMusic = result.toString();
                // TODO get rid of toast for FINAL version
                Toast.makeText(UserInterests.this, userMusic, Toast.LENGTH_SHORT).show();

                thirdLayout.setVisibility(View.INVISIBLE);
                fourthLayout.setVisibility(View.VISIBLE);

            }
        });

    }

    /**
     * This method asks the user what genre of movies they like.
     * @Result all checkboxes which are ticked have their text appended
     * into a a userMovies string.
     */
    private void setFourthLayout() {
        fifthLayout = (RelativeLayout)findViewById(R.id.fifthLayout);
        fifthLayout.setVisibility(View.INVISIBLE);
        btnFourthLayoutBack = (Button)findViewById(R.id.btnFourthLayoutBack);
        btnFourthLayoutNext =  (Button)findViewById(R.id.btnFourthLayoutNext);

        btnFourthLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourthLayout.setVisibility(View.INVISIBLE);
                thirdLayout.setVisibility(View.VISIBLE);
            }
        });

        btnFourthLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();

                for(int i = 1; i < 10; i++) {
                    int resId = getResources().getIdentifier("checkMovies" + i, "id", getPackageName());
                    CheckBox box = (CheckBox)findViewById(resId);
                    if(box.isChecked() == true) {
                        result.append(box.getText().toString() + ", ");

                    }
                }

                userMovies = result.toString();
                // TODO get rid of toast for FINAL version
                Toast.makeText(UserInterests.this, userMovies, Toast.LENGTH_SHORT).show();

                fourthLayout.setVisibility(View.INVISIBLE);
                fifthLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    private void setFifthLayout() {
        sixthLayout = (RelativeLayout)findViewById(R.id.sixthLayout);
        sixthLayout.setVisibility(View.INVISIBLE);
        btnFifthLayoutBack = (Button)findViewById(R.id.btnFifthLayoutBack);
        btnFifthLayoutNext =  (Button)findViewById(R.id.btnFifthLayoutNext);

        btnFifthLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fifthLayout.setVisibility(View.INVISIBLE);
                fourthLayout.setVisibility(View.VISIBLE);
            }
        });

        btnFifthLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();

                for(int i = 1; i < 10; i++) {
                    int resId = getResources().getIdentifier("checkSports" + i, "id", getPackageName());
                    CheckBox box = (CheckBox)findViewById(resId);
                    if(box.isChecked() == true) {
                        result.append(box.getText().toString() + ", ");

                    }
                }

                userSports = result.toString();
                // TODO get rid of toast for FINAL version
                Toast.makeText(UserInterests.this, userSports, Toast.LENGTH_SHORT).show();

                fifthLayout.setVisibility(View.INVISIBLE);
                sixthLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    private void setSixthLayout() {
        seventhLayout = (RelativeLayout)findViewById(R.id.seventhLayout);
        seventhLayout.setVisibility(View.INVISIBLE);
        btnSixthLayoutBack = (Button)findViewById(R.id.btnSixthLayoutBack);
        btnSixthLayoutNext =  (Button)findViewById(R.id.btnSixthLayoutNext);

        btnSixthLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sixthLayout.setVisibility(View.INVISIBLE);
                fifthLayout.setVisibility(View.VISIBLE);
            }
        });

        btnSixthLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();

                for(int i = 1; i < 10; i++) {
                    int resId = getResources().getIdentifier("checkFood" + i, "id", getPackageName());
                    CheckBox box = (CheckBox)findViewById(resId);
                    if(box.isChecked() == true) {
                        result.append(box.getText().toString() + ", ");

                    }
                }

                userFood = result.toString();
                // TODO get rid of toast for FINAL version
                Toast.makeText(UserInterests.this, userFood, Toast.LENGTH_SHORT).show();

                sixthLayout.setVisibility(View.INVISIBLE);
                seventhLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setSeventhLayout() {
        btnSeventhLayoutBack = (Button)findViewById(R.id.btnSeventhLayoutBack);
        btnSeventhLayoutNext =  (Button)findViewById(R.id.btnSeventhLayoutNext);

        btnSeventhLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seventhLayout.setVisibility(View.INVISIBLE);
                sixthLayout.setVisibility(View.VISIBLE);
            }
        });

        btnSeventhLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();

                for(int i = 1; i < 10; i++) {
                    int resId = getResources().getIdentifier("checkHobbies" + i, "id", getPackageName());
                    CheckBox box = (CheckBox)findViewById(resId);
                    if(box.isChecked() == true) {
                        result.append(box.getText().toString() + ", ");

                    }
                }

                userHobbies = result.toString();
                // TODO get rid of toast for FINAL version
                Toast.makeText(UserInterests.this, userHobbies, Toast.LENGTH_SHORT).show();

                //seventhLayout.setVisibility(View.INVISIBLE);
            }
        });
    }




}
