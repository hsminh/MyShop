function paymentCart(productId) {
    let quantity = parseInt($('#quantityInput' + productId).val());
    let confirmation = confirm("Are you sure you want to proceed with the payment?");
    alert(quantity)
    if (confirmation) {
        $.ajax({
            type: 'GET',
            url: '/order',
            data: {
                cartLineItemId: productId,
                quantity: quantity
            },
            success: function(response) {
                window.location.href = '/order/success';
            },
            error: function(xhr, status, error) {
                alert("Error updating quantity");
                console.error('Error updating quantity:', error);
            }
        });
    }
}
function updateHiddenQuantity(productId, value) {
    $('#quantityInputHidden' + productId).val(value);
}
function clearAll() {
    let confirmation = confirm("Are you sure you want to Clear All Cart?");
    if (confirmation) {
        $.ajax({
            type: 'GET',
            url: '/cart/clear',
            success: function(response) {
                window.location.href = '/cart/shopping-cart';
            },
            error: function(xhr, status, error) {
                alert("Error updating quantity");
                console.error('Error clearing cart:', error);
            }
        });
    }
}


    function updateHiddenQuantity(productId, value) {
        $('#quantityInputHidden' + productId).val(value);
    }

function decrementQuantity(productId) {
    let quantityElement = $('#quantityInput' + productId);
    let quantity = parseInt(quantityElement.val());
    quantity--;
    if (quantity < 1) quantity = 1;
    quantityElement.val(quantity);
    updatePriceDetails(productId, quantity);
}

function incrementQuantity(productId) {
    let quantityElement = $('#quantityInput' + productId);
    let quantity = parseInt(quantityElement.val());
    quantity++;
    quantityElement.val(quantity);
    updatePriceDetails(productId, quantity);
}

function updatePriceDetails(productId, quantity) {
    let originalDiscountPrice = parseFloat($('#originalDiscountProductPrice' + productId).val());
    let taxPercentage = parseFloat($('#taxPrice' + productId).val());
    let subTotal = parseFloat($('#subTotal').text().replace('$', ''));
    let taxTotal = parseFloat($('#taxTotal').text().replace('$', ''));
    let totalAmount = parseFloat($('#totalAmount').text().replace('$', ''));
    let quantityInputHidden = parseFloat($('#quantityInputHidden' + productId).val());
    let totalDiscountPrice = originalDiscountPrice * quantity;
    taxPercentage=(originalDiscountPrice*taxPercentage)/100;

    if (quantityInputHidden < quantity) {
        subTotal+=originalDiscountPrice;
        taxTotal+=taxPercentage;
        totalAmount+=(originalDiscountPrice+taxPercentage)
    }else
    {
        subTotal-=originalDiscountPrice;
        taxTotal-=taxPercentage;
        totalAmount-=(originalDiscountPrice+taxPercentage)
    }
    updateHiddenQuantity(productId, quantity);
    $('#discountPrice' + productId).text("$" + totalDiscountPrice.toFixed(2));
    $('#subTotal').text("$" + subTotal.toFixed(2));
    $('#taxTotal').text("$" + taxTotal.toFixed(2));
    $('#totalAmount').text("$" + totalAmount.toFixed(2));
}

$(document).ready(function() {
    $('.product').each(function() {
        let productId = $(this).attr('data-productId');
        let quantity = parseInt($('#quantityInput' + productId).val());
        updatePriceDetails(productId, quantity);
    });
});