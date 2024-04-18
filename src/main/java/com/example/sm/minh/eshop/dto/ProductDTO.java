    package com.example.sm.minh.eshop.dto;

    import com.example.sm.minh.eshop.models.Product;
    import lombok.Getter;

    @Getter
    public class ProductDTO {
        private Product product;
            private Long quantityProduct;


        public void setProduct(Product product) {
            this.product = product;
        }

        public void setQuantityProduct(Long quantityProduct) {
            this.quantityProduct = quantityProduct;
        }

        public ProductDTO(Product product, Long quantiryPurchase) {
            this.product = product;
            this.quantityProduct = quantiryPurchase;
        }
    }
