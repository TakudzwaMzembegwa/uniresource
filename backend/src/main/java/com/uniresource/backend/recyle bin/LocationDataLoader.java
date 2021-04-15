/*package com.uniresource.backend.location;

import com.uniresource.backend.location.Entity.Province;
import com.uniresource.backend.location.Entity.University;
import com.uniresource.backend.location.Entity.Country;
import com.uniresource.backend.location.Entity.Location;
import com.uniresource.backend.location.Repository.ProvinceRepository;
import com.uniresource.backend.location.Repository.CountryRepository;
import com.uniresource.backend.location.Repository.LocationRepository;
import com.uniresource.backend.location.Repository.UniversityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocationDataLoader {
    private static final Logger log = LoggerFactory.getLogger(LocationDataLoader.class);

    @Bean("LocationDataLoader")
    public CommandLineRunner initLocationDatabase(CountryRepository countryRepo, ProvinceRepository provinceRepo, UniversityRepository uniRepo, LocationRepository locationRepo){
        return args -> {
            Country zar = new Country("South Africa");
            log.info("Preloading " + countryRepo.save(zar));

            Province wCape = new Province("Western Cape", zar);
            log.info("Preloading " + provinceRepo.save(wCape));
            Province gauteng = new Province("Gauteng", zar);
            log.info("Preloading " + provinceRepo.save(gauteng));


            University uwc = new University("University of the Western Cape", "UWC", wCape);
            log.info("Preloading " + uniRepo.save(uwc));
            University uct = new University("University of Cape Town", "UCT", wCape);
            log.info("Preloading " + uniRepo.save(uct));
            University cput = new University("Cape Peninsula University of Technology", "CPUT", wCape);
            log.info("Preloading " + uniRepo.save(cput));
            University wits = new University("University of the Witwatersrand", "wits", gauteng);
            log.info("Preloading " + uniRepo.save(wits));

            log.info("Preloading " + locationRepo.save(new Location(zar, wCape, uwc)));
            log.info("Preloading " + locationRepo.save(new Location(zar, wCape, uct)));
            log.info("Preloading " + locationRepo.save(new Location(zar, wCape, cput)));
            log.info("Preloading " + locationRepo.save(new Location(zar, gauteng, wits)));


        };
    }
}
*/