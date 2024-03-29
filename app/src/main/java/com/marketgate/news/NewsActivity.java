package com.marketgate.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.marketgate.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsActivity extends AppCompatActivity {

    String API_KEY = "57fc74ed348a4199bc57f680544ff204"; // ### YOUE NEWS API HERE ###
    String NEWS_SOURCE = "google-news";
    ListView listNews;
    ProgressBar loader;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listNews = (ListView) findViewById(R.id.listNews);
        loader = (ProgressBar) findViewById(R.id.loader);
        listNews.setEmptyView(loader);



        if(Function.isNetworkAvailable(getApplicationContext()))
        {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        }else{
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }


    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            String xml = "";

            String urlParameters = "";
            xml = Function.excuteGet("https://newsapi.org/v1/articles?source="+NEWS_SOURCE+"&sortBy=top&category=agriculture&apiKey="+API_KEY, urlParameters);

            return  xml;

        }
        @Override
        protected void onPostExecute(String xml) {

                if(xml.length()>10){ // Just checking if not empty

                    try {
                        JSONObject jsonResponse = new JSONObject(xml);
                        JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                            map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                            map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                            map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                            map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                            map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                            dataList.add(map);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                    ListNewsAdapter adapter = new ListNewsAdapter(NewsActivity.this, dataList);
                    listNews.setAdapter(adapter);

                    listNews.setOnItemClickListener((parent, view, position, id) -> {
                        Intent i = new Intent(NewsActivity.this, DetailsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
                        startActivity(i);
                    });

                }else{
                    Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
                }
        }



    }



}
