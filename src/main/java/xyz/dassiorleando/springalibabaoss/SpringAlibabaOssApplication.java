package xyz.dassiorleando.springalibabaoss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CannedAccessControlList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URISyntaxException;

/**
 * SpringAlibabaOss Application
 * @author dassiorleando
 */
@SpringBootApplication
public class SpringAlibabaOssApplication {
    public static void main(String[] args) throws URISyntaxException {
        SpringApplication.run(SpringAlibabaOssApplication.class, args);
    }

    @Bean
    public AppRunner appRunner() {
        return new AppRunner();
    }

    class AppRunner implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) {
        }
    }
}
