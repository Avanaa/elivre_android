package br.com.avana.elivreapp.quickstart;

import android.support.v7.app.AppCompatActivity;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class TapTarget {

    private MaterialTapTargetPrompt.Builder builder;
    private AppCompatActivity activity;
    private int target;
    private int primaryText;
    private int secondaryText;
    private int color;
    private TapTarget next;

    public TapTarget(AppCompatActivity activity, int target, int primaryText, int secondaryText, int color, TapTarget next){

        this.activity = activity;
        this.target = target;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.color = color;
        this.next = next;

        builder = createBuilder();
    }

    private MaterialTapTargetPrompt.Builder createBuilder(){

        MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(target)
                .setPrimaryText(primaryText)
                .setSecondaryText(secondaryText)
                .setBackgroundColour(activity.getResources().getColor(color))
                .setAutoDismiss(true);

        if (next != null){
            builder.setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                @Override
                public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        next.getBuilder().show();
                    }
                    if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                        next.getBuilder().show();
                    }
                }
            });
        }
        return builder;
    }

    public MaterialTapTargetPrompt.Builder getBuilder() {
        return builder;
    }

}
