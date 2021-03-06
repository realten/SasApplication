package com.example.realz.sasapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.realz.sasapplication.R.id.quest_id;
import static com.example.realz.sasapplication.R.id.radio1;

public class DoAssess extends AppCompatActivity {

    private ListView ListViewJSon;
    private ProgressDialog progressDialog;
    private Button ButtonSub;
    private RadioGroup GroupRadio1;
    private int numques=0;
    private int numchoice=0;
    private SimpleAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_assess);
        ListViewJSon = (ListView)findViewById(R.id.listvieww);
        ButtonSub = (Button)findViewById(R.id.ButtonSubmit);

        final ArrayList<String> exData = new ArrayList<String>();
        exData.clear();

        final ArrayList<String> exData2 = new ArrayList<String>();
        exData2.clear();

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DoAssess.this,R.layout.list_question,R.id.listview_text,exData);
        ListViewJSon.setAdapter(myAdapter);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(DoAssess.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Downloding ..... ");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    disableSSLCertificateChecking();
                    URL url = new URL("https://10.51.4.17/TSP57/PCK/index.php/sas/Alumni/DoAssess/ListQuestion");

                    URLConnection urlConnection = url.openConnection();

                    HttpURLConnection httpURLConnection = (HttpsURLConnection)urlConnection;
                    httpURLConnection.setAllowUserInteraction(false);
                    httpURLConnection.setInstanceFollowRedirects(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = null;

                    if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        inputStream = httpURLConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);

                    StringBuilder jsonbuild = new StringBuilder();
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line=reader.readLine()) != null ){
                        stringBuilder.append(line + "\n");
                    }

                    inputStream.close();
                    Log.d("JSON Result", stringBuilder.toString());

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(stringBuilder.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray exArray = null;
                    try {
                        exArray = jsonObject.getJSONArray("question");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    numques=exArray.length();
                    for(int i = 0; i < exArray.length(); i++){
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = exArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            exData.add(jsonObj.getString("quest_detail"));
                            exData2.add(jsonObj.getString("quest_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DoAssess.this,R.layout.list_question,R.id.listview_text,exData);
                //final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DoAssess.this,R.layout.list_question,new String[] {R.id.listview_text,R.id.quest_id},new int[] {exData,exData2});
//                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//                Map<String, String> map;
//                int count = exData.size();
//                for(int i = 0; i < count; i++) {
//                    map = new HashMap<String, String>();
//                    map.put("name", exData.get(i));
//                    map.put("total", exData2.get(i));
//                    list.add(map);
//                }
//
//                adapter = new SimpleAdapter(DoAssess.this, list, R.layout.list_question, new String[] { "name", "total" }, new int[] { R.id.listview_text, R.id.quest_id });


                ListViewJSon.setAdapter(myAdapter);
//                ListViewJSon.setAdapter(myAdapter2);

                /*ListViewJSon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getApplicationContext(),String.valueOf(adapter.getItem(position)),
                        //Toast.LENGTH_SHORT).show();

                        //UserActivity แก้เป็น Class ที่จะให้ไป
                        Intent editIntent = new Intent(getApplicationContext(), UserActivity.class);
                        editIntent.putExtra("editTodolist", (Serializable) myAdapter.getItem(position));
                        startActivity(editIntent);
                    }
                });*/

            }
        }.execute();

        //namesAA = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, android.R.id.text1, exData );
        //ListViewJSon.setAdapter(namesAA);

        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,exData);
        //ListViewJSon.setAdapter(myAdapter);


    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                }
        };

        try {

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }

            });
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?

        //Context ss = ((TextView) context).findViewById(R.id.quest_id);
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case radio1:
                if (checked) {

                        numchoice++;

                }
                break;
            case R.id.radio2:
                if (checked)
                    numchoice++;
                break;
            case R.id.radio3:
                if (checked)
                    numchoice++;
                break;
            case R.id.radio4:
                if (checked)
                    numchoice++;
                break;
        }
    }
    public void checkQuiz(View v) {
        ArrayList<String> incorrectAnswersList = new ArrayList<String>();
        int numberOfQuestionsCorrect = 0;
        StringBuilder sb = new StringBuilder();
        for (String s : incorrectAnswersList)
        {
            sb.append(s);
            sb.append("\n");
        }
        Context context = getApplicationContext();
        CharSequence text = "You got " + numchoice + "/"+ numques+" answers check.\n\nRecheck the following:\n" + sb.toString();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}
