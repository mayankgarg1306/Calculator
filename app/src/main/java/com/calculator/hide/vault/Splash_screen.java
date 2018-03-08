package com.calculator.hide.vault;
import android.content.Intent;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;


public class Splash_screen extends AwesomeSplash {

    Thread splashTread;
    ImageView imageView;

    @Override
    public void initSplash(ConfigSplash configSplash) {

   /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimaryDark); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1200); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.icon); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms//choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)
        configSplash.setAnimLogoSplashTechnique(Techniques.BounceInUp);
        //Customize Title
        configSplash.setTitleSplash("");
        configSplash.setTitleTextColor(R.color.cardview_dark_background);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(0);
        configSplash.setTitleFont("fonts/diti_sweet.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(Splash_screen.this,
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        Splash_screen.this.finish();

    }

}