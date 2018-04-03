package org.bitman.ay27.view.writer.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/23.
 */
class ImageUploadUtils {

    // the standard md syntax for image url.
    // ![xxx](xxx)
    private static final String IMAGE_PATTERN = "![\\[](.*)[\\]]\\(file://(.*)\\)";
    private static final Pattern ImagePattern = Pattern.compile(IMAGE_PATTERN);

    private ImageUploadUtils() {}

    public static class DataContainer {
        int start, end;
        String filePath;
        String replacementUrl;

        private DataContainer(String filePath, int start, int end) {
            this.filePath = filePath;
            this.start = start;
            this.end = end;
        }
    }

    public static ArrayList<DataContainer> findImagesInMd(String rawData) {
        Matcher matcher = ImagePattern.matcher(rawData);
        ArrayList<DataContainer> containers = new ArrayList<DataContainer>();
        while (matcher.find()) {
            containers.add(new ImageUploadUtils.DataContainer(matcher.group(2), matcher.start(2), matcher.end(2)));
        }

        return containers;
    }

    public static String replaceImageUrlsInId(String rawData, List<DataContainer> containers) {
        int positionDelta = 0;
        StringBuilder sb = new StringBuilder(rawData);
        for (DataContainer container : containers) {
            if (container.replacementUrl == null)
                continue;
            sb.replace(container.start + positionDelta, container.end + positionDelta, container.replacementUrl);
            positionDelta += (container.replacementUrl.length() - (container.end - container.start));
        }
        return sb.toString();
    }
}
