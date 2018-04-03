/**
 * Created by ns on 14-7-6.
 */
function sidebar_init(){
    var action = {
        'width': '300px',
        'hide': function(){
    //        收起call
            $('#menu').animate({'width': 0});
            $('#switch').data('effect', 'show').text('>');
            $('#cancel_layer').css('display', 'none');
        },
        'show': function(){
    //        张开call
            $('#menu').animate({'width': this.width});
            $('#switch').data('effect', 'hide').text('<');
            $('#cancel_layer').css('display', 'block');
        }
    };

    $('#switch').click(function(){
        action[$('#switch').data('effect')]();
    }).hover(function(){
        var t = $(this).text();
        $(this).text(t+t);
    }, function(){
        var t = $(this).text();
        $(this).text(t.substring(0, 1));
    });

    $('#cancel_layer').on('click', function(){
        action.hide();
    });

//    $(document).delay(1000).queue(function(){
//        action.hide();
//    })
}

$(document).ready(
    function () {
        $('.show_type').click(function (){
            $('.selected').removeClass('selected').addClass('show_type');
            $(this).removeClass('show_type').addClass('selected');
        });

        sidebar_init();
    }
);