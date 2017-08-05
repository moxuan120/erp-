$(function () {
    $("#feedback").dialog({
        title: '反馈',
        width: 400,
        height: 300,
        closed: true,
        cache: false,
        modal: true,
        buttons:[{
            iconCls: 'icon-add',
            text:'联系客服QQ',
            handler:function(){
                location.href = 'tencent://message/?uin=525600983&Menu=yes';
            }
        },{
            iconCls: 'icon-add',
            text:'发送反馈邮件',
            handler:function(){
                $("#feedbackForm").form('submit',{
                    url:'user_feedback',
                    success:function(data){
                        var data = eval('(' + data + ')'); //json转化成js对象
                        $.messager.alert("提示",data.message,'info',function (yes) {
                            if (data.success){
                                $("#feedback").dialog("close")
                            }
                        })
                    }
                });
            }
        },{
            text:'取消',
            iconCls: 'icon-cancel',
            handler:function(){
                $("#feedback").dialog("close")
            }
        }]
    })
});

function feedback() {
    $("#feedback").dialog("open")
}