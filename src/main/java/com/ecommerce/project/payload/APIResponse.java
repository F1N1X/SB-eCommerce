package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {

    @Schema(
            description = "Message describing the result of the API operation",
            example = "Address deleted successfully"
    )
    private String message;

    @Schema(
            description = "Indicates whether the API operation was successful",
            example = "true"
    )
    private boolean status;
}
