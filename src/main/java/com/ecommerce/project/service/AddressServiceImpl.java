package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);

        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);


        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressesById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",id));
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressesByUser(User user) {
        List<Address> addresses = user.getAddresses();
        return addresses.stream()
                .map( address -> modelMapper.map(address,AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address addressFromDatabase = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("address","addressId",id));
        addressFromDatabase.setCity(addressDTO.getCity());
        addressFromDatabase.setZipCode(addressDTO.getPincode());
        addressFromDatabase.setCountry(addressDTO.getCountry());
        addressFromDatabase.setState(addressDTO.getState());
        addressFromDatabase.setStreet(addressDTO.getStreet());

        Address updatedAddress = addressRepository.save(addressFromDatabase);

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(id));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long id) {
        Address addressFromDatabase = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("address","addressId",id));

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(id));
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " +id;
    }
}
