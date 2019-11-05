package com.example.android.riviapp.Recycler;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.android.riviapp.BottomSheet;
import com.example.android.riviapp.R;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;





import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.MyViewHolder> {

    Context context;
    List<Blog> datas;
    Activity activity;
    String objString="";
    String response="";

    public  BlogRecyclerAdapter(Context context, List<Blog> datas, Activity activity,String response)
    {
        this.context = context;
        this.datas = datas;
        this.activity = activity;
        this.response=response;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_recycler, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NotNull final MyViewHolder viewHolder, final int i) {



        final Animation slide_down,slide_up;
        final View view = viewHolder.view;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View sheetView = View.inflate(context,R.layout.expand_detail,null);


        bottomSheetDialog.setContentView(sheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) (sheetView.getParent()));
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HALF_EXPANDED);
        sheetView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;



        slide_down = AnimationUtils.loadAnimation(context,
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);

        final LinearLayout ll2=activity.findViewById(R.id.details_ll);
        final LinearLayout ll1=activity.findViewById(R.id.linear_layout);



         int currentPage = 0;
         // Integer[] XMEN= {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3,R.drawable.slide4};

        RoundedImageView roundedImageView=view.findViewById(R.id.blog_image);
        TextView blogTitle=view.findViewById(R.id.blog_title),
        blogDesc=view.findViewById(R.id.blog_desc);
        final Blog data = datas.get(i);
         objString=data.getBlog();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(objString);
            String urlString=jsonObject.getString("img");
            String title=jsonObject.getString("title");
            String desc=jsonObject.getString("desc");
            blogTitle.setText(title);
            blogDesc.setText(desc);
          String finalurlString=  urlString.replaceAll("\\/","/");
            Picasso.get().load(Uri.parse(finalurlString)).into(roundedImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }


















                   // Picasso.get().load(Uri.parse(object.getString("doc_img_url"))).into(Histimage);


//       ll2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll2.startAnimation(slide_down);
//               ll2.setVisibility(View.GONE);
//
//            }
//        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ll2.setVisibility(View.VISIBLE);
//                 ll2.startAnimation(slide_up);


//bottomSheetDialog.show();
//                android.enableJetifier=true
//                android.useAndroidX=true
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
                String temp=arr[0];
                arr[0]=arr[i];
                arr[i]=temp;

                BottomSheet bottomSheet = new BottomSheet(response,arr,arr2,i);
             //   BottomSheet bs=new BottomSheet(objString,response,arr,bottomSheet);
            AppCompatActivity act=(AppCompatActivity) activity;
            bottomSheet.show( act.getSupportFragmentManager(),"abc");

//                Intent intent=new Intent(context, HistoryDetails.class);
//                intent.putExtra("HistoryObject",datas.get(i).getObject());
//                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

}
