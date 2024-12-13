
$(document).ready(function () {
    $('#currentDevice').text(getUUID());

    $('#copyButton').click(function () {
        const textToCopy = $('#currentDevice').text();
        const tempInput = $('<input>');
        $('body').append(tempInput);
        tempInput.val(textToCopy).select();
        document.execCommand('copy');
        tempInput.remove();

        $('#copyMessage').text('복사 되었습니다.');
    });

    $('#applyButton').click(function (){
        if(!isValidUUID($('#saveDevice').val())) {
            alert('식별 정보가 정확하지 않습니다. 다시 입력해 주세요');
        } else {
            saveUUID($('#saveDevice').val())
            $('#saveMessage').text('저장 되었습니다.');
            console.log('save..........');
        }
    });
});
