package in.edureal.opennews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private SweetAlertDialog loader;
    private String url;

    RecyclerView newsRecycler;
    RecyclerView.Adapter adapter;

    private List<ListItem> listItems;

    private ClipboardManager clipboard;

    private void showError(String str){
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(str)
                .show();
    }

    private void refreshData(){

        loader.show();
        if(SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("sources","").length()>0){
            url="https://newsapi.org/v2/everything?pageSize=100&domains="+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("sources","ndtv.com")+"&apiKey="+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("apiKey","3e3ede875fb54bb5839065a9972f861f")+"&sortBy=publishedAt";
        }else{
            url="https://newsapi.org/v2/top-headlines?pageSize=100&country="+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("country","in")+"&category="+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("category","")+"&apiKey="+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("apiKey","3e3ede875fb54bb5839065a9972f861f")+"&sortBy=publishedAt";
        }
        StringRequest stringRequest = new StringRequest(0, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{

                            JSONObject obj=new JSONObject(response);
                            String status=obj.getString("status");

                            if(status.equals("ok")){

                                int totalResults=obj.getInt("totalResults");

                                if(totalResults>0){

                                    JSONArray arr=obj.getJSONArray("articles");
                                    listItems.clear();
                                    for(int i=0;i<arr.length();i++){

                                        JSONObject temp=arr.getJSONObject(i);
                                        //Log.i("Name:",temp.getJSONObject("source").getString("name"));
                                        //Log.i("Title:",temp.getString("title"));
                                        //Log.i("URL:",temp.getString("url"));
                                        //Log.i("Image:",temp.getString("urlToImage"));
                                        //Log.i("Published At:",temp.getString("publishedAt"));

                                        ListItem listItem=new ListItem(temp.getString("title"),temp.getJSONObject("source").getString("name"),temp.getString("url"),temp.getString("urlToImage"));
                                        listItems.add(listItem);

                                    }
                                    adapter.notifyDataSetChanged();
                                    newsRecycler.setVisibility(View.VISIBLE);
                                    loader.hide();

                                }else{
                                    loader.hide();
                                    new SweetAlertDialog(MainActivity.this)
                                            .setTitleText("So Sorry...")
                                            .setContentText("No article found in this category and country. Swipe down to try again.")
                                            .show();
                                }

                            }else{
                                loader.hide();
                                showError("There was a problem. Swipe down to try again.");
                            }

                        }catch(JSONException e){
                            loader.hide();
                            showError("There was a problem. Swipe down to try again.");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    loader.hide();
                    showError("There is no internet connection. Swipe down to try again.");
                }else if(error instanceof TimeoutError){
                    loader.hide();
                    showError("Your request has timed out. Swipe down to try again.");
                }else if(error instanceof AuthFailureError) {
                    loader.hide();
                    showError("There was an Authentication Failure while performing the request. Swipe down to try again.");
                }else if(error instanceof ServerError) {
                    loader.hide();
                    showError("Server responded with a error response. Swipe down to try again.");
                }else if(error instanceof NetworkError) {
                    loader.hide();
                    showError("There was network error while performing the request. Swipe down to try again.");
                }else if(error instanceof ParseError) {
                    loader.hide();
                    showError("Server response could not be parsed. Swipe down to try again.");
                }else{
                    loader.hide();
                    showError("There was a problem. Swipe down to try again.");
                }
            }
        });

        VollySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void githubSources(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText("Version " + BuildConfig.VERSION_NAME);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_openGitHub)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vishu103/"));
                startActivity(browserIntent);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_settings);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        loader = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        loader.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loader.setTitleText("Please wait...");
        loader.setCancelable(false);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        listItems=new ArrayList<>();
        newsRecycler=(RecyclerView) findViewById(R.id.newsRecycler);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(newsRecycler);
        newsRecycler.setVisibility(View.GONE);
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MyAdapter(listItems, this);
        newsRecycler.setAdapter(adapter);
        url="";

        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                    refreshData();
                }
            }
        });

        refreshData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Tools.changeMenuIconColor(menu, Color.BLACK);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else {
            githubSources();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Exit")
                .setContentText("Are you sure you want to close Open News?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loader.dismiss();
    }

    ItemTouchHelper.SimpleCallback itemSimpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            if(direction == ItemTouchHelper.RIGHT){
                ClipData clip = ClipData.newPlainText("Article Link", listItems.get(viewHolder.getAdapterPosition()).getUrl());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied article link to the clipboard.", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
