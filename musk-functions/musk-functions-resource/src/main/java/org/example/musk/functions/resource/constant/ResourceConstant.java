package org.example.musk.functions.resource.constant;

/**
 * 资源模块常量
 *
 * @author musk-functions-resource
 */
public class ResourceConstant {

    /**
     * 资源缓存命名空间
     */
    public static final String CACHE_NAMESPACE = "RESOURCE";

    /**
     * 资源缓存过期时间（秒）
     */
    public static final int RESOURCE_CACHE_EXPIRE_SECONDS = 3600;

    /**
     * 资源分类缓存过期时间（秒）
     */
    public static final int CATEGORY_CACHE_EXPIRE_SECONDS = 3600;

    /**
     * 资源URL缓存过期时间（秒）
     */
    public static final int URL_CACHE_EXPIRE_SECONDS = 86400;

    /**
     * 默认缩略图尺寸
     */
    public static final String DEFAULT_THUMBNAIL_SIZE = "200x200";

    /**
     * 默认图片压缩质量
     */
    public static final int DEFAULT_COMPRESS_QUALITY = 85;

    /**
     * Redis流前缀
     */
    public static final String REDIS_STREAM_KEY_PREFIX = "musk:resource:";

    /**
     * 资源文件名分隔符
     */
    public static final String FILENAME_SEPARATOR = "_";

    /**
     * 资源文件路径分隔符
     */
    public static final String PATH_SEPARATOR = "/";

    /**
     * 资源文件名日期格式
     */
    public static final String FILENAME_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
     * 资源文件路径日期格式
     */
    public static final String PATH_DATE_FORMAT = "yyyy/MM/dd";

    /**
     * 资源文件名随机字符串长度
     */
    public static final int FILENAME_RANDOM_LENGTH = 8;

    /**
     * 资源文件分片大小（字节）
     */
    public static final int CHUNK_SIZE = 1024 * 1024; // 1MB

    /**
     * 资源文件分片临时目录
     */
    public static final String CHUNK_TEMP_DIR = "chunks";

    /**
     * 资源文件分片合并超时时间（毫秒）
     */
    public static final long CHUNK_MERGE_TIMEOUT = 30 * 60 * 1000; // 30分钟

    /**
     * 资源文件分片上传超时时间（毫秒）
     */
    public static final long CHUNK_UPLOAD_TIMEOUT = 30 * 60 * 1000; // 30分钟

    /**
     * 资源文件分片上传并发数
     */
    public static final int CHUNK_UPLOAD_CONCURRENCY = 5;

    /**
     * 资源文件分片上传重试次数
     */
    public static final int CHUNK_UPLOAD_RETRY = 3;

    /**
     * 资源文件分片上传重试间隔（毫秒）
     */
    public static final long CHUNK_UPLOAD_RETRY_INTERVAL = 1000; // 1秒

    /**
     * 资源文件分片上传重试指数退避因子
     */
    public static final double CHUNK_UPLOAD_RETRY_BACKOFF_FACTOR = 1.5;

    /**
     * 资源文件分片上传重试最大间隔（毫秒）
     */
    public static final long CHUNK_UPLOAD_RETRY_MAX_INTERVAL = 10 * 1000; // 10秒

    /**
     * 资源文件分片上传重试最大总时间（毫秒）
     */
    public static final long CHUNK_UPLOAD_RETRY_MAX_ELAPSED_TIME = 5 * 60 * 1000; // 5分钟

    /**
     * 资源文件分片上传重试状态码
     */
    public static final int[] CHUNK_UPLOAD_RETRY_STATUS_CODES = {408, 429, 500, 502, 503, 504};

    /**
     * 资源文件分片上传重试异常类
     */
    public static final Class<?>[] CHUNK_UPLOAD_RETRY_EXCEPTIONS = {
            java.io.IOException.class,
            java.net.SocketTimeoutException.class,
            java.net.ConnectException.class,
            java.net.SocketException.class,
            java.net.UnknownHostException.class,
            javax.net.ssl.SSLException.class
    };

    /**
     * 私有构造函数，防止实例化
     */
    private ResourceConstant() {
        throw new IllegalStateException("Constant class");
    }
}
