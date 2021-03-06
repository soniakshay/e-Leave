package com.example.user.e_leave;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Todayleave extends Activity {
    ListView llistview;
    customleaveadeptor ladeptor;
    List<leavetoday> llleave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todayleave);
        new MyTask(this).execute();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todayleave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class MyTask extends AsyncTask<String, Void, String> {
        String type;
        Context context;
        AlertDialog alertDialog;
        MyTask (Context ctx) {
            context = ctx;

        }
        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Status");
        }
        @Override
        protected String doInBackground(String... params) {

                String designation_url = "http://16mca004.000webhostapp.com/eleave/eleave/today_leave.php";

                try {
                    URL url = new URL(designation_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            return null;
        }

        @Override
        protected void onPostExecute(String args) {

            String name1 ,todate1 ,fromdate1 ,description,designation1;
            llistview = (ListView) findViewById(R.id.tvlist);
            llleave = new ArrayList<>();



            try {

                JSONArray jobj = new JSONArray(args);

                for (int i = 0; i <=jobj.length(); i++) {
                    JSONObject j=new JSONObject(jobj.getString(i));


                    name1 = j.getString("first_name")+" " + j.getString("last_name");
                    todate1 = j.getString("TO");
                    fromdate1 = j.getString("FROM");
                    description = j.getString("Desc");
                    designation1= j.getString("Name");

                    llleave.add(new leavetoday(name1 ,fromdate1 ,todate1,description,designation1));
                    ladeptor = new customleaveadeptor(getApplicationContext(), llleave);
                    llistview.setAdapter(ladeptor);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        
        }


    }






    public class customleaveadeptor extends BaseAdapter {
        private Context lcontext;
        private List<leavetoday> llist;
        public customleaveadeptor(Context lcontext, List<leavetoday> llist) {
            this.lcontext = lcontext;
            this.llist = llist;
        }

        @Override
        public int getCount() {
            return llist.size();
        }

        @Override
        public Object getItem(int position) {
            return llist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public void  popid(String s)
        {

        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            View v=View.inflate(lcontext,R.layout.customleavetoday,null);
            TextView name=(TextView)v.findViewById(R.id.tvname);
            TextView todate=(TextView)v.findViewById(R.id.todate);
            TextView fromdate=(TextView)v.findViewById((R.id.fromdate));
            TextView desc=(TextView)v.findViewById((R.id.desc));
            TextView designation=(TextView)v.findViewById((R.id.dept));


            name.setText(llist.get(position).getName());
            todate.setText(llist.get(position).getTodate());
            fromdate.setText(llist.get(position).getFromdate());
            desc.setText(llist.get(position).getDesc());
            designation.setText(llist.get(position).getDesignation());
            return v;


        }
    }







}
