package com.wz.mobilemedia.dao.manager;


import com.wz.mobilemedia.dao.MediaInfoBeanDao;

/**
 * Created by wz on 17-5-7.
 */

public class EntityManager {
    private static EntityManager entityManager;
    public MediaInfoBeanDao mMediaInfoBeanDao;

    /**
     * 创建User表实例
     *
     * @return
     */
    public MediaInfoBeanDao getMediaInfoBeanDao(){
        mMediaInfoBeanDao = DaoManager.getInstance().getSession().getMediaInfoBeanDao();
        return mMediaInfoBeanDao;
    }

    /**
     * 创建单例
     *
     * @return
     */
    public static EntityManager getInstance() {
        if (entityManager == null) {
            entityManager = new EntityManager();
        }
        return entityManager;
    }
}
