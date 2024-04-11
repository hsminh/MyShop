
function paymentCart(cartLineItemId)
{
    //get form
    var form = document.getElementById('submitForm');

    //get product and quantity
    var quantity = document.getElementById('quantityInputHidden'+cartLineItemId).value;
    quantity = parseInt(quantity);
    alert(quantity)
    alert(cartLineItemId)
    document.getElementById('cartLineItemId').setAttribute('value',cartLineItemId);
    document.getElementById('quantity').setAttribute('value',quantity);
    let confirmation = confirm("Are you sure you want to proceed with the payment?");
    if (confirmation) {
        form.setAttribute('action', '/cart/purchase-in-cart');
        form.submit();
    }
}



$('#addToCart').on('click', function() {
    $('#submitCheckout').attr('action', '/cart/checkout');
});

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

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("clearAllButton").addEventListener("click", function(event) {
        event.preventDefault();
        clearAll();
    });
});




function updateHiddenQuantity(productId, value) {
        $('#quantityInputHidden' + productId).val(value);
}

function decrementQuantity(productId) {
    let quantityElement = $('#quantityInput' + productId);
    let quantity = parseInt(quantityElement.val());
    quantity--;
    if (quantity < 1)
    {
        quantity = 1;
    }else
    {
        quantityElement.val(quantity);
        updatePriceDetails(productId, quantity);
    }
}

function incrementQuantity(productId) {
    let quantityElement = $('#quantityInput' + productId);
    let quantity = parseInt(quantityElement.val());
    quantity++;
    quantityElement.val(quantity);
    updatePriceDetails(productId, quantity);
}

// update label when click plus or minus
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

// Notice when check out
// Sự kiện khi submit form
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("submitForm").addEventListener("submit", function(event) {
        event.preventDefault();

        let confirmation = confirm("Are you sure you want to proceed with the payment?");

        if (confirmation) {
            this.submit();
        }
    });
});

