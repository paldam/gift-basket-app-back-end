package com.damian.domain.basket;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql({"/init_basket.sql"})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class BasketServiceTest {

//    @Autowired
//    private TestEntityManager entityManager;

    @Autowired
    private BasketDao basketDao;
    @Autowired
    private BasketTypeDao basketTypeDao;
    @Autowired
    private BasketSezonDao basketSezonDao;

    private static BasketService basketService;

    private Basket basket1;
    private Basket basket2;
    private Basket basket3;



    @BeforeEach
    void  initBasket(){
        basketService = new BasketService(basketDao);


    }


//    @AfterEach
//    void  clean() {
//
//        basketDao.delete(basket1);
//        basketDao.delete(basket2);
//        basketDao.delete(basket3);
//
//    }
    @Rollback(value = false)
    @Test
    void shouldReturnAllNotArchival() {
        //given
        basket1 = new Basket();
        basket1.setBasketName("testowy1");
        char da = 'a';
        Integer i = 2;
        Integer ii = 2;
        Integer a = i + ii;
        basket1.setBasketTotalPrice(1000);
        basket1.setBasketType(new BasketType(1,"ofertowy"));
        basket1.setBasketSezon(new BasketSezon(1,"brak"));
        basket1=basketDao.save(basket1);

        basket2 = new Basket();
        basket2.setBasketName("testowy2");
        basket2.setBasketTotalPrice(2000);
        basket2.setBasketType(new BasketType(1,"ofertowy"));
        basket2.setBasketSezon(new BasketSezon(2,"2018SW"));
        basket2=basketDao.save(basket2);

        basket3 = new Basket();
        basket3.setBasketName("testowy3");
        basket3.setBasketTotalPrice(4000);
        basket3.setBasketType(new BasketType(99,"usuniety"));
        basket3.setBasketSezon(new BasketSezon(3,"2019"));
        basket3=basketDao.save(basket3);

        List<Integer> seasons = Collections.emptyList();
        //List<Integer> seasons = Arrays.asList(1);
        BasketPageRequest basketPageRequest = basketService.getBasketsPage(0, 100, "", "basketName", -1, false,
            seasons);
        assertThat(basketPageRequest.getBasketsList().size(), equalTo(2));
        assertThat(basketPageRequest.getBasketsList().get(0).getBasketName(), equalTo("testowy1"));
        assertThat(basketPageRequest.getBasketsList().get(1).getBasketName(), equalTo("testowy2"));

         System.out.println(ANSI_YELLOW + "1111" + ANSI_RESET);






    }


    @Test
    void shouldReturnAllArchival() {



        basket1 = new Basket();
        basket1.setBasketName("testowy1");
        basket1.setBasketTotalPrice(1000);
        basket1.setBasketType(new BasketType(1,"ofertowy"));
        basket1.setBasketSezon(new BasketSezon(1,"brak"));
        basket1=basketDao.save(basket1);

        basket2 = new Basket();
        basket2.setBasketName("testowy2");
        basket2.setBasketTotalPrice(2000);
        basket2.setBasketType(new BasketType(1,"ofertowy"));
        basket2.setBasketSezon(new BasketSezon(2,"2018SW"));
        basket2=basketDao.save(basket2);

        basket3 = new Basket();
        basket3.setBasketName("testowy3");
        basket3.setBasketTotalPrice(4000);
        basket3.setBasketType(new BasketType(99,"usuniety"));
        basket3.setBasketSezon(new BasketSezon(3,"2019"));
        basket3=basketDao.save(basket3);


        List<Integer> seasons = Collections.emptyList();
        //List<Integer> seasons = Arrays.asList(1);
        BasketPageRequest basketPageRequest = basketService.getBasketsPage(0, 100, "", "basketName", -1, false,
            seasons);
        assertThat(basketPageRequest.getBasketsList().size(), equalTo(1));
        assertThat(basketPageRequest.getBasketsList().get(0).getBasketName(), equalTo("testowy3"));
        System.out.println(ANSI_YELLOW + "2222" + ANSI_RESET);
    }



}
