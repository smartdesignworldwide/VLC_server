//  Modified by Karmel Al Labadi - Smart Design LLC.
$(function () {
    $("#seekSlider").slider({
        range: "min",
        value: 0,
        min: 0,
        max: 100,
        start: function (event, ui) {
            $("#seekSlider").data( 'clicked', true );
        },
        stop: function (event, ui) {
            $("#currentTime").empty().append(format_time(Math.round((ui.value / 100) * $('#seekSlider').attr('totalLength'))));
            switch (current_que) {
            case 'main':
                sendCommand({
                    'command': 'seek',
                    'val': (ui.value) + '%'
                });
                break;
            case 'stream':
                sendVLMCmd('control Current seek ' + ui.value);
                break;
            }
            $("#seekSlider").data( 'clicked', false );
        }
    });
    $("#volumeSlider").slider({
        range: "min",
        value: 50,
        min: 0,
        max: 100,
        start: function (event, ui) {
            $("#volumeSlider").data( 'clicked', true );
        },
        stop: function (event, ui) {
            $("#currentVolume").empty().append(ui.value * 2 + "%");
            sendCommand({
                'command': 'volume',
                'val': Math.round(ui.value * 5.12)
            })
            $("#volumeSlider").data( 'clicked', false );
        }
    });
    /* To ensure that updateStatus() doesn't interfere while the user
     * slides the controls. */
    $("#seekSlider").data( 'clicked', false );
    $("#volumeSlider").data( 'clicked', false );
    $('#buttonStop').click(function () {
        sendSerialCmd({'serial':'tvOff'});
        switch (current_que) {
        case 'main':
            sendCommand({
                'command': 'pl_stop'
            })
            break;
        case 'stream':
            sendVLMCmd('control Current stop');
            break;
        }
        return false;
    });
    $('#buttonPlay').click(function () {
        if ($(this).attr('state') == 'stopped') {
            switch (current_que) {
            case 'main':
                var id = $('.jstree-clicked', '#libraryTree').length > 0 ? $('.jstree-clicked', '#libraryTree').first().parents().first().attr('id').substr(5) : current_id;
                sendCommand({
                    'command': 'pl_play',
                    'id': id
                });
                break;
            case 'stream':
                sendVLMCmd('control Current play');
                flowplayer("player", "http://releases.flowplayer.org/swf/flowplayer-3.2.7.swf");
                break;
            }
        } else {
            switch (current_que) {
            case 'main':
                sendCommand({
                    'command': 'pl_pause'
                });
                break;
            case 'stream':
                sendVLMCmd('control Current pause');
                break;
            }
        }
        return false;
    });

    $('#stream_host').val(stream_server);
    $('#mobileintflink').click(function () {
        var urlimg = location.href + '/mobile.html';
        var codeimg = $('<img width="350" height="350" alt="qrcode"/>');
        codeimg.attr('src', 'http://chart.apis.google.com/chart?cht=qr&chs=350x350&chld=L&choe=UTF-8&chl=' + encodeURIComponent(urlimg));
        codeimg.dialog({width: 350, height: 350, title: 'QR-Code'});
        return false;
    });
})
