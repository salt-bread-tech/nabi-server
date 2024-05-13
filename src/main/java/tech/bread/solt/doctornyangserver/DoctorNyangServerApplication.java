package tech.bread.solt.doctornyangserver;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;


@EnableScheduling
@SpringBootApplication
public class DoctorNyangServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctorNyangServerApplication.class, args);
    }

    @PostConstruct
    public void setTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
