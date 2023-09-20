package com.web.billim.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketConnectRequest {
    private long chatRoomId;
    private long connectedMemberId;
    private boolean isConnected;
}
