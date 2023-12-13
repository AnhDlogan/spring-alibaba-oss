package xyz.dassiorleando.springalibabaoss.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.dassiorleando.springalibabaoss.model.MetadataInfo;
import xyz.dassiorleando.springalibabaoss.service.OssService;

/**
 * OSS Controller
 * @author dassiorleando
 */
@RestController
public class OssController {

    @Autowired
    private OssService ossService;

    @GetMapping("/api/tokens/")
    public String getTokenAccess() {
        return ossService.genToken();
    }

    @PostMapping("/api/bucket")
    public void createBucket(@RequestBody MetadataInfo bucket) {ossService.createBucket(bucket);}

    @DeleteMapping("/api/bucket")
    public void deleteBucket(@RequestBody MetadataInfo bucket) {ossService.deleteBucket(bucket);}

    @PostMapping("/api/get-bucket-info")
    public com.aliyun.oss.model.BucketInfo getBucketInfo(@RequestBody MetadataInfo bucket) {return ossService.getBucketInfo(bucket);}

    @PostMapping("/api/upload-video")
    public void uploadVideo(@RequestBody MetadataInfo metadataInfo) {ossService.uploadVideo(metadataInfo);}

    @PostMapping("/api/download-video")
    public void downloadVideo(@RequestBody MetadataInfo metadataInfo) {ossService.downloadVideo(metadataInfo);}


}
