document.addEventListener("DOMContentLoaded", function() {
    var searchButton = document.getElementById('searchButton');
    var categoryId = document.getElementById('categoryId').value;
    searchButton.addEventListener('click', function() {
        var keyword = document.getElementById("default-search").value;
        window.location.href = "/main-page?&search=" + encodeURIComponent(keyword);
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
        var keyWord = $('#default-search').val();
        $.ajax({
            type: "GET",
            url: "/products/load-product",
            data: {
                category: categoryId,
                search: keyWord,

            },
                success: function(response) {
                    $('.delete-item').remove();
                    var html = '';
                    response.forEach(function(ProductDTO) {
                                var name = ProductDTO.product.name.substring(0, 30);
                                var content = ProductDTO.product.content.substring(0, 33);
                                var price=ProductDTO.product.price||0  ;
                                var discountPrice = parseInt(ProductDTO.product.discountPrice) || 0;
                                var link='cart?productId='+ProductDTO.product.id;
                                var saleItem = formatNumber(ProductDTO.quantityProduct);
                                var saleTag = formatDiscount(((1 - (discountPrice / price)) * 100).toFixed(2));

                                if(parseFloat(saleTag)>=0)
                                {
                                    saleTag='<div class="absolute top-3 left-0 bg-green-400 py-2 px-4 rounded-md shadow-md flex items-center">\n' +
                                        '         <span>'+saleTag+'</span>\n' +
                                        '    </div>';
                                }else
                                {
                                    saleTag='';
                                }
                        html+=
                            ' <div class="rounded bg-gray-100 delete-item  hover:border-amber-500 flex flex-col justify-center items-center hover:shadow-md transition-all duration-500 border-r-[1px] border-stone-200 hover: transform transition duration-300 hover:scale-105    ">\n' +
                            '                    <a href="'+link+'">\n' +
                            '                        <div class="relative flex flex-col text-gray-700  w-96  ">\n' +
                            '                            <div class=" mx-4 mt-4  text-gray-700   h-96 ">\n' +
                            '                           <img src="/images/products/' + ProductDTO.product.id + '/' + ProductDTO.product.image + '" alt="card-image"\n' +
                            '                                     class="object-fill h-full w-full"/> '+saleTag+'\n' +

                            '                            </div>\n' +
                            '                            <div class="flex items-center  flex-col relative">\n' +
                            '                                <div class="py-2">\n' +
                            '                                    <p class="text-center text-3xl font-mono leading-normal text-gray-700 ">\n' +
                            '                                        <span >'+name+'</span>\n' +
                            '                                    </p>\n' +
                            '                                    <p class="text-center text-lg font-mono leading-normal text-gray-700 opacity-70 ">\n' +
                            '                                        <span>'+content+'</span>\n' +
                            '                                    </p>\n' +
                            '                                </div>\n' +
                            '                                <div class="justify-between">\n' +
                            '                                    <span class="justify-left  text-xl line-through opacity-70"\n' +
                            '                                         >'+price+'$</span>\n' +
                            '                                    <span class="ml-3 text-3xl font-bold text-red-900 dark:text-white"\n' +
                            '                                          >'+discountPrice+'$</span></span>\n' +
                            '                                </div>\n' +
                            '                            </div>\n' +
                            '                            <span th:if="${saleItemMap.get(Product.getProduct().getId())!=0}"\n' +
                            '                                  class="justify-right text-m opacity-70 ml-3 font-bold">\n' +
                            '                       Sold Items:'+saleItem+'</span>\n' +
                            '                        </div>\n' +
                            '                    </a>\n' +
                            '                </div>'
                    });

                    // Update the product list with the new HTML
                    $('#all-item').html(html);
                },
                error: function() {
                    alert('Yêu cầu AJAX không thành công.');
                }
            });
        }

function formatNumber(num) {
    if (num >= 1000000) {
        return (num / 1000000).toFixed(1) + 'm';
    } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k';
    } else {
        return num.toString();
    }
}

function formatDiscount(discount) {
    var roundedDiscount = Math.round(discount);
    if (discount - roundedDiscount >= 0.5) {
        return (roundedDiscount + 1) + '%';
    } else {
        return roundedDiscount + '%';
    }
}
