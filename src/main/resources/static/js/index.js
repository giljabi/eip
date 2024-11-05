function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}


$(document).ready(function () {
    const existingUuid = getCookie('EIPclientUUID');

    if (existingUuid == 'undefined' || existingUuid == null) {
        // UUID 쿠키가 없으면 서버에 요청하여 생성
        $.ajax({
            url: '/questions/generate-uuid',
            type: 'GET',
            success: function (data) {
                $('#uuidResult').text(data);
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
            }
        });
    }

    $('.startButton').click(function () {
        const value = $(this).val();
        const url = `/questions/random/${value}/0`;
        window.location.href = url;
    });
});
