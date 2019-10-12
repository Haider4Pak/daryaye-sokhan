package ir.ham3da.darya.utility;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.core.content.res.ResourcesCompat;
import android.os.Build;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ir.ham3da.darya.ganjoor.GanjoorVerse;
import ir.ham3da.darya.ganjoor.GanjoorVerseB;
import ir.ham3da.darya.R;

public class UtilFunctions {
    private Context context1;
    int Store = 3;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    public UtilFunctions(Context mCtx) {

        this.context1 = mCtx;
    }

    public void openMyTelegram() {
        try {

            Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
            telegramIntent.setData(Uri.parse("https://telegram.me/ham3da_ir"));
            telegramIntent.setPackage("org.telegram.messenger");
            context1.startActivity(telegramIntent);

        } catch (Exception e) {
            Toast.makeText(context1, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getAppLink() {
        String packageName = context1.getPackageName();
        String store = "https://play.google.com/store/apps/details?id=" + packageName;//google play
        switch (this.Store) {
            case 0:
                store = "https://play.google.com/store/apps/details?id=" + packageName;//google play
                break;
            case 1:
                store = "https://cafebazaar.ir/app/" + packageName + "/";//cafebazaar
                break;
            case 2:
                store = "https://myket.ir/app/" + packageName + "/";//myket
                break;
            case 3:
                store = "http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName; //samsung apps

            case 4:
                store = "https://iranapps.ir/app/" + packageName; //iranapps
        }
        return store;
    }

    public void openWhatsApp() {
        String smsNumber = "989118833904"; //without '+'
        try {
            Uri mUri = Uri.parse("smsto:+9118833904");
            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
            mIntent.setPackage("com.whatsapp");
            mIntent.putExtra("chat", true);
            context1.startActivity(Intent.createChooser(mIntent, context1.getString(R.string.contact_us)));

        } catch (Exception e) {
            Toast.makeText(context1, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public static void shareImage(Context context, Uri imageUri) {
        try {

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));

        } catch (Exception e)
        {
            Log.e("shareImage", "shareImage: "+e.getMessage() );
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void shareApp() {
        String link = getAppLink();

        String app_name = context1.getString(R.string.app_name);

        String app_des = String.format(Locale.getDefault(), context1.getString(R.string.app_share_des), app_name);

        shareText(app_des + " " + System.lineSeparator() + link);
    }

    public void shareText(String subject) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, subject);
        context1.startActivity(Intent.createChooser(txtIntent, context1.getString(R.string.share)));
    }

    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context1.startActivity(Intent.createChooser(txtIntent, context1.getString(R.string.share)));
    }

    public void gotoAppPage() {
        try {
            String packageName = context1.getPackageName();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            switch (this.Store) {
                case 0:
                    Uri.Builder uriBuilder = Uri.parse("https://play.google.com/store/apps/details")
                            .buildUpon()
                            .appendQueryParameter("id", packageName)
                            .appendQueryParameter("launch", "false");
                    intent.setData(uriBuilder.build());
                    intent.setPackage("com.android.vending");
                    break;

                case 1:
                    intent.setData(Uri.parse("https://cafebazaar.ir/app/" + packageName + "/"));
                    intent.setPackage("com.farsitel.bazaar");
                    break;
                case 2:
                    intent.setData(Uri.parse("https://myket.ir/app/" + packageName));
                    intent.setPackage("ir.mservices.market");
                    break;
                case 3:
                    intent.setData(Uri.parse("samsungapps://ProductDetail/" + packageName));
                    intent.setPackage("com.sec.android.app.samsungapps");
                    break;
                case 4:
                    intent.setData(Uri.parse("http://iranapps.ir/app/" + packageName));
                    intent.setPackage("ir.tgbs.android.iranapp");
                    break;
            }
            context1.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(context1, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("gotoAppPage", "err: " + e.getMessage());
        }

    }

    public void gotoRateing() {
        try {
            String packageName = context1.getPackageName();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            switch (this.Store) {
                case 0:
                    intent = new Intent(Intent.ACTION_VIEW);
                    Uri.Builder uriBuilder = Uri.parse("https://play.google.com/store/apps/details")
                            .buildUpon()
                            .appendQueryParameter("id", packageName)
                            .appendQueryParameter("launch", "false");
                    intent.setData(uriBuilder.build());
                    intent.setPackage("com.android.vending");
                    break;

                case 1:
                    intent = new Intent(Intent.ACTION_EDIT);
                    intent.setData(Uri.parse("bazaar://details?id=" + packageName));
                    intent.setPackage("com.farsitel.bazaar");

                    break;

                case 2:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("myket://comment/#Intent;scheme=comment;package=" + packageName + ";end"));
                    intent.setPackage("ir.mservices.market");
                    break;
                case 3:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("samsungapps://ProductDetail/" + packageName));
                    intent.setPackage("com.sec.android.app.samsungapps");
                    break;
                case 4:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("iranapps://app/"+packageName+"?a=comment&r=5"));
                    intent.setPackage("ir.tgbs.android.iranapp");
                    break;


            }
            context1.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(context1, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("gotoRateing", "err: " + e.getMessage());
        }

    }

    public void openUrl(String url)
    {

        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        intent1.setData(Uri.parse(url));
        context1.startActivity(intent1);
    }

    public void copyText(String text) {

        myClipboard = (ClipboardManager) context1.getSystemService(Context.CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(context1, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    public String getLanguage() {
        String CurrentLang = Locale.getDefault().getLanguage();
        return CurrentLang;
    }

    public void changeFont(View viewObject) {

        Typeface typeface = ResourcesCompat.getFont(context1, R.font.iran_sans_mobile_light);
        if (viewObject instanceof TextView) {
            viewObject = viewObject;
            ((TextView) viewObject).setTypeface(typeface);
        } else if (viewObject instanceof Button) {
            viewObject = viewObject;
            ((Button) viewObject).setTypeface(typeface);
        }

    }

    public void changeFont(View viewObject, int font) {

        Typeface typeface = ResourcesCompat.getFont(context1, font);
        if (viewObject instanceof TextView) {
            viewObject = viewObject;
            ((TextView) viewObject).setTypeface(typeface);
        } else if (viewObject instanceof Button) {
            viewObject = viewObject;
            ((Button) viewObject).setTypeface(typeface);
        }
    }

    public void openEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:ham3da.j@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.contact_us);
            emailIntent.putExtra(Intent.EXTRA_TEXT, context1.getString(R.string.contact_text));
            context1.startActivity(Intent.createChooser(emailIntent, context1.getString(R.string.choose_email_app)));
        } catch (Exception e) {
            Toast.makeText(context1, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public static String ConvertNumbersToPersian(String strWithNumber) {
        String answer = strWithNumber;
        answer = answer.replace("1", "١");
        answer = answer.replace("2", "٢");
        answer = answer.replace("3", "۳");
        answer = answer.replace("4", "۴");
        answer = answer.replace("5", "۵");
        answer = answer.replace("6", "۶");
        answer = answer.replace("7", "٧");
        answer = answer.replace("8", "٨");
        answer = answer.replace("9", "٩");
        answer = answer.replace("0", "٠");
        return answer;
    }

    /**
     * Arrange Verses for the RecycleView adaptor
     *
     * @param GanjoorVerseList
     * @return List of GanjoorVerseB
     */
    public List<GanjoorVerseB> VerseArrangement(List<GanjoorVerse> GanjoorVerseList) {
        List<GanjoorVerseB> ganjoorVerseBList = new ArrayList<>();

        int position = 0;
        int index = 0;
        int setIndex = 0;
        for (int i = 0; i < GanjoorVerseList.size(); i++) {
            GanjoorVerse ganjoorVerse1 = GanjoorVerseList.get(i);
            GanjoorVerse ganjoorVerse2;
            GanjoorVerseB ganjoorVerseB1;

            String verse_text1, verse_text2;

            int vOrder1 = 0;
            int vOrder2 = 0;

            switch (ganjoorVerse1._Position) {
                case GanjoorVerse.POSTION_RIGHT:

                    verse_text1 = ganjoorVerse1._Text;//Verse 1
                    vOrder1 = ganjoorVerse1._Order;

                    if (GanjoorVerseList.size() - 1 >= i + 1) {
                        ganjoorVerse2 = GanjoorVerseList.get(i + 1);
                        verse_text2 = ganjoorVerse2._Text;//Verse 2
                        vOrder2 = ganjoorVerse2._Order;

                    } else {
                        verse_text2 = "";
                    }
                    position++;
                    if (!ganjoorVerse1._Text.trim().equals("□") && !ganjoorVerse1._Text.trim().isEmpty()) {
                        index++;
                        setIndex = index;
                    } else {
                        setIndex = -1;
                    }
                    ganjoorVerseB1 = new GanjoorVerseB(ganjoorVerse1._PoemID, position, verse_text1, verse_text2, false, false, setIndex, vOrder1, vOrder2);
                    ganjoorVerseBList.add(ganjoorVerseB1);//Add verse to VerseList
                    break;
                case GanjoorVerse.POSITION_LEFT:
                    //IGNORE
                    break;

                case GanjoorVerse.POSITION_CENTEREDVERSE1:
                    verse_text1 = ganjoorVerse1._Text;//Verse 1
                    vOrder1 = ganjoorVerse1._Order;

                    if (GanjoorVerseList.size() - 1 >= position + 1) {

                        ganjoorVerse2 = GanjoorVerseList.get(position + 1);
                        if (ganjoorVerse2._Position == GanjoorVerse.POSITION_CENTEREDVERSE2) {
                            verse_text2 = ganjoorVerse2._Text;//Verse 2
                            vOrder2 = ganjoorVerse2._Order;
                        } else {
                            verse_text2 = "";
                        }
                    } else {
                        verse_text2 = "";
                    }
                    position++;
                    if (!ganjoorVerse1._Text.trim().equals("□") && !ganjoorVerse1._Text.trim().isEmpty()) {
                        index++;
                        setIndex = index;
                    } else {
                        setIndex = -1;
                    }

                    ganjoorVerseB1 = new GanjoorVerseB(ganjoorVerse1._PoemID, position, verse_text1, verse_text2, true, false, setIndex, vOrder1, vOrder2);
                    ganjoorVerseBList.add(ganjoorVerseB1);//Add verse to VerseList
                    break;
                case GanjoorVerse.POSITION_CENTEREDVERSE2:
                    //IGNORE
                    break;
                case GanjoorVerse.POSITION_SINGLE:
                case GanjoorVerse.POSITION_PARAGRAPH:
                    verse_text2 = "";
                    vOrder1 = ganjoorVerse1._Order;
                    vOrder2 = 0;

                    position++;
                    if (!ganjoorVerse1._Text.trim().equals("□") && !ganjoorVerse1._Text.trim().isEmpty()) {
                        index++;
                        setIndex = index;
                    } else {
                        setIndex = -1;
                    }

                    verse_text1 = ganjoorVerse1._Text;//Verse 1
                    ganjoorVerseB1 = new GanjoorVerseB(ganjoorVerse1._PoemID, position, verse_text1, verse_text2, false, false, setIndex, vOrder1, vOrder2);
                    ganjoorVerseBList.add(ganjoorVerseB1);//Add verse to VerseList

                    break;
            }

        }

        return ganjoorVerseBList;
    }


    public static void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {
        // Environment.getDataDirectory()

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdirs();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(
                        targetLocation, children[i]));
            }
        } else {

            copyFile(sourceLocation, targetLocation);
        }
    }

    /**
     * @param sourceLocation
     * @param targetLocation
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void copyFile(File sourceLocation, File targetLocation)
            throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(targetLocation);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public void setupToolbarLayout(CollapsingToolbarLayout toolbarLayout, boolean gravityRight) {
        try {

            Typeface typeface = ResourcesCompat.getFont(context1, R.font.iran_sans_mobile_light);
            toolbarLayout.setCollapsedTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);
            toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleTextAppearance);
            toolbarLayout.setCollapsedTitleTypeface(typeface);
            toolbarLayout.setExpandedTitleTypeface(typeface);
            if (gravityRight) {
                toolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM | Gravity.RIGHT);
            } else {
                toolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM | Gravity.START);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showKeyboard(final EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(message, 0);
        } else {
            return Html.fromHtml(message);
        }
    }

    public static List<LangSettingList> getLanguageList(Context context) {
        List<LangSettingList> result = new ArrayList<>();

        result.add(new LangSettingList(0, context.getString(R.string.persian), "fa", "IR"));
        result.add(new LangSettingList(1, context.getString(R.string.english), "en", "US"));

        return result;
    }

    @SuppressWarnings("deprecation")
    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE || ni.getType() == ConnectivityManager.TYPE_VPN));
                }

            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    return (
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                    || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                    || nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN));
                }
            }
        }

        return false;
    }


    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static boolean isFloat(String number) {
        //check if float
        try {
            Float.parseFloat(number);
            return true;
        } catch (NumberFormatException e) {
            //not float
            return false;
        }
    }


    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }


}