package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    @Schema(
            description = "List of products returned for the current page"
    )
    private List<ProductDTO> content;

    @Schema(
            description = "Current page number (zero-based index)",
            example = "0"
    )
    private Integer pageNumber;

    @Schema(
            description = "Number of products per page",
            example = "10"
    )
    private Integer pageSize;

    @Schema(
            description = "Total number of available products",
            example = "250"
    )
    private Long totalElements;

    @Schema(
            description = "Total number of pages available",
            example = "25"
    )
    private Integer totalPages;

    @Schema(
            description = "Indicates whether this is the last page of results",
            example = "false"
    )
    private boolean lastPage;
}
