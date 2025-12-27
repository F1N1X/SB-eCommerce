package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    @Schema(
            description = "List of categories returned for the current page"
    )
    private List<CategoryDTO> content;

    @Schema(
            description = "Current page number (zero-based index)",
            example = "0"
    )
    private Integer pageNumber;

    @Schema(
            description = "Number of items per page",
            example = "10"
    )
    private Integer pageSize;

    @Schema(
            description = "Total number of available categories",
            example = "125"
    )
    private Long totalElements;

    @Schema(
            description = "Total number of pages available",
            example = "13"
    )
    private Integer totalPages;

    @Schema(
            description = "Indicates whether this is the last page of results",
            example = "false"
    )
    private boolean lastPage;
}
