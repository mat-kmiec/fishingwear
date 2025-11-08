package pl.fishingwear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FishingWearApplication {

    public static void main(String[] args) {
        SpringApplication.run(FishingWearApplication.class, args);
        System.out.println(new BCryptPasswordEncoder().encode("test123"));
    }

}
