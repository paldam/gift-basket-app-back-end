package com.damian.domain.basket;

import com.damian.domain.order.OrderItem;
import com.damian.domain.order.OrderPageRequest;
import com.damian.util.PdfBasketContents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class BasketService {

    private BasketDao basketDao;

    BasketService(BasketDao basketDao) {
        this.basketDao = basketDao;
    }

    public Basket addBasket(Basket basket) {
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        return basketDao.save(basket);
    }

    public Basket addBasketWithImg(Basket basket, MultipartFile[] basketMultipartFiles) {
        try {
            basket.setBasketImageData(basketMultipartFiles[0].getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        basket.setIsBasketImg(1);
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        return basketDao.save(basket);
    }

    public Basket editBasketWithoutImage(Basket basket) {
        Basket basketTmp = basketDao.findById(basket.getBasketId()).orElseThrow(EntityNotFoundException::new);
        basket.setBasketImageData(basketTmp.getBasketImageData());
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        return basketDao.save(basket);
    }

    private Integer computeTotalProductsPriceInBasket(Basket basket) {
        int totalProductsPriceInBasket = 0;
        for (BasketItems bi : basket.getBasketItems()) {
            totalProductsPriceInBasket += bi.getProduct().getPrice() * bi.getQuantity();
        }
        return totalProductsPriceInBasket;
    }

    public List<Basket> getBasketFilterMod(Integer priceMin, Integer priceMax, Boolean basketPrice, Optional<List<Integer>> productsSubTypes) {
        priceMax = priceMax * 100;
        priceMin = priceMin * 100;
        if (!productsSubTypes.isPresent()) {
            if (basketPrice) {
                return basketDao.findBasketsWithFilterWithoutTypes(priceMin, priceMax);
            } else {
                return basketDao.findBasketsWithFilterWithoutTypesByProductsPrice(priceMin, priceMax);
            }
        } else {
            if (basketPrice) {
                return basketDao.findBasketsWithFilter(priceMin, priceMax, productsSubTypes.get(), productsSubTypes.get().size());
            } else {
                return basketDao.findBasketsWithFilterByProductsPrice(priceMin, priceMax, productsSubTypes.get(), productsSubTypes.get().size());
            }
        }
    }

    public byte[] prepareBasketImage(Long basketId) {
        byte[] basketFile = basketDao.getBasketImageByBasketId(basketId);
        Optional<byte[]> imgOpt = Optional.ofNullable(basketFile);
        if (!imgOpt.isPresent()) {
            basketFile = new byte[0];
        }
        return basketFile;
    }

    public ByteArrayInputStream prepareBasketProductsListPdf(Long id) throws IOException {
        byte[] img = basketDao.getBasketImageByBasketId(id);
        Optional<byte[]> imgOpt = Optional.ofNullable(img);
        if (!imgOpt.isPresent()) {
            img = new byte[0];
        }
        Optional<Basket> basketToGen = basketDao.findById(id);
        Basket basketToGenerate = new Basket();
        if (basketToGen.isPresent()) {
            basketToGenerate = basketToGen.get();
        }
        return PdfBasketContents.generateBasketProductsListPdf(basketToGenerate, img);
    }

    @Transactional
    public void addBasketToStock(List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> {
            basketDao.addBasketToStock(orderItem.getBasket().getBasketId(), orderItem.getQuantity());
        });
    }

    public BasketPageRequest getBasketsPege(int page, int size, String text, String orderBy, int sortingDirection, boolean onlyArchival, List<Integer> basketSeasonFilter) {
        Sort.Direction sortDirection = sortingDirection == -1 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Basket> basketPage;
        //        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));
        if (!onlyArchival) {
            if (basketSeasonFilter.size() == 0) {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getWithoutDeleted()), pageable);
            } else {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getOrderWithSeasons(basketSeasonFilter).and(BasketSpecyficationJpa.getWithoutDeleted())), pageable);
            }
        } else {
            if (basketSeasonFilter.isEmpty()) {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getOnlyDeleted()), pageable);
            } else {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getOnlyDeleted()).and(BasketSpecyficationJpa.getOrderWithSeasons(basketSeasonFilter)), pageable);
            }
        }
        return new BasketPageRequest(basketPage.getContent(), basketPage.getTotalElements());
    }


}
