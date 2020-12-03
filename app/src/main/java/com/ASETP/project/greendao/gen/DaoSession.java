package com.ASETP.project.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.ASETP.project.model.LocationPlaces;
import com.ASETP.project.model.PlacePaidData;

import com.ASETP.project.greendao.gen.LocationPlacesDao;
import com.ASETP.project.greendao.gen.PlacePaidDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig locationPlacesDaoConfig;
    private final DaoConfig placePaidDataDaoConfig;

    private final LocationPlacesDao locationPlacesDao;
    private final PlacePaidDataDao placePaidDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        locationPlacesDaoConfig = daoConfigMap.get(LocationPlacesDao.class).clone();
        locationPlacesDaoConfig.initIdentityScope(type);

        placePaidDataDaoConfig = daoConfigMap.get(PlacePaidDataDao.class).clone();
        placePaidDataDaoConfig.initIdentityScope(type);

        locationPlacesDao = new LocationPlacesDao(locationPlacesDaoConfig, this);
        placePaidDataDao = new PlacePaidDataDao(placePaidDataDaoConfig, this);

        registerDao(LocationPlaces.class, locationPlacesDao);
        registerDao(PlacePaidData.class, placePaidDataDao);
    }
    
    public void clear() {
        locationPlacesDaoConfig.clearIdentityScope();
        placePaidDataDaoConfig.clearIdentityScope();
    }

    public LocationPlacesDao getLocationPlacesDao() {
        return locationPlacesDao;
    }

    public PlacePaidDataDao getPlacePaidDataDao() {
        return placePaidDataDao;
    }

}