package org.example.payment.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        // Load .env từ root project (2 levels up từ payment-service/src/main/java/...)
        File rootProjectDir = new File(System.getProperty("user.dir"));
        // Nếu đang chạy từ thư mục payment-service, đi lên 1 level
        if (rootProjectDir.getName().equals("payment-service")) {
            rootProjectDir = rootProjectDir.getParentFile();
        }
        
        File envFile = new File(rootProjectDir, ".env");
        
        Dotenv dotenv = Dotenv.configure()
                .directory(rootProjectDir.getAbsolutePath())
                .ignoreIfMissing() // nếu không có .env thì cũng không lỗi
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        System.out.println(">>> Loaded .env variables from: " + envFile.getAbsolutePath());
    }
}
