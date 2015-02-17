package com.example.arp.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.arp.start.data.PatContract.PatEntry;

/**
 * Created by Arp on 2/8/2015.
 */
public class menu2_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    public static final String LOG_TAG = "MyApp";
    View rootview;
    private  String mLocation;
    private  static  final int FORECAST_LOADER=0;

    private  static final String[] FORECAST_COLUMNS ={
            PatEntry.TABLE_NAME+"."+PatEntry._ID,
            PatEntry.COLUMN_SERIAL_NUMBER,
            PatEntry.COLUMN_COMPANY_NAME,
            PatEntry.COLUMN_DAT,
            PatEntry.COLUMN_ELIGIBILITY_CRITERIA,
            PatEntry.COLUMN_BRANCH,
            PatEntry.COLUMN_SALARY,
            PatEntry.COLUMN_DEADLINE,
            PatEntry.COLUMN_OTHER_INFO
    };


    public menu2_Fragment() {

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }
    private SimpleCursorAdapter checkingAdapter;
    String[] s = new String[20];




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }//for a refresh menu ,has a menu


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)

    {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


      /*  String[] checkinglist = { "name-arpan","name-shreya","name-piyush"};
        List<String> checklist = new ArrayList<String>(Arrays.asList(checkinglist));*/
        checkingAdapter = new SimpleCursorAdapter(


                getActivity(),
                R.layout.list_all,
                null,
                new String[]{
                        PatEntry.COLUMN_SERIAL_NUMBER,
                        PatEntry.COLUMN_COMPANY_NAME,
                        PatEntry.COLUMN_DAT,
                        PatEntry.COLUMN_ELIGIBILITY_CRITERIA,
                        PatEntry.COLUMN_BRANCH,
                        PatEntry.COLUMN_SALARY,
                        PatEntry.COLUMN_DEADLINE,
                        PatEntry.COLUMN_OTHER_INFO

                },
                new int[]{R.id.serial_number,
                        R.id.company_name,
                        R.id.date,
                        R.id.eligibility_criteria,
                        R.id.branch,
                        R.id.salary,
                        R.id.deadline,
                        R.id.other_info

                },
                0
        );
        rootview = inflater.inflate(R.layout.menu2_layout, container, false);
        ListView listView = (ListView) rootview.findViewById(R.id.all_companies);
        listView.setAdapter(checkingAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, "placeholder");
                startActivity(intent);
            }
        });

        return rootview;
    }




    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString("", "");

        weatherTask.execute(location);

    }

    @Override

    public void onStart() {

        super.onStart();
        updateWeather();
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {





        Uri tableUri= PatEntry.CONTENT_URI;

                       /*     String sortOrder = WeatherEntry.COLUMN_DATETEXT + " ASC";

                    mLocation = Utility.getPreferredLocation(getActivity());
           Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
                            mLocation, startDate); */

        return new CursorLoader
                (
                        getActivity(),
                        tableUri,
                        FORECAST_COLUMNS,
                        null,
                        null,
                        null
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        checkingAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        checkingAdapter.swapCursor(null);
    }
}


  /*  public class  FetchWeatherTask extends AsyncTask<String,Void,String[] >
  {


        int numDays=5;
        private String[] getWeatherDataFromJson(String forecastJsonStr)
                throws JSONException {
            try
            {

                JSONArray jarray = new JSONArray(forecastJsonStr);
                int length = jarray.length();
                Log.v(LOG_TAG,"Getting data");

                for (int i = 0; i < length; i++) {
                    JSONObject details = jarray.getJSONObject(i);
                    String serial_number= details.getString("serial_number");
                    String company_name= details.getString("company_name");
                    String dat= details.getString("dat");
                    String eligibility_criteria= details.getString("eligibility_criteria");
                    String branch= details.getString("branch");
                    String salary= details.getString("salary");
                    String deadline= details.getString("deadline");
                    String other_info= details.getString("other_info");
                    s[i]="serial_number"+serial_number+"company_name"+company_name+"dat"+dat+"eligibility_criteria"+eligibility_criteria+"branch"+branch+
                            "salary"+salary+"deadline"+deadline+"other_info"+other_info;
                    Log.d("sample",s[i]);

                }

            }
            catch(Exception e){
                Log.d(menu2_Fragment.LOG_TAG,"e1");
            }
                return s;
        }

 /*       @Override
        protected String[] doInBackground(String... params)
        {
            if(params.length==0)
            {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;



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
                return getWeatherDataFromJson(forecastJsonStr);
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

*/
