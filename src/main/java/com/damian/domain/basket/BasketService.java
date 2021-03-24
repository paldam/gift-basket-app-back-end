package com.damian.domain.basket;

import com.damian.domain.order.OrderItem;
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


@Service
public class BasketService {

    private  BasketDao basketDao;
    private BasketImageDao basketImageDao;

    BasketService(BasketImageDao basketImageDao,BasketDao basketDao) {
        this.basketImageDao = basketImageDao;
        this.basketDao = basketDao;
    }

    public Basket addBasket(Basket basket) {
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        return basketDao.save(basket);
    }

    @Transactional
    public Basket addBasketWithImg(Basket basket, MultipartFile[] basketMultipartFiles) {
        try {

            BasketImage savedImage =  basketImageDao.save(new BasketImage(basketMultipartFiles[0].getBytes()));
            basket.setBasketImage(savedImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        basket.setIsBasketImg(1);
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        return basketDao.save(basket);
    }

    @Transactional
    public Basket editBasketWithoutImage(Basket basket) {
        Basket basketTmp =
            basketDao.findById(basket.getBasketId())
                .orElseThrow(EntityNotFoundException::new);
        basket.setBasketImage(basketTmp.getBasketImage());
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        return basketDao.save(basket);
    }

    private Integer computeTotalProductsPriceInBasket(Basket basket) {
        int totalProductsPriceInBasket = 0;

        if(basket.getBasketItems() != null) {
            for (BasketItems bi : basket.getBasketItems()) {
                totalProductsPriceInBasket += bi.getProduct().getPrice() * bi.getQuantity();
            }
        }
        return totalProductsPriceInBasket;
    }

    public List<Basket> getBasketFilterMod(Integer priceMin, Integer priceMax, Boolean basketPrice,
                                           Optional<List<Integer>> productsSubTypes) {
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
                return basketDao.findBasketsWithFilter(priceMin, priceMax, productsSubTypes.get(),
                    productsSubTypes.get().size());
            } else {
                return basketDao.findBasketsWithFilterByProductsPrice(priceMin, priceMax, productsSubTypes.get(),
                    productsSubTypes.get().size());
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

    @Transactional
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
    public ByteArrayInputStream prepareBasketProductsListCatalogNameVersionPdf(Long id) throws IOException {
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
        return PdfBasketContents.generateBasketProductsListCatalogNameVersionPdf(basketToGenerate, img);
    }

    @Transactional
    public void addBasketToStock(List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> basketDao.addBasketToStock(
            orderItem.getBasket().getBasketId(), orderItem.getQuantity()));
    }



    public BasketPageRequest getBasketsPage(int page, int size, String text, String orderBy, int sortingDirection,
                                            boolean onlyArchival, List<Integer> basketSeasonFilter) {
        Sort.Direction sortDirection = sortingDirection == -1 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Basket> basketPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));


                basketPage = basketDao
                    .findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text)
                        .and(BasketSpecyficationJpa.getOrderWithSeasons(basketSeasonFilter)
                            .and(BasketSpecyficationJpa.getWithoutDeleted(onlyArchival))), pageable);



        return new BasketPageRequest(basketPage.getContent(), basketPage.getTotalElements());
    }
}
