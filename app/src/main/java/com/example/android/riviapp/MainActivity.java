package com.example.android.riviapp;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.riviapp.Recycler.Blog;
import com.example.android.riviapp.Recycler.BlogRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout,seemore;
    Animation slide_down,slide_up;
    TextView main_title;
    JSONObject card_details;
    String res="";
    List<Blog> datas;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seemore=findViewById(R.id.seemore);
        final SharedPreferences shp=getSharedPreferences("Last", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed=shp.edit();
        ed.putInt("last",-1);
        ed.apply();

        recyclerView = findViewById(R.id.BlogRecycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(manager);
        main_title=findViewById(R.id.main_title);
        linearLayout=findViewById(R.id.linear_layout);
        //Load animation
        datas=new ArrayList<>(10);
       slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

         slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

// Start animation
//        final LinearLayout linearLayout1=findViewById(R.id.details_ll);
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linearLayout1.startAnimation(slide_down);
//                linearLayout1.setVisibility(View.GONE);
//
//
//            }
//        });

        getData();

        seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arr[]=new String[4];
                String arr2[]=new String[4];
                for (int j=0;j<4;j++) {
                    String s=datas.get(j).getBlog();
                    try {
                        JSONObject jso=new JSONObject(s);
                        arr[j]=jso.getString("img").replaceAll("\\/","/");
                        arr2[j]=jso.getString("img").replaceAll("\\/","/");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                BottomSheet bottomSheet = new BottomSheet(res,arr,arr2,-1);


                bottomSheet.show(getSupportFragmentManager(),"abc");
            }
        });

// https://my-json-server.typicode.com/guljar-rivi/server/db  
    }

    @Override
    public void onBackPressed() {


    }

   public void getData()
   {
       RequestQueue queue = Volley.newRequestQueue(this);
       final String url = "https://my-json-server.typicode.com/guljar-rivi/server/db";

// prepare the Request
       JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
               new Response.Listener<JSONObject>()
               {
                   @Override
                   public void onResponse(JSONObject response) {
                       // display response
                       Log.i("Response", response.toString());
                       try {
                           res=response.toString();
                         JSONObject data=  response.getJSONObject("data");
                            card_details =data.getJSONObject("card_details");
                         String mainTitle=card_details.getString("title");
                           String City=card_details.getString("city");
                         main_title.setText(mainTitle+" "+City);
                           JSONArray card=data.getJSONArray("card");

                           int[] index=new int[4];
                           List<Blog> datas2=new ArrayList<>();
                           for(int n = 0; n < card.length(); n++)
                           {
                               JSONObject object = card.getJSONObject(n);
                               Blog blog = new Blog(object.toString());
                            index[n]=   Integer.parseInt(object.getString("card_no"))-1;
                               datas.add(blog);
        datas2.add(blog);
                           }
                          for(int k=0;k<4;k++)
                          {datas.remove(index[k]);
                          datas.add(index[k],datas2.get(k));}

                           BlogRecyclerAdapter adapter = new BlogRecyclerAdapter(getApplicationContext(),datas, MainActivity.this,response.toString());
//                           recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                           recyclerView.setAdapter(adapter);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener()
               {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.i("Error.Response", error.toString());
                   }
               }
       );

       getRequest.setRetryPolicy(new RetryPolicy() {
           @Override
           public int getCurrentTimeout() {
               return 3000;
           }

           @Override
           public int getCurrentRetryCount() {
               return 3000;
           }

           @Override
           public void retry(VolleyError error) throws VolleyError {

           }
       });
// add it to the RequestQueue
       queue.add(getRequest);
   }
}
