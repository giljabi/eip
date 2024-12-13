

$(document).ready(function () {

    $('.startButton').click(function () {
        const value = $(this).val();
        const url = `/questions/random/${value}/0/${getUUID()}`;
        const fullUrl = `${window.location.protocol}//${window.location.host}${url}`;

        window.location.href = fullUrl;
    });
});



