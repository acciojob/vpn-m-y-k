package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    public Admin register(String username, String password) {

        Admin admin = new Admin(username, password);
        return adminRepository1.save(admin);
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        //add a serviceProvider under the admin and return updated admin
        Admin admin = adminRepository1.findById(adminId).get();

        ServiceProvider serviceProvider = new ServiceProvider(providerName);
        serviceProvider.setAdmin(admin);

        admin.getServiceProviders().add(serviceProvider);
//        admin.setServiceProviders(admin.getServiceProviders());

        return adminRepository1.save(admin);
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        //add a country under the serviceProvider and return respective service provider
        //country name would be a 3-character string out of ind, aus, usa, chi, jpn. Each character can be in uppercase or lowercase. You should create a new Country object based on the given country name and add it to the country list of the service provider. Note that the user attribute of the country in this case would be null.
        //In case country name is not amongst the above mentioned strings, throw "Country not found" exception

        // check if country is valid
        String country_name = countryName.toUpperCase();
        if (!country_name.equals("IND") || !country_name.equals("AUS") || !country_name.equals("CHI") || !country_name.equals("JAP") || !country_name.equals("USA")) {
            throw new Exception("Country not found");
        }
        Country country = new Country(CountryName.valueOf(country_name), CountryName.valueOf(country_name).toCode());

        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        serviceProvider.getCountryList().add(country);
        country.setServiceProvider(serviceProvider);

        return serviceProviderRepository1.save(serviceProvider);
    }
}
