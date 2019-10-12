package ir.ham3da.darya;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.ham3da.darya.adaptors.AdapterSocialList;
import ir.ham3da.darya.ganjoor.GanjoorDbBrowser;
import ir.ham3da.darya.ganjoor.GanjoorPoet;
import ir.ham3da.darya.utility.AppFontManager;
import ir.ham3da.darya.utility.AppSettings;
import ir.ham3da.darya.utility.CustomProgress;
import ir.ham3da.darya.utility.LangSettingList;
import ir.ham3da.darya.utility.LinkItem;
import ir.ham3da.darya.utility.SetLanguage;

public class ActivitySettings extends AppCompatActivity {


    static Preference  pref_op_db, pref_setFont;


//    static Preference  pref_rand_poem;
    int currentLocalIndex;
    GanjoorDbBrowser GanjoorDbBrowser1;


    public void setRandomPoemText() {

    }


    public static String getRandomPoetsName(GanjoorDbBrowser GanjoorDbBrowser1) {
        List<String> names = new ArrayList<>();
        String randomSelectedPoets = AppSettings.getRandomSelectedPoets();

        List<GanjoorPoet> poets = GanjoorDbBrowser1.getPoets(randomSelectedPoets);
        for (GanjoorPoet poet : poets) {
            names.add(poet._Name);
        }
        String names2 = TextUtils.join(" - ", names);
        return names2;
    }


    public static String getRandomPoetsNameFromCat(GanjoorDbBrowser GanjoorDbBrowser1) {
        List<String> names = new ArrayList<>();
        String randomSelectedCategories = AppSettings.getRandomSelectedCategories();

        List<GanjoorPoet> poets = GanjoorDbBrowser1.getPoetsFromCat(randomSelectedCategories);
        for (GanjoorPoet poet : poets) {
            names.add(poet._Name);
        }
        String names2 = TextUtils.join(" - ", names);
        return names2;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(SetLanguage.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            SetLanguage.wrap(this);
        }

        setContentView(R.layout.settings_activity);

        AppSettings.Init(this);
        LangSettingList langSetting = AppSettings.getLangSettingList(this);
        currentLocalIndex = langSetting.getId();
        GanjoorDbBrowser1 = new GanjoorDbBrowser(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.action_settings);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            GanjoorDbBrowser GanjoorDbBrowser1 = new GanjoorDbBrowser(getContext());

            EditTextPreference prefTextSize = findPreference("TextSize");

            final float textSize = AppSettings.getTextSize();


            if (prefTextSize != null) {

                String textSizeStr = String.format(Locale.getDefault(), "%.0f", textSize);

                prefTextSize.setTitle(prefTextSize.getTitle() + " (" + textSizeStr + ")");

                prefTextSize.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        editText.setTextDirection(View.TEXT_DIRECTION_LTR);
                        editText.selectAll();
                    }
                });
                prefTextSize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue)
                    {
                        App globalVariable = (App) getContext().getApplicationContext();
                        globalVariable.setUpdatePoetList(true);
                        globalVariable.setUpdateFavList(true);
                        String textSizeStr = String.format(Locale.getDefault(), "%.0f", Float.parseFloat( newValue.toString()) );
                        prefTextSize.setTitle( getContext().getString(R.string.Text_size )+ " (" + textSizeStr + ")");
                        return true;
                    }
                });

            }

            final ActivitySettings activitySettings = (ActivitySettings) getActivity();

            LangSettingList LangSettingList1 = AppSettings.getLangSettingList(getContext());

            Preference preferenceLang = findPreference("langSettingList");

            if (preferenceLang != null)
            {
                preferenceLang.setTitle(preferenceLang.getTitle() + " (" + LangSettingList1.getText() + ")");

                preferenceLang.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        activitySettings.openLangDailog(preference);
                        return false;
                    }
                });

            }



            pref_op_db = findPreference("optimize_db");
            pref_op_db.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    activitySettings.optimizeDatabase();
                    return true;
                }
            });

            pref_setFont = findPreference("setFont");

            int fontId = AppSettings.getPoemsFont();

           // ArrayList<LinkItem> fonts = activitySettings.getFontsList();

            String fontName = AppFontManager.getFontName(getContext(), fontId);

            pref_setFont.setTitle(pref_setFont.getTitle() + " (" + fontName + ")");

            pref_setFont.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    activitySettings.openFontDailog(preference);

                    return true;
                }
            });


        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    protected  ArrayList<LinkItem> getFontsList()
    {
        ArrayList<LinkItem> links = AppFontManager.getFontsList(this);
        return links;
    }

    protected void openFontDailog(Preference preference)
    {

       final Dialog dialog = new Dialog(preference.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listview);

        dialog.setCancelable(true);


        TextView dlg_title = dialog.findViewById(R.id.dlg_title);
        ImageView dialog_icon = dialog.findViewById(R.id.dialog_icon);

        dlg_title.setText(getString(R.string.change_poem_font));

        dialog_icon.setImageResource(R.drawable.ic_text);

        ListView listView = dialog.findViewById(R.id.listView);

        AdapterSocialList adapterSocialList = new AdapterSocialList(getFontsList(), this, R.color.transparent);
        adapterSocialList.ChangeFont = true;

        listView.setAdapter(adapterSocialList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dialog.dismiss();
                AppSettings.setPoemsFont(position);
                App globalVariable = (App) getApplicationContext();
                globalVariable.setUpdatePoetList(true);
                globalVariable.setUpdateFavList(true);

                String fontName = AppFontManager.getFontName(getBaseContext(), position);

                preference.setTitle(getString(R.string.change_poem_font)  + " (" + fontName + ")");

            }
        });

        Button okBtn = dialog.findViewById(R.id.okBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    protected void openLangDailog(Preference preference)
    {

        ArrayList<LinkItem> links = new ArrayList<>();

        links.add(new LinkItem(0, getString(R.string.persian), "", R.drawable.ic_iran));
        links.add(new LinkItem(1, getString(R.string.english), "", R.drawable.ic_united_states));

        final Dialog dialog = new Dialog(preference.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listview);

        dialog.setCancelable(true);


        TextView dlg_title = dialog.findViewById(R.id.dlg_title);
        ImageView dialog_icon = dialog.findViewById(R.id.dialog_icon);

        dlg_title.setText(getString(R.string.change_lang));
        dialog_icon.setImageResource(R.drawable.ic_language_black_24dp);

        ListView listView = dialog.findViewById(R.id.listView);

        AdapterSocialList adapterSocialList = new AdapterSocialList(links, this, R.color.transparent);

        listView.setAdapter(adapterSocialList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dialog.dismiss();
                AppSettings.saveLanguageSettings(position);
                if (currentLocalIndex != position) {
                    currentLocalIndex = position;

                    recreate();
                }
            }
        });

        Button okBtn = dialog.findViewById(R.id.okBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

       dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.slideDown(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (android.R.id.home == id) {
            finish();
            Bungee.slideDown(this);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void optimizeDatabase()
    {
        final CustomProgress customProgressDlg = new CustomProgress(this);
        customProgressDlg.showProgress(getString(R.string.optimize_db), getString(R.string.please_wait2) , false, false, true);

        new Thread(new Runnable() {
            public void run() {
                try
                {
                    GanjoorDbBrowser1.Vacum();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                customProgressDlg.dismiss();
            }
        }).start();
    }

    public void setRandomPoetText()//برای آینده
    {
//        GanjoorDbBrowser GanjoorDbBrowser1 = new GanjoorDbBrowser(this);
//        String des = getString(R.string.current_scope);
//        des += " " + getRandomPoetsNameFromCat(GanjoorDbBrowser1) + "";
//        pref_rand_poem.setSummary(des);
    }

}