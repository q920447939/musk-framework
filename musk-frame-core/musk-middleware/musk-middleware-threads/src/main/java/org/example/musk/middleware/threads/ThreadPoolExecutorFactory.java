/**
 * @Project:
 * @Author:
 * @Date: 2021年09月07日
 */
package org.example.musk.middleware.threads;

import java.util.concurrent.ExecutorService;

/**
 * ClassName: ThreadFactory
 *
 * @author
 * @Description:
 * @date 2021年09月07日
 */
public interface ThreadPoolExecutorFactory {
    ExecutorService getThreadPool();
}
