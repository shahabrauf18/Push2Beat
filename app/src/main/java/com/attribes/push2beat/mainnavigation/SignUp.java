package com.attribes.push2beat.mainnavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.models.BodyParams.SignUpParams;
import com.attribes.push2beat.network.DAL.SignUpDAL;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText password;
    EditText repeatpassword;
    Button createacount;
    CheckBox checkBox;
    Context mcontext;
    Boolean issuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
       Common.getInstance().initializeQBInstance(getApplicationContext());

        checkBox = (CheckBox) findViewById(R.id.checkbox);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);

        repeatpassword = (EditText) findViewById(R.id.repeatpassword);
        createacount = (Button) findViewById(R.id.create);



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {


                if (isChecked) {
                    createacount.setEnabled(true);

                } else {
                    createacount.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "You Must Agree Terms And Conditions", Toast.LENGTH_SHORT).show();


                }

            }
        });

        createacount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SignUpParams user = new SignUpParams();

                user.setFirstName("" + username.getText().toString());
                user.setLastName("");
                user.setLongitude("67.123432");
                user.setLattitude("24.861512");
                user.setProfileImage("");
                user.setEmail("" + email.getText().toString());
//                if(user.getPassword().toString().length()<8)
//                {
//                    Toast.makeText(getApplicationContext(), "Password Must be of 8 digits", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    createacount.setEnabled(true);
//                }
                user.setPassword("" + password.getText().toString());
//if(user.getPassword().toString().length()<8&&!isValidPassword(user.getPassword().toString()))
//{
//    createacount.setEnabled(false);
//    Toast.makeText(getApplicationContext(), "Password Should 8 characters", Toast.LENGTH_SHORT).show();
//}
//else{
//    createacount.setEnabled(true);
//}
                user.setPassword("" + password.getText().toString());

//
                SignUpDAL.userRegister(user, getApplicationContext(), new OnSignUpSuccess() {
                    @Override
                    public void onSuccess() {
                        acntsignup(email.getText().toString().trim(), password.getText().toString().trim());

                    }


                    @Override
                    public void onFailure() {

                    }
                });




            }
        });
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    public  void acntsignup(String email,String password) {
        QBUser qbUser = new QBUser();
        qbUser.setLogin(email);
        qbUser.setEmail(email);
        qbUser.setPassword(password);

        QBUsers.signUp(qbUser).performAsync( new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle)
            {
                Toast.makeText(getApplicationContext(), "QuickBlox Signup Success", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SignUp.this,SignIn.class));
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getApplicationContext(), "QuickBlox Signup Failed", Toast.LENGTH_SHORT).show();

            }
        });



    }

}