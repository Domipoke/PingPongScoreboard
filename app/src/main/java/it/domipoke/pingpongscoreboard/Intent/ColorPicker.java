package it.domipoke.pingpongscoreboard.Intent;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class ColorPicker {
    public int r;
    public int g;
    public int b;
    public int c = Color.WHITE;

    public LinearLayout ll;
    public ColorPicker(Context ctx){
        ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.VERTICAL);
        View v = new View(ctx);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,256));
        SeekBar sR = new SeekBar(ctx);
        ViewGroup.LayoutParams WHseekbar = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,128);
        sR.setMin(0);
        sR.setMax(255);
        sR.getThumb().setTint(Color.RED);
        sR.getProgressDrawable().setTint(Color.RED);
        sR.setLayoutParams(WHseekbar);
        SeekBar sG = new SeekBar(ctx);
        sG.setMin(0);
        sG.setMax(255);
        sG.getThumb().setTint(Color.GREEN);
        sG.getProgressDrawable().setTint(Color.GREEN);
        sG.setLayoutParams(WHseekbar);
        SeekBar sB = new SeekBar(ctx);
        sB.setMin(0);
        sB.setMax(255);
        sB.getThumb().setTint(Color.BLUE);
        sB.getProgressDrawable().setTint(Color.BLUE);
        sB.setLayoutParams(WHseekbar);
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean bool) {
                r = sR.getProgress();
                g = sG.getProgress();
                b = sB.getProgress();
                c = Color.rgb(r, g, b);
                v.setBackgroundColor(c);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        } ;
        sR.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sG.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sB.setOnSeekBarChangeListener(onSeekBarChangeListener);

        ll.addView(v);
        ll.addView(sR);
        ll.addView(sG);
        ll.addView(sB);
    }

    public LinearLayout getLl() {
        return ll;
    }
}
