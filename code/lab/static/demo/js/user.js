/**
 * Created by youzh_000 on 2014/7/15.
 */

//提问，笔记，回答
function box_init() {
    $('.box').hover(function () {
        $(this).children().css('color', '#009EDA')
    }, function () {
        $(this).children('.count_num').css('color', '#000000');
        $(this).children('.count_word').css('color', '#999999');
    });
}

//关注按钮动作
function action_init(){
    $('.follow_action').on('click', function () {
        $(this).hide();
        $(this).parent().find('.cancel_follow').show();
    });
    $('.cancel_follow').on('click', function () {
        $(this).hide();
        $(this).parent().find('.follow_action').show();
    })
}

//面板之间的切换操作及动画
function panel_init() {
    $('.content_panel').parent().css({overflow: 'hidden'});
    var handles = {
        'current_panel': $('#home_panel'),
        'search_label': function () {
            console.log('show search panel');
        },
        'follow': function () {
            this.showPanel($('#follow_panel'));
        },
        'followed': function () {
            this.showPanel($('#followed_panel'))
        },
        'showPanel': function (e) {
            if(e.attr('id') === handles.current_panel.attr('id'))
                return;

            this.current_panel.animate({left: -this.current_panel.outerWidth(true)}, function () {
                $(this).hide();
                $(this).css({left: 0});
                e.fadeIn();
                handles.current_panel = e;
            }).bind(this);
        },
        'slideBack': function (e) {
            if(e.attr('id') === handles.current_panel.attr('id'))
                return;

            this.current_panel.animate({left: this.current_panel.outerWidth(true)}, function () {
                $(this).hide();
                $(this).css({left: 0});
                e.fadeIn();
                handles.current_panel = e;
            })
        }

// 连续滑动
//        'slideChangeFromRight': function (oldPanel, newPanel) {
//            newPanel.queue('hide_old', function (next) {
//                oldPanel.animate({left: -oldPanel.outerWidth(true)});
//                next();
//            }).queue('show_new', function (next) {
//                newPanel.css({left: oldPanel.outerWidth(true), 'z-index':1000, top: -oldPanel.outerHeight()});
//                newPanel.show();
//                newPanel.animate({left: 0}, function () {
//                    //compelete
//                    newPanel.css({top: 0, 'z-index': 'auto'});
//                    oldPanel.hide();
//                });
//                next();
//            }).dequeue('hide_old').dequeue('show_new');
//        },
//        'slideChangeFromLeft': function (oldPanel, newPanel) {
//            newPanel.queue('hide_old', function (next) {
//                oldPanel.animate({left: newPanel.outerWidth(true)});
//                next();
//            }).queue('show_new', function (next) {
//                newPanel.css({left: -oldPanel.outerWidth(true), 'z-index':1000, top: -oldPanel.outerHeight()});
//                newPanel.show();
//                newPanel.animate({left: 0}, function () {
//                    //compelete
//                    newPanel.css({top: 0, 'z-index': 'auto'});
//                    oldPanel.hide();
//                });
//                next();
//            }).dequeue('hide_old').dequeue('show_new');
//        }
    };

    $('.show_all').on('click', function () {
        handles[$(this).data('tab')]();
    });
    $('.go_back').on('click', function () {
        handles.slideBack($('#home_panel'));
    });
}

//动态简介与详细信息的切换
function feed_init(){
    $('.feed_brief').on('click', function () {
        $(this).hide();
        $(this).parent().find('.feed_all').show();
    });

    $('.feed_all_roll_up').on('click', function () {
        $(this).parent().parent().find('.feed_brief').show();
        $(this).parent().hide();
    })
}

$(document).ready(function () {
    panel_init();
    box_init();
    action_init();
    feed_init();
});