package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @Schema(
            description = "Unique identifier of the address",
            example = "301"
    )
    private Long addressId;

    @Schema(
            description = "Street name of the address",
            example = "Main Street"
    )
    private String street;

    @Schema(
            description = "Building name or house number",
            example = "Apartment 12B"
    )
    private String buildingName;

    @Schema(
            description = "City where the address is located",
            example = "Berlin"
    )
    private String city;

    @Schema(
            description = "State or province of the address",
            example = "Berlin"
    )
    private String state;

    @Schema(
            description = "Country where the address is located",
            example = "Germany"
    )
    private String country;

    @Schema(
            description = "Postal or ZIP code of the address",
            example = "10115"
    )
    private String pincode;
}
