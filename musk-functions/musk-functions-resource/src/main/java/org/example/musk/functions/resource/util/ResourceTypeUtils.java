package org.example.musk.functions.resource.util;

import org.apache.commons.lang3.StringUtils;
import org.example.musk.functions.resource.enums.ResourceTypeEnum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 资源类型工具类
 *
 * @author musk-functions-resource
 */
public class ResourceTypeUtils {

    /**
     * 图片类型集合
     */
    private static final Set<String> IMAGE_TYPES = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico", "tif", "tiff")
    );

    /**
     * 文档类型集合
     */
    private static final Set<String> DOCUMENT_TYPES = new HashSet<>(
            Arrays.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt", "csv", "md", "rtf", "odt", "ods", "odp")
    );

    /**
     * 视频类型集合
     */
    private static final Set<String> VIDEO_TYPES = new HashSet<>(
            Arrays.asList("mp4", "avi", "mov", "wmv", "flv", "mkv", "webm", "m4v", "3gp", "mpeg", "mpg", "rm", "rmvb")
    );

    /**
     * 音频类型集合
     */
    private static final Set<String> AUDIO_TYPES = new HashSet<>(
            Arrays.asList("mp3", "wav", "wma", "ogg", "aac", "flac", "m4a", "mid", "midi", "amr")
    );

    /**
     * 压缩包类型集合
     */
    private static final Set<String> ARCHIVE_TYPES = new HashSet<>(
            Arrays.asList("zip", "rar", "7z", "tar", "gz", "bz2", "xz", "jar", "war", "ear")
    );

    /**
     * 根据文件类型获取资源类型
     *
     * @param fileType 文件类型（扩展名）
     * @return 资源类型编码
     */
    public static Integer getResourceTypeByFileType(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            return ResourceTypeEnum.OTHER.getCode();
        }

        String lowerFileType = fileType.toLowerCase();

        if (IMAGE_TYPES.contains(lowerFileType)) {
            return ResourceTypeEnum.IMAGE.getCode();
        } else if (DOCUMENT_TYPES.contains(lowerFileType)) {
            return ResourceTypeEnum.DOCUMENT.getCode();
        } else if (VIDEO_TYPES.contains(lowerFileType)) {
            return ResourceTypeEnum.VIDEO.getCode();
        } else if (AUDIO_TYPES.contains(lowerFileType)) {
            return ResourceTypeEnum.AUDIO.getCode();
        } else if (ARCHIVE_TYPES.contains(lowerFileType)) {
            return ResourceTypeEnum.ARCHIVE.getCode();
        } else {
            return ResourceTypeEnum.OTHER.getCode();
        }
    }

    /**
     * 判断是否为图片类型
     *
     * @param fileType 文件类型（扩展名）
     * @return 是否为图片类型
     */
    public static boolean isImageType(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            return false;
        }
        return IMAGE_TYPES.contains(fileType.toLowerCase());
    }

    /**
     * 判断是否为文档类型
     *
     * @param fileType 文件类型（扩展名）
     * @return 是否为文档类型
     */
    public static boolean isDocumentType(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            return false;
        }
        return DOCUMENT_TYPES.contains(fileType.toLowerCase());
    }

    /**
     * 判断是否为视频类型
     *
     * @param fileType 文件类型（扩展名）
     * @return 是否为视频类型
     */
    public static boolean isVideoType(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            return false;
        }
        return VIDEO_TYPES.contains(fileType.toLowerCase());
    }

    /**
     * 判断是否为音频类型
     *
     * @param fileType 文件类型（扩展名）
     * @return 是否为音频类型
     */
    public static boolean isAudioType(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            return false;
        }
        return AUDIO_TYPES.contains(fileType.toLowerCase());
    }

    /**
     * 判断是否为压缩包类型
     *
     * @param fileType 文件类型（扩展名）
     * @return 是否为压缩包类型
     */
    public static boolean isArchiveType(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            return false;
        }
        return ARCHIVE_TYPES.contains(fileType.toLowerCase());
    }

    /**
     * 私有构造函数，防止实例化
     */
    private ResourceTypeUtils() {
        throw new IllegalStateException("Utility class");
    }
}
