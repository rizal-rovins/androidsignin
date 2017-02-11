package test.com.signinapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import mehdi.sakout.fancybuttons.FancyButton;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText mEmailView,mUsernameView,mNameView,mDateView,mSexView;
    private EditText mPasswordView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmailView = (EditText) findViewById(R.id.email);
        mSexView=(EditText)findViewById(R.id.sex);
        mPasswordView = (EditText) findViewById(R.id.password);
        mNameView=(EditText)findViewById(R.id.name);
        mUsernameView=(EditText)findViewById(R.id.username);
        mDateView=(EditText)findViewById(R.id.date);
        mDateView.setFocusable(false);
       mDateView.setOnClickListener(this);
        FancyButton registerButton=(FancyButton)findViewById(R.id.button);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email,password,username,name,sex,dob;
                email=mEmailView.getText().toString();
                password=mPasswordView.getText().toString();
                username=mUsernameView.getText().toString();
                sex=mSexView.getText().toString();
                dob=mDateView.getText().toString();
                name=mNameView.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)|| TextUtils.isEmpty(name)||TextUtils.isEmpty(username)||TextUtils.isEmpty(sex))
                {
                    Toast.makeText(getApplicationContext(),"One or more fields is empty!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(!(email.contains("@")&& email.contains(".")))
                    {
                        Toast.makeText(getApplicationContext(),"Invalid E-mail address!",Toast.LENGTH_LONG).show();
                    }
                    else{
                    JSONObject jsonParams = new JSONObject();
                    StringEntity entity = null;
                    try {
                        jsonParams.put("username", username);
                        jsonParams.put("password", password);
                        jsonParams.put("name",name);
                        jsonParams.put("email",email);
                        jsonParams.put("sex",sex);
                        jsonParams.put("dob",dob);
                        entity = new StringEntity(jsonParams.toString());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Json failed",Toast.LENGTH_LONG).show();
                    }

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(getApplicationContext(), BaseUrl.url + "registeruser.php", entity, "application/json", new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                            // called when response HTTP status is "200 OK"
                            Log.d("statusCode",String.valueOf(statusCode));
                            try
                            {
                                if( new String(response, "UTF-8").contains("alreadyexists"))
                                    Toast.makeText(RegisterActivity.this,"Username already exists! Please try a different one!", Toast.LENGTH_LONG).show();

                                else
                                {
                                    Toast.makeText(RegisterActivity.this,"Registered successfully! Please Login", Toast.LENGTH_LONG).show();

                                    finish();
                                }
                                //Toast.makeText(RegisterActivity.this, new String(response, "UTF-8")+" " + statusCode, Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {

                            }



                        }


                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.d("statusCode",String.valueOf(statusCode));
                            Toast.makeText(RegisterActivity.this, errorResponse.toString()+"Failed", Toast.LENGTH_LONG).show();

                        }
                    });

                }
                }
            }
        });

    }


    @Override
    public void onClick(View v)
    {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String m , d;
                        if(dayOfMonth<10)
                            d="0"+String.valueOf(dayOfMonth);
                        else d=String.valueOf(dayOfMonth);

                        if(monthOfYear+1<10)
                            m="0"+String.valueOf(monthOfYear+1);
                        else
                            m=String.valueOf(monthOfYear+1);

                        Log.d("df",String.valueOf(1+1));


                        mDateView.setText(d + "/" + m + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}
