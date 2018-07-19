package com.lee.learn.house.domain;

import java.util.List;

public class HouseIndexTemplate extends House{

    private List<HouseSuggest> suggest;

    public List<HouseSuggest> getSuggest() {
        return suggest;
    }

    public void setSuggest(List<HouseSuggest> suggest) {
        this.suggest = suggest;
    }
}
