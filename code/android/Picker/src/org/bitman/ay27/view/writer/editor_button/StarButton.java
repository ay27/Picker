package org.bitman.ay27.view.writer.editor_button;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-29.
 */
public class StarButton extends EditorButton {
    public StarButton(Context context) {
        super(context);
    }

    public StarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StarButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void processClick() {
        insert("*", 0);
    }
}
