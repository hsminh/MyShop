    package com.example.sm.minh.eshop.dto;

    import com.example.sm.minh.eshop.models.Product;
    import lombok.Getter;

    @Getter
    public class ProductDTO {
        private Product product;
        private Long quantiryPurchase;


        public void setProduct(Product product) {
            this.product = product;
        }

        public void setQuantiryPurchase(Long quantiryPurchase) {
            this.quantiryPurchase = quantiryPurchase;
        }

        public ProductDTO(Product product, Long quantiryPurchase) {
            this.product = product;
            this.quantiryPurchase = quantiryPurchase;
        }
    }
