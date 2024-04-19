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
                var products = response;
                $('.delete-item').remove();
                var html = '';
                products.forEach(function(ProductDTO) {
                        var name = ProductDTO.product.name.substring(0, 30);
                        var content = ProductDTO.product.content.substring(0, 33);
                        var price=ProductDTO.product.price||0  ;
                        var discountPrice = ProductDTO.product.discount_price || 0;
                    html+=' <div class="rounded delete-item">\n' +
                            '                   <div class="  relative flex flex-col text-gray-700 bg-white shadow-md bg-clip-border rounded-xl w-96 hover:shadow-md transition-all duration-500 border-r-[1px] border-stone-200 hover: transform transition duration-300 hover:scale-110 ">\n' +
                            '                       <div class="relative mx-4 mt-4  text-gray-700 bg-white bg-clip-border rounded-xl h-96 ">\n' +
                            '                           <img src="/images/products/' + ProductDTO.product.id + '/' + ProductDTO.product.image + '" alt="card-image" class="object-fill h-full w-full" />\n' +
                            '                           <div if="${Product.discountPrice != 0}" class="absolute top-0 font-bold text-sm text-white rounded-sm  py-1 bg-green-300">\n' +
                            '                               Sale: <span th:text="${((Product.price - Product.discountPrice) / Product.discountPrice * 100).toString().substring(0, ((Product.price - Product.discountPrice) / Product.discountPrice * 100).toString().indexOf(\'.\') + 3)}"></span><span>%</span>\n' +
                            '                           </div>\n' +
                            '                       </div>\n' +
                            '                   </div>\n' +
                            '                   <div class="flex items-center justify-center flex-col">\n' +
                            '                       <div class="py-2">\n' +
                            '                           <p class="text-center text-3xl font-mono leading-normal text-gray-700 ">\n' +
                            '                               <span>'+name+'</span>\n' +
                            '                           </p>\n' +
                            '                           <p class="text-center text-lg font-mono leading-normal text-gray-700 opacity-70 ">\n' +
                            '                               <span >'+content+'</span>\n' +
                            '                           </p>\n' +
                            '                       </div>\n' +
                            '                       <div class="font-bold">\n' +
                            '                        <span class="text-xl line-through opacity-70">\n' +
                            '                            <span >'+price+'</span>$\n' +
                            '                        </span>\n' +
                            '                           <span class="ml-3 text-3xl font-bold text-red-900 dark:text-white">\n' +
                            '                            <span >'+discountPrice+'</span>$\n' +
                            '                        </span>\n' +
                            '                       </div>\n' +
                            '                   </div>\n' +
                            '               </div>'
                });

                // Update the product list with the new HTML
                $('#all-item').html(html);
            },
            error: function() {
                alert('Yêu cầu AJAX không thành công.');
            }
        });
    }



function formatNumber(number, divisor) {
    if (number < divisor) {
        return number;
    }

    const suffixes = ["k", "m", "b", "t"];
    let currentDivisor = divisor;
    let formattedNumber = "";

    while (number >= currentDivisor) {
        formattedNumber = `<span class="math-inline">\{Math\.floor\(number / currentDivisor\)\}</span>{suffixes.shift()}${formattedNumber}`;
        number %= currentDivisor;
        currentDivisor *= 1000;
    }

    return formattedNumber;
}

