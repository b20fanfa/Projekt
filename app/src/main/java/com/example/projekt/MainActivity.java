package com.example.projekt;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Skolor> skolorArrayList;
    private ArrayAdapter <Skolor> adapter;
    private Skolor[] listor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView my_listview =(ListView) findViewById(R.id.my_listview);

        skolorArrayList = new ArrayList<>();
        adapter=new ArrayAdapter<>(MainActivity.this,R.layout.list_item_textview,R.id.list_item_textview_xml,skolorArrayList);
        my_listview.setAdapter(adapter);

        my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name  = skolorArrayList.get(position).getName("name");
                String category = skolorArrayList.get(position).getCategory("category");
                String location = skolorArrayList.get(position).getLocation("location");
                int size = skolorArrayList.get(position).getSize("size");

                String utskrift = "Namn p?? skolan: " + name + "\n\n" + "Detta ??r en: " + category + "\n\n" + "Skolan ??r placerad vid " + location + "\n\n" + "Det g??r ca " + size + " studenter";
                Toast.makeText(MainActivity.this, utskrift ,  Toast.LENGTH_SHORT).show();

            }
        });

        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=b20fanfa");

   //Det som st??r om man trycker p?? kanppen
       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Appen ??r skapad av Fanny", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Toast.makeText(getApplicationContext(), "Denna app ??r till f??r alla m??nniskor som ??r redo att b??rja studera p?? universitet. \nAppen inneh??ller snabbfakta om olika skolor runt om i Sverige.  Den snabbfakta som finns ??r namn, plats, typ av skola och hur m??nga studenter som g??r d??r. \n" +
                            "Detta ??r enbart skolor i Sverige.  ",
                Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_facts) {
            Toast.makeText(getApplicationContext(), " \n" +
                            "Vad ??r skillnaden mellan h??gskola och universitet?\n\n" +
                            "Skillnaden ??r universitet har generell r??tt att utf??rda examen p?? forskarniv??. \n" +
                            "H??gskolor m??ste specifikt ans??ka om??s??dan??r??ttighet, eller samarbeta med ett annat l??ros??te som har r??ttigheter.\n" +
                            " I praktiken ??r det dock ingen skillnad att studera p?? en svensk h??gskola eller universitet f??r de allra flesta studenter. ",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            Log.d("TAG", json);

            try {
                Gson gson=new Gson();
                listor=gson.fromJson(json,Skolor[].class);
                skolorArrayList.clear();
                for(int i=0; i <listor.length; i++){
                    skolorArrayList.add(listor[i]);
                }
                adapter.notifyDataSetChanged();

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            //adapter.notifyDataSetChanged();
        }
    }

}
