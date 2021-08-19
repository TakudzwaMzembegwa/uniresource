package com.uniresource.backend;

import java.util.List;

import com.uniresource.backend.domain.entity.Category;
import com.uniresource.backend.domain.entity.Condition;
import com.uniresource.backend.domain.entity.Country;
import com.uniresource.backend.domain.entity.Gender;
import com.uniresource.backend.domain.entity.Location;
import com.uniresource.backend.domain.entity.Post;
import com.uniresource.backend.domain.entity.PostES;
import com.uniresource.backend.domain.entity.PostImage;
import com.uniresource.backend.domain.entity.PostStatus;
import com.uniresource.backend.domain.entity.Province;
import com.uniresource.backend.domain.entity.StudyYear;
import com.uniresource.backend.domain.entity.University;
import com.uniresource.backend.domain.entity.User;
import com.uniresource.backend.domain.mapper.CountryMapper;
import com.uniresource.backend.domain.mapper.LocationMapper;
import com.uniresource.backend.domain.mapper.ProvinceMapper;
import com.uniresource.backend.domain.mapper.UniversityMapper;
import com.uniresource.backend.domain.mapper.UserMapper;
import com.uniresource.backend.repository.CountryRepository;
import com.uniresource.backend.repository.LocationRepository;
import com.uniresource.backend.repository.PostImageRepository;
import com.uniresource.backend.repository.PostRepository;
import com.uniresource.backend.repository.PostElasticsearchRepository;
import com.uniresource.backend.repository.ProvinceRepository;
import com.uniresource.backend.repository.UniversityRepository;
import com.uniresource.backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class LoadDatabase {
        public static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

        public BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        @Autowired
        UserMapper mapper;
        @Autowired
        LocationMapper locationMapper;
        @Autowired
        CountryMapper countryMapper;
        @Autowired
        ProvinceMapper provinceMapper;
        @Autowired
        UniversityMapper universityMapper;

        @Bean
        public CommandLineRunner initDatabase( PostElasticsearchRepository postESRepo,CountryRepository countryRepo, ProvinceRepository provinceRepo,
                        UniversityRepository uniRepo, LocationRepository locationRepo, UserRepository userRepo,
                       PostRepository postRepo ,PostImageRepository postImageRepo) {
                return args -> {
                        // ----------------------Location------------------------
                        Country zar = new Country("South Africa");
                        log.info("\nPreloading " + countryRepo.save(zar));

                        Province wCape = new Province("Western Cape", zar);
                        log.info("\nPreloading " + provinceRepo.save(wCape));
                        Province gauteng = new Province("Gauteng", zar);
                        log.info("\nPreloading " + provinceRepo.save(gauteng));

                        University uwc = new University("University of the Western Cape", "UWC", wCape);
                        log.info("\nPreloading " + uniRepo.save(uwc));
                        University uct = new University("University of Cape Town", "UCT", wCape);
                        log.info("\nPreloading " + uniRepo.save(uct));
                        University cput = new University("Cape Peninsula University of Technology", "CPUT", wCape);
                        log.info("\nPreloading " + uniRepo.save(cput));
                        University wits = new University("University of the Witwatersrand", "wits", gauteng);
                        log.info("\nPreloading " + uniRepo.save(wits));

                        Location location1 = new Location(zar, wCape, uwc);
                        Location location2 = new Location(zar, gauteng, wits);
                        log.info("\nPreloading " + locationRepo.save(location1));
                        log.info("\nPreloading " + locationRepo.save(new Location(zar, wCape, uct)));
                        log.info("\nPreloading " + locationRepo.save(new Location(zar, wCape, cput)));
                        log.info("\nPreloading " + locationRepo.save(location2));

                        log.info("Preloading Location: Done");
                        // ------------------------------User---------------------------------------------
                        User taku = new User("TeeKay", "takkudzwa mzembegwa","teekay@gmail.com", "0815238379",
                        bCryptPasswordEncoder.encode("TeeKay12"), "Student at UWC", "hashcode2018.JPG", location1, Gender.MALE,
                        StudyYear.YEAR3);

                        log.info("\nPreloading " + userRepo.save(taku));
                                        
                        log.info("\nPreloading " + userRepo
                                        .save(new User("Kat","katlego", "kat@gmail.com", "0811237648", bCryptPasswordEncoder.encode("Kat123"), "UWC Student",
                                                        "hashcode2018.JPG", location1, Gender.MALE, StudyYear.Honors)));
                        log.info("\nPreloading " + userRepo.save(new User("Don", "tinotenda","Don@gmail.com", "0823689573",
                        bCryptPasswordEncoder.encode("Don1234"), "UWC 3rd year Student", "hashcode2018.JPG", location1, Gender.MALE,
                                        StudyYear.YEAR3)));
                        log.info("\nPreloading " + userRepo
                                        .save(new User("Sef", "sefane","sef@gmail.com", "0819372748", bCryptPasswordEncoder.encode("Sef988"), "UWC Student",
                                                        "hashcode2018.JPG", location1, Gender.MALE, StudyYear.Honors)));
                        log.info("\nPreloading " + userRepo
                                        .save(new User("Erico", "erico kayumba","erico@gmail.com", "0819372748", bCryptPasswordEncoder.encode("Erico"), "UWC Student",
                                                        "hashcode2018.JPG", location1, Gender.MALE, StudyYear.Honors)));
                        log.info("\nPreloading " + userRepo
                                        .save(new User("Nkosi", "nkosinati","nkosi@gmail.com", "0814577545", bCryptPasswordEncoder.encode("Nkosi"), "UWC Student",
                                                        "hashcode2018.JPG", location1, Gender.MALE, StudyYear.YEAR3)));
                        log.info("\nPreloading " + userRepo.save(new User("Alphanose", "alphanose mahachi","alphanose@gmail.com",
                                        "0715864454", bCryptPasswordEncoder.encode("alpha"), "Wits Student", "hashcode2018.JPG", location2, Gender.MALE,
                                        StudyYear.Honors)));

                        log.info("Preloading User: Done");
                        // ----------------------------------------------Post-------------------------------------------------------

                        Post post1 = new Post("STA112 course guide by author","first year stats",
                                                (float) 40.00, userRepo.findByEmail("teekay@gmail.com").orElse(new User()),
                                        location1, Category.BOOKS,
                                                Condition.GOOD, PostStatus.ACTIVE);
                        Post post2 =  new Post("STA112 course guid by Stewart", "first year and second year Calculus textbook",
                                (float) 799.99, userRepo.findByEmail("sef@gmail.com").orElse(new User()),
                                location1, Category.BOOKS,
                                Condition.MINT, PostStatus.ACTIVE);
                        Post post3 = new Post("STA112 course guides by me",
                                "advanced calculator for math, stats and anything numbers", (float) 115.00,
                                userRepo.findByEmail("teekay@gmail.com").orElse(new User()), location1, Category.BOOKS,
                                Condition.FAIR, PostStatus.ACTIVE);
                        
                        log.info("Preloading Post1: Done");
                        post1.setPostImages(List.of(new PostImage(
                                                 "hyena with heart.JPG",
                                                 post1), 
                                                 new PostImage(
                                                        "hashcode.JPG", post1)));
                        
                        log.info("Saving: " + postRepo.save(post1));
                        log.info("Preloading PostImages: Done");
                        //postESRepo.save(new PostES(post1));
                        log.info("Saved");
                        
                        //----------
                        postRepo.save(post2);
                        postRepo.save(post3);
                        //log.info("PostESRepo>>" + postESRepo.save(new PostES(postRepo.save(post2))));
                        //postESRepo.save(new PostES(postRepo.save(post3)));
                        log.info("Preloading Posts: Done");
                        // --------------------------------------------PostImage-------------------------------------------------------
                       
                        log.info("Preloading Post Images: Done");
                        log.info(userRepo.findByEmail("teekay@gmail.com").toString());
                        

                        //PostService postService = new PostService();
                        //postService.getPosts(new PostSearchRequest("STA112 course guide", locationMapper.toLocationDto(location1), Category.BOOKS.name()));
                       // log.info(postRepo.getsPosts("calculus").toString());
                        //log.info("-----------------------------------------\n" + countryMapper.toCountryDto(zar).toString());
                        //log.info("-----------------------------------------\n" + provinceMapper.toProvinceDto(wCape).toString());
                        //log.info("-----------------------------------------\n" + universityMapper.toUniversityDto(uwc).toString());
                        //log.info("-----------------------------------------\n" + locationMapper.toLocationDto(location1).toString());

                        //log.info("-----------------------------------------\n" +mapper.toUserDto(taku).toString());
                        /*List<Province> provinces = countryRepo.findById(1).orElse(new Country()).getProvinces();
                        for( Province province : provinces){
                                log.info(province.toString());
                        }*/
                };

        }
}
