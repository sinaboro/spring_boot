package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.MemberFormDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.Member;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(1000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){

        Order order = new Order();

        for(int i=0; i<3; i++){
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order);
        em.clear();


        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(()-> new EntityNotFoundException());

        assertEquals(3, savedOrder.getOrderItems().size());
    }

    /*
     @Query(
            "select o from Order o where o.member.email = :email " +
                    "order by o.orderDate desc "
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
     */

    @Test
    @DisplayName("회원 이메일로 주문 조회 및 페이징 처리")
    public void findOrderByEmailWithPaging(){
        //given
        String email  = "bbb2@bbb.com";
        Pageable pageable = PageRequest.of(0,2);

        //when
        List<Order> orders = orderRepository.findOrders(email, pageable);

        //then
        assertEquals(2, orders.size());
        orders.forEach(o -> log.info(o));
    }

}