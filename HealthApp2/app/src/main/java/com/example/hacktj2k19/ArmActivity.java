package com.example.hacktj2k19;

import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.net.*;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.android.gms.security.ProviderInstaller;

import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class ArmActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private TextView help;
    private LinearLayout hand_wrist_questions;
    private LinearLayout elbow_questions;
    private Button mDoneButton1;
    private Button mDoneButton2;
    private int height;
    private int width;
    private static ArrayList<String> symptoms;
    private static CheckBox[] boxes;
    private static String[] injuryList;

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
        setContentView(R.layout.activity_arm);
        mImageButton = (ImageButton) findViewById(R.id.image_button);
        help = (TextView) findViewById(R.id.title_text_view);
        hand_wrist_questions = (LinearLayout) findViewById(R.id.hand_wrist_questions);
        elbow_questions = (LinearLayout) findViewById(R.id.elbow_questions);
        mDoneButton1 = (Button) findViewById(R.id.done_button1);
        mDoneButton2 = (Button) findViewById(R.id.done_button2);
        symptoms = new ArrayList<String>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        mImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final float evX = event.getX();
                    final float evY = (int) event.getY();
                    double newWidth = evX / width;
                    double newHeight = 1 - evY / height;
                    Toast.makeText(ArmActivity.this,
                            newWidth + " " + newHeight,
                            Toast.LENGTH_SHORT).show();

                    String bodyPart = "none";
                    if (((newWidth - 0.38) * (newWidth - 0.38) + (newHeight - 0.63) * (newHeight - 0.63)) <= 0.004){
                        bodyPart = "Elbow";
                    }
                    else if (((newWidth - 0.59) * (newWidth - 0.59) + (newHeight - 0.34) * (newHeight - 0.34)) <= 0.0136) {
                        bodyPart = "Hand+Wrist";
                    }

                    if (bodyPart.equals("none")) {
                        Toast.makeText(ArmActivity.this,
                                "Please be more clear",
                                Toast.LENGTH_LONG).show();
                    }

                    if (!bodyPart.equals("none")) {
                        mImageButton.setVisibility(View.GONE);
                        symptoms.add(bodyPart);
                        if (bodyPart.equals("Hand+Wrist")) {
                            injuryList = new String[]{"carpal tunnel", "wrist fracture", "finger fracture", "cysts", "hand numbness", "jammed finger", "dislocation", "skier’s thumb", "tendonitis", "tenosynovitis", "trigger finger", "wrist sprains"};
                            hand_wrist_questions.setVisibility(View.VISIBLE);
                            boxes = new CheckBox[]{(CheckBox) findViewById(R.id.wrist), (CheckBox) findViewById(R.id.knuckles), (CheckBox) findViewById(R.id.fingers), (CheckBox) findViewById(R.id.stiffness), (CheckBox) findViewById(R.id.swellingHW), (CheckBox) findViewById(R.id.bruising), (CheckBox) findViewById(R.id.decreasedMotion), (CheckBox) findViewById(R.id.tendernessHW), (CheckBox) findViewById(R.id.deformity), (CheckBox) findViewById(R.id.rednessHW), (CheckBox) findViewById(R.id.weakness)};
                            for (CheckBox c : boxes) {
                                final CheckBox helper = c;
                                c.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean checked = ((CheckBox) v).isChecked();
                                        if (checked) {
                                            symptoms.add(helper.getText().toString());
                                        } else {
                                            if (symptoms.contains(helper.getText().toString())) {
                                                symptoms.remove(helper.getText());
                                            }
                                        }
                                    }
                                });
                            }
                        } else if (bodyPart.equals("Elbow")) {
                            injuryList = new String[]{"bursitis", "epicondylitis", "tennis elbow"};
                            elbow_questions.setVisibility(View.VISIBLE);
                            boxes = new CheckBox[]{(CheckBox) findViewById(R.id.inside), (CheckBox) findViewById(R.id.outside), (CheckBox) findViewById(R.id.swellingE), (CheckBox) findViewById(R.id.rednessE), (CheckBox) findViewById(R.id.tendernessE)};
                            for (CheckBox c : boxes) {
                                final CheckBox helper = c;
                                c.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean checked = ((CheckBox) v).isChecked();
                                        if (checked) {
                                            symptoms.add(helper.getText().toString());
                                        } else {
                                            if (symptoms.contains(helper.getText().toString())) {
                                                symptoms.remove(helper.getText());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }

                    return true;
                }
                return false;
            }
        });

        mDoneButton1.setOnClickListener(new View.OnClickListener() {
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
        mDoneButton2.setOnClickListener(new View.OnClickListener() {
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

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                conn.setHostnameVerifier(new AllowAllHostnameVerifier());


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

                Intent i = new Intent(ArmActivity.this, PieChart.class);
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
