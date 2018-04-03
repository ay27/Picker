package org.bitman.ay27.view.writer.editor_button;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-28.
 */
public class BracketButton extends EditorButton {
    public BracketButton(Context context) {
        super(context);
    }

    public BracketButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BracketButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void processClick() {
        insert("()", -1);
    }
}
