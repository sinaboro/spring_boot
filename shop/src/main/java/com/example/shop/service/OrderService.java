package com.example.shop.service;

import com.example.shop.dto.OrderDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.Member;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
