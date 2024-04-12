    package com.example.sm.minh.eshop.dto;

    import com.example.sm.minh.eshop.models.Product;

    public class ProductDTO {
        private Product product;
        private Long qualityPurchase;



        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Long getQualityPurchase() {
            return qualityPurchase;
        }

        public void setQualityPurchase(Long qualityPurchase) {
            this.qualityPurchase = qualityPurchase;
        }

        public ProductDTO(Product product, Long quantiryPurchase) {
            this.product = product;
            this.qualityPurchase = quantiryPurchase;
        }
    }
