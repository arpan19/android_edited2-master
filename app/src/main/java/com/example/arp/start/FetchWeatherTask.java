package com.example.arp.start;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.arp.start.data.PatContract.PatEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Arp on 2/8/2015.
 */

    public class  FetchWeatherTask extends AsyncTask<String,Void,Void > {
    String[] s1=new String[50];
    String[] s=new String[50];

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private ArrayAdapter<String> mForecastAdapter;
    private final Context mContext;
    public FetchWeatherTask(Context context) {
        mContext = context;

    }


int i;

        private Void getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {


            JSONArray jarray = new JSONArray(forecastJsonStr);
            int length = jarray.length();
            Vector<ContentValues> cVVector = new Vector<ContentValues>(jarray.length());
            Log.v(LOG_TAG, "Getting data");

            for (i = 0; i < length; i++) {

                JSONObject details = jarray.getJSONObject(i);
                String serial_number = details.getString("serial_number");
                String company_name = details.getString("company_name");
                String dat = details.getString("dat");
                String eligibility_criteria = details.getString("eligibility_criteria");
                String branch = details.getString("branch");
                String salary = details.getString("salary");
                String deadline = details.getString("deadline");
                String other_info = details.getString("other_info");
                s[i] = "\nCompany:" + company_name + "\nDate:" + dat + "\n";
                Log.d("sample", s[i]);
                s1[i] = "\n" + i + "\t" + serial_number + "\n\nCompany:" + company_name + "\n\nDate:" + dat + "\n\nEligibilty Criteria:" + eligibility_criteria + "\n\nBranch:" + branch + "\n\nDeadline:" + deadline + "\n\nOther Information:" + other_info + "\n";
                ContentValues weatherValues = new ContentValues();

                weatherValues.put(PatEntry.COLUMN_SERIAL_NUMBER, serial_number);

                weatherValues.put(PatEntry.COLUMN_COMPANY_NAME, company_name);
                weatherValues.put(PatEntry.COLUMN_DAT, dat);
                weatherValues.put(PatEntry.COLUMN_ELIGIBILITY_CRITERIA, eligibility_criteria);
                weatherValues.put(PatEntry.COLUMN_BRANCH, branch);
                weatherValues.put(PatEntry.COLUMN_SALARY, salary);
                weatherValues.put(PatEntry.COLUMN_DEADLINE, deadline);
                weatherValues.put(PatEntry.COLUMN_OTHER_INFO, other_info);

                cVVector.add(weatherValues);
            }
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                mContext.getContentResolver().bulkInsert(PatEntry.CONTENT_URI, cvArray);
            }
return null;
        }



        @Override
        protected Void doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            int numDays = 5;


            try {

                final String FORECAST_BASE_URL = "http://codeworld.uphero.com/details.php";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().build();


                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG, "built URI" + urlConnection.toString());
                urlConnection.setRequestMethod("GET");
                //error here in connection
                urlConnection.connect();
                Log.v(LOG_TAG, "Connection established");
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
                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();


            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }

            }
            try {
                 getWeatherDataFromJson(forecastJsonStr, numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            }
            return null;


        }



    }





