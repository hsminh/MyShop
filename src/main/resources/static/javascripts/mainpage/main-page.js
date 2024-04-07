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