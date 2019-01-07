package com.worldretroday;

class WRDEntry {
    private String id;
    private String url;
    private String moderators;
    private String title;
    private String utcOffset;
    private String timezone;
    private String city;
    private String country;
    private String latitude;
    private String longitude;

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getUtcOffset() {
        return utcOffset;
    }

    void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    String getTimezone() {
        return timezone;
    }

    void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }

    String getCountry() {
        return country;
    }

    void setCountry(String country) {
        this.country = country;
    }

    String getLatitude() {
        return latitude;
    }

    void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    String getLongitude() {
        return longitude;
    }

    void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String getModerators() {
        return moderators;
    }

    void setModerators(String moderators) {
        this.moderators = moderators;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

}
