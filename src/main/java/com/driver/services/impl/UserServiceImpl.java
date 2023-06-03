package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        //create a user of given country. The originalIp of the user should be "countryCode.userId" and return the user. Note that right now user is not connected and thus connected would be false and maskedIp would be null
        //Note that the userId is created automatically by the repository layer
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // check if country is valid
        String country_name = countryName.toUpperCase();
        if (!country_name.equals("IND") || !country_name.equals("AUS") || !country_name.equals("CHI") || !country_name.equals("JAP") || !country_name.equals("USA")) {
            throw new Exception("Country not found");
        }
        Country country = new Country(CountryName.valueOf(country_name), CountryName.valueOf(country_name).toCode());

        // set country to user
        country.setUser(user);
        user.setCountry(country);

        //save the originalIp in given format
        user.setOriginalIp(user.getCountry().getCode() + "." + user.getId());
        //save user
        return userRepository3.save(user);
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) throws Exception {
        //subscribe to the serviceProvider by adding it to the list of providers and return updated User

        Optional<User> optionalUser = userRepository3.findById(userId);
        if (optionalUser.equals(null)) {
            throw new Exception("User not found");
        }

        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository3.findById(serviceProviderId);
        if (optionalServiceProvider.equals(null)) {
            throw new Exception("Service provider not found");
        }

        // now the logic
        User user = optionalUser.get();
        ServiceProvider serviceProvider = optionalServiceProvider.get();

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        serviceProviderRepository3.save(serviceProvider);

        return user;
    }
}
