$(function () {
    $("#grid").datagrid({
        url: 'emp_list',
        singleSelect: true,
        pagination: true,
        fitColumns:true,
        toolbar: '#tb',
        columns: [[
            {field: 'uuid', title: 'uuid', width: 60, align: 'center', halign: 'center'},
            {field: 'username', title: '登陆名', width: 100, halign: 'center'},
            {field: 'name', title: '真实姓名', width: 120, halign: 'center'},
            {
                field: 'gender', title: '性别', width: 80, halign: 'center', formatter: function (value) {
                if (value == 1) return '男';
                if (value == 0) return '女';
            }
            },
            {field: 'email', title: '邮箱', width: 120, halign: 'center'},
            {field: 'tele', title: '电话', width: 120, halign: 'center'},
            {field: 'birthday', title: '生日', width: 120, halign: 'center'},
            {field: 'address', title: '地址', width: 120, halign: 'center'},
            {
                field: 'dep', title: '部门', width: 120, halign: 'center', formatter: function (value) {
                if (value != null) return value.name;
                else return "";
            }
            },

            {
                field: '-',
                title: '操作',
                width: 120,
                halign: 'center',
                align: 'center',
                formatter: function (value, row, index) {
                    var oper = '<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openResetPwd(' + row.uuid + ')"><img src="images/reset.png"></a>';
                    return oper;
                }
            }

        ]]
    });

    //simpleList；
    $.post('dep_simpleList', function (data) {
        var simpleList = [{"name":"未定义","uuid":""}];
        for(var attr in data){
            simpleList[attr * 1 + 1]=data[attr];
        }
        $("#select1").combobox({
            data: simpleList
        });
    }, 'json');
});

function refresh() {
    var data = $("#searchForm").serializeJSON();
    $("#grid").datagrid('load', data)
}

function openResetPwd(uuid) {
    $("#resetForm").form('clear');
    $("#resetPwd").window("open");
    $('#uuid').val(uuid);
}
function closeResetPwd() {
    $("#resetPwd").window("close");
    $("#resetForm").form('clear');
}
function submitReset() {
    $('#resetForm').form('submit', {
        url: 'user_resetPwd',
        success: function (data) {
            var data = eval('(' + data + ')'); //json转化成js对象
            $.messager.alert('提示', data.message, 'info', function () {
                if (data.success) {
                    $("#grid").datagrid('reload');
                    $("#resetPwd").window('close');
                    $('#resetForm').form('clear');
                }
            });
        }
    });
}