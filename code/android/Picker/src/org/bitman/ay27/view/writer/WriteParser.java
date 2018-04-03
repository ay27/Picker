package org.bitman.ay27.view.writer;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import org.bitman.ay27.common.ImageDecodeUtils;
import org.bitman.ay27.common.Utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-10.
 */
public class WriteParser {

    public static final String IMAGE_PATTERN = "<image>(.*)</image>";
    public static final Pattern ImagePattern = Pattern.compile(IMAGE_PATTERN);

    public static String text2Show(Context context, final Editable text) {
        Editable editable = Editable.Factory.getInstance().newEditable(text);

        CharacterStyle[] styleSpans = editable.getSpans(0, editable.length(), CharacterStyle.class);

        for (CharacterStyle span : styleSpans) {
            int start = editable.getSpanStart(span);
            int end = editable.getSpanEnd(span);
            if (span instanceof URLSpan) {
                editable.replace(start, end, "["+editable.subSequence(start, end)+"]("+((URLSpan) span).getURL()+")");
            }

            else if (span instanceof ImageSpan) {
                CharSequence uriStr = editable.subSequence(start, end);
                Matcher matcher = ImagePattern.matcher(uriStr);
                Uri uri = null;
                if (matcher.find()) {
                    uri = Uri.parse(matcher.group(1));
                }

                String newPath = null;
                try {
                    newPath = Utils.write2cache(context, ImageDecodeUtils.decodeSource(context, uri, 250));
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                if (newPath==null)
                    continue;
                if (newPath.startsWith("/mnt"))
                    editable.replace(start, end, "\n![image]"+"(file:/"+newPath+")\n");
                else
                    editable.replace(start, end, "\n![image]"+"(file://"+newPath+")\n");
            }
        }

        return editable.toString();
    }
}
