package com.uniresource.backend.service;
import java.util.List;

import javax.transaction.Transactional;

import com.uniresource.backend.domain.dto.CountrySelector;
import com.uniresource.backend.domain.entity.Country;
import com.uniresource.backend.domain.mapper.CountryMapper;
import com.uniresource.backend.repository.CountryRepository;
import com.uniresource.backend.repository.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    
    @Autowired
    public LocationRepository locationRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryMapper countryMapper;

    @Transactional
    public List<CountrySelector> selector(){
        List<Country> countries = countryRepository.findAll();
        countries.stream().forEach(c -> c.getProvinces().stream().forEach(p -> p.getUniversities()));
        return countryMapper.toCountrySelector(countries);
    }
}
