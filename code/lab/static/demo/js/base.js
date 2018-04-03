/**
 * Created by ns on 14-7-6.
 */

//关于导航条的动作
function nav_thing(){
    $('.dropdown-toggle').dropdown()
//    $('.ui.dropdown').dropdown({on: 'hover', delay:{show: 10, hide:300}});
    $('#nav').find('.item').hover(
            function(){
                $(this).addClass('nav_hover');
            },
            function(){
                $(this).removeClass('nav_hover');
            }
    ).unbind('click').click(
            function(){
                console.log($(this).attr('data-value'));
            }
    );
}

function dropdownOpen() {
    var in_timer, out_timer;        //延时执行hover
    $('div.dropdown').hover(function () {
        clearTimeout(out_timer);
        in_timer = setTimeout(function(){
            $('#nav').find('.dropdown-menu').first().stop(true, true).slideDown('fast');
//            $('div.dropdown').addClass('open');
        },50);
    },function(){
        clearTimeout(in_timer);
        out_timer = setTimeout(function(){
            $('#nav').find('.dropdown-menu').first().stop(true, true).fadeOut('fast');
//            $('div.dropdown').removeClass('open');
        }, 200);
    });

    $('a.nav_item').hover(function(){
        $(this).addClass('nav_hover');
    },function(){
        $(this).removeClass('nav_hover');
    })
}

$(document).ready(function(){
    nav_thing();
    dropdownOpen()
});