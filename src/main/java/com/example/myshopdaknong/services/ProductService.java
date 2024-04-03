package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.*;
import com.example.myshopdaknong.exception.CategoryProductException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.repository.*;
import com.example.myshopdaknong.util.FileUploadUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {
    private static final Integer INT_PAGE_SIZE=10;
    @Autowired
    private ProductRepository producsRepository;
    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private CartReposttory cartReposttory;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    public List<Product> findAll(Integer id, String search,Boolean isHide)
    {
        if(id!=null&&search!=null)
        {
            return this.producsRepository.findAll(id,search,isHide);
        }else if(id!=null)
        {
            return this.producsRepository.findAll(id,isHide);
        }else if(search!=null&&!search.trim().isEmpty())
        {
            return this.producsRepository.findAll(search,isHide);

        }
        return this.producsRepository.findAll(isHide);
    }

    public List<ProductCategory>findAllCategory()
    {
        return this.productCategoryRepository.findAll(true);
    }

    public List<ProductCategory>findAllCategoryContainProduct()
    {
        return this.productCategoryRepository.findAllCategoriesWithProducts();
    }
    public ProductCategory getCategoryById(Integer id) throws CategoryProductException {
        Optional<ProductCategory> productCategory= this.productCategoryRepository.findById(id);
        if(productCategory.isPresent())
        {
            return  productCategory.get();
        }
        throw new CategoryProductException("Cannot Find Category With ID :"+id);
    }

    public Product save(Product product) {
        return this.producsRepository.save(product);
    }

    public void delete(Integer id) throws ProductException {
        Optional<Product> productOption=this.findByid(id);
        if(productOption.isPresent())
        {
            Product product=productOption.get();
            product.setIsActive(false);
            this.producsRepository.save(product);
        }else {
            throw new ProductException("Cannot Found Product With Id : "+id);
        }
    }

    public void restoreProduct(Integer id) throws ProductException {
        Optional<Product> productOption=this.producsRepository.findProductByIsActiveIsFalse(id);
        if(productOption.isPresent())
        {
            Product product=productOption.get();
            this.setCartLineItemAndCartToDelete(product);
            product.setIsActive(true);
            this.producsRepository.save(productOption.get());
        }else {
            throw new ProductException("Cannot Found Product With Id : "+id);
        }
    }
    public void setCartLineItemAndCartToDelete(Product product)
    {
        for(CartLineItem cartLineItem : this.cartLineItemRepositoty.findByProductId(product))
        {
            Cart setCart=cartLineItem.getCartId();
            setCart.setTax_amount(setCart.getTax_amount()-cartLineItem.getTaxTotalAmount());
            setCart.setCount_items(setCart.getCount_items()-cartLineItem.getQuantity());
            setCart.setTotal_amount(setCart.getTotal_amount()-cartLineItem.getTotalAmount());
            setCart.setCreatedAt(new Date());
            cartLineItem.setCartId(null);
            this.cartLineItemRepositoty.save(cartLineItem);
            this.cartReposttory.save(setCart);
            this.cartLineItemRepositoty.delete(cartLineItem);
        }
    }
    public Optional<Product> findByid(Integer id) throws ProductException {
        Optional<Product> productOption=this.producsRepository.findById(id);
        if(productOption.isPresent())
        {
            return this.producsRepository.findById(id);
        }else {
            throw new ProductException("Cannot Found Products With Id : "+id);
        }
    }

    public String checkNameAndSkuUnique(String name, String sku, Integer id) {
        if (id == null || id == 0) {
            List<Product> checkProductsByNameOrSku = producsRepository.findProductByNameOrSku(name, sku);
            if (!checkProductsByNameOrSku.isEmpty()) {
                return "duplicated";
            }
        } else {
            Product existingProduct = producsRepository.findById(id).orElse(null);
            if (existingProduct == null) {
                // Handle case where product with given id does not exist
                return "not_found";
            }

            Product checkProductByName = producsRepository.findProductByName(name);
            Product checkProductBySku = producsRepository.findProductBySku(sku);

            // Check if the new name or sku conflicts with existing products
            if ((checkProductBySku != null && !existingProduct.getSku().equals(sku)) ||
                    (checkProductByName != null && !existingProduct.getName().equals(name))) {
                return "duplicated";
            }
        }
        return "ok";
    }

    public ArrayList<ProductDTO> productOrderMost() {
        Pageable pageable = PageRequest.of(0, INT_PAGE_SIZE);
        List<Object[]> listProductOrderMost = this.orderLineItemRepository.findProductsOrderedMost(pageable);
        ArrayList<ProductDTO>productDTOS=new ArrayList<>();

        for (Object[] obj : listProductOrderMost) {

            Product product = (Product) obj[0];
            Long quantityPurchase=(Long) obj[1];
            ProductDTO productDTO=new ProductDTO(product,quantityPurchase);
            productDTOS.add(productDTO);

        }
        return productDTOS;
    }

//    public List<Product> productOrderMost() {
//        Pageable pageable = PageRequest.of(0, INT_PAGE_SIZE);
//        List<Product> listProductOrderMost = this.orderLineItemRepository.findProductsOrderedMost(pageable);
//
//        return listProductOrderMost;
//    }

    public Product setData(Product product,Product productInForm) throws IOException {
        product.setUpdatedAt(new Date());
        product.setContent(productInForm.getContent());
        product.setSku(productInForm.getSku());
        product.setListProductCategories(productInForm.getListProductCategories());
        product.setPrice(productInForm.getPrice());
        product.setName(productInForm.getName());
        product.setDiscount_price(productInForm.getDiscount_price());
        product.setTax(productInForm.getTax());
        product.setIsActive(productInForm.getIsActive());
        return product;
    }

    public Product setImage(Product product,MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            product.setImage(fileName);
            this.save(product);

            String directory = "public/images/" + product.getId();
            FileUploadUltil.saveFile(directory, fileName, multipartFile, null);
        }else
        {
            this.save(product);
        }
        return product;
    }
}
