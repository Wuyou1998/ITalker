package com.wy.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.wy.factory.model.db.AppDatabase;

import java.util.Arrays;

/* 名称: ITalker.com.wy.factory.data.helper.DbHelper
 * 用户: _VIEW
 * 时间: 2019/9/17,14:21
 * 描述: 数据库的辅助工具类，完成增删改功能
 */
public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {
    }

    /**
     * 新增或者修改的统一方法
     *
     * @param clazz   bean类
     * @param models  bean类的实例数组
     * @param <Model> 泛型，限定条件是继承自baseModel
     */
    public static <Model extends BaseModel> void save(final Class<Model> clazz, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        //当前数据库的管理者
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(clazz);
                //保存
                adapter.saveAll(Arrays.asList(models));
                //唤起通知
                instance.notifySave(clazz, models);
            }
        }).build().execute();
    }

    /**
     * 数据库进行删除操作的统一封装
     *
     * @param clazz   bean类
     * @param models  bean类的实例数组
     * @param <Model> 泛型，限定条件是继承自baseModel
     */
    public static <Model extends BaseModel> void delete(final Class<Model> clazz, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        //当前数据库的管理者
        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(clazz);
                //删除
                adapter.deleteAll(Arrays.asList(models));
                //唤起通知
                instance.notifyDelete(clazz, models);
            }
        }).build().execute();
    }

    /**
     * 进行通知调用
     *
     * @param clazz   通知的类型
     * @param models  通知的Model数组
     * @param <Model> 泛型，限定条件是继承自baseModel
     */
    private final <Model extends BaseModel> void notifySave(final Class<Model> clazz, final Model... models) {
        //TODO
    }

    /**
     * 进行通知调用
     *
     * @param clazz   通知的类型
     * @param models  通知的Model数组
     * @param <Model> 泛型，限定条件是继承自baseModel
     */
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> clazz, final Model... models) {
        //TODO
    }
}
