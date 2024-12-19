package store.aurora.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductInfoDTO {
    @NotNull
    private String productId;   //결제 상품 목록
    @NotNull
    private List<Integer> categoryIds;  //상품이 속해있는 카테고리 Id
    @NotNull
    private Integer bookId; //상품의 고유 ID
    @NotNull
    private Integer price; //상품의 가격
}