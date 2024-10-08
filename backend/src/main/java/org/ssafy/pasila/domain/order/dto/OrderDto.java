package org.ssafy.pasila.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ssafy.pasila.domain.order.entity.Order;
import org.ssafy.pasila.domain.order.entity.Status;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    @Schema(description = "주문 id")
    private Long id;

    @Schema(description = "주문 수량")
    private Integer orderCnt;

    @Schema(description = "수령자 이름")
    private String name;

    @Schema(description = "구매자 이름")
    private String buyerName;

    @Schema(description = "주문지")
    private String address;

    @Schema(description = "주문 가격")
    private Integer price;

    @Schema(description = "주문 일시")
    private LocalDateTime createdAt;

    @Schema(description = "주문 상태")
    private Status status;

    @Schema(description = "주문 옵션 id")
    private Long productOptionId;

    @Schema(description = "주문 옵션 이름")
    private String productOptionName;

    @Schema(description = "제품 id")
    private String productId;

    @Schema(description = "제품 명")
    private String productName;

    @Schema(description = "대표 사진")
    private String productUrl;

    @Schema(description = "판매자")
    private Long sellerId;

    public OrderDto(Order order) {

        this.id = order.getId();
        this.orderCnt = order.getOrderCnt();
        this.name = order.getName();
        this.buyerName = order.getMember().getName();
        this.address = order.getAddress();
        this.price = order.getPrice();
        this.createdAt = order.getCreatedAt();
        this.status = order.getStatus();
        this.productOptionId = order.getProductOption().getId();
        this.productOptionName = order.getProductOption().getName();
        this.productId = order.getProductOption().getProduct().getId();
        this.productName = order.getProductOption().getProduct().getName();
        this.productUrl = order.getProductOption().getProduct().getThumbnail();
        this.sellerId = order.getProductOption().getProduct().getMember().getId();

    }

}
