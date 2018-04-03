/**
 * Created by ns on 14-7-6.
 */

$(document).ready(
    function () {
        $('.show_type').click(function (){
            $('.selected').removeClass('selected').addClass('show_type');
            $(this).removeClass('show_type').addClass('selected');
        });
    }
);