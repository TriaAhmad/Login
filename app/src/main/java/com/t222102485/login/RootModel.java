package com.t222102485.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootModel {
    @SerializedName("list")
    private List<ListModel> listModelList;
    @SerializedName("city")
    private CityModel cityModel;

    public RootModel() { }

    public List<ListModel> getListModelList() {
        return listModelList;
    }

    public CityModel getCityModel() {
        return cityModel;
    }

    public void setCityModel(CityModel cityModel) {
        this.cityModel = cityModel;
    }
}
