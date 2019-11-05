package com.example.android.riviapp.Recycler;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.riviapp.BottomSheet;
import com.example.android.riviapp.R;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class DetailBlogRecyclerAdapter extends RecyclerView.Adapter<DetailBlogRecyclerAdapter.MyViewHolder> {

    Context context;
    List<Blog> datas;
    Activity activity;
    ArrayList<String> photos;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentManager fm;
    BottomSheet bs;
    int par;

    public DetailBlogRecyclerAdapter(Context context, List<Blog> datas, Activity activity ) {
        this.context = context;
        this.datas = datas;
        this.activity = activity;
        par=-1;


    }

    public DetailBlogRecyclerAdapter(Context context, List<Blog> datas, Activity activity,int parent ) {
        this.context = context;
        this.datas = datas;
        this.activity = activity;
        par=parent;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.details_blog_recycler, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull final MyViewHolder viewHolder, final int i) {


        sharedPreferences=context.getSharedPreferences("Expanded",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        photos = new ArrayList<>();
        final View view = viewHolder.view;
        TextView aboutconst = view.findViewById(R.id.details_recycler_aboutus_constant);
        TextView about = view.findViewById(R.id.details_recycler_aboutus_text);
        TextView bestdishconst = view.findViewById(R.id.details_recycler_bestdishes_constant);
        TextView wheretoeatconst = view.findViewById(R.id.details_recycler_wheretoeat_constant);
        TextView wheretoeat = view.findViewById(R.id.details_recycler_wheretoeat_text);
        TextView bestdisheslist = view.findViewById(R.id.details_recycler_bestdishes_text);
        RoundedImageView roundedImageView = view.findViewById(R.id.blog_image);
        RecyclerView photosrecyclerView = view.findViewById(R.id.details_recycler_photos_recycler);

        TextView blogTitle = view.findViewById(R.id.blog_title),
                blogDesc = view.findViewById(R.id.blog_desc);
        Blog data = datas.get(i);
        String objString = data.getBlog();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(objString);
            String urlString = jsonObject.getString("img");
            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");
            JSONObject details = jsonObject.getJSONObject("details");
            JSONArray aboutus = details.getJSONArray("about");
            blogTitle.setText(title);
            blogDesc.setText(desc);
            String finalurlString = urlString.replaceAll("\\/", "/");
            Picasso.get().load(Uri.parse(finalurlString)).into(roundedImageView);

            //about section
            if (aboutus.toString().length() > 2)
                about.setText(aboutus.toString().substring(2, aboutus.toString().length() - 2));
            else {
                aboutconst.setVisibility(View.GONE);
                about.setVisibility(View.GONE);
            }

            //wheretoeat
            JSONArray wherelistarray = details.getJSONArray("where");
            String finalstr = "";
            if (wherelistarray.length() > 0) {

                for (int n = 0; n < wherelistarray.length(); n++) {
                    JSONObject location = wherelistarray.getJSONObject(n);
                    String name = location.getString("name");
                    String distance = location.getString("distance");
                    if (wherelistarray.length() == 1) {
                        finalstr = name + "\n";
                    } else {
                        if (!distance.equals("null")) {

                            finalstr = finalstr + (n + 1) + ". " + name + " (" + distance + "Km from City Centre)\n";
                        } else
                            finalstr = finalstr + (n + 1) + ". " + name + "\n";
                    }

                }
                wheretoeat.setText(finalstr);
            } else if (wherelistarray.length() == 0) {
                wheretoeat.setVisibility(View.GONE);
                wheretoeatconst.setVisibility(View.GONE);
            }

            //dishes
            JSONArray dishesarray = details.getJSONArray("dishes");
            bestdishconst.setText("BEST " + title.toUpperCase() + " DISHES");
            String finaldishstr = "";
            if (dishesarray.length() > 0) {
                for (int n = 0; n < dishesarray.length(); n++) {
                    String dish = dishesarray.getString(n);
                    if (dishesarray.length() == 1)
                        finaldishstr = dish + "\n";
                    else
                        finaldishstr = finaldishstr + (n + 1) + ". " + dish + "\n";
                }
                bestdisheslist.setText(finaldishstr);
            } else {
                bestdishconst.setVisibility(View.GONE);
                bestdisheslist.setVisibility(View.GONE);
            }

            //photos
            photosrecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            JSONArray photosarray = details.getJSONArray("images");
            if (photosarray.length() > 0) {
                for (int n = 0; n < photosarray.length(); n++) {
                    String img = photosarray.getString(n).replaceAll("\\/", "/");
                    photos.add(img);
                }
                PhotosAdapter adapter = new PhotosAdapter(context, photos);

                photosrecyclerView.setAdapter(adapter);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Picasso.get().load(Uri.parse(object.getString("doc_img_url"))).into(Histimage);

        editor.putInt("expanded",0);
        editor.apply();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     bottomSheetDialog.show();
//
//SharedPreferences sp=context.getSharedPreferences("Expanded",Context.MODE_PRIVATE);
//               if(sharedPreferences.getInt("expanded",0)==0) {
//                   expand(view);
//                   editor.putInt("expanded", 1);
//                   editor.putInt("position",i);
//                   editor.commit();
//                   editor.apply();
//               }
//               else {
//                   int pos=sp.getInt("position",-1);
//                   if(pos==i) {
//                       collapse(view);
//                       editor.putInt("expanded", 0);
//                       editor.commit();
//                       editor.apply();
//                   }
//                   else
//                   {
//
//                       expand(view);
//                       editor.putInt("expanded", 1);
//                       editor.putInt("position",i);
//                       editor.commit();
//                       editor.apply();
//
//                   }
//               }

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

    public void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        ImageView down=v.findViewById(R.id.down);
        down.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_up));

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public  void collapse(final View v) {
       final int initialHeight = v.getMeasuredHeight();


        ImageView down=v.findViewById(R.id.down);
        down.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_down));

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                   // v.setVisibility(View.GONE);
                 //   v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                   v.getLayoutParams().height=300;
                    v.requestLayout();
                } else {
                  //  v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.getLayoutParams().height=300;
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        //(int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)
        a.setDuration(1000);
        v.startAnimation(a);
    }
}

