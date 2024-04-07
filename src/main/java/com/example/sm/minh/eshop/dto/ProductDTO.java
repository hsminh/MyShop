    package com.example.sm.minh.eshop.dto;

    import com.example.sm.minh.eshop.entities.Product;

    public class ProductDTO {
        private Product product;
        private Long quantiryPurchase;



        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Long getQuantiryPurchase() {
            return quantiryPurchase;
        }

        public void setQuantiryPurchase(Long quantiryPurchase) {
            this.quantiryPurchase = quantiryPurchase;
        }

        public ProductDTO(Product product, Long quantiryPurchase) {
            this.product = product;
            this.quantiryPurchase = quantiryPurchase;
        }
    }
