$(document).ready(function ()
{
    $('#searchButton').on('click',function ()
    {
        var keyword = document.getElementById("valueSearch").value.trim();
        var isHideStr = document.getElementById("isHide").value.trim();
        var isHide = (isHideStr === "true");
        isHide = !isHide;
        if(keyword.length!==0)
        {
            window.location.href = "/products?search=" + encodeURIComponent(keyword)+"&isHide="+encodeURIComponent(isHide);

        }
    })
})