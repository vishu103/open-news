package in.edureal.opennews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

public class SettingsActivity extends AppCompatActivity {

    private TextView newsCountry;
    private TextView newsCategory;
    private TextView newsSource;
    private String[] countries;
    private String[] countries_short;
    private String[] categories;
    private String[] sources;

    public void setCountry(View view){

        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.success)
                .setTitle("Select the country:")
                .setIcon(R.drawable.ic_country)
                .setItems(countries, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("country",countries_short[position]);
                        SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                        newsCountry.setText("Current: "+countries_short[position]+"\nClick to change the country of origin for news.");
                        Toast.makeText(SettingsActivity.this, "Country changed", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }

    public void setCategory(View view){
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.success)
                .setTitle("Select the category:")
                .setIcon(R.drawable.ic_category)
                .setItems(categories, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        if(position==0){
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("category","");
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                            newsCategory.setText("Current: Not Set\nClick to change the category of news.");
                            Toast.makeText(SettingsActivity.this, "Category changed", Toast.LENGTH_SHORT).show();
                        }else{
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("category",categories[position].toLowerCase());
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                            newsCategory.setText("Current: "+categories[position]+"\nClick to change the category of news.");
                            Toast.makeText(SettingsActivity.this, "Category changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    public void setSources(View view){
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.success)
                .setTitle("Select the Source:")
                .setIcon(R.drawable.ic_source)
                .setItems(sources, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        if(position==0){
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("sources","");
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                            newsSource.setText("Current: Not Set\nClick to set your own source of news. Note: If you set a source, country and category parameter are ignored.");
                            Toast.makeText(SettingsActivity.this, "Source changed", Toast.LENGTH_SHORT).show();
                        }else{
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("sources",sources[position]);
                            SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                            newsSource.setText("Current: "+sources[position]+"\nClick to set your own source of news. Note: If you set a source, country and category parameter are ignored.");
                            Toast.makeText(SettingsActivity.this, "Source changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newsCountry=(TextView) findViewById(R.id.newsCountry);
        newsCategory=(TextView) findViewById(R.id.newsCategory);
        newsSource=(TextView) findViewById(R.id.newsSource);

        countries=new String[]{"Argentina","Australia","Austria","Belgium","Brazil","Bulgaria","Canada","China","Colombia","Cuba","Czech Republic","Egypt","France","Germany","Greece","Hong Kong","Hungary","India","Indonesia","Ireland","Israel","Italy","Japan","Latvia","Malaysia","Mexico","Russia","Saudi Arabia","Singapore","South Korea","Sweden","Thailand","UAE","United Kingdom","United States"};
        countries_short=new String[]{"ar","au","at","be","br","bg","ca","cn","co","cu","cz","eg","fr","de","gr","hk","hu","in","id","ie","il","it","jp","lv","my","mx","ru","sa","sg","kr","se","th","ae","gb","us"};
        categories=new String[]{"Show me everything","Business","Sports","Politics","Entertainment","General","Health","Science","Technology","","","","",""};
        sources=new String[]{"No Source","indiatimes.com","ndtv.com","indiatoday.in","indianexpress.com","thehindu.com","business-standard.com","scroll.in","financialexpress.com","thehindubusinessline.com","thequint.com","bbc.com","nytimes.com","buzzfeed.com","aljazeera.com","cnn.com","theguardian.com","rt.com","washingtonpost.com","cnbc.com","reuters.com","express.co.uk","independent.co.uk","vox.com","cbsnews.com","time.com","mirror.co.uk","abcnews.go.com","skysports.com","sportskeeda.com"};

        if(SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("country","").length()>0){
            newsCountry.setText("Current: "+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("country","")+"\nClick to change the country of origin for news.");
        }else{
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("country","in");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().commit();
        }

        if(SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("category","").length()>0){
            newsCategory.setText("Current: "+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("category","")+"\nClick to change the category of news.");
        }else{
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("category","");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().commit();
        }

        if(SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("sources","").length()>0){
            newsSource.setText("Current: "+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("sources","")+"\nClick to set your own source of news. Note: If you set a source, country and category parameter are ignored.");
        }else{
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("sources","");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
