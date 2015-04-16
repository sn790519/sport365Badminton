package com.sport365.badminton.entity.webservice;

import com.sport365.badminton.http.json.CacheOptions;
import com.sport365.badminton.http.json.WebService;

public class SportWebService extends WebService {

    private SportParameter mSportParameter;

    public SportWebService(SportParameter SportParameter) {
        this.mSportParameter = SportParameter;
    }

    @Override
    protected String getServiceAction() {
        return mSportParameter.getServiceAction();
    }

    @Override
    public String getServiceName() {
        return mSportParameter.getServiceName();
    }

    @Override
    public CacheOptions getCacheOptions() {
        return CacheOptions
                .buildCacheOptions(mSportParameter.getCacheOptions());
    }

}
