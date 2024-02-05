package org.ssafy.pasila.domain.order.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ssafy.pasila.domain.apihandler.ApiCommonResponse;
import org.ssafy.pasila.domain.order.dto.OrderDto;
import org.ssafy.pasila.domain.order.dto.OrderFormDto;
import org.ssafy.pasila.domain.order.entity.Status;
import org.ssafy.pasila.domain.order.service.OrderService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Save order", description = "주문을 등록한다.")
    @PostMapping
    public ApiCommonResponse<?> createOrder(@RequestBody OrderFormDto orderFormDto) {

        Long orderId = orderService.saveOrder(orderFormDto);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), orderId);

    }

    @Operation(summary = "Get Order List", description = "[구매자]주문 내역을 조회")
    @GetMapping //주문자 id
    public ApiCommonResponse<?> getOrderList(@RequestParam Long id) {

        List<OrderDto> orderList = orderService.getOrderList(id);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), orderList);

    }

    @Operation(summary = "Get Order List", description = "[구매자]주문 내역을 상세 조회")
    @GetMapping("{id}/detail")
    public ApiCommonResponse<?> getOrderDetail(@PathVariable Long id) {

        OrderDto order = orderService.getOrderDetail(id);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), order);

    }

    @Operation(summary = "CancelOrder", description = "[구매자] 주문 취소 버튼 클릭 시")
    @DeleteMapping("{id}/cancel")
    public ApiCommonResponse<?> cancelOrder(@PathVariable Long id) {

        Long cancelId = orderService.cancelOrder(id);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), cancelId);

    }

    @Operation(summary = "ChangeStatus", description = "[판매자] 주문 상태 변경")
    @PutMapping("{id}/status")
    public ApiCommonResponse<?> changeStatus(@PathVariable Long id, @RequestBody String status) {

        Long orderId = orderService.changeStatus(id, status);
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), orderId);

    }

    @Operation(summary = "GetStatusValues", description = "주문 상태 전체 보여주기")
    @GetMapping("/statusValues")
    public ApiCommonResponse<List<Map<String, String>>> getStatusValues() {

        List< Map<String, String>> list = new ArrayList<>();
        for (Status status : Status.values()) {
            Map<String, String> statusMap = new HashMap<>();
            statusMap.put("key", status.name());
            statusMap.put("desc", status.getDescription());
            list.add(statusMap);
        }
        return ApiCommonResponse.successResponse(HttpStatus.OK.value(), list);

    }

}