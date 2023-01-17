package models.admin;

public class HealthCheck {
    public String SiteName;
    public String CronJob;
    public String OrderExport;
    public String OrderImport;
    public String IndexManagement;
    public String OrderTrackingStatus;
    public int OrderTrackingPrevious;
    public int OrderTrackingCurrent;
    public int GoogleInsightWeb;
    public int GoogleInsightMobile;
    public String getSiteName() {
        return SiteName;
    }
    public void setSiteName(String siteName) {
        SiteName = siteName;
    }
    public String getCronJob() {
        return CronJob;
    }
    public void setCronJob(String cronJob) {
        CronJob = cronJob;
    }
    public String getOrderExport() {
        return OrderExport;
    }
    public void setOrderExport(String orderExport) {
        OrderExport = orderExport;
    }
    public String getOrderImport() {
        return OrderImport;
    }
    public void setOrderImport(String orderImport) {
        OrderImport = orderImport;
    }
    public String getIndexManagement() {
        return IndexManagement;
    }
    public void setIndexManagement(String indexManagement) {
        IndexManagement = indexManagement;
    }
    public String getOrderTrackingStatus() {
        return OrderTrackingStatus;
    }
    public void setOrderTrackingStatus(String orderTrackingStatus) {
        OrderTrackingStatus = orderTrackingStatus;
    }
    public int getOrderTrackingPrevious() {
        return OrderTrackingPrevious;
    }
    public void setOrderTrackingPrevious(int orderTrackingPrevious) {
        OrderTrackingPrevious = orderTrackingPrevious;
    }
    public int getOrderTrackingCurrent() {
        return OrderTrackingCurrent;
    }
    public void setOrderTrackingCurrent(int orderTrackingCurrent) {
        OrderTrackingCurrent = orderTrackingCurrent;
    }
    public int getGoogleInsightWeb() {
        return GoogleInsightWeb;
    }
    public void setGoogleInsightWeb(int googleInsightWeb) {
        GoogleInsightWeb = googleInsightWeb;
    }
    public int getGoogleInsightMobile() {
        return GoogleInsightMobile;
    }
    public void setGoogleInsightMobile(int googleInsightMobile) {
        GoogleInsightMobile = googleInsightMobile;
    }
}
