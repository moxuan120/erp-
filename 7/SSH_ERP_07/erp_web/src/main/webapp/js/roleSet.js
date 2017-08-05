$(function () {
    //权限树
    $("#tree").tree({
        url: treeUrl,
        animate: true,
        checkbox: true
    })
    //角色表格数据
    $("#grid").datagrid({
        url: gridUrl,
        singleSelect: true,
        fitColumns: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'name', title: '名称', width: 100}
        ]],
        onClickRow: function (index, data) {
            //权限树
            $("#tree").tree({
                url: treeUrl + '?id=' + data.uuid,
                animate: true,
                checkbox: true
            })
        }
    });

});

function saveTree() {
    //获取所有选择的节点id并用'-'拼接
    var checkedNodes = $("#tree").tree('getChecked');
    var checked = '';
    $.each(checkedNodes, function (i, data) {
        checked += data.id;
        checked += '-';
    });
    //提交的数据
    var formData = {};
    formData.id = $("#grid").datagrid('getSelected').uuid;
    formData.checked = checked;
    //提交数据
    $.post(updateUrl, formData, function (data) {
        $.messager.alert('提示', data.message, 'info')
    }, 'json')
}