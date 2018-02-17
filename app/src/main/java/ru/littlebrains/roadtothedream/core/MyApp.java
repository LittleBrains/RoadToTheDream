package ru.littlebrains.roadtothedream.core;

import ru.littlebrains.roadtothedream.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by evgeniy on 04.07.2017.
 */

public class MyApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*Fabric.with(this, new Crashlytics());

        FirebaseAnalytics.getInstance(this);*/

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BebasNeueRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}