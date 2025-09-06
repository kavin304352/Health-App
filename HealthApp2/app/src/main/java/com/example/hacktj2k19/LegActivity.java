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
import android.widget.LinearLayout;
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

public class LegActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private TextView help;
    private LinearLayout knee_questions;
    private LinearLayout leg_questions;
    private LinearLayout foot_ankle_questions;
    private Button mDoneButton1;
    private Button mDoneButton2;
    private Button mDoneButton3;
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
        setContentView(R.layout.activity_leg);
        mImageButton = (ImageButton)findViewById(R.id.image_button);
        help = (TextView)findViewById(R.id.title_text_view);
        knee_questions = (LinearLayout)findViewById(R.id.knee_questions);
        leg_questions = (LinearLayout)findViewById(R.id.leg_questions);
        foot_ankle_questions = (LinearLayout)findViewById(R.id.foot_ankle_questions);
        mDoneButton1 = (Button)findViewById(R.id.done_button1);
        mDoneButton2 = (Button)findViewById(R.id.done_button2);
        mDoneButton3 = (Button)findViewById(R.id.done_button3);
        symptoms = new ArrayList<String>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        mImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    final float evX = event.getX();
                    final float evY = (int) event.getY();
                    double newWidth = evX / width;
                    double newHeight = 1 - evY / height;
                    String bodyPart = "none";

                    if(((newWidth - 0.46) * (newWidth - 0.46) + (newHeight - 0.60) * (newHeight - 0.60)) <= 0.0082) {
                        bodyPart = "Knee";
                    } else if(newWidth >= 0.415 && newWidth <= 0.6 && newHeight <= 0.32) {
                        bodyPart = "Foot/Ankle";
                    } else if((-4.394 * newWidth + 2.098) < newHeight && newWidth <= 0.575) {
                        bodyPart = "Leg";
                    }

                    Toast.makeText(LegActivity.this,
                            newWidth + " " + newHeight,
                            Toast.LENGTH_LONG).show();

                    if(bodyPart.equals("none")) {
                        Toast.makeText(LegActivity.this,
                                "Please by more clear",
                                Toast.LENGTH_LONG).show();
                    }

                    if(!bodyPart.equals("none")) {
                        mImageButton.setVisibility(View.GONE);
                        symptoms.add(bodyPart);
                        if(bodyPart.equals("Knee")) {
                            injuryList = new String[]{"ligament", "dislocate", "meniscus", "tendon", "plica", "iliotibial"};
                            knee_questions.setVisibility(View.VISIBLE);
                            boxes = new CheckBox[]{(CheckBox)findViewById(R.id.kneecap), (CheckBox)findViewById(R.id.sidesOfKnee), (CheckBox)findViewById(R.id.backOfKnee), (CheckBox)findViewById(R.id.cannotPinpoint), (CheckBox)findViewById(R.id.poppingK), (CheckBox)findViewById(R.id.unstable), (CheckBox)findViewById(R.id.swollen), (CheckBox)findViewById(R.id.decreasedMotionK), (CheckBox)findViewById(R.id.cannotSupportWeight), (CheckBox)findViewById(R.id.inflammation), (CheckBox)findViewById(R.id.tendernessK)};
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
                        } else if(bodyPart.equals("Leg")) {
                            injuryList = new String[]{"compartment", "iliac", "hamstrings", "muscle cramps", "calf", "quadriceps", "hin splints", "fracture"};
                            leg_questions.setVisibility(View.VISIBLE);
                            boxes = new CheckBox[]{(CheckBox)findViewById(R.id.bone), (CheckBox)findViewById(R.id.skin), (CheckBox)findViewById(R.id.swellingL), (CheckBox)findViewById(R.id.weakness), (CheckBox)findViewById(R.id.tendernessL), (CheckBox)findViewById(R.id.decreasedMotionL), (CheckBox)findViewById(R.id.bleeding), (CheckBox)findViewById(R.id.limping)};
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
                        } else if(bodyPart.equals("Foot/Ankle")) {
                            injuryList = new String[]{"achilles tendonitis", "ankle sprains", "arch pain", "athlete’s foot", "plantar fasciitis", "fracture", "turf toe"};
                            foot_ankle_questions.setVisibility(View.VISIBLE);
                            boxes = new CheckBox[]{(CheckBox)findViewById(R.id.backOfHeel), (CheckBox)findViewById(R.id.ankle), (CheckBox)findViewById(R.id.bottomOfFoot), (CheckBox)findViewById(R.id.ballOfFoot), (CheckBox)findViewById(R.id.bigToe), (CheckBox)findViewById(R.id.swellingFA), (CheckBox)findViewById(R.id.rednessFA), (CheckBox)findViewById(R.id.tendernessFA), (CheckBox)findViewById(R.id.poppingFA), (CheckBox)findViewById(R.id.bruisingFA), (CheckBox)findViewById(R.id.decreasedMotionFA)};
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

        mDoneButton3.setOnClickListener(new View.OnClickListener() {
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

                Intent i = new Intent(LegActivity.this, PieChart.class);
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
