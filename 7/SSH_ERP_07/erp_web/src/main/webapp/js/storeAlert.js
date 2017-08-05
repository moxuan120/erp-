$(function () {
    $("#grid").datagrid({
        url: 'storeAlert_list',
        singleSelect: true,
        toolbar: '#tb',
        fitColumns: true,
        columns: [[
            {field: 'uuid', title: '商品编号', width: 60, align: 'center', halign: 'center'},
            {field: 'name', title: '商品名称', width: 100, halign: 'center'},
            {field: 'storenum', title: '库存数量', width: 120, halign: 'center'},
            {field: 'outnum', title: '待发货数量', width: 120, halign: 'center'}
        ]],
    });
});

function refresh() {
    var data = $("#searchForm").serializeJSON();
    $("#grid").datagrid('load', data)
}

function clearSearchForm() {
    $("#searchForm").form('clear');
}

function sendMail() {
    $.post('storeAlert_sendStoreAlertMail', function (data) {
        $.messager.alert('提示', data.message, 'info');
    }, 'json')
}