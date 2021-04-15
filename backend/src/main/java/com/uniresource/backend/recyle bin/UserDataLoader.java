/*package com.uniresource.backend.user;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.uniresource.backend.location.Repository.UniversityRepository;
import com.uniresource.backend.user.Entity.Gender;
import com.uniresource.backend.user.Entity.StudyYear;
import com.uniresource.backend.user.Entity.User;
import com.uniresource.backend.user.Repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("LocationDataLoader")
public class UserDataLoader {
    private static final Logger log = LoggerFactory.getLogger(UserDataLoader.class);

    @Bean("UserDataLoader")
    CommandLineRunner initUserDatabase(UserRepository userRepo, UniversityRepository uniRep) {
       
        return args -> {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = formatter.parse("1990-10-22");
            java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());

            log.info("Preloading "
                    + userRepo.save(new User("TeeKay", "teekay@gmail.com", "0815238379", "TeeKay12", sqlDate,
                            "Student at UWC", new File("C:\\Users\\lenovo\\Pictures\\hashcode2018.JPG").toURI().toURL(),
                            uniRep.getOne(1), Gender.MALE, StudyYear.YEAR3)));
            log.info("Preloading " + userRepo.save(new User("Kat", "kat@gmail.com", "0811237648", "Kat123", sqlDate,
                    "UWC Student", new File("C:\\Users\\lenovo\\Pictures\\hashcode2018.JPG").toURI().toURL(),
                    uniRep.getOne(1), Gender.MALE, StudyYear.Honors)));
            log.info("Preloading " + userRepo.save(new User("Don", "Don@gmail.com", "0823689573", "Don1234", sqlDate,
                    "UWC 3rd year Student", new File("C:\\Users\\lenovo\\Pictures\\hashcode2018.JPG").toURI().toURL(),
                    uniRep.getOne(1), Gender.MALE, StudyYear.YEAR3)));
            log.info("Preloading " + userRepo.save(new User("Sef", "sef@gmail.com", "0819372748", "Sef988", sqlDate,
                    "UWC Student", new File("C:\\Users\\lenovo\\Pictures\\hashcode2018.JPG").toURI().toURL(),
                    uniRep.getOne(1), Gender.MALE, StudyYear.Honors)));
            log.info("Preloading " + userRepo.save(new User("Erico", "erico@gmail.com", "0819372748", "Erico", sqlDate,
                    "UWC Student", new File("C:\\Users\\lenovo\\Pictures\\hashcode2018.JPG").toURI().toURL(),
                    uniRep.getOne(1), Gender.MALE, StudyYear.Honors)));
            log.info("Preloading " + userRepo.save(new User("Nkosi", "nkosi@gmail.com", "0814577545", "Nkosi", sqlDate,
                    "UWC Student", new File("C:\\Users\\lenovo\\Pictures\\hashcode2018.JPG").toURI().toURL(),
                    uniRep.getOne(1), Gender.MALE, StudyYear.YEAR3)));
            log.info("Preloading " + userRepo.save(new User("Alphanose", "alphanose@gmail.com", "0715864454", "alpha", sqlDate,
                    "Wits Student", new File("C:\\Users\\lenovo\\Pictures\\hashcode2018.JPG").toURI().toURL(),
                    uniRep.getOne(4), Gender.MALE, StudyYear.Honors)));

        };
    }
}*/