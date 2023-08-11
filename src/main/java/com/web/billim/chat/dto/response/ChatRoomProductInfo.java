package com.web.billim.chat.dto.response;

import com.web.billim.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomProductInfo {
    private String productName;
    private long price;
    private String productImageUrl;

    public static ChatRoomProductInfo from(ChatRoom chatRoom) {
        var product = chatRoom.getProduct();
        return ChatRoomProductInfo.builder()
                .productName(product.getProductName())
                .price(product.getPrice())
                .productImageUrl(product.mainImage())
                .build();
    }
}
