function showName() {
    $.post("user_getName", function (data) {
        if (data.success) {
            $("#loginName").html(data.message)
        } else {
            $.messager.alert('警告', data.message, 'info', function () {
                location.href = 'login.html';
            });
        }
    }, 'json')

}

function donate(){
	$("#donateDlg").dialog("open");
}

function showGod(){
	$("#godDlg").dialog("open");
}

var onlyOpenTitle = "欢迎使用";//不允许关闭的标签的标题

//获取菜单
var _menus;

function getMenus() {
    $.post("menu_getTree", function (data) {
        _menus = data;
        //初始化左侧菜单
        InitLeftMenu();
    }, "json")
}


//初始化左侧
function InitLeftMenu() {
    $("#nav").accordion({animate: false, fit: false, border: false});
    var selectedPanelname = '';

    $.each(_menus.menus, function (i, n) {
        var menulist = '';
        menulist += '<ul class="navlist">';
        $.each(n.menus, function (j, o) {
            menulist += '<li><div ><a ref="' + o.menuid + '" rel="' + o.url + '" ><span class="icon ' + o.icon + '" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div> ';
            /*
                        if(o.child && o.child.length>0)
                        {
                            //li.find('div').addClass('icon-arrow');

                            menulist += '<ul class="third_ul">';
                            $.each(o.child,function(k,p){
                                menulist += '<li><div><a ref="'+p.menuid+'" href="#" rel="' + p.url + '" ><span class="icon '+p.icon+'" >&nbsp;</span><span class="nav">' + p.menuname + '</span></a></div> </li>'
                            });
                            menulist += '</ul>';
                        }
            */
            menulist += '</li>';
        });
        menulist += '</ul>';

        $('#nav').accordion('add', {
            title: n.menuname,
            content: menulist,
            width: '100%',
            border: false,
            iconCls: 'icon ' + n.icon
        });

        if (i == 0)
            selectedPanelname = n.menuname;

    });

    $('#nav').accordion('select', selectedPanelname);


    $('.navlist li a').click(function () {
        var tabTitle = $(this).children('.nav').text();

        var url = $(this).attr("rel");
        var menuid = $(this).attr("ref");
        var icon = $(this).find('.icon').attr('class');

        var third = find(menuid);
        if (third && third.child && third.child.length > 0) {
            $('.third_ul').slideUp();

            var ul = $(this).parent().next();
            if (ul.is(":hidden"))
                ul.slideDown();
            else
                ul.slideUp();
        }
        else {
            addTab(tabTitle, url, icon);
            $('.navlist li div').removeClass("selected");
            $(this).parent().addClass("selected");
        }
    }).hover(function () {
        $(this).parent().addClass("hover");
    }, function () {
        $(this).parent().removeClass("hover");
    });


    //选中第一个
    //var panels = $('#nav').accordion('panels');
    //var t = panels[0].panel('options').title;
    //$('#nav').accordion('select', t);
}

//获取左侧导航的图标
function getIcon(menuid) {
    var icon = 'icon ';
    $.each(_menus.menus, function (i, n) {
        $.each(n.menus, function (j, o) {
            if (o.menuid == menuid) {
                icon += o.icon;
            }
        })
    });

    return icon;
}

function find(menuid) {
    var obj = null;
    $.each(_menus.menus, function (i, n) {
        $.each(n.menus, function (j, o) {
            if (o.menuid == menuid) {
                obj = o;
            }
        });
    });

    return obj;
}

function addTab(subtitle, url, icon) {
    if (!$('#tabs').tabs('exists', subtitle)) {
        $('#tabs').tabs('add', {
            title: subtitle,
            content: createFrame(url),
            closable: true,
            icon: icon
        });
    } else {
        $('#tabs').tabs('select', subtitle);
        $('#mm-tabupdate').click();
    }
    tabClose();
}

function createFrame(url) {
    var s = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
    return s;
}

function tabClose() {
    /*双击关闭TAB选项卡*/
    $(".tabs-inner").dblclick(function () {
        var subtitle = $(this).children(".tabs-closable").text();
        $('#tabs').tabs('close', subtitle);
    });
    /*为选项卡绑定右键*/
    $(".tabs-inner").bind('contextmenu', function (e) {
        $('#mm').menu('show', {
            left: e.pageX,
            top: e.pageY
        });

        var subtitle = $(this).children(".tabs-closable").text();

        $('#mm').data("currtab", subtitle);
        $('#tabs').tabs('select', subtitle);
        return false;
    });
}


//绑定右键菜单事件
function tabCloseEven() {

    $('#mm').menu({
        onClick: function (item) {
            closeTab(item.id);
        }
    });

    return false;
}

function closeTab(action) {
    var alltabs = $('#tabs').tabs('tabs');
    var currentTab = $('#tabs').tabs('getSelected');
    var allTabtitle = [];
    $.each(alltabs, function (i, n) {
        allTabtitle.push($(n).panel('options').title);
    });


    switch (action) {
        case "refresh":
            var iframe = $(currentTab.panel('options').content);
            var src = iframe.attr('src');
            $('#tabs').tabs('update', {
                tab: currentTab,
                options: {
                    content: createFrame(src)
                }
            });
            break;
        case "close":
            var currtab_title = currentTab.panel('options').title;
            $('#tabs').tabs('close', currtab_title);
            break;
        case "closeall":
            $.each(allTabtitle, function (i, n) {
                if (n != onlyOpenTitle) {
                    $('#tabs').tabs('close', n);
                }
            });
            break;
        case "closeother":
            var currtab_title = currentTab.panel('options').title;
            $.each(allTabtitle, function (i, n) {
                if (n != currtab_title && n != onlyOpenTitle) {
                    $('#tabs').tabs('close', n);
                }
            });
            break;
        case "closeright":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);

            if (tabIndex == alltabs.length - 1) {
                alert('亲，后边没有啦 ^@^!!');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i > tabIndex) {
                    if (n != onlyOpenTitle) {
                        $('#tabs').tabs('close', n);
                    }
                }
            });

            break;
        case "closeleft":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);
            if (tabIndex == 1) {
                alert('亲，前边那个上头有人，咱惹不起哦。 ^@^!!');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i < tabIndex) {
                    if (n != onlyOpenTitle) {
                        $('#tabs').tabs('close', n);
                    }
                }
            });

            break;
        case "exit":
            $('#closeMenu').menu('hide');
            break;
    }
}


//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
    $.messager.alert(title, msgString, msgType);
}


//初始化密码修改框
function initPwd() {
    $('#w').window({
        title: '修改密码',
        width: 300,
        modal: true,
        shadow: true,
        closed: true,
        height: 200,
        resizable: false
    });
}

//打开密码修改框
function openPwd() {
    $('#w').window('open');
}

//关闭密码修改框
function closePwd() {
    $('#w').window('close');
    $("#pwdForm").form("clear")
}


//修改密码
function alterPwd() {
    $("#pwdForm").form('submit', {
        url: 'user_alterPwd',
        onSubmit: function (param) {
            var $oldPass = $('#txtOldPass');
            var $newpass = $('#txtNewPass');
            var $rePass = $('#txtRePass');

            if ($oldPass.val() == '') {
                msgShow('系统提示', '请输入旧密码！', 'warning');
                return false;
            }
            if ($newpass.val() == '') {
                msgShow('系统提示', '请输入新密码！', 'warning');
                return false;
            }
            if ($rePass.val() == '') {
                msgShow('系统提示', '请再一次输入新密码！', 'warning');
                return false;
            }

            if ($newpass.val() != $rePass.val()) {
                msgShow('系统提示', '两次密码不一致！请重新输入', 'warning');
                return false;
            }
        },
        success: function (data) {
            var data = eval('(' + data + ')'); //json转化成js对象
            $.messager.alert('提示', data.message, 'info', function () {
                if (data.success) {
                    closePwd()
                }
            });
        }

    });
}

//退出
function logout() {
    $.post('user_logout', function () {
        location.href = "login.html";
    })
}

$(function () {
    //正在加载中效果
    $('#loading-mask').fadeOut();
    //获取左侧菜单数据
    getMenus();
    //绑定关闭TAB选项卡
    tabClose();
    //绑定右键菜单事件
    tabCloseEven();
    //显示登陆用户名
    showName();
    //初始化密码框
    initPwd();
    //刷新时间
    myDate();
});


//获取当前时间
function myDate() {
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth();
    var date = now.getDate();
    var day = now.getDay();
    var hour = now.getHours();
    var minu = now.getMinutes();
    var sec = now.getSeconds();
    var week;
    month = month + 1;
    if (month < 10) month = "0" + month;
    if (date < 10) date = "0" + date;
    if (hour < 10) hour = "0" + hour;
    if (minu < 10) minu = "0" + minu;
    if (sec < 10) sec = "0" + sec;
    var arr_week = new Array("Sun.", "Mon.", "Tues.", "Wed.", "Thur.", "Fri", "Sat");
    week = arr_week[day];
    var time = "";
    time = "GMT+8, " + year + "-" + month + "-" + date + "  " + week + ". Updated at: " + hour + ":" + minu + ":" + sec;
    $("#myDate").html(time);
    window.setTimeout(myDate,1000);
}


