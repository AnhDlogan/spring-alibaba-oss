package xyz.dassiorleando.springalibabaoss.model;

public class MetadataInfo {
    String bucketName;
    String objectName;
    String filePath;
    String liveChannelName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLiveChannelName() {
        return liveChannelName;
    }

    public void setLiveChannelName(String liveChannelName) {
        this.liveChannelName = liveChannelName;
    }
}
