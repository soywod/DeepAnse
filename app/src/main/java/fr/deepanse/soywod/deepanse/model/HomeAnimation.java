package fr.deepanse.soywod.deepanse.model;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import fr.deepanse.soywod.deepanse.activity.Home;

/**
 * Created by soywod on 24/02/2015.
 */
public class HomeAnimation extends TranslateAnimation implements Animation.AnimationListener {

    private String message;
    private Context context;
    private View nextView;
    private MediaPlayer sound;
    private HomeAnimation nextAnim;

    public HomeAnimation(MediaPlayer sound, Context context, View nextView, HomeAnimation nextAnim, String message) {
        super(0, 0, 0, -15);

        this.message = message;
        this.context = context;
        this.nextView = nextView;
        this.nextAnim = nextAnim;
        this.sound = sound;

        initData();
    }

    public void initData() {
        setDuration(300);
        setRepeatCount(19);
        setRepeatMode(Animation.REVERSE);
        setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        if (sound != null) sound.start();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (nextView != null)
            nextView.startAnimation(new HomeAnimation(nextAnim.getSound(), nextAnim.getContext(), nextAnim.getNextView(), nextAnim.getNextAnim(), nextAnim.getMessage()));
        else
            ((Home) context).endAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public String getMessage() {
        return message;
    }

    public Context getContext() {
        return context;
    }

    public View getNextView() {
        return nextView;
    }

    public HomeAnimation getNextAnim() {
        return nextAnim;
    }

    public MediaPlayer getSound() {
        return sound;
    }
}
