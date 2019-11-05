package com.example.android.riviapp;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.riviapp.Recycler.Blog;
import com.example.android.riviapp.Recycler.BlogRecyclerAdapter;
import com.example.android.riviapp.Recycler.DetailBlogRecyclerAdapter;
import com.example.android.riviapp.Recycler.MyAdapter;
import com.example.android.riviapp.Recycler.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class BottomSheet extends BottomSheetDialogFragment {


    private BottomSheetBehavior mBehavior;
    private String jsonobjectString;
    private String response;
    private static int currentPage = 0;
    ViewPager pager;
    Timer swipeTimer;
    Handler handler;
    List<Blog> datas;
    RecyclerView recyclerView;
    Runnable Update;
    CircleIndicator indicator;
    private ArrayList<String> XMENArray = new ArrayList<String>();
    private static String[] XMEN=new String[4];
    private static String[] XMEN2=new String[4];
    BottomSheet bs;
    int par;
    JSONObject jsonObject;
    public BottomSheet()
    {

    }
    public BottomSheet(String response,String[] arr,String[] arr2,int parent)
    {

        this.response=response;
        for(int l=0;l<4;l++) {
            XMEN[l] = arr[l];
        XMEN2[l]=arr2[l];}
        this.par=parent;
    }
    public BottomSheet(String jsonobjectString,String response,String[] arr,BottomSheet bs)
    {
        this.jsonobjectString=jsonobjectString;
        this.response=response;
        for(int l=0;l<4;l++)
        XMEN[l]=arr[l];
        this.bs=bs;
        par=-1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        datas=new ArrayList<Blog>();

        View view = View.inflate(getContext(), R.layout.expand_detail, null);
//        try {
//           jsonObject = new JSONObject(jsonobjectString);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
       pager=view.findViewById(R.id.expand_pager);
      indicator=view.findViewById(R.id.expand_indicator);
         recyclerView=view.findViewById(R.id.expand_recycler_view);

            View parent=(View) view.getParent();
        LinearLayout linearLayout = view.findViewById(R.id.root);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) linearLayout.getLayoutParams();
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
//        CoordinatorLayout.Behavior behavior = params.getBehavior();
//        mBehavior = (BottomSheetBehavior) behavior;
//        mBehavior.setPeekHeight(view.getMeasuredHeight());
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);




dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(manager);
        try {
            JSONObject resp=new JSONObject(response);

        JSONObject data=  resp.getJSONObject("data");
        JSONObject card_details=data.getJSONObject("card_details");

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
        if(par!=-1)
        { DetailBlogRecyclerAdapter adapter = new DetailBlogRecyclerAdapter(getContext(),datas, getActivity(),par);
            recyclerView.setAdapter(adapter);}

        else
        {DetailBlogRecyclerAdapter adapter = new DetailBlogRecyclerAdapter(getContext(),datas, getActivity());
            recyclerView.setAdapter(adapter);
           }

//                           recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SharedPreferences shp=getContext().getSharedPreferences("Last",Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed=shp.edit();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                    Log.i("Touch made","True"+" "+position);
                    ImageView iv=view.findViewById(R.id.blog_image);

                        SharedPreferences sp1=getContext().getSharedPreferences("Last",Context.MODE_PRIVATE);
                        Log.i("sp pos","T"+" "+sp1.getInt("last",-1));


                            if (position != sp1.getInt("last", -1)) {
                               if(position!=0) {
                                   String temp;
                                   temp = XMEN[0];
                                   if(sp1.getInt("last",-1)==-1) {
                                       Log.i("one", XMEN2[position] + "");
                                       ed.putInt("last", position);
                                       ed.commit();
                                       ed.apply();
                                       XMEN[0] = XMEN2[position];
                                       XMEN[sp1.getInt("last", -1)] = temp;
                                   }
                                   else
                                   {  Log.i("one", XMEN2[position] + "");

                                       XMEN[0] = XMEN2[position];
                                       XMEN[position] = temp;
                                       ed.putInt("last", position);
                                       ed.commit();
                                       ed.apply();}

                                   getImages();
                               }
                               else
                               {  String temp;
                                   temp = XMEN[0];
                                   if(sp1.getInt("last",-1)==-1) {
                                       ed.putInt("last", position);
                                       Log.i("two", XMEN2[0] + "");
                                       ed.commit();
                                       ed.apply();
                                       XMEN[0] = XMEN2[0];
                                       XMEN[sp1.getInt("last", -1)] = temp;

                                   }
                                   else
                                   {
                                       XMEN[0] = XMEN2[0];
                                       XMEN[sp1.getInt("last", -1)] = temp;
                                       ed.putInt("last", position);
                                       Log.i("two", XMEN2[0] + "");
                                       ed.commit();
                                       ed.apply();
                                   }
                                   getImages();}
                            } else {
//                                XMEN[position];
//                                XMEN[0] = XMEN2[position];
//
//                                ed.putInt("last", position);
//                                ed.commit();
//                                ed.apply();
//                                getImages();
                            }

SharedPreferences sharedPreferences=getContext().getSharedPreferences("Expanded",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        SharedPreferences sp=getContext().getSharedPreferences("Expanded",Context.MODE_PRIVATE);
                        if(sharedPreferences.getInt("expanded",0)==0) {
                            expand(view);
                            editor.putInt("expanded", 1);
                            editor.putInt("position",position);
                            editor.commit();
                            editor.apply();
                        }
                        else {
                            int pos=sp.getInt("position",-1);
                            if(pos==position) {
                                collapse(view);
                                editor.putInt("expanded", 0);
                                editor.commit();
                                editor.apply();
                            }
                            else
                            {
                                collapse(pos,getContext());
                                expand(view);
                                editor.putInt("expanded", 1);
                                editor.putInt("position",position);
                                editor.commit();
                                editor.apply();

                            }
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        getImages();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void init(String[] arr) {
        XMENArray.clear();
        for(int i=0;i<arr.length;i++)
            XMENArray.add(arr[i]);


        pager.setAdapter(new MyAdapter(getContext(),XMENArray));

        indicator.setViewPager(pager);

        // Auto start of viewpager
        handler = new Handler();


    }

    public void getImages()
    {


            init(XMEN);

    }

   public void collapse(int k, Context con)
   {
//       SharedPreferences sp=con.getSharedPreferences("Expanded",getContext().MODE_PRIVATE);
//       SharedPreferences.Editor editor;
//       editor=sp.edit();
       RecyclerView.ViewHolder vh=recyclerView.findViewHolderForAdapterPosition(k);
    final View v=vh.itemView;
       final int initialHeight = v.getMeasuredHeight();


       ImageView down=v.findViewById(R.id.down);
       TextView tv=v.findViewById(R.id.details_recycler_aboutus_constant);
       Log.i("Collapse", tv.getText().toString()+"bb");
       down.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_down));

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
//       editor.putInt("expanded",0);
//       editor.apply();
   }

    public void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        ImageView down=v.findViewById(R.id.down);
        down.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_up));

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
        down.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_down));

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