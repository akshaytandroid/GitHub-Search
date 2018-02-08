package com.akshay.gitrepoproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String BASE_URL = "https://api.github.com/search/repositories?q=";
    private String SORT_BY_STARS_COMMAND = "&sort=stars&order=desc";

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private EditText repoSearchEditText;
    private Button repoSearchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        repoSearchEditText = (EditText) findViewById(R.id.search_repo_edit_text);
        repoSearchButton = (Button) findViewById(R.id.search_repo_button);

        repoSearchButton.setOnClickListener(listener);

    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                if (repoSearchEditText.getText().toString().trim().isEmpty()) {
                    repoSearchEditText.setError(getString(R.string.string_search_entry));
                } else {
                    String url = constructUrl(repoSearchEditText.getText().toString().trim());
                    new GitRepositoriesAsyncTask().execute(url);
                }
            } else {
                showAlertDialog(getString(R.string.string_no_internet));
            }
        }
    };

    public String constructUrl(String searchQuery) {
        return BASE_URL + searchQuery + SORT_BY_STARS_COMMAND;
    }


    /**
     * ASYNC TASK to Download Response from GIT API.
     */
    public class GitRepositoriesAsyncTask extends AsyncTask<String, Void, String> {

        private String jsonResponse;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    jsonResponse = readStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, jsonResponse);
            Gson gson = new Gson(); //GSON for Serializing and GitResponse-serializing

            final GitHubResponseBean gitReposResponse = gson.fromJson(jsonResponse, GitHubResponseBean.class);

            if (gitReposResponse.getItems().isEmpty() || gitReposResponse.getItems().size() == 0) {
                showAlertDialog(getResources().getString(R.string.string_no_repos_found));
            }
            recyclerAdapter = new RecyclerAdapter(MainActivity.this, gitReposResponse);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(recyclerAdapter);

            progressDialog.dismiss();

            recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(GitHubResponseBean responseBean, int position) {
                    showAlertDialog("Title: " + responseBean.getItems().get(position).getName() + "\n" + "Description: " + responseBean.getItems().get(position).getDescription());
                }

            });


        }
    }


    /**
     * @param message AlertDialog message
     */
    private void showAlertDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.string_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    /**
     * @param inputStream InputStream
     * @return responseString
     */
    private String readStream(InputStream inputStream) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


}
