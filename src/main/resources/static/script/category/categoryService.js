$(document).ready(function ()
{
    $('#searchButton').on('click',function ()
    {
        var keyword = document.getElementById("valueSearch").value;
        if(keyword.length!==0)
        {
            window.location.href = "/category?search=" + encodeURIComponent(keyword);
        }
    })
})