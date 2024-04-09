$(document).ready(function() {
    $('#submitForm').on('submit', function(event) {
        var confirmation = confirm("Are you sure you want to buy?");
        if (!confirmation) {
            event.preventDefault();
        }
    });
});

$(document).ready(function() {

        $('#addToCart').on('click', function() {
            var quantity = document.getElementById('quantity').value;
            var productId = document.getElementById('selectProduct').value;
            $.ajax({
                url: "/cart/update-cart",
                method: "GET",
                data: {
                    selectProduct: productId,
                    quantity: quantity
                },
                success: function(data) {
                    alert(data);
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText);
                }
            });
        });
    });




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