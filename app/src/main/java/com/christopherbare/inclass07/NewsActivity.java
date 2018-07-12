package com.christopherbare.inclass07;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    String apiFirst = "https://newsapi.org/v2/top-headlines?apiKey=5e9b2a45503c43e988151236956f3f54&sources=";
    Activity activity;
    ArrayList<Article> articles;
    ArticleAdapter adapter;
    ListView articleList;
    Context context;
    Article article;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        articles = new ArrayList<>();
        articleList = findViewById(R.id.newsList);
        context = getApplicationContext();
        activity = NewsActivity.this;

        if(isConnected()) {
            if (getIntent().getExtras() != null){
                Source source = new Source();
                source.setName(getIntent().getExtras().getString("sourceName"));
                source.setId(getIntent().getExtras().getString("sourceID"));
                new GetArticleAsync().execute(apiFirst + source.getId());

                articleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Article article = articles.get(position);
                        String urlToArticle = article.getArticleURL();
                        Intent intent = new Intent(NewsActivity.this, WebViewActivity.class);
                        intent.putExtra("Url", urlToArticle);
                        startActivity(intent);
                    }
                });
            }

        } else
            Toast.makeText(getApplicationContext(), "No Network Connection.", Toast.LENGTH_LONG).show();
    }


    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    private class GetArticleAsync extends AsyncTask<String, Void, ArrayList<Article>> {
        ProgressDialog dialog;
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Article> result = new ArrayList<>();
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
                    JSONArray articlesJSON = root.getJSONArray("articles");

                    for (int i = 0; i < articlesJSON.length(); i++) {

                        //Initialize objects
                        article = new Article();

                        //Get the JSON sources
                        JSONObject source = articlesJSON.getJSONObject(i);

                        //Set the fields for object from the JSON one
                        article.setAuthorName(source.getString("author"));
                        article.setArticleURL(source.getString("url"));
                        article.setImageURL(source.getString("urlToImage"));
                        article.setReleaseDate(source.getString("publishedAt"));
                        article.setDescription(source.getString("description"));
                        article.setTitle(source.getString("title"));

                        Log.d("demo", "doInBackground: " + source.toString());

                        result.add(article);
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
            dialog.setMessage("Getting Articles");
            dialog.show();
        }


        public GetArticleAsync() {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPostExecute(ArrayList<Article> result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (articles.size() == 0) articles.addAll(result);
            Log.d("demo", "onPostExecute: Result size = " + result.size());
            adapter = new ArticleAdapter(NewsActivity.this, R.layout.article_item, result);
            Log.d("demo", "onPostExecute: adapter is null =  " + (adapter==null));
            articleList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}


