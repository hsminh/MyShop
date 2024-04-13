// JavaScript for dropdown menu
document.addEventListener("DOMContentLoaded", function() {
    const userMenuButton = document.getElementById("user-menu-button");
    const userMenu = document.querySelector(".dropdown-menu");

    userMenuButton.addEventListener("click", function() {
        const expanded = this.getAttribute("aria-expanded") === "true" || false;
        this.setAttribute("aria-expanded", !expanded);
        userMenu.classList.toggle("hidden");
    });

    // Close the dropdown menu when clicking outside
    document.addEventListener("click", function(event) {
        const isClickInside = userMenu.contains(event.target) || userMenuButton.contains(event.target);
        if (!isClickInside) {
            userMenu.classList.add("hidden");
            userMenuButton.setAttribute("aria-expanded", "false");
        }
    });
});



// var lastScrollTop = 0;
// var navbar = document.getElementById('navbar');
//
// window.addEventListener('scroll', function() {
//     var currentScroll = window.pageYOffset || document.documentElement.scrollTop;
//
//     if (currentScroll > lastScrollTop) {
//         navbar.style.transition = 'transform 0.3s ease-in-out';
//         navbar.style.transform = 'translateY(-100%)';
//         } else {
//         navbar.style.transition = 'transform 0.3s ease-in-out';
//         navbar.style.transform = 'translateY(0)';
//         navbar.style.position = 'fixed';
//         navbar.style.top = '0';
//         navbar.style.width = '100%';
//
//         if (currentScroll === 0 ) {
//             navbar.style.position = 'static';
//             navbar.style.width = 'auto';
//             navbar.style.zIndex = 'auto';
//         }
//     }
//     lastScrollTop = currentScroll;
// });
//
//
//
