package org.bitman.ay27.view.writer.editor_button;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-28.
 */
public abstract class EditorButton extends Button {
    public EditorButton(Context context) {
        super(context);
    }

    public EditorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditorButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected EditText editor = null;
    public final void setEditor(EditText editor) {
        this.editor = editor;
    }

    public abstract void processClick();

    public void insert(String str, int selectedOffset) {
        if (editor == null)
            return;

        editor.getText().replace(editor.getSelectionStart(), editor.getSelectionEnd(), str);
        editor.setSelection(editor.getSelectionStart()+selectedOffset);
    }
}
