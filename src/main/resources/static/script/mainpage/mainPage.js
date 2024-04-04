var searchButton = document.getElementById('searchButton');
var categoryId = document.getElementById('categoryId').value;

searchButton.addEventListener('click', function() {
    var keyword = document.getElementById("default-search").value;
    window.location.href = "/main-page?category=" + categoryId + "&search=" + encodeURIComponent(keyword);
});


document.addEventListener("DOMContentLoaded", function() {
    var successMessage = document.getElementById("successMessage");
    if (successMessage) {
        setTimeout(function() {
            successMessage.style.display = "none";
        }, 3000);
    }
});

