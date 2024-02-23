package tech.bread.solt.doctornyangserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class DoctorNyangServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctorNyangServerApplication.class, args);
    }

}
