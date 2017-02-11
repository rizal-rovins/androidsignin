package test.com.signinapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import mehdi.sakout.fancybuttons.FancyButton;


public class AddChild extends AppCompatActivity implements View.OnClickListener
{


    EditText Edate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
       Edate =(EditText)findViewById(R.id.editText);
        final EditText Esex=(EditText)findViewById(R.id.sex);
        final EditText Ename=(EditText)findViewById(R.id.editTextname);
Edate.setFocusable(false);
        Edate.setOnClickListener(this);
        Button b=(Button)findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String sex=Esex.getText().toString();
                String name=Ename.getText().toString();
                String date=Edate.getText().toString();
                if(!(sex.isEmpty()||name.isEmpty()||date.isEmpty()))
                try
                {
                    JSONObject mObject = new JSONObject();
                    mObject.put("childname", name);
                    mObject.put("dob", date);
                    mObject.put("sex", sex);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    mObject.put("username", pref.getString("username",null));

                   StringEntity entity = new StringEntity(mObject.toString());
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(getApplicationContext(), BaseUrl.url + "addchild.php", entity, "application/json", new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                            // called when response HTTP status is "200 OK"
                            Log.d("statusCode",String.valueOf(statusCode));
                            try
                            {
                                Toast.makeText(getApplicationContext(),"Successfully Added!",Toast.LENGTH_SHORT).show();
                                finish();

                            }
                            catch (Exception e)
                            {

                            }
                            }


                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                        }
                    });



                }
                catch (Exception e)
                {}
                else
                    Toast.makeText(getApplicationContext(),"One or more fields is empty!",Toast.LENGTH_SHORT).show();
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


                        Edate.setText(d + "/" + m + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}
