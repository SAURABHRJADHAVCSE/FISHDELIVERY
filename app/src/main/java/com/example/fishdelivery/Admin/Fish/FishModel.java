package com.example.fishdelivery.Admin.Fish;

public class FishModel {

    private String fishImageUrl;
    private String fishItemTextUrl;
    private String fishPriceTextUrl;

    public FishModel() {

    }

    public FishModel(String fishImageUrl, String fishItemTextUrl, String fishPriceTextUrl) {
        this.fishImageUrl = fishImageUrl;
        this.fishItemTextUrl = fishItemTextUrl;
        this.fishPriceTextUrl = fishPriceTextUrl;
    }

    public String getFishImageUrl() {
        return fishImageUrl;
    }

    public void setFishImageUrl(String fishImageUrl) {
        this.fishImageUrl = fishImageUrl;
    }

    public String getFishItemTextUrl() {
        return fishItemTextUrl;
    }

    public void setFishItemTextUrl(String fishItemTextUrl) {
        this.fishItemTextUrl = fishItemTextUrl;
    }

    public String getFishPriceTextUrl() {
        return fishPriceTextUrl;
    }

    public void setFishPriceTextUrl(String fishPriceTextUrl) {
        this.fishPriceTextUrl = fishPriceTextUrl;
    }
}
