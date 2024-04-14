

function addCartItem() {
    handleFormSubmission("Are you sure you want to Add this item to cart?", function(confirm) {
        if(confirm===true)
        {
            var form = document.getElementById('submitForm');
                form.setAttribute('action', '/cart/update-cart');
                form.submit();
        }
    });
}

function purchase() {
    handleFormSubmission("Are you sure you want to purchase this item?", function(confirm) {
        if(confirm===true)
        {
            var form = document.getElementById('submitForm');
            form.setAttribute('action', '/order/purchase');
            form.submit();
        }
    });
}


function decrementQuantity() {
        var quantityElement = document.getElementById('quantity');
        var originalProductPrice = parseFloat(document.getElementById('originalProductPrice').value);
        var originalDiscountPrice = parseFloat(document.getElementById('originalDiscountProductPrice').value);
        var quantity = parseInt(quantityElement.value);
        quantity--;
        if(quantity<1) quantity=1;

        quantityElement.value = quantity;

        if (isNaN(originalProductPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
            alert("Invalid input. Please check the values.");
            return;
        }

        var totalPrice = originalProductPrice * quantity;
        if (isNaN(totalPrice)) {
            alert("Invalid calculation. Please check the values.");
            return;
        }

        if (isNaN(originalDiscountPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
            alert("Invalid input. Please check the values.");
            return;
        }

        var totalDiscountPrice = originalDiscountPrice * quantity;
        if (isNaN(totalDiscountPrice)) {
            alert("Invalid calculation. Please check the values.");
            return;
        }
        if(quantity<1) quantity=1;
        document.getElementById('price').textContent = "Price: $" + totalPrice;
        document.getElementById('discountPrice').textContent = "Price: $" + totalDiscountPrice;

    }
    function incrementQuantity() {
        var quantityElement = document.getElementById('quantity');
        var originalProductPrice = parseFloat(document.getElementById('originalProductPrice').value);
        var originalDiscountPrice = parseFloat(document.getElementById('originalDiscountProductPrice').value);
        var quantity = parseInt(quantityElement.value);
        quantity++;
        if(quantity<1) quantity=1;

        quantityElement.value = quantity;

        if (isNaN(originalProductPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
            alert("Invalid input. Please check the values.");
            return;
        }

        var totalPrice = originalProductPrice * quantity;
        if (isNaN(totalPrice)) {
            alert("Invalid calculation. Please check the values.");
            return;
        }

        if (isNaN(originalDiscountPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
            alert("Invalid input. Please check the values.");
            return;
        }

        var totalDiscountPrice = originalDiscountPrice * quantity;
        if (isNaN(totalDiscountPrice)) {
            alert("Invalid calculation. Please check the values.");
            return;
        }
        if(quantity<1) quantity=1;
        document.getElementById('price').textContent = "Price: $" + totalPrice;
        document.getElementById('discountPrice').textContent = "Price: $" + totalDiscountPrice;

    }