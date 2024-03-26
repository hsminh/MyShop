function paymentCart() {
    var quantity = parseInt($('#quantity').val());
    var productId = parseInt($('#selectProduct').val());


    alert(quantity)
    var confirmation = confirm("Are you sure you want to proceed with the payment?");
    if(confirmation)
    {
        $.ajax({
            type: 'GET',
            url: '/order/buy-direct',
            data: {
                productId: productId,
                quantity: quantity
            },
            success: function(response) {

                window.location.href = '/order/success'; // Chuyển hướng đến trang order_success.html

                // Handle success response, if needed
            },
            error: function(xhr, status, error) {
                alert("Error updating quantity")

                console.error('Error updating quantity:', error);
                // Handle error response, if needed
            }
        });
    }
}

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

    // Kiểm tra xem có phải là số hay không, nếu không phải số sẽ trả về NaN
    if (isNaN(originalProductPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
        alert("Invalid input. Please check the values.");
        return;
    }

    // Kiểm tra xem có giá trị `NaN` trong quá trình tính toán không
    var totalPrice = originalProductPrice * quantity;
    if (isNaN(totalPrice)) {
        alert("Invalid calculation. Please check the values.");
        return;
    }

    // Kiểm tra xem có phải là số hay không, nếu không phải số sẽ trả về NaN
    if (isNaN(originalDiscountPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
        alert("Invalid input. Please check the values.");
        return;
    }

    // Kiểm tra xem có giá trị `NaN` trong quá trình tính toán không
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

    // Kiểm tra xem có phải là số hay không, nếu không phải số sẽ trả về NaN
    if (isNaN(originalProductPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
        alert("Invalid input. Please check the values.");
        return;
    }

    // Kiểm tra xem có giá trị `NaN` trong quá trình tính toán không
    var totalPrice = originalProductPrice * quantity;
    if (isNaN(totalPrice)) {
        alert("Invalid calculation. Please check the values.");
        return;
    }

    // Kiểm tra xem có phải là số hay không, nếu không phải số sẽ trả về NaN
    if (isNaN(originalDiscountPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
        alert("Invalid input. Please check the values.");
        return;
    }

    // Kiểm tra xem có giá trị `NaN` trong quá trình tính toán không
    var totalDiscountPrice = originalDiscountPrice * quantity;
    if (isNaN(totalDiscountPrice)) {
        alert("Invalid calculation. Please check the values.");
        return;
    }
    if(quantity<1) quantity=1;
    document.getElementById('price').textContent = "Price: $" + totalPrice;
    document.getElementById('discountPrice').textContent = "Price: $" + totalDiscountPrice;

}