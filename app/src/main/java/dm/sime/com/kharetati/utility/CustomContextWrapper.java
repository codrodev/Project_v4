package dm.sime.com.kharetati.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class CustomContextWrapper extends ContextWrapper {

    public CustomContextWrapper(Context base) {
        super(base);
    }

   @SuppressWarnings("deprecation")
    public static ContextWrapper wrap(Context context, String language) {
 /*       Configuration config = context.getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }
        //if (!language.equals("") && !sysLocale.getLanguage().equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale);
                context = context.createConfigurationContext(config);

            } else {
                config.locale=locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        //}
        return new CustomContextWrapper(context);*/
    Resources res = context.getResources();
    Configuration configuration = res.getConfiguration();
    Locale newLocale = new Locale(language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.setLocale(newLocale);
        LocaleList localeList = new LocaleList(newLocale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        //context = context.getApplicationContext().createConfigurationContext(configuration);//change made by sudeep
        context = context.createConfigurationContext(configuration);

    } /*else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        configuration.setLocale(newLocale);
        context = context.createConfigurationContext(configuration);

    }*/ else {
        configuration.locale = newLocale;
        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }


        return new CustomContextWrapper(context);
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale){
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale){
        config.setLocale(locale);
    }
}
