package org.ssafy.pasila.domain.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ssafy.pasila.domain.apihandler.ApiCommonResponse;
import org.ssafy.pasila.domain.apihandler.RestApiException;
import org.ssafy.pasila.domain.product.dto.ProductRequestDto;
import org.ssafy.pasila.domain.product.dto.ProductResponseDto;
import org.ssafy.pasila.domain.product.repository.*;
import org.ssafy.pasila.domain.product.service.ProductService;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
@Tag(name = "Product", description = "Product API")

public class ProductController {

    private final ProductJoinRepository productJoinRepository;

    private final ProductService productService;

    // Product 생성, 추후 @Valid 설정
    @Operation(summary = "Save product", description = "상품을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string")))
    })
    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiCommonResponse<?> createProduct(@RequestPart(value = "pr") ProductRequestDto productRequestDto,
                                           @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        String savaId = productService.saveProduct(productRequestDto, image);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), savaId);

    }

    // 모든 상품 조회 (카테고리 조인)
    @Operation(summary = "Get all product with category", description = "모든 상품을 조회한다.(카테고리까지 나옴)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class)))
                    })})
    @GetMapping("/product")
    public ApiCommonResponse<?> getAllProducts() {

        List<ProductResponseDto> list = productJoinRepository.findAllWithCategory();
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), list);

    }

    // id 에 따른 상품 조회 (카테고리 조인)
    @Operation(summary = "Get product", description = "상품을 조회한다(id)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    })
    @GetMapping("/product/{id}")
    public ApiCommonResponse<?> getProduct(@PathVariable("id") String id){
        ProductResponseDto response = productService.getProduct(id);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), response);

    }

    // 상품 정보 수정
    @Operation(summary = "Update product", description = "상품을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string")))
    })
    @PutMapping("/product/{id}")
    public  ApiCommonResponse<?> updateProduct(@PathVariable("id") String id,
                                                 @RequestPart(value = "pr") ProductRequestDto request,
                                                 @RequestPart(value = "new_image", required = false) MultipartFile newImageName) throws RestApiException, IOException {

        String updateId = productService.updateProduct(id, request, newImageName);
        return ApiCommonResponse.successResponse(HttpStatus.CREATED.value(), updateId);

    }
    //상품 정보 삭제 - isActive
    @Operation(summary = "Delete product", description = "상품을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string")))
    })
    @DeleteMapping("/product/{id}/delete")
    ApiCommonResponse<?> deleteProduct(@PathVariable("id") String id){

        String productId = productService.deleteProduct(id);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), productId);
    }

}