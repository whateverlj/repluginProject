package  com.example.commonlib.util;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * 数据库操作工具类
 *
 * @author Like
 * @version 1.0.0
 */
public class DBHelper {

    private static DbManager mDb;

    private static DbManager.DaoConfig mDaoConfig = null;

    /**
     * 初始化数据库
     *
     * @param dbName 数据库名称
     */
    public static void init(String dbName) {
        mDaoConfig = new DbManager.DaoConfig()
                .setDbName(dbName)
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
    }

    /**
     * 重新初始化，适用于数据库版本更新
     *
     * @param dbName   如果之前掉用过init方法， 则此参数无效
     * @param version
     * @param listener
     */
    public static void reInit(String dbName, int version, DbManager.DbUpgradeListener listener) {
        if (mDaoConfig == null) {
            init(dbName);
        }
        mDaoConfig.setDbVersion(version).setDbUpgradeListener(listener);
    }

    /**
     * 获得数据库操作类
     *
     * @return
     */
    public static DbManager getDbManager() {
        if (mDb == null) {
            mDb = x.getDb(mDaoConfig);
        }
        return mDb;
    }
}
