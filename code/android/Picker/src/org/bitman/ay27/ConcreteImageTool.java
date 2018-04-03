package org.bitman.ay27;

import android.os.Environment;
import org.bitman.picker.imageproc_module.ConcreteImageProc;
import org.bitman.picker.imageproc_module.ImageProc;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-31.
 */
public final class ConcreteImageTool {

    private static ImageProc imageProc;

    public static ImageProc getImageProc() {
        if (imageProc == null)
            return imageProc = new ConcreteImageProc(Environment.getExternalStorageDirectory() + "/Picker/tesseract", "eng");
        return imageProc;
    }

    public static void recycle() {
        imageProc = null;
    }
}
