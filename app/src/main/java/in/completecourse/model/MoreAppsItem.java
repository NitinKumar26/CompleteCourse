package in.completecourse.model;

import androidx.annotation.Keep;

@Keep
public class MoreAppsItem {
    private String icon_url;
    private String app_name;
    private String app_desc;
    private String app_play_url;

    public String getApp_play_url() {
        return app_play_url;
    }

    public void setApp_play_url(String app_play_url) {
        this.app_play_url = app_play_url;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_desc() {
        return app_desc;
    }

    public void setApp_desc(String app_desc) {
        this.app_desc = app_desc;
    }
}
