function paymentCart(productId) {
    var quantity = parseInt($('#quantityInput' + productId).val())  ;
    var confirmation = confirm("Are you sure you want to proceed with the payment?");
    if(confirmation)
    {
        $.ajax({
            type: 'GET',
            url: '/order',
            data: {
                cartLineItemId: productId,
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

/*<![CDATA[*/
function decrementQuantity(productId) {
    var quantityElement = $('#quantityInput' + productId);
    var quantity = parseInt(quantityElement.val());
    quantity--;
    if (quantity < 1) quantity = 1;
    quantityElement.val(quantity);
    updatePriceDetails(productId, quantity);
}

function incrementQuantity(productId) {
    var quantityElement = $('#quantityInput' + productId);
    var quantity = parseInt(quantityElement.val());
    quantity++;
    quantityElement.val(quantity);
    updatePriceDetails(productId, quantity);
}

function updatePriceDetails(productId, quantity) {
    var originalProductPrice = parseFloat($('#originalProductPrice' + productId).val());
    var originalDiscountPrice = parseFloat($('#originalDiscountProductPrice' + productId).val());
    var taxPercentage = parseFloat($('#taxPrice' + productId).text().replace('Tax: ', '').replace('%', ''));

    var totalPrice = originalProductPrice * quantity;
    var totalDiscountPrice = originalDiscountPrice * quantity;
    var totalTax = (totalPrice - totalDiscountPrice) * (taxPercentage / 100);

    $('#price' + productId).text("Price: $" + totalPrice.toFixed(2));
    $('#discountPrice' + productId).text("Discount Price: $" + totalDiscountPrice.toFixed(2));
    $('#taxPrice' + productId).text("Tax: " + taxPercentage + "% ($" + totalTax.toFixed(2) + ")");
}
/*]]>*/

/*<![CDATA[*/
$(document).ready(function() {
    // Tính toán giá ban đầu khi tải trang
    $('.product').each(function() {
        var productId = $(this).attr('data-productId');
        var quantity = parseInt($('#quantityInput' + productId).val());
        updatePriceDetails(productId, quantity);
    });
});
