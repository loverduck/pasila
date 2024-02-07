package org.ssafy.pasila.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.ssafy.pasila.domain.apihandler.ErrorCode;
import org.ssafy.pasila.domain.apihandler.NotEnoughStockException;
import org.ssafy.pasila.domain.product.dto.ProductOptionDto;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_option")
@ToString(exclude = "product")
public class ProductOption {

    @Id @GeneratedValue
    private Long id;

    @Column(length = 20)
    private String name;

    private Integer stock;

    private Integer price;

    @Column(name = "discount_price")
    private Integer discountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    //== 연관메서드==//
    public void updateProductOption(ProductOptionDto productOption){

        this.id = productOption.getId();
        this.name = productOption.getName();
        this.stock = productOption.getStock();
        this.price = productOption.getPrice();
        this.discountPrice = productOption.getDiscountPrice();

    }

    //== 재고 관련 메서드 ==//

    //재고 증가 메서드
    public void removeStock(int quantity){

        int restStock = this.stock - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException(ErrorCode.NOT_ENOUGH_STOCK);
        }
        this.stock = restStock;

    }

    public void addStock(int quantity){

        this.stock += quantity;


    }

    //== 생성 메서드 ==//
    public static void createProductOption(Product product, ProductOptionDto productOptionDto){

        ProductOption.builder()
                .name(productOptionDto.getName())
                .stock(productOptionDto.getStock())
                .price(productOptionDto.getPrice())
                .discountPrice(productOptionDto.getDiscountPrice())
                .product(product)
                .build();

    }

}