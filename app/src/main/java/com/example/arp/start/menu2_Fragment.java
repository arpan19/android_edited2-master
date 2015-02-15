package com.example.arp.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Arp on 2/8/2015.
 */
public class menu2_Fragment extends Fragment {
    int i;
    String[] s1=new String[50];
    public static final String LOG_TAG = "MyApp";
    View rootview;
    private ArrayAdapter<String> checkingAdapter;
    String[] s = new String[20];
    public menu2_Fragment()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }//for a refresh menu ,has a menu


    @Override
    public  void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        inflater.inflate(R.menu.refresh,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)

    {
        int id= item.getItemId();
        if(id == R.id.action_refresh)
        {
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("94043");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


      /*  String[] checkinglist = { "name-arpan","name-shreya","name-piyush"};
        List<String> checklist = new ArrayList<String>(Arrays.asList(checkinglist));*/
      checkingAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_all,
                R.id.list_all_text,
                new ArrayList<String>());
        rootview=inflater.inflate(R.layout.menu2_layout,container,false);
        ListView listView = (ListView) rootview.findViewById(R.id.all_companies);
        listView.setAdapter(checkingAdapter);
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String forecast = checkingAdapter.getItem(position);
        Intent intent=new Intent(getActivity(),DetailActivity.class).putExtra(Intent.EXTRA_TEXT,s1[position]);
        startActivity(intent);
    }
});
        return rootview;
    }

    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location=prefs.getString("","");

        weatherTask.execute(location);

    }
    @Override
    public  void onStart()
    {
        super.onStart();
        updateWeather();
    }


    public class  FetchWeatherTask extends AsyncTask<String,Void,String[] > {

        private String[] getWeatherDataFromJson(String forecastJsonStr,int numDays)
                throws JSONException {
            try
            {

                JSONArray jarray = new JSONArray(forecastJsonStr);
                int length = jarray.length();
Log.v(LOG_TAG,"Getting data");

                for (i = 0; i < length; i++) {
                    JSONObject details = jarray.getJSONObject(i);
                    String serial_number= details.getString("serial_number");
                    String company_name= details.getString("company_name");
                    String dat= details.getString("dat");
                    String eligibility_criteria= details.getString("eligibility_criteria");
                    String branch= details.getString("branch");
                    String salary= details.getString("salary");
                    String deadline= details.getString("deadline");
                    String other_info= details.getString("other_info");
                    s[i]="\nCompany:\n"+company_name+"\nDate:\n"+dat+"\n";
                    Log.d("sample",s[i]);
                    s1[i]="\n"+i+"\t"+serial_number+"\n\nCompany:\n"+company_name +"\n\nDate:\n"+dat +"\n\nEligibilty Criteria:\n"+eligibility_criteria+"\n\nBranch:\n"+branch+"\n\nDeadline:\n"+deadline+"\n\nOther Information:\n"+other_info+"\n";


                }

            }
            catch(Exception e){
                Log.d(menu2_Fragment.LOG_TAG,"e1");
            }
return s;
        }

        @Override
        protected String[] doInBackground(String... params)
        {
            if(params.length==0)
            {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
int numDays=5;


            try {

                final String FORECAST_BASE_URL= "http://codeworld.uphero.com/details.php";


                Uri builtUri=Uri.parse(FORECAST_BASE_URL).buildUpon().build();


                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG,"built URI"+ urlConnection.toString());
                urlConnection.setRequestMethod("GET");
                //error here in connection
                urlConnection.connect();
                Log.v(LOG_TAG,"Connection established");
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");

                }
                if (buffer.length() == 0)
                {
                    return null;
                }
                forecastJsonStr = buffer.toString();




            }
            catch (IOException e){
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final IOException e)
                    {

                    }
                }

            }
            try {
                return getWeatherDataFromJson(forecastJsonStr, numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            }
            return null;


        }


        @Override
        protected void onPostExecute(String[] result)
        {
            if(result!=null)
            {
                checkingAdapter.clear();
                for(String dayForecastStr : result)
                {
                    checkingAdapter.add(dayForecastStr);
                }
            }
        }

        }
    }


