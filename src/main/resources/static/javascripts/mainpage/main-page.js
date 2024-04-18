document.addEventListener("DOMContentLoaded", function() {
    var searchButton = document.getElementById('searchButton');
    var categoryId = document.getElementById('categoryId').value;
    searchButton.addEventListener('click', function() {
        var keyword = document.getElementById("default-search").value;
        window.location.href = "/main-page?category=" + categoryId + "&search=" + encodeURIComponent(keyword);
    });

    function saveSearchValue(value) {
        localStorage.setItem('previousSearch', value);
    }

    function restoreSearchValue() {
        var previousSearch = localStorage.getItem('previousSearch');
        if (previousSearch) {
            document.getElementById('default-search').value = previousSearch;
        }
    }

    var previousBtn = document.getElementById('previous-btn');
    var nextBtn = document.getElementById('next-btn');
    if (previousBtn && nextBtn) {
        var currentPage = 1;
        var productsPerPage = 5;
        var totalProducts = document.getElementsByClassName('product-item').length;
        var totalPages = Math.ceil(totalProducts / productsPerPage);

        previousBtn.addEventListener('click', function() {
            if (currentPage > 1) {
                currentPage--;
                showProducts();
            }
        });

        nextBtn.addEventListener('click', function() {
            if (currentPage < totalPages) {
                currentPage++;
                showProducts();
            }
        });

        function showProducts() {
            var products = document.getElementsByClassName('product-item');
            var start = (currentPage - 1) * productsPerPage;
            var end = Math.min(start + productsPerPage, totalProducts);

            for (var i = 0; i < products.length; i++) {
                if (i >= start && i < end) {
                    products[i].style.display = 'block';
                } else {
                    products[i].style.display = 'none';
                }
            }
        }
        showProducts();
        previousBtn.style.display = "inline-block";
        nextBtn.style.display = "inline-block";
    }


});

function choiceCategory(categoryId) {
    var keyWord = $('#search').val();
    var isCategory = $('#isCategory').val();

    $.ajax({
        type: "GET",
        url: "/products/load-product",
        data: {
            category: categoryId,
            search: keyWord,
            isChoiceCategory: isCategory
        },
        success: function(response) {
            var products = response;
            $('.delete-item').remove();
            var html = '';
            products.forEach(function(Product) {
                html += '<div class="delete-item w-full max-w-sm bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700">';
                html += '<a href="#">';
                html += '<img class="h-48 w-70 object-cover" src="/images/products/' + Product.id + '/' + Product.image + '" alt="product image" />';
                html += '</a>';
                html += '<div class="px-5 pb-5">';
                html += '<a href="#">';
                html += '<div>';
                html += '<h5 class="text-xl font-semibold tracking-tight text-gray-900 dark:text-white">' + Product.name + '</h5>';
                html += '</div>';
                html += '</a>';
                html += '<a href="#">';
                html += '<h5 class="text-sm tracking-tight text-gray-900 dark:text-white">' + Product.content + '</h5>';
                html += '</a>';
                html += '<div class="flex items-center mt-2.5 mb-5">';
                html += '<div class="flex items-center space-x-1 rtl:space-x-reverse">';
                html += '<svg class="w-4 h-4 text-yellow-300" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 22 20">';
                html += '<path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>';
                html += '</svg>';
                //Thêm các biểu tượng khác ở đây
                html += '</div>';
                html += '<span class="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded dark:bg-blue-200 dark:text-blue-800 ms-3">5.0</span>';
                html += '</div>';
                html += '<div class="flex items-center justify-start">';
                html += '<span class="text-xl font-bold  dark:text-white" style="color: #f97316">$' + Product.discountPrice + '</span>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
            });

            // Update the product list with the new HTML
            $('#all-item    ').html(html);
        },
        error: function() {
            alert('Yêu cầu AJAX không thành công.');
        }
    });
}
