package com.christopherbare.inclass07;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Source source;
    String newsAPI = "https://newsapi.org/v2/sources?apiKey=5e9b2a45503c43e988151236956f3f54";
    Activity activity;
    ArrayList<Source> sources;
    SourceAdapter adapter;
    ListView sourceList;
    Context context;
    TextView sourceName;
    TextView sourceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sources = new ArrayList<>();
        sourceList = findViewById(R.id.list_view_sources);
        context = getApplicationContext();
        activity = MainActivity.this;

        if(isConnected()) {
            new GetSourcesAsync().execute(newsAPI);
        } else
            Toast.makeText(getApplicationContext(), "No Network Connection.", Toast.LENGTH_LONG).show();

        sourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                sourceName = findViewById(R.id.name);
                sourceID = findViewById(R.id.sourceID);

                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                intent.putExtra("sourceID", sourceID.getText().toString());
                intent.putExtra("sourceName", sourceName.getText().toString());


                startActivity(intent);
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    private class GetSourcesAsync extends AsyncTask<String, Void, ArrayList<Source>> {
        ProgressDialog dialog;
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected ArrayList<Source> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Source> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    //The JSON file in string format
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    //The entire JSON object
                    JSONObject root = new JSONObject(json);

                    //The array within the JSON object
                    JSONArray sourceJSON = root.getJSONArray("sources");


                    for (int i = 0; i < sourceJSON.length(); i++) {

                        //Initialize objects
                        source = new Source();

                        //Get the JSON sources
                        JSONObject sources = sourceJSON.getJSONObject(i);

                        //Set the fields for object from the JSON one
                        source.setName(sources.getString("name"));
                        source.setId(sources.getString("id"));

                        Log.d("demo", "doInBackground: " + source.toString());

                        result.add(source);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Getting Sources");
            dialog.show();
        }


        public GetSourcesAsync() {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPostExecute(ArrayList<Source> result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (sources.size() == 0) sources.addAll(result);


            adapter = new SourceAdapter(getApplicationContext(), R.layout.activity_source_item, result);
            sourceList = findViewById(R.id.list_view_sources);
            sourceList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}


