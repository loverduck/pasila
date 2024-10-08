package org.ssafy.pasila.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ssafy.pasila.domain.product.dto.ProductOptionFormDto;

import java.util.List;

/**주문이 들어올때 dto*/
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderFormDto {

    @Schema(description = " 선택한 옵션id")
    List<ProductOptionFormDto> options;

    @Schema(description = "구매자")
    Long memberId;

    @Schema(description = "수령자 이름")
    String name;

    @Schema(description = "배송지")
    String address;

}