package org.bitman.ay27.view.writer.editor_button;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-29.
 */
public class SharpButton extends EditorButton {
    public SharpButton(Context context) {
        super(context);
    }

    public SharpButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SharpButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void processClick() {
        insert("#", 0);
    }
}
