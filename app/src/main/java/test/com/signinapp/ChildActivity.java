package test.com.signinapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ChildActivity extends AppCompatActivity
{



    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    String filename,filename2;

    Child child_list[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        Bundle extras = getIntent().getExtras();
        this.setTitle("Children");
///        Log.d("extra",extras.getString("data"));

        if (extras != null)
        {

            filename = extras.getString("data");
            JSONObject obj=new JSONObject();
            try
            {
                obj.put("username", filename);

            StringEntity e=new StringEntity(obj.toString());
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(getApplicationContext(), BaseUrl.url + "children.php", e, "application/json", new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                        //Toast.makeText(getApplicationContext(), "YO",Toast.LENGTH_SHORT).show();
                       try
                       {
                           filename2 = new String(response, "UTF-8");
                           Log.d("he",filename2);
                           final JSONObject temp = new JSONObject(filename2).getJSONObject("data");


                           final JSONArray temp2=temp.getJSONArray("children");
                           Log.d("child",String.valueOf(temp2));
                           child_list=new Child[temp2.length()+1];
                           for(int i = 0 ; i < temp2.length() ; i++)
                           {
                               JSONObject JObject = temp2.getJSONObject(i);
                               child_list[i] = new Child(R.drawable.face, JObject.getInt("childid"),JObject.getString("childname"),JObject.getString("dob"),JObject.getString("sex"));
                                if(JObject.getString("sex").equals("f"))
                                    child_list[i] = new Child(R.drawable.facegirl, JObject.getInt("childid"),JObject.getString("childname"),JObject.getString("dob"),JObject.getString("sex"));



                           }
                           child_list[temp2.length()]=new Child(R.drawable.add, 0,"ADD ANOTHER","00/00/0000","m");


                           chilAdapter adapter = new chilAdapter(getApplicationContext(), R.layout.listview_item_row, child_list);
                           ListView listView1 = (ListView)findViewById(R.id.listview2);

                           listView1.setAdapter(adapter);
                           listView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
                           {
                               @Override
                               public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                               {
                                   if(position==temp2.length())
                                   {
                                       Intent i=new Intent(ChildActivity.this, AddChild.class);
                                       i.putExtra("data",filename);
                                       startActivity(i);
                                   }
                                   else
                                   {    Intent i=new Intent(ChildActivity.this, ChildDetails.class);
                                        i.putExtra("childdata",String.valueOf(child_list[position].getChildid()));
                                       startActivity(i);
                                   }


                               }
                           });



                       }catch (Exception e)
                       {}
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        Toast.makeText(getApplicationContext(), "Yi",Toast.LENGTH_SHORT).show();

                    }
                });


            }catch (Exception e)
            {}
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main2);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();
        mToggle.syncState();
        //navList=(ListView)findViewById(R.id.)
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout.requestLayout();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        switch (menuItem.getItemId())
                        {
                            case R.id.children:
                                break;

                            case R.id.pets:
                                Intent intent = new Intent(ChildActivity.this, PetActivity.class);
                                intent.putExtra("data",filename);
                                startActivity(intent);
                                finish();
                                break;
                        }

                        mDrawerLayout.closeDrawers();


                        return true;
                    }
                });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    ////////////SETTING ACTIONBAR MENU//////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////
    //////////SETTING UP ACTION BAR OPTION METHODS///////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.action_exit:
                finish();
                break;
            case R.id.rate_app:
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try
                {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }


        }
        return true;
    }

}

