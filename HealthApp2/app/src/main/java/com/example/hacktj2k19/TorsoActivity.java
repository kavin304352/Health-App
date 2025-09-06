package com.example.hacktj2k19;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.security.ProviderInstaller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class TorsoActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private TextView help;
    private Button mDoneButton;
    private int height;
    private int width;
    private ArrayList<String> symptoms;
    private CheckBox[] boxes;
    private String[] injuryList;

    public static int count(String a, String b) {
        int count = 0;
        for(int k = 0; k < b.length()-a.length(); k++){
            if(b.substring(k,k+a.length()).equals(a)){
                count++;
            }
        }
        return count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torso);
        mImageButton = (ImageButton)findViewById(R.id.image_button);
        help = (TextView)findViewById(R.id.title_text_view);
        mDoneButton = (Button)findViewById(R.id.done_button);
        symptoms = new ArrayList<String>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        symptoms.add("Hip/Groin");
        injuryList = new String[]{"groin pain", "groin pull", "hip pointer", "piriformis"};
        boxes = new CheckBox[]{(CheckBox)findViewById(R.id.hip), (CheckBox)findViewById(R.id.groin), (CheckBox)findViewById(R.id.pelvis), (CheckBox)findViewById(R.id.tenderness), (CheckBox)findViewById(R.id.swelling), (CheckBox)findViewById(R.id.pain)};
        for(CheckBox c: boxes) {
            final CheckBox helper = c;
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if(checked) {
                        symptoms.add(helper.getText().toString());
                    } else {
                        if(symptoms.contains(helper.getText().toString())) {
                            symptoms.remove(helper.getText());
                        }
                    }
                }
            });
        }

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (CheckBox c : boxes) {
                        c.setChecked(false);
                    }
                    new FetchData().execute();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }
    private class FetchData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                ProviderInstaller.installIfNeeded(getApplicationContext());
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, null, null);
                SSLEngine engine = sslContext.createSSLEngine();
                String key = "AIzaSyAh_GPe9q3m4CopiAwF-00tCgx5Pr6sYgY";
                String qry = "";
                for (String s : symptoms) {
                    s = s.replaceAll("\\s","+");
                    qry = qry + s + "+";
                }
                qry = qry.substring(0, qry.length() - 1);
                URL url = new URL(
                        "https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=013036536707430787589:_pqjad5hr1a&q=" + qry + "&alt=json");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");

                ArrayList<String> sites = new ArrayList<String>();

                while ((output = br.readLine()) != null) {
                    if (output.contains("\"link\": \"")) {
                        String link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(), output.indexOf("\","));
                        System.out.println(link);
                        sites.add(link);
                    }
                }
                conn.disconnect();

                HashMap<String, Integer> injuries = new HashMap<String, Integer>();
                for (String site : sites) {
                    Document document = Jsoup.connect(site).get();
                    String temp = document.toString().toLowerCase();
                    for (String inj : injuryList) {
                        if (!injuries.containsKey(inj))
                            injuries.put(inj, 0);
                        int ct = count(inj, temp);
                        injuries.put(inj, injuries.get(inj) + ct);
                    }
                }
                System.out.println(injuries);
                Entry[] probs = new Entry[injuryList.length];
                int sum = 0;
                for (String i : injuryList) sum += injuries.get(i);
                for (int i = 0; i < probs.length; i++) {
                    probs[i] = new Entry(injuryList[i], (double) injuries.get(injuryList[i]) / (double) sum);
                }
                Arrays.sort(probs, Collections.reverseOrder());
                for (Entry e : probs) System.out.println(e);

                double[] values = new double[probs.length];
                String[] names = new String[probs.length];
                for (int i = 0; i  < probs.length; i++) {
                    values[i] = probs[i].getProb();
                    names[i] = probs[i].getInjury();
                }

                Intent i = new Intent(TorsoActivity.this, PieChart.class);
                i.putExtra("VALUES", values);
                i.putExtra("NAMES", names);
                startActivity(i);
            } catch(Exception e) {
                System.out.println(e);
            }
            return null;
        }
    }
}
