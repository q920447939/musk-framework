package org.example.musk.middleware.generator.uid.orderHelper;


/**
 * @Description:
 * @date 2024年07月04日
 */

public class OrderHelper {
    static final UniqueOrderGenerate idWorker = new UniqueOrderGenerate(0, 0);

    public static final long getNextId() {
        return idWorker.nextId();
    }
}
