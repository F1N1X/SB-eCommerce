package com.ecommerce.project.controller;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Addresses",
        description = "Endpoints for creating, retrieving, updating, and deleting customer addresses"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @Operation(
            summary = "Create an address",
            description = "Creates a new address for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data")
    })
    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(
            @Valid @RequestBody AddressDTO addressDTO) {

        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all addresses",
            description = "Returns a list of all addresses"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    })
    @GetMapping("/address")
    public ResponseEntity<List<AddressDTO>> getAddresses() {

        List<AddressDTO> addresses = addressService.getAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @Operation(
            summary = "Get address by ID",
            description = "Returns a single address identified by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping("/address/{id}")
    public ResponseEntity<AddressDTO> getAddressById(
            @Parameter(description = "Unique identifier of the address", example = "12")
            @PathVariable Long id) {

        AddressDTO addressDTO = addressService.getAddressesById(id);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get current user's addresses",
            description = "Returns all addresses associated with the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User addresses retrieved successfully")
    })
    @GetMapping("/user/address")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {

        User user = authUtil.loggedInUser();
        List<AddressDTO> addresses = addressService.getAddressesByUser(user);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @Operation(
            summary = "Update an address",
            description = "Updates an existing address identified by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping("/address/{id}")
    public ResponseEntity<AddressDTO> updateAddress(
            @Parameter(description = "Unique identifier of the address to update", example = "12")
            @PathVariable Long id,
            @Valid @RequestBody AddressDTO addressDTO) {

        AddressDTO updatedAddress = addressService.updateAddress(id, addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an address",
            description = "Deletes an existing address identified by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddress(
            @Parameter(description = "Unique identifier of the address to delete", example = "12")
            @PathVariable Long id) {

        String status = addressService.deleteAddress(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
