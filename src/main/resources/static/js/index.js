function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}


$(document).ready(function () {
    const existingUuid = getCookie(EIPclientUUID);

    if (existingUuid == 'undefined' || existingUuid == null) {
        ajaxRequest({
            url: gContextPath + '/questions/generate-uuid',
            type: 'GET',
            success: function (data) {
                $('#uuidResult').text(data);
            }
        });
    }

    $('.startButton').click(function () {
        const value = $(this).val();
        const url = gContextPath + `/questions/random/${value}/0`;
        window.location.href = url;
    });
});

