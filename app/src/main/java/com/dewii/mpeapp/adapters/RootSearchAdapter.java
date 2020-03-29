package com.dewii.mpeapp.adapters;

import com.dewii.mpeapp.utils.StringUtils;

public abstract class RootSearchAdapter extends RootAdapter {
    private boolean searching;
    private int searchType;
    private String searchText;

    public boolean isSearching() {
        return searching;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getSearchText() {
        return StringUtils.isInvalid(searchText) ? "" : searchText;
    }

    public boolean containsSearchText(String targetText) {
        return targetText.toLowerCase().contains(searchText.toLowerCase());
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        searching = !StringUtils.isInvalid(searchText);
    }
}
