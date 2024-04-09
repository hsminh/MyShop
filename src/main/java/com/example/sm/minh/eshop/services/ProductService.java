package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.dto.ProductDTO;
import com.example.sm.minh.eshop.models.*;
import com.example.sm.minh.eshop.exceptions.CategoryProductException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.repositories.*;
import com.example.sm.minh.eshop.utilities.FileUploadUltil;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {
    private static final Integer INT_PAGE_SIZE=8;
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
    public List<Product> findAll(Integer id, String search, Boolean isHide)
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
        Optional<ProductCategory> optionalProductCategory= this.productCategoryRepository.findById(id);
        return optionalProductCategory.orElseThrow(()->new CategoryProductException("Cannot Find Category With ID :"+id));
    }

    public Product save(Product product) {
        return this.producsRepository.save(product);
    }

    public void delete(Integer id) throws ProductException {
        Product deleteProduct = this.findById(id);
         // unlink Product from Product
         deleteCartLineItemsByProduct(deleteProduct);
         // Mark the product as deleted and inactive
        deleteProduct.setDeletedAt(new Date());
        deleteProduct.setIsActive(false);
        this.producsRepository.save(deleteProduct);

    }

    // Method to delete cart items related to a product
    private void deleteCartLineItemsByProduct(Product product) {
        for (CartLineItem cartLineItem : this.cartLineItemRepositoty.findByProductId(product)) {
            Cart cart = cartLineItem.getCartId();
            // Update cart information
            updateCartInfo(cart, cartLineItem);

            // Update and save cart information
            this.cartReposttory.save(cart);

            // Delete cart item
            this.cartLineItemRepositoty.delete(cartLineItem);
        }
    }


    // Method to update cart information after deleting a cart item related to a product
    private void updateCartInfo(Cart cart, CartLineItem cartLineItem) {
        cart.setTaxAmount(cart.getTaxAmount() - cartLineItem.getTaxTotalAmount());
        cart.setCountItem(cart.getCountItem() - cartLineItem.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount() - cartLineItem.getTotalAmount());
        cart.setCreatedAt(new Date());
        cartLineItem.setCartId(null);
    }

    public void restoreProduct(Integer id) throws ProductException {
        Optional<Product> productOption=this.producsRepository.findProductByIsActiveIsFalse(id);
        if(productOption.isPresent())
        {
            Product product=productOption.get();
            for(ProductCategory productCategory : product.getListProductCategories())
            {
                if(!productCategory.getIsActive())
                {
                    throw new ProductException("Cannot Restore "+productCategory.getName() + "Because it not exist");
                }
            }
            this.updateCartAndCAndCartLineItem(product);
            product.setIsActive(true);
            this.producsRepository.save(productOption.get());
        }else {
            throw new ProductException("Cannot Found Product With Id : "+id);
        }
    }
    public void updateCartAndCAndCartLineItem(Product product)
    {
        for(CartLineItem cartLineItem : this.cartLineItemRepositoty.findByProductId(product))
        {
            Cart updateCart=cartLineItem.getCartId();
            updateCart.setTaxAmount(updateCart.getTaxAmount()-cartLineItem.getTaxTotalAmount());
            updateCart.setCountItem(updateCart.getCountItem()-cartLineItem.getQuantity());
            updateCart.setTotalAmount(updateCart.getTotalAmount()-cartLineItem.getTotalAmount());
            updateCart.setUpdatedAt(new Date());
            cartLineItem.setCartId(null);
            this.cartLineItemRepositoty.save(cartLineItem);
            this.cartReposttory.save(updateCart);
            this.cartLineItemRepositoty.delete(cartLineItem);
        }
    }
    public Product findById(Integer id) throws ProductException {
            Optional<Product> productOption=this.producsRepository.findById(id);
            return productOption.orElseThrow(()->new ProductException("Cannot Found Products With Id : "+id));
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



    public Product setDataProduct(Product product, Product productInForm) throws IOException {
        product.setUpdatedAt(new Date());
        product.setContent(productInForm.getContent());
        product.setSku(productInForm.getSku());
        product.setListProductCategories(productInForm.getListProductCategories());
        product.setPrice(productInForm.getPrice());
        product.setName(productInForm.getName());
        product.setDiscountPrice(productInForm.getDiscountPrice());
        product.setTax(productInForm.getTax());
        product.setIsActive(productInForm.getIsActive());
        return product;
    }

    public Product saveImage(Product product, MultipartFile multipartFile) throws IOException {
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

    public void saveProduct(Product product, MultipartFile multipartFile) throws IOException, ProductException {
        if (product.getId() != null) {
            Product productInDb = this.findById(product.getId());
            productInDb=this.setDataProduct(productInDb,product);
            productInDb=this.saveImage(productInDb,multipartFile);
            this.saveImage(productInDb,multipartFile);
        } else {
            product.setCreatedAt(new Date());
            this.saveImage(product,multipartFile);
        }
    }
}
