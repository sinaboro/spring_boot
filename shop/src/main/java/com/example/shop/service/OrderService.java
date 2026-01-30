package com.example.shop.service;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.dto.OrderItemDto;
import com.example.shop.entity.*;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItemList = new ArrayList<>();

        //상품명, 수량 -> 새우깡 2개 구매
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String eamil, Pageable pageable){

        // order - 3 ->memberId, orderId
        List<Order> orders = orderRepository.findOrders(eamil, pageable);

        // count - 5
        Long totalCount = orderRepository.countOrder(eamil);

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        for(Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);

            List<OrderItem> orderItemLists = order.getOrderItems();

            for(OrderItem orderItem : orderItemLists){

                ItemImg itemImg = itemImgRepository
                        .findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");

                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());

                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtoList.add(orderHistDto);
        }

        return new PageImpl<>(orderHistDtoList, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        //로그인 유저
        Member curMember = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        //주문한 유저
        log.info("---------- Member saveMember = order.getMember();------------------");
        Member saveMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        order.cancelOrder();;
    }
}
